/*
 * PROYECTO 4 Y 5
 * Nombre: Miriam G Ramirez Sanchez
 * Grupo: 7CM2
 */

package source;

public class SearchRequest {

    private String bookA;
    private String bookB;
    private int n;

    public SearchRequest() {
    }

    public SearchRequest(String bookA, String bookB, int n) {
        this.bookA = bookA;
        this.bookB = bookB;
        this.n = n;
    }

    public String getBookA() {
        return bookA;
    }

    public void setBookA(String bookA) {
        this.bookA = bookA;
    }

    public String getBookB() {
        return bookB;
    }

    public void setBookB(String bookB) {
        this.bookB = bookB;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    @Override
    public String toString() {
        return "SearchRequest{" +
                "bookA='" + bookA + '\'' +
                ", bookB='" + bookB + '\'' +
                ", n=" + n +
                '}';
    }
}