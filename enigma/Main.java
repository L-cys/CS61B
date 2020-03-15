package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/**
 * Enigma simulator.
 *
 * @author Yuanshan Chen
 */
public final class Main {

    /**
     * Process a sequence of encryptions and decryptions, as
     * specified by ARGS, where 1 <= ARGS.length <= 3.
     * ARGS[0] is the name of a configuration file.
     * ARGS[1] is optional; when present, it names an input file
     * containing messages.  Otherwise, input comes from the standard
     * input.  ARGS[2] is optional; when present, it names an output
     * file for processed messages.  Otherwise, output goes to the
     * standard output. Exits normally if there are no errors in the input;
     * otherwise with code 1.
     */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /**
     * Check ARGS and open the necessary files (see comment on main).
     */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /**
     * Return a Scanner reading from the file named NAME.
     */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Return a PrintStream writing to the file named NAME.
     */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Configure an Enigma machine from the contents of configuration
     * file _config and apply it to the messages in _input, sending the
     * results to _output.
     */
    private void process() {
        machine = readConfig();
        rotorsname = new String[machine.numRotors()];
        if (_input.hasNextLine()) {
            String f = _input.nextLine();
            if (f.length() == 0 && _input.hasNextLine()) {
                f = _input.nextLine();
            }
            if (f.charAt(0) != '*') {
                throw new EnigmaException("No setting line");
            } else {
                readsetting(f);
            }
            while (_input.hasNextLine()) {
                String firstline = _input.nextLine();
                if (firstline.length() == 0) {
                    _output.println();
                    continue;
                }
                if (firstline.charAt(0) != '*') {
                    coding(firstline);
                } else if (firstline.charAt(0) == '*') {
                    readsetting(firstline);
                }
            }
        } else {
            throw new EnigmaException("Wrong configuration format");
        }
    }

    /**
     * To test whether the setting LINE is right format.
     */
    private void testfirstline(String[] line) {
        boolean flag = false;
        for (int i = 0; i < rflnames.size(); i = i + 1) {
            if (rflnames.get(i).equals(line[1])) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new EnigmaException("Not start with an reflector");
        }
        for (int i = 0; i < line.length; i = i + 1) {
            if (line[i].charAt(0) == '(') {
                if (line[i].length() != 4) {
                    throw new EnigmaException("Wrong plugboard setting");
                }
                int l = line[i].length();
                String sb = line[i].substring(1, line[i].length() - 2);
                for (int j = 0; j < sb.length(); j = j + 1) {
                    if (!_alphabet.contains(sb.charAt(j))) {
                        throw new EnigmaException("Wrong plugboard setting");
                    }
                }
            } else if (line[i].equals("*")) {
                continue;
            }
        }

        countpb = 0;
        for (int y = 1; y < line.length; y = y + 1) {
            if (line[y].charAt(0) == '(') {
                countpb = countpb + 1;
            }
        }
        int counta = 0;
        for (int i = 1; i < line.length - countpb; i = i + 1) {
            for (int j = 0; j < allRotorsname.size(); j = j + 1) {
                if (line[i].equals(allRotorsname.get(j))) {
                    counta = counta + 1;
                }
            }
        }
        if (counta != rtnum) {
            throw new EnigmaException("Wrong rotors setting");
        }
    }

    /**
     * Read the setting LINE and set the machine.
     */
    private void readsetting(String line) {
        String[] splitline = line.split(" ");
        if (!(splitline.length >= machine.numRotors() + 2)) {
            throw new EnigmaException("Wrong number of argument");
        }
        testfirstline(splitline);
        for (int i = 1; i < machine.numRotors() + 1; i = i + 1) {
            for (int z = i + 1; z < machine.numRotors() + 1; z = z + 1) {
                if (splitline[i].equals(splitline[z])) {
                    throw new EnigmaException("Duplicate rotors");
                }
            }
        }
        String rotorsetting = splitline[machine.numRotors() + 1];
        if (rotorsetting.length() != machine.numRotors() - 1) {
            throw new EnigmaException("Wrong setting number");
        }
        if (splitline[rtnum + 1].charAt(0) == '(') {
            throw new EnigmaException("No setting");
        }
        for (int y = 0; y < rotorsetting.length(); y = y + 1) {
            if (!_alphabet.contains(rotorsetting.charAt(y))) {
                throw new EnigmaException("Wrong setting");
            }
        }
        ring = "";
        if (splitline.length > rtnum + 2 + countpb) {
            ring = splitline[rtnum + 2];
        }

        int i;
        for (i = 1; i < machine.numRotors() + 1; i = i + 1) {
            rotorsname[i - 1] = splitline[i];
        }
        machine.insertRotors(rotorsname);


        String plugcycles = "";
        for (int x = rtnum + 1; x < splitline.length; x = x + 1) {
            if (splitline[x].charAt(0) == '(') {
                plugcycles = plugcycles + splitline[x];
            }
        }
        Permutation plugb = new Permutation(plugcycles, _alphabet);
        machine.setPlugboard(plugb);
        machine.setRotors(rotorsetting);

        if (ring.length() != 0) {
            machine.setrings(ring);
        }

    }

    /**
     * Read the input LINE and do the coding job.
     */
    private void coding(String line) {
        testsecondline(line);
        String result = "";
        result = machine.convert(line);
        printMessageLine(result);
    }

    /**
     * To check whether the input words LINE are right format.
     */
    private void testsecondline(String line) {
        boolean flaga = true;
        line = line.replaceAll(" ", "");
        for (int i = 0; i < line.length(); i = i + 1) {
            if (!_alphabet.contains(line.charAt(i))) {
                flaga = false;
                throw new EnigmaException("Wrong format of input words");
            }
        }
    }

    /**
     * Return an Enigma machine configured from the contents of configuration
     * file _config.
     */
    private Machine readConfig() {
        try {
            String alpha = _config.next();
            _alphabet = new Alphabet(alpha);
            int numRotors = _config.nextInt();
            rtnum = numRotors;
            int pawls = _config.nextInt();
            nextchar = _config.next();
            while (_config.hasNext()) {
                aname = nextchar;
                achar = _config.next();
                allRotors.add(readRotor());
            }
            for (Rotor x : allRotors) {
                allRotorsname.add(x.name());
            }
            return new Machine(_alphabet, numRotors, pawls, allRotors);

        } catch (NoSuchElementException excp) {
            throw new EnigmaException("configuration file truncated");
        }
    }

    /**
     * Return a rotor, reading its description from _config.
     */
    private Rotor readRotor() {
        try {
            String cycles = "";
            String notches = "";
            nextchar = _config.next();
            while (nextchar.charAt(0) == '(' && _config.hasNext()) {
                cycles = cycles + nextchar;
                nextchar = _config.next();

            }
            if (!_config.hasNext() && nextchar.charAt(0) == '(') {
                cycles = cycles + nextchar;
            }
            Permutation perm = new Permutation(cycles, _alphabet);
            if (achar.charAt(0) == 'M') {
                notches = achar.substring(1);
                return new MovingRotor(aname, perm, notches);

            } else if (achar.charAt(0) == 'N') {
                return new FixedRotor(aname, perm);
            } else {
                rflnames.add(aname);
                return new Reflector(aname, perm);
            }
        } catch (NoSuchElementException excp) {
            throw new EnigmaException("bad rotor description");
        }
    }

    /**
     * Set M according to the specification given on SETTINGS,
     * which must have the format specified in the assignment.
     */
    private void setUp(Machine M, String settings) {
        M.setRotors(settings);
    }

    /**
     * Print MSG in groups of five (except that the last group may
     * have fewer letters).
     */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i += 5) {
            int left = msg.length() - i;
            if (left <= 5) {
                _output.println(msg.substring(i, i + left));
            } else {
                _output.print(msg.substring(i, i + 5) + " ");
            }
        }
    }

    /**
     * Alphabet used in this machine.
     */
    private Alphabet _alphabet;

    /**
     * Source of input messages.
     */
    private Scanner _input;

    /**
     * Source of machine configuration.
     */
    private Scanner _config;

    /**
     * File for encoded/decoded messages.
     */
    private PrintStream _output;

    /**
     * Create the name for a rotor.
     */
    private String aname;

    /**
     * Set the character of the rotor, check which type of rotor would it be.
     * MOVINGROTOR OR FIXEDROTOR OR REFLECTOR.
     */
    private String achar;

    /**
     * Set an argument for saving the next char.
     */
    private String nextchar;

    /**
     * Set an rotors list of this machine.
     */
    private ArrayList<Rotor> allRotors = new ArrayList<Rotor>();

    /**
     * Set an list for allrotors' name.
     */
    private ArrayList<String> allRotorsname = new ArrayList<String>();

    /**
     * Store the number of Machine's rotors.
     */
    private int rtnum;

    /**
     * Initialize an machine.
     */
    private Machine machine;

    /**
     * A string list to store the rotorsname.
     */
    private String[] rotorsname;

    /**
     * A String contains all the reflectors name.
     */
    private ArrayList<String> rflnames = new ArrayList<String>();

    /**
     * Set up a field for ring.
     */
    private String ring;

    /**
     * The number of plugboard in String lisr.
     */
    private int countpb;
}
