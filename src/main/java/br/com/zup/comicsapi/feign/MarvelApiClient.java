package br.com.zup.comicsapi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.zup.comicsapi.marvel.MarvelResponse;

@FeignClient(value = "marvelApi", url = "https://gateway.marvel.com")
public interface MarvelApiClient {

    @GetMapping(value = "/v1/public/comics/{comicId}", produces = "application/json")
    MarvelResponse getComicByComicId(@PathVariable("comicId") Long comicId,
                                     @RequestParam("ts") String ts,
                                     @RequestParam("apikey") String apikey,
                                     @RequestParam("hash") String hash);

}
