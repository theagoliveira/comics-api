package br.com.zup.comicsapi.models;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class ComicDTO {

    private Long comicId;
    private String title;
    private BigDecimal price;
    private boolean discounted;
    private Set<String> authors = new HashSet<>();
    private String isbn;
    private String description;

    public ComicDTO() {}

    public ComicDTO(Long comicId, String title, BigDecimal price, Set<String> authors, String isbn,
                    String description) {
        this.comicId = comicId;
        this.title = title;
        this.price = price;
        this.authors = authors;
        this.isbn = isbn;
        this.description = description;
    }

    public ComicDTO(Long comicId, String title, BigDecimal price, boolean discounted,
                    Set<String> authors, String isbn, String description) {
        this.comicId = comicId;
        this.title = title;
        this.price = price;
        this.discounted = discounted;
        this.authors = authors;
        this.isbn = isbn;
        this.description = description;
    }

    public Long getComicId() {
        return comicId;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean getDiscounted() {
        return discounted;
    }

    public Set<String> getAuthors() {
        return authors;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getDescription() {
        return description;
    }

}
