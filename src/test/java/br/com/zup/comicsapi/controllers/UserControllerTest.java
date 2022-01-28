package br.com.zup.comicsapi.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.zup.comicsapi.models.User;
import br.com.zup.comicsapi.models.UserDTO;
import br.com.zup.comicsapi.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    void createValid() throws Exception {
        UserDTO userDto = new UserDTO("User", "user@example.com", "84848518423", "01/01/1991");
        User user = new User(1L, "User", "user@example.com", "84848518423", "01/01/1991");
        String userDtoJson = new ObjectMapper().writeValueAsString(userDto);

        when(userService.save(any(User.class))).thenReturn(user);

        mockMvc.perform(
            post(UserController.BASE_URI).contentType(MediaType.APPLICATION_JSON)
                                         .content(userDtoJson)
        ).andExpect(status().isCreated());
    }

    @Test
    @Disabled
    void index() {}

    @Test
    @Disabled
    void comics() {}

}
