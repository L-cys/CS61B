package enigma;


/**
 * Superclass that represents a rotor in the enigma machine.
 *
 * @author Yuanshan Chen
 */
class Rotor {

    /**
     * A rotor named NAME whose permutation is given by PERM.
     */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
    }

    /**
     * Return my name.
     */
    String name() {
        return _name;
    }

    /**
     * Return my alphabet.
     */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /**
     * Return my permutation.
     */
    Permutation permutation() {
        return _permutation;
    }

    /**
     * Return the size of my alphabet.
     */
    int size() {
        return _permutation.size();
    }

    /**
     * Return true iff I have a ratchet and can move.
     */
    boolean rotates() {
        return false;
    }

    /**
     * Return true iff I reflect.
     */
    boolean reflecting() {
        return false;
    }

    /**
     * Return my current setting.
     */
    int setting() {
        return _setting;
    }

    /**
     * Set setting() to POSN.
     */
    void set(int posn) {
        _setting = _permutation.wrap(posn);
    }

    /**
     * Set setting() to character CPOSN.
     */
    void set(char cposn) {
        int bf = _permutation.alphabet().toInt(cposn);
        _setting = _permutation.wrap(bf);
    }

    /**
     * Return the conversion of P (an integer in the range 0..size()-1)
     * according to my permutation.
     */
    int convertForward(int p) {
        int mdl = _permutation.wrap(p + _setting);
        int bf = _permutation.permute(mdl);
        return _permutation.wrap(bf - _setting);
    }

    /**
     * Return the conversion of E (an integer in the range 0..size()-1)
     * according to the inverse of my permutation.
     */
    int convertBackward(int e) {
        int mdla = _permutation.wrap(e + _setting);
        int bfa = _permutation.invert(mdla);
        return _permutation.wrap(bfa - _setting);
    }

    /**
     * Returns true iff I am positioned to allow the rotor to my left
     * to advance.
     */
    boolean atNotch() {
        return false;
    }

    /**
     * Create a helper function to deal with ring A.
     * If there is no ring, do nothing.
     */
    void setring(char a) {
        int now = _permutation.wrap(_permutation.alphabet().toInt(a));
        this.ringn = now;
        this.set(_permutation.wrap(this.setting() - now));
    }

    /**
     * Advance me one position, if possible. By default, does nothing.
     */
    void advance() {
    }

    /**
     * return the number of my ring.
     */
    int findringnum() {
        return ringn;
    }


    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /**
     * My name.
     */
    private final String _name;

    /**
     * The permutation implemented by this rotor in its 0 position.
     */
    private Permutation _permutation;

    /**
     * The setting of this rotor.
     */
    private int _setting;

    /**
     * The alphabet applied by this rotor.
     */
    private Alphabet _alphabet;

    /**
     * Set the number of my ring.
     */
    private int ringn;

}
