package br.com.zup.comicsapi.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.zup.comicsapi.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Boolean existsByEmail(String email);

    Boolean existsByCpf(String cpf);

}
