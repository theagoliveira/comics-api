package br.com.zup.comicsapi.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.zup.comicsapi.models.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        userRepository.save(new User("User 1", "user1@example.com", "12345678901", "01/01/1991"));
        userRepository.save(new User("User 2", "user2@example.com", "12345678902", "02/02/1992"));
    }

    @Test
    void findAll() {
        List<User> users = userRepository.findAll();

        assertEquals(2, users.size());
        assertEquals(1L, users.get(0).getId());
        assertEquals("User 1", users.get(0).getName());
        assertEquals(2L, users.get(1).getId());
        assertEquals("User 2", users.get(1).getName());
    }

    @Test
    void existsByEmail() {
        assertEquals(true, userRepository.existsByEmail("user1@example.com"));
    }

    @Test
    void existsByCpf() {
        assertEquals(true, userRepository.existsByCpf("12345678902"));
    }

}
