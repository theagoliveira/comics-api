package br.com.zup.comicsapi.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.zup.comicsapi.models.Comic;
import br.com.zup.comicsapi.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Override
    List<User> findAll();

    List<Comic> findComicsByUserId(Long id);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

}
