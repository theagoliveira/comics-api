package br.com.zup.comicsapi.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.comicsapi.converters.ComicConverter;
import br.com.zup.comicsapi.converters.UserConverter;
import br.com.zup.comicsapi.exceptions.InvalidUserIdException;
import br.com.zup.comicsapi.models.ComicDTO;
import br.com.zup.comicsapi.models.UserDTO;
import br.com.zup.comicsapi.repositories.UserRepository;
import br.com.zup.comicsapi.services.UserService;

@RestController
@RequestMapping(value = UserController.BASE_URI)
public class UserController {

    public static final String BASE_URI = "/users";

    private final UserService userService;
    private final UserConverter userConverter;
    private final UserRepository userRepository;
    private final ComicConverter comicConverter;

    public UserController(UserService userService, UserConverter userConverter,
                          UserRepository userRepository, ComicConverter comicConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.userRepository = userRepository;
        this.comicConverter = comicConverter;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@RequestBody @Valid UserDTO userDTO) {
        userService.save(userConverter.toEntity(userDTO));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserDTO> index() {
        return userService.findAll()
                          .stream()
                          .map(userConverter::toDto)
                          .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{userId}/comics")
    public List<ComicDTO> userComics(@PathVariable("userId") Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new InvalidUserIdException("Error when searching user comics.");
        }

        return userService.findComicsById(userId)
                          .stream()
                          .map(comicConverter::toDto)
                          .collect(Collectors.toList());
    }

}
