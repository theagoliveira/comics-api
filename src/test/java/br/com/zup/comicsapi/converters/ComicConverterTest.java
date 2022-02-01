package br.com.zup.comicsapi.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.zup.comicsapi.marvel.MarvelCreators;
import br.com.zup.comicsapi.marvel.MarvelCreatorsItem;
import br.com.zup.comicsapi.marvel.MarvelData;
import br.com.zup.comicsapi.marvel.MarvelResponse;
import br.com.zup.comicsapi.marvel.MarvelPrice;
import br.com.zup.comicsapi.marvel.MarvelResult;
import br.com.zup.comicsapi.models.Comic;
import br.com.zup.comicsapi.models.ComicDTO;

public class ComicConverterTest {

    ComicConverter comicConverter;

    @BeforeEach
    void setUp() {
        comicConverter = new ComicConverter();
    }

    @Test
    void convertNullComicParameterToDto() {
        assertNull(comicConverter.toDto(null));
    }

    @Test
    void convertNullParameterToEntity() {
        assertNull(comicConverter.toEntity((ComicDTO) null));
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
        Comic comic = new Comic(
            1L, "Title", new BigDecimal("10.00"), true, authors, "12345678900", "A comic"
        );

        ComicDTO comicDto = comicConverter.toDto(comic);

        assertNotNull(comicDto);
        assertEquals(1L, comicDto.getComicId());
        assertEquals("Title", comicDto.getTitle());
        assertEquals(new BigDecimal("10.00"), comicDto.getPrice());
        assertEquals(true, comicDto.getDiscounted());
        assertEquals(authors, comicDto.getAuthors());
        assertEquals("12345678900", comicDto.getIsbn());
        assertEquals("A comic", comicDto.getDescription());
    }

    @Test
    void convertToEntity() {
        Set<String> authors = new HashSet<>(List.of("Author 1", "Author 2"));
        ComicDTO comicDto = new ComicDTO(
            1L, "Title", new BigDecimal("1.99"), authors, "12345678900", "A comic"
        );

        Comic comic = comicConverter.toEntity(comicDto);

        assertNotNull(comic);
        assertEquals(1L, comic.getComicId());
        assertEquals("Title", comic.getTitle());
        assertEquals(new BigDecimal("1.99"), comic.getPrice());
        assertEquals(authors, comic.getAuthors());
        assertEquals("12345678900", comic.getIsbn());
        assertEquals("A comic", comic.getDescription());
    }

    @Test
    void convertNullmarvelResponseParameterToEntity() {
        assertNull(comicConverter.toEntity((MarvelResponse) null));
    }

    @Test
    void convertNullMarvelDataParameterToEntity() {
        assertNull(comicConverter.toEntity(new MarvelResponse()));
    }

    @Test
    void convertNullMarvelResultParameterToEntity() {
        assertNull(comicConverter.toEntity(new MarvelResponse(new MarvelData())));
    }

    @Test
    void convertEmptyMarvelResultListParameterToEntity() {
        assertNull(
            comicConverter.toEntity(
                new MarvelResponse(new MarvelData(new ArrayList<MarvelResult>()))
            )
        );
    }

    @Test
    void convertEmptyMarvelResultParameterToEntity() {
        Comic comic = comicConverter.toEntity(
            new MarvelResponse(
                new MarvelData(new ArrayList<MarvelResult>(List.of(new MarvelResult())))
            )
        );

        assertNull(comic.getComicId());
        assertNull(comic.getTitle());
        assertNull(comic.getPrice());
        assertNull(comic.getIsbn());
        assertNull(comic.getDescription());
        assertEquals(0, comic.getAuthors().size());
    }

    @Test
    void convertBlankMarvelResultParameterToEntity() {
        Comic comic = comicConverter.toEntity(
            new MarvelResponse(
                new MarvelData(
                    new ArrayList<MarvelResult>(
                        List.of(
                            new MarvelResult(
                                1L, "", "", "", new ArrayList<MarvelPrice>(),
                                new MarvelCreators(new ArrayList<MarvelCreatorsItem>())
                            )
                        )
                    )
                )
            )
        );

        assertEquals(1L, comic.getComicId());
        assertEquals("", comic.getTitle());
        assertNull(comic.getPrice());
        assertEquals("", comic.getIsbn());
        assertEquals("", comic.getDescription());
        assertEquals(0, comic.getAuthors().size());
    }

    @Test
    void convertValidMarvelResultParameterToEntity() {
        Long comicId = 4100L;
        String title = "Uncanny X-Men Omnibus Vol. 1 (Hardcover)";
        String description = "When a young writer named Chris Claremont took over X-Men in 1976, "
                + "few fans could predict the incredible impact he would have on the Marvel Comics "
                + "series. With a flair for realistic dialogue, heartfelt storylines and "
                + "hard-hitting action, Claremont's writing breathed life into the characters. In "
                + "collaboration with artists Dave Cockrum and John Byrne, Claremont crafted a run "
                + "still heralded as a definitive era on the book. UNCANNY X-MEN became more than "
                + "just another super-hero title: this diverse cast of mutants fighting against "
                + "prejudice and intolerance has resonated in the hearts of millions of devoted "
                + "readers. Now, the first five years of their landmark run on UNCANNY X-MEN are "
                + "collected in one oversized volume. This keepsake edition also includes all "
                + "original letters pages, newly remastered coloring and other uncanny extras! "
                + "Collects UNCANNY X-MEN #94-131 and ANNUAL #3, and GIANT-SIZE X-MEN #1.\r<br>848 "
                + "PGS./Rated T+ SUGGESTED FOR TEENS AND UP ...$99.99\r<br>";
        String isbn = "0-7851-2101-3";
        List<MarvelPrice> prices = new ArrayList<>(
            List.of(new MarvelPrice("printPrice", new BigDecimal("9.99")))
        );
        List<MarvelCreatorsItem> items = new ArrayList<>(
            List.of(
                new MarvelCreatorsItem("Terry Kevin Austin", "inker"),
                new MarvelCreatorsItem("John Byrne", "penciller"),
                new MarvelCreatorsItem("Chris Claremont", "writer"),
                new MarvelCreatorsItem("Tom Orzechowski", "letterer"),
                new MarvelCreatorsItem("Roger Stern", "editor"),
                new MarvelCreatorsItem("Glynis Wein", "colorist")
            )
        );

        MarvelResponse obj = new MarvelResponse(
            new MarvelData(
                new ArrayList<MarvelResult>(
                    List.of(
                        new MarvelResult(
                            comicId, title, description, isbn, prices, new MarvelCreators(items)
                        )
                    )
                )
            )
        );

        Comic comic = comicConverter.toEntity(obj);

        assertEquals(comicId, comic.getComicId());
        assertEquals(title, comic.getTitle());
        assertEquals(new BigDecimal("9.99"), comic.getPrice());
        assertEquals(isbn, comic.getIsbn());
        assertEquals(description, comic.getDescription());
        assertEquals(6, comic.getAuthors().size());
        assertTrue(comic.getAuthors().contains("Terry Kevin Austin (inker)"));
    }

}
