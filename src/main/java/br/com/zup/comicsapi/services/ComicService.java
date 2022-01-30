package br.com.zup.comicsapi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.zup.comicsapi.models.Comic;
import br.com.zup.comicsapi.models.User;
import br.com.zup.comicsapi.repositories.ComicRepository;
import br.com.zup.comicsapi.repositories.UserRepository;

@Service
public class ComicService {

    private final ComicRepository comicRepository;
    private final UserRepository userRepository;

    public ComicService(ComicRepository comicRepository, UserRepository userRepository) {
        this.comicRepository = comicRepository;
        this.userRepository = userRepository;
    }

    public Comic save(Comic comic, User user) {
        Comic savedComic = comicRepository.save(comic);

        user.getComics().add(savedComic);
        userRepository.save(user);

        return savedComic;
    }

    public List<Comic> findAll() {
        return comicRepository.findAll();
    }

}
