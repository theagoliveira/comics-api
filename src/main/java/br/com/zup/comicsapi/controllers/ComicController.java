package br.com.zup.comicsapi.controllers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.comicsapi.converters.ComicConverter;
import br.com.zup.comicsapi.exceptions.InvalidIsbnException;
import br.com.zup.comicsapi.exceptions.InvalidUserIdException;
import br.com.zup.comicsapi.feign.MarvelApiClient;
import br.com.zup.comicsapi.marvel.MarvelResponse;
import br.com.zup.comicsapi.models.Comic;
import br.com.zup.comicsapi.repositories.ComicRepository;
import br.com.zup.comicsapi.repositories.UserRepository;
import br.com.zup.comicsapi.services.ComicService;

@RestController
@RequestMapping(value = ComicController.BASE_URI)
public class ComicController {

    public static final String BASE_URI = "/comics";

    private final ComicService comicService;
    private final ComicConverter comicConverter;
    private final ComicRepository comicRepository;
    private final MarvelApiClient marvelApiClient;
    private final UserRepository userRepository;

    @Value("${marvel.api.public.key}")
    private String pubKey;

    @Value("${marvel.api.private.key}")
    private String privKey;

    public ComicController(ComicService comicService, ComicConverter comicConverter,
                           ComicRepository comicRepository, MarvelApiClient marvelApiClient,
                           UserRepository userRepository) {
        this.comicService = comicService;
        this.comicConverter = comicConverter;
        this.comicRepository = comicRepository;
        this.marvelApiClient = marvelApiClient;
        this.userRepository = userRepository;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{comicId}")
    public void create(@PathVariable("comicId") Long comicId,
                       @RequestParam("userId") Long userId) throws NoSuchAlgorithmException {
        // Check if the user exists
        if (!userRepository.existsById(userId)) {
            throw new InvalidUserIdException("Error when creating a new comic.");
        }

        // Get the number of seconds from 1970-01-01 00:00:00 as a String
        String timestamp = String.valueOf(Instant.now().getEpochSecond());

        // String to bytes
        byte[] bytesOfMessage = (timestamp + privKey + pubKey).getBytes(StandardCharsets.UTF_8);
        // Hash object (throws NoSuchAlgorithmException)
        MessageDigest md = MessageDigest.getInstance("MD5");
        // Hash the information
        byte[] bytesOfDigest = md.digest(bytesOfMessage);
        // Convert bytes to String
        String hash = DatatypeConverter.printHexBinary(bytesOfDigest).toLowerCase();

        // Get the result from Marvel API
        MarvelResponse marvelResponse = marvelApiClient.getComicByComicId(
            comicId, timestamp, pubKey, hash
        );

        // Convert the result to a Comic
        Comic comic = comicConverter.toEntity(marvelResponse);

        // Validate the Comic
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Comic>> violations = validator.validate(comic);
        if (!violations.isEmpty()) {
            String errorWord = violations.size() == 1 ? "error" : "errors";
            String message = "Validation failed with " + violations.size() + " " + errorWord + ".";
            throw new ConstraintViolationException(message, violations);
        }

        // Check if the ISBN is valid
        String isbn = comic.getIsbn();

        boolean validIsbn = !comicRepository.existsByIsbn(isbn)
                || comicRepository.findByIsbn(isbn).getComicId().equals(comicId);

        if (validIsbn) {
            comicService.save(comic, userRepository.findById(userId).get());
        } else {
            throw new InvalidIsbnException("Error when creating a new comic.");
        }
    }

}
