/*
 * PROYECTO 4 Y 5
 * Nombre: Miriam G Ramirez Sanchez
 * Grupo: 7CM2
 */

package source;

import java.util.List;

public class SearchResponse {

    private int page;
    private int size;
    private int totalResults;

    private List<MatchResult> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<MatchResult> getResults() {
        return results;
    }

    public void setResults(List<MatchResult> results) {
        this.results = results;
    }
}