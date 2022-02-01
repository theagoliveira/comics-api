package br.com.zup.comicsapi.feign;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.zup.comicsapi.marvel.MarvelCreators;
import br.com.zup.comicsapi.marvel.MarvelCreatorsItem;
import br.com.zup.comicsapi.marvel.MarvelData;
import br.com.zup.comicsapi.marvel.MarvelResponse;
import br.com.zup.comicsapi.marvel.MarvelPrice;
import br.com.zup.comicsapi.marvel.MarvelResult;

@SpringBootTest
public class MarvelApiClientTest {

    @Autowired
    MarvelApiClient marvelApiClient;

    @Test
    void getComicByComicId() throws NoSuchAlgorithmException {
        // Create expected result
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

        // Set up authentication
        Map<String, String> env = System.getenv();
        String timestamp = String.valueOf(Instant.now().getEpochSecond());
        String apiPublicKey = env.get("MARVEL_API_PUBLIC_KEY");
        String apiPrivateKey = env.get("MARVEL_API_PRIVATE_KEY");

        byte[] bytesOfMessage = (timestamp + apiPrivateKey + apiPublicKey).getBytes(
            StandardCharsets.UTF_8
        );
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytesOfDigest = md.digest(bytesOfMessage);
        String hash = DatatypeConverter.printHexBinary(bytesOfDigest).toLowerCase();

        // Get result
        MarvelResponse feignObj = marvelApiClient.getComicByComicId(
            comicId, timestamp, apiPublicKey, hash
        );

        MarvelResult objRes = obj.getData().getResults().get(0);
        MarvelResult feignObjRes = feignObj.getData().getResults().get(0);

        assertEquals(objRes.getId(), feignObjRes.getId());
        assertEquals(objRes.getTitle(), feignObjRes.getTitle());
        assertEquals(objRes.getDescription(), feignObjRes.getDescription());
        assertEquals(objRes.getIsbn(), feignObjRes.getIsbn());
        assertEquals(
            objRes.getPrices().get(0).getPrice(), feignObjRes.getPrices().get(0).getPrice()
        );
        assertEquals(
            objRes.getCreators().getItems().get(0).getName(),
            feignObjRes.getCreators().getItems().get(0).getName()
        );
    }

}
