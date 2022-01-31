package br.com.zup.comicsapi.controllers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.xml.bind.DatatypeConverter;

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
import br.com.zup.comicsapi.marvel.MarvelObject;
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
    private final ComicRepository comicRepository;
    private final MarvelApiClient marvelApiClient;
    private final UserRepository userRepository;

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

        // Set up Marvel API authentication
        Map<String, String> env = System.getenv();

        String apiPubKey = env.get("MARVEL_API_PUBLIC_KEY");
        String apiPrivKey = env.get("MARVEL_API_PRIVATE_KEY");
        String timestamp = String.valueOf(Instant.now().getEpochSecond());

        byte[] bytesOfMessage = (timestamp + apiPrivKey + apiPubKey).getBytes(
            StandardCharsets.UTF_8
        );
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytesOfDigest = md.digest(bytesOfMessage);
        String hash = DatatypeConverter.printHexBinary(bytesOfDigest).toLowerCase();

        // Get the result from Marvel API
        MarvelObject marvelObject = marvelApiClient.getComicByComicId(
            comicId, timestamp, apiPubKey, hash
        );

        // Convert the result to a ComicDTO
        ComicDTO comicDto = comicConverter.toDto(marvelObject);

        // Validate the ComicDTO
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ComicDTO>> violations = validator.validate(comicDto);
        if (!violations.isEmpty()) {
            String errorWord = violations.size() == 1 ? "error" : "errors";
            String message = "Validation failed with " + violations.size() + " " + errorWord + ".";
            throw new ConstraintViolationException(message, violations);
        }

        // Check if the ISBN is valid
        String isbn = comicDto.getIsbn();

        boolean validIsbn = !comicRepository.existsByIsbn(isbn)
                || comicRepository.findByIsbn(isbn).getComicId().equals(comicId);

        if (validIsbn) {
            comicService.save(
                comicConverter.toEntity(comicDto), userRepository.findById(userId).get()
            );
        } else {
            throw new InvalidIsbnException("Error when creating a new comic.");
        }
    }

}
