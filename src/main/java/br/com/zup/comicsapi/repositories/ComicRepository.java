package br.com.zup.comicsapi.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.zup.comicsapi.models.Comic;

@Repository
public interface ComicRepository extends CrudRepository<Comic, Long> {

    @Override
    List<Comic> findAll();

    Boolean existsByIsbn(String isbn);

    Comic findByIsbn(String isbn);

}
