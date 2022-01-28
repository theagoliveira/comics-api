package br.com.zup.comicsapi.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.comicsapi.converters.UserConverter;
import br.com.zup.comicsapi.models.UserDTO;
import br.com.zup.comicsapi.services.UserService;

@RestController
@RequestMapping(value = UserController.BASE_URI)
public class UserController {

    public static final String BASE_URI = "/users";

    private final UserConverter userConverter;
    private final UserService userService;

    public UserController(UserService userService, UserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@RequestBody @Valid UserDTO userDTO) {
        userService.save(userConverter.toEntity(userDTO));
    }

}
