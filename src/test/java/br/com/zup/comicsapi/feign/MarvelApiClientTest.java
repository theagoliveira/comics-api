package br.com.zup.comicsapi.feign;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

import javax.xml.bind.DatatypeConverter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.zup.comicsapi.marvel.MarvelResponse;
import br.com.zup.comicsapi.marvel.MarvelResult;

@SpringBootTest
class MarvelApiClientTest {

    @Value("${marvel.api.public.key}")
    private String pubKey;

    @Value("${marvel.api.private.key}")
    private String privKey;

    @Autowired
    MarvelApiClient marvelApiClient;

    @Test
    void getComicByComicId() throws NoSuchAlgorithmException {
        Long comicId = 4100L;

        // Set up authentication
        String timestamp = String.valueOf(Instant.now().getEpochSecond());
        byte[] bytesOfMessage = (timestamp + privKey + pubKey).getBytes(StandardCharsets.UTF_8);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytesOfDigest = md.digest(bytesOfMessage);
        String hash = DatatypeConverter.printHexBinary(bytesOfDigest).toLowerCase();

        // Get result
        MarvelResponse marvelResponse = marvelApiClient.getComicByComicId(
            comicId, timestamp, pubKey, hash
        );

        assertNotNull(marvelResponse);
        assertNotNull(marvelResponse.getData());
        assertNotNull(marvelResponse.getData().getResults());

        MarvelResult marvelResult = marvelResponse.getData().getResults().get(0);

        assertNotNull(marvelResult.getId());
        assertNotNull(marvelResult.getTitle());
        assertNotNull(marvelResult.getDescription());
        assertNotNull(marvelResult.getIsbn());
        assertNotNull(marvelResult.getPrices());
        assertNotNull(marvelResult.getCreators());
        assertNotNull(marvelResult.getCreators().getItems());
    }

}
