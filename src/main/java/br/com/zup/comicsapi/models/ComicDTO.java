package br.com.zup.comicsapi.models;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ComicDTO {

    private Long comicId;

    @NotBlank(message = "Title cannot be blank.")
    private String title;

    @NotNull(message = "Price cannot be null.")
    private Double price;

    @NotEmpty(message = "Authors cannot be empty.")
    private Set<String> creators = new HashSet<>();

    @NotBlank(message = "ISBN cannot be blank.")
    private String isbn;

    @NotBlank(message = "Description cannot be blank.")
    private String description;

}
