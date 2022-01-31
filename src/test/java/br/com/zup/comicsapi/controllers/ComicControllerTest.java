package br.com.zup.comicsapi.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import br.com.zup.comicsapi.marvel.MarvelObject;
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
        verify(comicConverter, never()).toDto(any(MarvelObject.class));
        verify(comicService, never()).save(any(Comic.class), any(User.class));
    }

    @Test
    void createNoTitle() throws Exception {
        ComicDTO comicDto = new ComicDTO(1L, "", 1.99, AUTHORS, "12345678900", "A comic");

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(comicConverter.toDto(any(MarvelObject.class))).thenReturn(comicDto);

        mockMvc.perform(post(ComicController.BASE_URI + "/" + 100L).param("userId", "1"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errors.title", equalTo("Title cannot be blank.")));

        verify(userRepository).existsById(anyLong());
        verify(comicConverter).toDto(any(MarvelObject.class));
        verify(comicService, never()).save(any(Comic.class), any(User.class));
    }

    @Test
    void createNoIsbn() throws Exception {
        ComicDTO comicDto = new ComicDTO(1L, "Title1", 1.99, AUTHORS, "", "A comic");

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(comicConverter.toDto(any(MarvelObject.class))).thenReturn(comicDto);

        mockMvc.perform(post(ComicController.BASE_URI + "/" + 100L).param("userId", "1"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errors.isbn", equalTo("ISBN cannot be blank.")));

        verify(userRepository).existsById(anyLong());
        verify(comicConverter).toDto(any(MarvelObject.class));
        verify(comicService, never()).save(any(Comic.class), any(User.class));
    }

    @Test
    void createNoDescription() throws Exception {
        ComicDTO comicDto = new ComicDTO(1L, "Title1", 1.99, AUTHORS, "12345678900", "");

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(comicConverter.toDto(any(MarvelObject.class))).thenReturn(comicDto);

        mockMvc.perform(post(ComicController.BASE_URI + "/" + 100L).param("userId", "1"))
               .andExpect(status().isBadRequest())
               .andExpect(
                   jsonPath("$.errors.description", equalTo("Description cannot be blank."))
               );

        verify(userRepository).existsById(anyLong());
        verify(comicConverter).toDto(any(MarvelObject.class));
        verify(comicService, never()).save(any(Comic.class), any(User.class));
    }

    @Test
    void createNoPrice() throws Exception {
        ComicDTO comicDto = new ComicDTO(1L, "Title1", null, AUTHORS, "12345678900", "A comic");

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(comicConverter.toDto(any(MarvelObject.class))).thenReturn(comicDto);

        mockMvc.perform(post(ComicController.BASE_URI + "/" + 100L).param("userId", "1"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errors.price", equalTo("Price cannot be null.")));

        verify(userRepository).existsById(anyLong());
        verify(comicConverter).toDto(any(MarvelObject.class));
        verify(comicService, never()).save(any(Comic.class), any(User.class));
    }

    @Test
    void createNoAuthors() throws Exception {
        ComicDTO comicDto = new ComicDTO(
            1L, "Title1", 1.99, new HashSet<>(), "12345678900", "A comic"
        );

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(comicConverter.toDto(any(MarvelObject.class))).thenReturn(comicDto);

        mockMvc.perform(post(ComicController.BASE_URI + "/" + 100L).param("userId", "1"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errors.authors", equalTo("Authors cannot be empty.")));

        verify(userRepository).existsById(anyLong());
        verify(comicConverter).toDto(any(MarvelObject.class));
        verify(comicService, never()).save(any(Comic.class), any(User.class));
    }

    @Test
    void createBlank() throws Exception {
        ComicDTO comicDto = new ComicDTO(1L, "", null, new HashSet<>(), "", "");

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(comicConverter.toDto(any(MarvelObject.class))).thenReturn(comicDto);

        mockMvc.perform(post(ComicController.BASE_URI + "/" + 100L).param("userId", "1"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errors.title", equalTo("Title cannot be blank.")))
               .andExpect(jsonPath("$.errors.isbn", equalTo("ISBN cannot be blank.")))
               .andExpect(jsonPath("$.errors.description", equalTo("Description cannot be blank.")))
               .andExpect(jsonPath("$.errors.price", equalTo("Price cannot be null.")))
               .andExpect(jsonPath("$.errors.authors", equalTo("Authors cannot be empty.")));

        verify(userRepository).existsById(anyLong());
        verify(comicConverter).toDto(any(MarvelObject.class));
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
        verify(comicConverter, never()).toDto(any(MarvelObject.class));
        verify(comicService, never()).save(any(Comic.class), any(User.class));
    }

    @Test
    void createInvalidIsbn() throws Exception {
        ComicDTO comicDto = new ComicDTO(1L, "Title1", 1.99, AUTHORS, "12345678900", "A comic");
        Comic comic = new Comic(1L, "Title1", 1.99, AUTHORS, "12345678900", "A comic");

        when(comicConverter.toDto(any(MarvelObject.class))).thenReturn(comicDto);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(comicRepository.existsByIsbn(anyString())).thenReturn(true);
        when(comicRepository.findByIsbn(anyString())).thenReturn(comic);

        mockMvc.perform(post(ComicController.BASE_URI + "/" + 100L).param("userId", "1"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message", equalTo("Error when creating a new comic.")))
               .andExpect(jsonPath("$.errors[0]", equalTo("The provided ISBN is invalid.")));

        verify(comicConverter).toDto(any(MarvelObject.class));
        verify(userRepository).existsById(anyLong());
        verify(comicRepository).existsByIsbn(anyString());
        verify(comicRepository).findByIsbn(anyString());
        verify(comicService, never()).save(any(Comic.class), any(User.class));
    }

    @Test
    void createValid() throws Exception {
        ComicDTO comicDto = new ComicDTO(100L, "Title1", 1.99, AUTHORS, "12345678900", "A comic");
        Comic comic = new Comic(100L, "Title1", 1.99, AUTHORS, "12345678900", "A comic");
        User user = new User(1L, "User", "user@example.com", "84848518423", "01/01/1991");

        when(comicConverter.toDto(any(MarvelObject.class))).thenReturn(comicDto);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(comicRepository.existsByIsbn(anyString())).thenReturn(true);
        when(comicRepository.findByIsbn(anyString())).thenReturn(comic);
        when(comicService.save(any(Comic.class), any(User.class))).thenReturn(comic);
        when(comicConverter.toEntity(any(ComicDTO.class))).thenReturn(comic);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        mockMvc.perform(post(ComicController.BASE_URI + "/" + 100L).param("userId", "1"))
               .andExpect(status().isCreated());

        verify(comicConverter).toDto(any(MarvelObject.class));
        verify(userRepository).existsById(anyLong());
        verify(comicRepository).existsByIsbn(anyString());
        verify(comicRepository).findByIsbn(anyString());
        verify(comicService).save(any(Comic.class), any(User.class));
        verify(comicConverter).toEntity(any(ComicDTO.class));
        verify(userRepository).findById(anyLong());
    }

    @Test
    void index() throws Exception {
        Comic comic1 = new Comic(1L, "Title1", 1.99, AUTHORS, "12345678900", "A comic");
        ComicDTO comic1Dto = new ComicDTO(1L, "Title1", 1.99, AUTHORS, "12345678900", "A comic");

        when(comicService.findAll()).thenReturn(List.of(comic1, comic1));
        when(comicConverter.toDto(any(Comic.class))).thenReturn(comic1Dto);

        mockMvc.perform(get(ComicController.BASE_URI))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$.[0].comicId", equalTo(1)))
               .andExpect(jsonPath("$.[0].title", equalTo("Title1")))
               .andExpect(jsonPath("$.[0].price", equalTo(1.99)))
               .andExpect(jsonPath("$.[0].isbn", equalTo("12345678900")))
               .andExpect(jsonPath("$.[0].description", equalTo("A comic")));

        verify(comicService).findAll();
        verify(comicConverter, times(2)).toDto(any(Comic.class));
    }

    @Test
    void deleteNotSupported() throws Exception {
        mockMvc.perform(delete(ComicController.BASE_URI)).andExpect(status().isMethodNotAllowed());
    }

    @Test
    void putNotSupported() throws Exception {
        mockMvc.perform(put(ComicController.BASE_URI)).andExpect(status().isMethodNotAllowed());
    }

    @Test
    void patchNotSupported() throws Exception {
        mockMvc.perform(patch(ComicController.BASE_URI)).andExpect(status().isMethodNotAllowed());
    }

}
