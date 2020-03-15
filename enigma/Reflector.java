package enigma;

import static enigma.EnigmaException.*;

/**
 * Class that represents a reflector in the enigma.
 *
 * @author Yuanshan Chen
 */
class Reflector extends FixedRotor {

    /**
     * A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM.
     */
    Reflector(String name, Permutation perm) {
        super(name, perm);
    }

    @Override
    /**
     * A reflector which reflecting should be true.
     */
    boolean reflecting() {
        return true;
    }

    @Override
    /**
     * A reflector would never converbackward.
     */
    int convertBackward(int e) {
        throw error("Reflector never convertBackward");
    }

    @Override
    /**
     * A reflector should only have one position, if not, throw error.
     */
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }

}
