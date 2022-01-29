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
    private Set<String> creators = new HashSet<>();
    private String isbn;
    private String description;

    @ManyToMany(mappedBy = "comics")
    private Set<User> user = new HashSet<>();

}
