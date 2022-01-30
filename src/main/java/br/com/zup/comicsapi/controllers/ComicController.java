package br.com.zup.comicsapi.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.comicsapi.converters.ComicConverter;
import br.com.zup.comicsapi.feign.MarvelApiClient;
import br.com.zup.comicsapi.models.ComicDTO;
import br.com.zup.comicsapi.repositories.ComicRepository;
import br.com.zup.comicsapi.repositories.UserRepository;
import br.com.zup.comicsapi.services.ComicService;

@RestController
@RequestMapping(value = ComicController.BASE_URI)
public class ComicController {

    public static final String BASE_URI = "/comics";

    private final ComicService comicService;
    private final ComicConverter comicConverter;

    public ComicController(ComicService comicService, ComicConverter comicConverter,
                           ComicRepository comicRepository, MarvelApiClient marvelApiClient,
                           UserRepository userRepository) {
        this.comicService = comicService;
        this.comicConverter = comicConverter;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ComicDTO> index() {
        return comicService.findAll()
                           .stream()
                           .map(comicConverter::toDto)
                           .collect(Collectors.toList());
    }

}
