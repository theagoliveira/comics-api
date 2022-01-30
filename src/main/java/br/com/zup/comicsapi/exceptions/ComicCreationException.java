package br.com.zup.comicsapi.exceptions;

public class ComicCreationException extends RuntimeException {

    public ComicCreationException() {
        super("Error when creating a new comic.");
    }

}
