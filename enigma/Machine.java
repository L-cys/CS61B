package enigma;

import java.util.Collection;


/**
 * Class that represents a complete enigma machine.
 *
 * @author Yuanshan Chen
 */
class Machine {

    /**
     * A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     * and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     * available rotors.
     */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
    }

    /**
     * Return the number of rotor slots I have.
     */
    int numRotors() {
        return this._numRotors;
    }

    /**
     * Return the number pawls (and thus rotating rotors) I have.
     */
    int numPawls() {
        return this._pawls;
    }

    /**
     * Set my rotor slots to the rotors named ROTORS from my set of
     * available rotors (ROTORS[0] names the reflector).
     * Initially, all rotors are set at their 0 setting.
     */
    void insertRotors(String[] rotors) {
        int i = 0;
        _rotors = new Rotor[numRotors()];
        for (String name : rotors) {
            for (Rotor x : _allRotors) {
                if (name.equals(x.name())) {
                    _rotors[i] = x;
                    i = i + 1;
                }
            }
        }
    }

    /**
     * Set my rotors for the situation of rings from RINGSTRING.
     */
    void setrings(String ringstring) {
        for (int i = 1; i < numRotors(); i = i + 1) {
            _rotors[i].setring(ringstring.charAt(i - 1));
        }
    }

    /**
     * Set my rotors according to SETTING, which must be a string of
     * numRotors()-1 characters in my alphabet. The first letter refers
     * to the leftmost rotor setting (not counting the reflector).
     */
    void setRotors(String setting) {
        for (int i = 1; i < numRotors(); i = i + 1) {
            _rotors[i].set(setting.charAt(i - 1));
        }
    }

    /**
     * Set the plugboard to PLUGBOARD.
     */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /**
     * Returns the result of converting the input character C (as an
     * index in the range 0..alphabet size - 1), after first advancing
     * the machine.
     */
    int convert(int c) {
        int[] atNRotors = new int[_rotors.length];
        for (int i = 0; i < _rotors.length; i = i + 1) {
            if (_rotors[i].atNotch()) {
                atNRotors[i] = 1;
            } else {
                atNRotors[i] = 0;
            }
        }
        _rotors[_numRotors - 1].advance();
        for (int i = 1; i < _rotors.length - 1; i = i + 1) {
            if ((atNRotors[i] == 1 && _rotors[i - 1].rotates())
                    || atNRotors[i + 1] == 1) {
                _rotors[i].advance();
            }
        }
        int temp = _plugboard.permute(c);
        for (int i = _numRotors - 1; i >= 0; i = i - 1) {
            temp = _rotors[i].convertForward(temp);
        }

        int tempa = temp;
        for (int x = 1; x < _numRotors; x = x + 1) {
            tempa = _rotors[x].convertBackward(tempa);
        }

        tempa = _plugboard.permute(tempa);
        return tempa;
    }

    /**
     * Returns the encoding/decoding of MSG, updating the state of
     * the rotors accordingly.
     */
    String convert(String msg) {
        String msgs = msg.replaceAll(" ", "");
        String result = "";
        for (int i = 0; i < msgs.length(); i = i + 1) {
            char character = msgs.charAt(i);
            int characterint = _alphabet.toInt(character);
            int resultint = this.convert(characterint);
            char out = _alphabet.toChar(resultint);
            result = result + out;
        }
        return result;
    }

    /**
     * Common alphabet of my rotors.
     */
    private final Alphabet _alphabet;

    /**
     * Set the number of my rotors.
     */
    private int _numRotors;

    /**
     * Set the number of my pawls.
     */
    private int _pawls;

    /**
     * Set the whole collection of my rotors.
     */
    private Collection<Rotor> _allRotors;

    /**
     * Set the rotors that I used.
     */
    private Rotor[] _rotors;

    /**
     * Set the plugboard of this rotors.
     */
    private Permutation _plugboard;

}
