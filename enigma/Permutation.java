package enigma;


/**
 * Represents a permutation of a range of integers starting at 0 corresponding
 * to the characters of an alphabet.
 *
 * @author Yuanshan Chen
 */
class Permutation {

    /**
     * Set this Permutation to that specified by CYCLES, a string in the
     * form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     * is interpreted as a permutation in cycle notation.  Characters in the
     * alphabet that are not included in any cycle map to themselves.
     * Whitespace is ignored.
     */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
    }

    /**
     * Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     * c0c1...cm.
     */
    private void addCycle(String cycle) {
        _cycles = _cycles + cycle;
    }

    /**
     * Return the value of P modulo the size of this permutation.
     */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /**
     * Returns the size of the alphabet I permute.
     */
    int size() {
        return this._alphabet.size();
    }

    /**
     * Return the result of applying this permutation to P modulo the
     * alphabet size.
     */
    int permute(int p) {
        int mdl = this.wrap(p);
        char bf = _alphabet.toChar(mdl);
        char aft = this.permute(bf);
        return _alphabet.toInt(aft);
    }

    /**
     * Return the result of applying the inverse of this permutation
     * to  C modulo the alphabet size.
     */
    int invert(int c) {
        int mdl = this.wrap(c);
        char bf = _alphabet.toChar(mdl);
        char aft = this.invert(bf);
        return _alphabet.toInt(aft);
    }

    /**
     * Return the result of applying this permutation to the index of P
     * in ALPHABET, and converting the result to a character of ALPHABET.
     */
    char permute(char p) {
        for (int i = 0; i < this._cycles.length(); i = i + 1) {
            if (this._cycles.charAt(i) == p) {
                int place = _cycles.indexOf(p);
                if (_cycles.charAt(place + 1) == ')') {
                    for (int x = place; x >= 0; x = x - 1) {
                        if (_cycles.charAt(x) == '(') {
                            return _cycles.charAt(x + 1);
                        }
                    }
                } else {
                    return _cycles.charAt(place + 1);
                }
            }
        }
        return p;
    }

    /**
     * Return the result of applying the inverse of this permutation to C.
     */

    char invert(char c) {
        for (int i = 0; i < this._cycles.length(); i = i + 1) {
            if (this._cycles.charAt(i) == c) {
                int place = _cycles.indexOf(_cycles.charAt(i));
                if (_cycles.charAt(place - 1) == '(') {
                    for (int x = place; x < _cycles.length(); x = x + 1) {
                        if (_cycles.charAt(x) == ')') {
                            char fin = _cycles.charAt(x - 1);
                            return fin;
                        }
                    }
                } else {
                    int afp = place - 1;
                    return _cycles.charAt(afp);
                }
            }
        }
        return c;
    }

    /**
     * Return the alphabet used to initialize this Permutation.
     */
    Alphabet alphabet() {
        return _alphabet;
    }

    /**
     * Return true iff this permutation is a derangement (i.e., a
     * permutation for which no value maps to itself).
     */
    boolean derangement() {
        for (int i = 0; i < this._cycles.length() - 2; i = i + 1) {
            if (this._cycles.charAt(i) == '(' && _cycles.charAt(i + 2) == ')') {
                return false;
            }
        }
        return true;
    }

    /**
     * Alphabet of this permutation.
     */
    private Alphabet _alphabet;

    /**
     * The _cycles of this permutation.
     */
    private String _cycles;

}
