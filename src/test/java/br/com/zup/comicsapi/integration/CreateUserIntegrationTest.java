package br.com.zup.comicsapi.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.zup.comicsapi.controllers.UserController;
import br.com.zup.comicsapi.models.User;
import br.com.zup.comicsapi.models.UserDTO;
import br.com.zup.comicsapi.repositories.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class CreateUserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createUser() throws Exception {
        UserDTO userDto = new UserDTO("User", "user@example.com", "84848518423", "01/01/1991");
        String userDtoJson = new ObjectMapper().writeValueAsString(userDto);

        mockMvc.perform(
            post(UserController.BASE_URI).contentType(MediaType.APPLICATION_JSON)
                                         .content(userDtoJson)
        ).andExpect(status().isCreated());

        User user = userRepository.findAll().get(0);

        assertNotNull(user.getId());
        assertEquals("user@example.com", user.getEmail());
    }

}
