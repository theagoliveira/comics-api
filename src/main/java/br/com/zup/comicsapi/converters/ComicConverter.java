package br.com.zup.comicsapi.converters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import br.com.zup.comicsapi.marvel.MarvelCreators;
import br.com.zup.comicsapi.marvel.MarvelCreatorsItem;
import br.com.zup.comicsapi.marvel.MarvelObject;
import br.com.zup.comicsapi.marvel.MarvelPrice;
import br.com.zup.comicsapi.marvel.MarvelResult;
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

        BigDecimal price = comic.getPrice();

        if (comic.getDiscounted()) {
            price = price.multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.HALF_EVEN);
        }

        return new ComicDTO(
            comic.getComicId(), comic.getTitle(), price, comic.getDiscounted(), authors,
            comic.getIsbn(), comic.getDescription()
        );
    }

    public ComicDTO toDto(MarvelObject marvelObject) {
        if (marvelObject == null || marvelObject.getData() == null
                || marvelObject.getData().getResults() == null
                || marvelObject.getData().getResults().isEmpty()) {
            return null;
        }

        MarvelResult marvelResult = marvelObject.getData().getResults().get(0);

        List<MarvelPrice> prices = marvelResult.getPrices();
        BigDecimal printPrice = null;
        if (prices != null && !prices.isEmpty()) {
            for (MarvelPrice price : prices) {
                if (price.getType().equals("printPrice")) {
                    printPrice = price.getPrice();
                    break;
                }
            }
        }

        MarvelCreators creators = marvelResult.getCreators();
        Set<String> authors = new HashSet<>();
        if (creators != null && !creators.getItems().isEmpty()) {
            for (MarvelCreatorsItem creator : creators.getItems()) {
                authors.add(creator.getName() + " (" + creator.getRole() + ")");
            }
        }

        return new ComicDTO(
            marvelResult.getId(), marvelResult.getTitle(), printPrice, authors,
            marvelResult.getIsbn(), marvelResult.getDescription()
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
            isbn = isbn.replaceAll("[^0-9X]", "");
        }

        return new Comic(
            comicDto.getComicId(), comicDto.getTitle(), comicDto.getPrice(), authors, isbn,
            comicDto.getDescription()
        );
    }

}
