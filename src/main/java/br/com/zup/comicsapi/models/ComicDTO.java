package br.com.zup.comicsapi.models;

import java.util.HashSet;
import java.util.Set;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.zup.comicsapi.groups.CustomGroup;
import br.com.zup.comicsapi.groups.FieldGroup;
import br.com.zup.comicsapi.validators.UniqueISBN;

@GroupSequence({FieldGroup.class, CustomGroup.class, UserDTO.class})
public class ComicDTO {

    private Long comicId;

    @NotBlank(message = "Title cannot be blank.", groups = FieldGroup.class)
    private String title;

    @NotNull(message = "Price cannot be null.", groups = FieldGroup.class)
    private Double price;

    @NotEmpty(message = "Authors cannot be empty.", groups = FieldGroup.class)
    private Set<String> authors = new HashSet<>();

    @NotBlank(message = "ISBN cannot be blank.", groups = FieldGroup.class)
    @UniqueISBN(message = "ISBN already exists.", groups = CustomGroup.class)
    private String isbn;

    @NotBlank(message = "Description cannot be blank.", groups = FieldGroup.class)
    private String description;

    public ComicDTO() {}

    public ComicDTO(String title, double price, String isbn, String description) {
        this.title = title;
        this.price = price;
        this.isbn = isbn;
        this.description = description;
    }

    public ComicDTO(Long comicId, String title, double price, String isbn, String description) {
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

}
