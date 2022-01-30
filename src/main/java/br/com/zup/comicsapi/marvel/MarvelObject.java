package br.com.zup.comicsapi.marvel;

public class MarvelObject {

    private MarvelData data;

    public MarvelObject() {}

    public MarvelObject(MarvelData data) {
        this.data = data;
    }

    public MarvelData getData() {
        return data;
    }

}
