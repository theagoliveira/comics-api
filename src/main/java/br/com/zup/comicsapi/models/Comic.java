package br.com.zup.comicsapi.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Comic {

    @Id
    private Long comicId;

    private String title;
    private double price;
    private Set<String> authors = new HashSet<>();
    private String isbn;
    private String description;

    @ManyToMany(mappedBy = "comics")
    private Set<User> users = new HashSet<>();

    public Comic() {}

    public Comic(String title, double price, String isbn, String description) {
        this.title = title;
        this.price = price;
        this.isbn = isbn;
        this.description = description;
    }

    public Comic(Long comicId, String title, double price, String isbn, String description) {
        this.comicId = comicId;
        this.title = title;
        this.price = price;
        this.isbn = isbn;
        this.description = description;
    }

    public Long getComicId() {
        return comicId;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
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

    public Set<User> getUsers() {
        return users;
    }

}
