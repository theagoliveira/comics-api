package br.com.zup.comicsapi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.zup.comicsapi.models.Comic;
import br.com.zup.comicsapi.models.User;
import br.com.zup.comicsapi.repositories.ComicRepository;
import br.com.zup.comicsapi.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class ComicServiceTest {

    @Mock
    ComicRepository comicRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ComicService comicService;

    private static final Set<String> AUTHORS = new HashSet<>(List.of("Author 1", "Author 2"));

    @Test
    void save() {
        Comic comicToSave = new Comic(1L, "Title1", 1.99, AUTHORS, "12345678900", "A comic");
        Comic returnedComic = new Comic(1L, "Title1", 1.99, AUTHORS, "12345678900", "A comic");
        User user = new User(1L, "User", "user@example.com", "12345678900", "01/01/1991");

        when(comicRepository.save(any(Comic.class))).thenReturn(returnedComic);
        Comic savedComic = comicService.save(comicToSave, user);

        assertNotNull(savedComic);
        assertEquals(1L, savedComic.getComicId());
        verify(comicRepository).save(any(Comic.class));
        verify(userRepository).save(any(User.class));
    }

}
