package enigma;

/**
 * An alphabet of encodable characters.  Provides a mapping from characters
 * to and from indices into the alphabet.
 *
 * @author Yuanshan Chen
 */
class Alphabet {

    /**
     * A new alphabet containing CHARS.  Character number #k has index
     * K (numbering from 0). No character may be duplicated.
     */
    Alphabet(String chars) {
        _chars = chars;
    }

    /**
     * A default alphabet of all upper-case characters.
     */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /**
     * Returns the size of the alphabet.
     */
    int size() {
        return this._chars.length();
    }

    /**
     * Returns true if preprocess(CH) is in this alphabet.
     */
    boolean contains(char ch) {
        for (int i = 0; i < _chars.length(); i = i + 1) {
            if (toChar(i) == ch) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns character number INDEX in the alphabet, where
     * 0 <= INDEX < size().
     */
    char toChar(int index) {
        return this._chars.charAt(index);
    }

    /**
     * Returns the index of character preprocess(CH), which must be in
     * the alphabet. This is the inverse of toChar().
     */
    int toInt(char ch) {
        return this._chars.indexOf(ch);
    }


    /**
     * Define the chars of my alphabet.
     */
    private String _chars;

}
