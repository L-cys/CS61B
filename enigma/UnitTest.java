package enigma;

import org.junit.Test;
import ucb.junit.textui;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * The suite of all JUnit tests for the enigma package.
 *
 * @author Yuanshan Chen
 */
public class UnitTest {

    /**
     * Run the JUnit tests in this package. Add xxxTest.class entries to
     * the arguments of runClasses to run other JUnit tests.
     */
    public static void main(String[] ignored) {
        textui.runClasses(PermutationTest.class, MovingRotorTest.class);
    }

    @Test
    public void testAlphabet() {
        String chs = "ABCDEFG";
        Alphabet al1 = new Alphabet(chs);
        assertEquals(7, al1.size());
        assertEquals(true, al1.contains('A'));
        assertEquals(false, al1.contains('Z'));
        assertEquals('C', al1.toChar(2));
        assertEquals(2, al1.toInt('C'));
    }

    private Alphabet alphabet;
    private String a = "(AE)(BN)(CK)(DQ)(FU)(GY)(HW)(IJ)(LO)(MP)(RX)(SZ)(TV)";
    Permutation pa = new Permutation(a, alphabet);
    Rotor B = new Rotor("B", pa);

    private String b = "(ALBEVFCYODJWUGNMQTZSKPR) (HIX)";
    Permutation pb = new Permutation(b, alphabet);
    Rotor beta = new Rotor("Beta", pb);

    private String c = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
    Permutation pc = new Permutation(c, alphabet);
    Rotor i = new Rotor("I", pc);

    private String d = "(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)";
    Permutation pd = new Permutation(d, alphabet);
    Rotor ii = new Rotor("II", pd);

    private String e = "(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)";
    Permutation pe = new Permutation(e, alphabet);
    Rotor iii = new Rotor("III", pd);


    @Test
    public void testMachine() {
        ArrayList<Rotor> testrotors = new ArrayList<Rotor>();
        testrotors.add(B);
        testrotors.add(beta);
        testrotors.add(i);
        testrotors.add(ii);
        testrotors.add(iii);

    }
}


