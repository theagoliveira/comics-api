package br.com.zup.comicsapi.marvel;

import java.util.List;

public class MarvelCreators {

    private List<MarvelCreatorsItem> items;

    public MarvelCreators() {}

    public MarvelCreators(List<MarvelCreatorsItem> items) {
        this.items = items;
    }

    public List<MarvelCreatorsItem> getItems() {
        return items;
    }

}
