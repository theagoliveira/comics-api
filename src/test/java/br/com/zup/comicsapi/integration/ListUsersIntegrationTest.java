package br.com.zup.comicsapi.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import br.com.zup.comicsapi.controllers.UserController;
import br.com.zup.comicsapi.models.User;
import br.com.zup.comicsapi.repositories.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class ListUsersIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        userRepository.save(new User("User 1", "user1@example.com", "12345678901", "01/01/1991"));
    }

    @Test
    void listUsers() throws Exception {
        mockMvc.perform(get(UserController.BASE_URI))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$.[0].name", equalTo("User 1")))
               .andExpect(jsonPath("$.[0].email", equalTo("user1@example.com")))
               .andExpect(jsonPath("$.[0].cpf", equalTo("12345678901")))
               .andExpect(jsonPath("$.[0].birthDate", equalTo("01/01/1991")));
    }

}
