package br.com.zup.comicsapi.converters;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import br.com.zup.comicsapi.models.Comic;
import br.com.zup.comicsapi.models.ComicDTO;

@Component
public class ComicConverter {

    public ComicDTO toDto(Comic comic) {
        if (comic == null) {
            return null;
        }

        Set<String> authors = comic.getAuthors();
        if (authors != null) {
            authors = new HashSet<>(authors);
        }

        return new ComicDTO(
            comic.getComicId(), comic.getTitle(), comic.getPrice(), authors, comic.getIsbn(),
            comic.getDescription()
        );
    }

    public Comic toEntity(ComicDTO comicDto) {
        if (comicDto == null) {
            return null;
        }

        Set<String> authors = comicDto.getAuthors();
        if (authors != null) {
            authors = new HashSet<>(authors);
        }

        String isbn = comicDto.getIsbn();
        if (isbn != null) {
            isbn = isbn.replaceAll("[^0-9]", "");
        }

        return new Comic(
            comicDto.getTitle(), comicDto.getPrice(), authors, isbn, comicDto.getDescription()
        );
    }

}
