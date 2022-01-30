package br.com.zup.comicsapi.marvel;

import java.util.List;

public class MarvelResult {

    private Long id;
    private String title;
    private String description;
    private String isbn;
    private List<MarvelPrice> prices;
    private MarvelCreators creators;

    public MarvelResult() {}

    public MarvelResult(Long id, String title, String description, String isbn,
                        List<MarvelPrice> prices, MarvelCreators creators) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isbn = isbn;
        this.prices = prices;
        this.creators = creators;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getIsbn() {
        return isbn;
    }

    public List<MarvelPrice> getPrices() {
        return prices;
    }

    public MarvelCreators getCreators() {
        return creators;
    }

}
