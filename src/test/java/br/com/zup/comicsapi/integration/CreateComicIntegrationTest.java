package br.com.zup.comicsapi.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import br.com.zup.comicsapi.controllers.ComicController;
import br.com.zup.comicsapi.models.Comic;
import br.com.zup.comicsapi.models.User;
import br.com.zup.comicsapi.repositories.ComicRepository;
import br.com.zup.comicsapi.repositories.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class CreateComicIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ComicRepository comicRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        userRepository.save(new User("User 1", "user1@example.com", "12345678901", "01/01/1991"));
    }

    // Problem with user.getComics()
    @Test
    @Transactional
    void createComic() throws Exception {
        Long comicId = 4100L;
        String isbn = "0-7851-2101-3";

        User user = userRepository.findAll().get(0);
        assertEquals(0, user.getComics().size());

        mockMvc.perform(
            post(ComicController.BASE_URI + "/" + comicId).param("userId", user.getId().toString())
        ).andExpect(status().isCreated());

        Comic comic = comicRepository.findByIsbn(isbn);

        assertNotNull(comic);
        assertEquals(comicId, comic.getComicId());
        assertEquals(1, user.getComics().size());
        assertEquals(true, user.getComics().contains(comic));
    }

}
