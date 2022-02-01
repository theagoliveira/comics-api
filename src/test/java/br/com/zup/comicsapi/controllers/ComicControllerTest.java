package br.com.zup.comicsapi.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.zup.comicsapi.converters.ComicConverter;
import br.com.zup.comicsapi.marvel.MarvelResponse;
import br.com.zup.comicsapi.models.Comic;
import br.com.zup.comicsapi.models.ComicDTO;
import br.com.zup.comicsapi.models.User;
import br.com.zup.comicsapi.repositories.ComicRepository;
import br.com.zup.comicsapi.repositories.UserRepository;
import br.com.zup.comicsapi.services.ComicService;

@SpringBootTest
@AutoConfigureMockMvc
class ComicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ComicService comicService;

    @MockBean
    ComicConverter comicConverter;

    @MockBean
    ComicRepository comicRepository;

    @MockBean
    UserRepository userRepository;

    private static final Set<String> AUTHORS = new HashSet<>(List.of("Author 1", "Author 2"));
    private static final Long NOT_FOUND_COMIC_ID = 774448L;

    @Test
    void createNotFound() throws Exception {
        when(userRepository.existsById(anyLong())).thenReturn(true);

        mockMvc.perform(
            post(ComicController.BASE_URI + "/" + NOT_FOUND_COMIC_ID).param("userId", "1")
        )
               .andExpect(status().isNotFound())
               .andExpect(
                   jsonPath("$.message", equalTo("Error when retrieving comic data from Marvel."))
               )
               .andExpect(jsonPath("$.errors[0]", equalTo("We couldn't find that comic_issue")));

        verify(userRepository).existsById(anyLong());
        verify(comicConverter, never()).toEntity(any(MarvelResponse.class));
        verify(comicService, never()).save(any(Comic.class), any(User.class));
    }

    @Test
    void createNoTitle() throws Exception {
        Comic comic = new Comic(1L, "", new BigDecimal("1.99"), AUTHORS, "12345678900", "A comic");

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(comicConverter.toEntity(any(MarvelResponse.class))).thenReturn(comic);

        mockMvc.perform(post(ComicController.BASE_URI + "/" + 100L).param("userId", "1"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errors.title", equalTo("Title cannot be blank.")));

        verify(userRepository).existsById(anyLong());
        verify(comicConverter).toEntity(any(MarvelResponse.class));
        verify(comicService, never()).save(any(Comic.class), any(User.class));
    }

    @Test
    void createNoIsbn() throws Exception {
        Comic comic = new Comic(1L, "Title1", new BigDecimal("1.99"), AUTHORS, "", "A comic");

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(comicConverter.toEntity(any(MarvelResponse.class))).thenReturn(comic);

        mockMvc.perform(post(ComicController.BASE_URI + "/" + 100L).param("userId", "1"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errors.isbn", equalTo("ISBN cannot be blank.")));

        verify(userRepository).existsById(anyLong());
        verify(comicConverter).toEntity(any(MarvelResponse.class));
        verify(comicService, never()).save(any(Comic.class), any(User.class));
    }

    @Test
    void createNoDescription() throws Exception {
        Comic comic = new Comic(1L, "Title1", new BigDecimal("1.99"), AUTHORS, "12345678900", "");

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(comicConverter.toEntity(any(MarvelResponse.class))).thenReturn(comic);

        mockMvc.perform(post(ComicController.BASE_URI + "/" + 100L).param("userId", "1"))
               .andExpect(status().isBadRequest())
               .andExpect(
                   jsonPath("$.errors.description", equalTo("Description cannot be blank."))
               );

        verify(userRepository).existsById(anyLong());
        verify(comicConverter).toEntity(any(MarvelResponse.class));
        verify(comicService, never()).save(any(Comic.class), any(User.class));
    }

    @Test
    void createNoPrice() throws Exception {
        Comic comic = new Comic(1L, "Title1", null, AUTHORS, "12345678900", "A comic");

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(comicConverter.toEntity(any(MarvelResponse.class))).thenReturn(comic);

        mockMvc.perform(post(ComicController.BASE_URI + "/" + 100L).param("userId", "1"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errors.price", equalTo("Price cannot be null.")));

        verify(userRepository).existsById(anyLong());
        verify(comicConverter).toEntity(any(MarvelResponse.class));
        verify(comicService, never()).save(any(Comic.class), any(User.class));
    }

    @Test
    void createNoAuthors() throws Exception {
        Comic comic = new Comic(
            1L, "Title1", new BigDecimal("1.99"), new HashSet<>(), "12345678900", "A comic"
        );

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(comicConverter.toEntity(any(MarvelResponse.class))).thenReturn(comic);

        mockMvc.perform(post(ComicController.BASE_URI + "/" + 100L).param("userId", "1"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errors.authors", equalTo("Authors cannot be empty.")));

        verify(userRepository).existsById(anyLong());
        verify(comicConverter).toEntity(any(MarvelResponse.class));
        verify(comicService, never()).save(any(Comic.class), any(User.class));
    }

    @Test
    void createBlank() throws Exception {
        Comic comic = new Comic(1L, "", null, new HashSet<>(), "", "");

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(comicConverter.toEntity(any(MarvelResponse.class))).thenReturn(comic);

        mockMvc.perform(post(ComicController.BASE_URI + "/" + 100L).param("userId", "1"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errors.title", equalTo("Title cannot be blank.")))
               .andExpect(jsonPath("$.errors.isbn", equalTo("ISBN cannot be blank.")))
               .andExpect(jsonPath("$.errors.description", equalTo("Description cannot be blank.")))
               .andExpect(jsonPath("$.errors.price", equalTo("Price cannot be null.")))
               .andExpect(jsonPath("$.errors.authors", equalTo("Authors cannot be empty.")));

        verify(userRepository).existsById(anyLong());
        verify(comicConverter).toEntity(any(MarvelResponse.class));
        verify(comicService, never()).save(any(Comic.class), any(User.class));
    }

    @Test
    void createInvalidUserId() throws Exception {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        mockMvc.perform(post(ComicController.BASE_URI + "/" + 100L).param("userId", "1"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message", equalTo("Error when creating a new comic.")))
               .andExpect(jsonPath("$.errors[0]", equalTo("The provided userId is invalid.")));

        verify(userRepository).existsById(anyLong());
        verify(comicConverter, never()).toEntity(any(MarvelResponse.class));
        verify(comicService, never()).save(any(Comic.class), any(User.class));
    }

    @Test
    void createInvalidIsbn() throws Exception {
        Comic comic = new Comic(
            1L, "Title1", new BigDecimal("1.99"), AUTHORS, "12345678900", "A comic"
        );

        when(comicConverter.toEntity(any(MarvelResponse.class))).thenReturn(comic);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(comicRepository.existsByIsbn(anyString())).thenReturn(true);
        when(comicRepository.findByIsbn(anyString())).thenReturn(comic);

        mockMvc.perform(post(ComicController.BASE_URI + "/" + 100L).param("userId", "1"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message", equalTo("Error when creating a new comic.")))
               .andExpect(jsonPath("$.errors[0]", equalTo("The provided ISBN is invalid.")));

        verify(comicConverter).toEntity(any(MarvelResponse.class));
        verify(userRepository).existsById(anyLong());
        verify(comicRepository).existsByIsbn(anyString());
        verify(comicRepository).findByIsbn(anyString());
        verify(comicService, never()).save(any(Comic.class), any(User.class));
    }

    @Test
    void createValid() throws Exception {
        Comic comic = new Comic(
            100L, "Title1", new BigDecimal("1.99"), AUTHORS, "12345678900", "A comic"
        );
        User user = new User(1L, "User", "user@example.com", "84848518423", "01/01/1991");

        when(comicConverter.toEntity(any(MarvelResponse.class))).thenReturn(comic);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(comicRepository.existsByIsbn(anyString())).thenReturn(true);
        when(comicRepository.findByIsbn(anyString())).thenReturn(comic);
        when(comicService.save(any(Comic.class), any(User.class))).thenReturn(comic);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        mockMvc.perform(post(ComicController.BASE_URI + "/" + 100L).param("userId", "1"))
               .andExpect(status().isCreated());

        verify(comicConverter).toEntity(any(MarvelResponse.class));
        verify(userRepository).existsById(anyLong());
        verify(comicRepository).existsByIsbn(anyString());
        verify(comicRepository).findByIsbn(anyString());
        verify(comicService).save(any(Comic.class), any(User.class));
        verify(userRepository).findById(anyLong());
    }

}
