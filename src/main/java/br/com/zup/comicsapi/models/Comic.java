package br.com.zup.comicsapi.models;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Comic {

    @Id
    private Long comicId;

    @NotBlank(message = "Title cannot be blank.")
    private String title;

    @NotNull(message = "Price cannot be null.")
    @Column(precision = 8, scale = 2)
    private BigDecimal price;

    private boolean discounted = false;

    @NotEmpty(message = "Authors cannot be empty.")
    @ElementCollection
    private Set<String> authors = new HashSet<>();

    @NotBlank(message = "ISBN cannot be blank.")
    private String isbn;

    @NotBlank(message = "Description cannot be blank.")
    @Lob
    @Column(columnDefinition = "CLOB")
    private String description;

    @ManyToMany(mappedBy = "comics")
    private Set<User> users;

    public Comic() {}

    public Comic(Long comicId, String title, BigDecimal price, Set<String> authors, String isbn,
                 String description) {
        this.comicId = comicId;
        this.title = title;
        this.price = price;
        this.authors = authors;
        this.isbn = isbn;
        this.description = description;
    }

    public Comic(Long comicId, String title, BigDecimal price, boolean discounted,
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

    public Set<User> getUsers() {
        return users;
    }

    public void setDiscounted(boolean discounted) {
        this.discounted = discounted;
    }

}
