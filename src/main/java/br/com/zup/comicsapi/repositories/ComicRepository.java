package br.com.zup.comicsapi.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.zup.comicsapi.models.Comic;

@Repository
public interface ComicRepository extends CrudRepository<Comic, Long> {

    boolean existsByIsbn(String isbn);

    Comic findByIsbn(String isbn);

}
