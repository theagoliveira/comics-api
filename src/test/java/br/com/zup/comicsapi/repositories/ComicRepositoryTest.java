package br.com.zup.comicsapi.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.zup.comicsapi.models.Comic;

@DataJpaTest
class ComicRepositoryTest {

    @Autowired
    ComicRepository comicRepository;

    @BeforeEach
    void setUp() {
        comicRepository.deleteAll();

        comicRepository.save(
            new Comic(
                1L, "Comic 1", new BigDecimal("1.99"), new HashSet<>(List.of("Author")),
                "9783161484100", "First comic."
            )
        );
        comicRepository.save(
            new Comic(
                2l, "Comic 2", new BigDecimal("2.99"), new HashSet<>(List.of("Author")),
                "9781492078005", "Second comic."
            )
        );
    }

    @Test
    void existsByIsbn() {
        assertEquals(true, comicRepository.existsByIsbn("9781492078005"));
    }

    @Test
    void findByIsbn() {
        Comic comic = comicRepository.findByIsbn("9783161484100");

        assertEquals(1L, comic.getComicId());
        assertEquals("Comic 1", comic.getTitle());
    }

}
