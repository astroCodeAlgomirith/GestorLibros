/*
 * PROYECTO 4 Y 5
 * Nombre: Miriam G Ramirez Sanchez
 * Grupo: 7CM2
 */

package source;

public class MatchResult {

    private String bookA;
    private int offsetA;

    private String bookB;
    private int offsetB;

    private String phrase;

    public MatchResult() {
    }

    public MatchResult(
            String bookA,
            int offsetA,
            String bookB,
            int offsetB,
            String phrase
    ) {
        this.bookA = bookA;
        this.offsetA = offsetA;
        this.bookB = bookB;
        this.offsetB = offsetB;
        this.phrase = phrase;
    }

    public String getBookA() {
        return bookA;
    }

    public void setBookA(String bookA) {
        this.bookA = bookA;
    }

    public int getOffsetA() {
        return offsetA;
    }

    public void setOffsetA(int offsetA) {
        this.offsetA = offsetA;
    }

    public String getBookB() {
        return bookB;
    }

    public void setBookB(String bookB) {
        this.bookB = bookB;
    }

    public int getOffsetB() {
        return offsetB;
    }

    public void setOffsetB(int offsetB) {
        this.offsetB = offsetB;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    @Override
    public String toString() {
        return "MatchResult{" +
                "bookA='" + bookA + '\'' +
                ", offsetA=" + offsetA +
                ", bookB='" + bookB + '\'' +
                ", offsetB=" + offsetB +
                ", phrase='" + phrase + '\'' +
                '}';
    }
}