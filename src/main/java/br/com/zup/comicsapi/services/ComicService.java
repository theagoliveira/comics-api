package br.com.zup.comicsapi.services;

import org.springframework.stereotype.Service;

import br.com.zup.comicsapi.repositories.ComicRepository;

@Service
public class ComicService {

    private final ComicRepository comicRepository;

    public ComicService(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

}
