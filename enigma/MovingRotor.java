package enigma;

/**
 * Class that represents a rotating rotor in the enigma machine.
 *
 * @author Yuanshan Chen
 */
class MovingRotor extends Rotor {

    /**
     * A rotor named NAME whose permutation in its default setting is
     * PERM, and whose notches are at the positions indicated in NOTCHES.
     * The Rotor is initally in its 0 setting (first character of its
     * alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _permutation = perm;
    }

    @Override
    /**
     * A moving rotor can rotate.
     */
    boolean rotates() {
        return true;
    }


    @Override
    /**
     * Introduce its NOTCH situation.
     */
    boolean atNotch() {

        for (int i = 0; i < _notches.length(); i = i + 1) {
            char a = _notches.charAt(i);
            int b = alphabet().toInt(a);
            b = permutation().wrap(b - findringnum());
            char c = alphabet().toChar(b);
            if (c == _permutation.alphabet().toChar(setting())) {
                return true;
            }
        }
        return false;
    }

    @Override
    /**
     * Set this MovingRotor's position in the case of setting
     * with one move.
     * Should set it in one position advance.
     */
    void advance() {
        super.set(_permutation.wrap(super.setting() + 1));
    }


    /**
     * Set this MovingRotor's notches.
     */
    private String _notches;

    /**
     * Set this rotor's permutation.
     */
    private Permutation _permutation;


}
