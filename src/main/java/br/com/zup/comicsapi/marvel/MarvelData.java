package br.com.zup.comicsapi.marvel;

import java.util.List;

public class MarvelData {

    private List<MarvelResult> results;

    public MarvelData() {}

    public MarvelData(List<MarvelResult> results) {
        this.results = results;
    }

    public List<MarvelResult> getResults() {
        return results;
    }

}
