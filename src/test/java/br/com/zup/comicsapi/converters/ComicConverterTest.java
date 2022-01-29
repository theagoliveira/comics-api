package br.com.zup.comicsapi.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.zup.comicsapi.models.Comic;
import br.com.zup.comicsapi.models.ComicDTO;

public class ComicConverterTest {

    ComicConverter comicConverter;

    @BeforeEach
    void setUp() {
        comicConverter = new ComicConverter();
    }

    @Test
    void convertNullParameterToDto() {
        assertNull(comicConverter.toDto(null));
    }

    @Test
    void convertNullParameterToEntity() {
        assertNull(comicConverter.toEntity(null));
    }

    @Test
    void convertEmptyObjectToDto() {
        assertNotNull(comicConverter.toDto(new Comic()));
    }

    @Test
    void convertEmptyObjectToEntity() {
        assertNotNull(comicConverter.toEntity(new ComicDTO()));
    }

    @Test
    void convertToDto() {
        Set<String> authors = new HashSet<>(List.of("Author 1", "Author 2"));
        Comic comic = new Comic(1L, "Title", 1.99, authors, "12345678900", "A comic");

        ComicDTO comicDto = comicConverter.toDto(comic);

        assertNotNull(comicDto);
        assertEquals(1L, comicDto.getComicId());
        assertEquals("Title", comicDto.getTitle());
        assertEquals(1.99, comicDto.getPrice());
        assertEquals(authors, comicDto.getAuthors());
        assertEquals("12345678900", comicDto.getIsbn());
        assertEquals("A comic", comicDto.getDescription());
    }

    @Test
    void convertToEntity() {
        Set<String> authors = new HashSet<>(List.of("Author 1", "Author 2"));
        ComicDTO comicDto = new ComicDTO(1L, "Title", 1.99, authors, "12345678900", "A comic");

        Comic comic = comicConverter.toEntity(comicDto);

        assertNotNull(comic);
        assertNull(comic.getComicId());
        assertEquals("Title", comic.getTitle());
        assertEquals(1.99, comic.getPrice());
        assertEquals(authors, comic.getAuthors());
        assertEquals("12345678900", comic.getIsbn());
        assertEquals("A comic", comic.getDescription());
    }

}
