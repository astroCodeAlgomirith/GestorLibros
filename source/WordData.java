package source;

public class WordData {

    private String word;
    private int offset;

    public WordData(String word, int offset) {
        this.word = word;
        this.offset = offset;
    }

    public String getWord() {
        return word;
    }

    public int getOffset() {
        return offset;
    }
}