package br.com.zup.comicsapi.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.zup.comicsapi.controllers.UserController;
import br.com.zup.comicsapi.models.Comic;
import br.com.zup.comicsapi.models.User;
import br.com.zup.comicsapi.repositories.ComicRepository;
import br.com.zup.comicsapi.repositories.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class ListComicsIntegrationTest {

    private final static LocalDate TEST_DATE_MONDAY = LocalDate.of(2022, 01, 31);
    private final static LocalDate TEST_DATE_TUESDAY = LocalDate.of(2022, 02, 01);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ComicRepository comicRepository;

    @MockBean
    private Clock clock;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        comicRepository.deleteAll();

        User user = new User("User 1", "user1@example.com", "12345678901", "01/01/1991");
        Comic comic = new Comic(
            1L, "Comic 1", new BigDecimal("10.00"), new HashSet<>(List.of("Author")),
            "9783161484100", "First comic."
        );

        User savedUser = userRepository.save(user);
        Comic savedComic = comicRepository.save(comic);

        savedUser.getComics().add(savedComic);
        userRepository.save(savedUser);
    }

    @Test
    void listComicsDiscounted() throws Exception {
        Clock fixedClock = Clock.fixed(
            TEST_DATE_MONDAY.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
        );

        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());

        User user = userRepository.findAll().get(0);

        mockMvc.perform(get(UserController.BASE_URI + "/" + user.getId() + "/comics"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$.[0].comicId", equalTo(1)))
               .andExpect(jsonPath("$.[0].title", equalTo("Comic 1")))
               .andExpect(jsonPath("$.[0].price", equalTo(9.0)))
               .andExpect(jsonPath("$.[0].discounted", equalTo(true)))
               .andExpect(jsonPath("$.[0].authors", hasSize(1)))
               .andExpect(jsonPath("$.[0].isbn", equalTo("9783161484100")))
               .andExpect(jsonPath("$.[0].description", equalTo("First comic.")));
    }

    @Test
    void listComicsNotDiscounted() throws Exception {
        Clock fixedClock = Clock.fixed(
            TEST_DATE_TUESDAY.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
        );

        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());

        User user = userRepository.findAll().get(0);

        mockMvc.perform(get(UserController.BASE_URI + "/" + user.getId() + "/comics"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$.[0].comicId", equalTo(1)))
               .andExpect(jsonPath("$.[0].title", equalTo("Comic 1")))
               .andExpect(jsonPath("$.[0].price", equalTo(10.0)))
               .andExpect(jsonPath("$.[0].discounted", equalTo(false)))
               .andExpect(jsonPath("$.[0].authors", hasSize(1)))
               .andExpect(jsonPath("$.[0].isbn", equalTo("9783161484100")))
               .andExpect(jsonPath("$.[0].description", equalTo("First comic.")));
    }

}
