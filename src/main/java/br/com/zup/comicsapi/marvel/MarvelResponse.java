package br.com.zup.comicsapi.marvel;

public class MarvelResponse {

    private MarvelData data;

    public MarvelResponse() {}

    public MarvelResponse(MarvelData data) {
        this.data = data;
    }

    public MarvelData getData() {
        return data;
    }

}
