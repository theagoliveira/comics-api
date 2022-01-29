package br.com.zup.comicsapi.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
import br.com.zup.comicsapi.repositories.UserRepository;
import br.com.zup.comicsapi.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

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

        verify(userService).save(any(User.class));
    }

    @Test
    void createInvalidAllBlank() throws Exception {
        UserDTO userDto = new UserDTO();
        String userDtoJson = new ObjectMapper().writeValueAsString(userDto);

        mockMvc.perform(
            post(UserController.BASE_URI).contentType(MediaType.APPLICATION_JSON)
                                         .content(userDtoJson)
        )
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errors.name", equalTo("Name cannot be blank.")))
               .andExpect(jsonPath("$.errors.email", equalTo("Email cannot be blank.")))
               .andExpect(jsonPath("$.errors.cpf", equalTo("CPF cannot be blank.")))
               .andExpect(jsonPath("$.errors.birthDate", equalTo("Birth date cannot be blank.")));

        verify(userService, never()).save(any(User.class));
    }

    @Test
    void createInvalidEmailAndCpf() throws Exception {
        UserDTO userDto = new UserDTO("User", "user@example,com", "12345678910", "01/01/1991");
        String userDtoJson = new ObjectMapper().writeValueAsString(userDto);

        mockMvc.perform(
            post(UserController.BASE_URI).contentType(MediaType.APPLICATION_JSON)
                                         .content(userDtoJson)
        )
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errors.email", equalTo("Email is not valid.")))
               .andExpect(jsonPath("$.errors.cpf", equalTo("CPF is not valid.")));

        verify(userService, never()).save(any(User.class));
    }

    @Test
    void createInvalidCustom() throws Exception {
        UserDTO userDto = new UserDTO("User", "user@example.com", "84848518423", "34/19/1000");
        String userDtoJson = new ObjectMapper().writeValueAsString(userDto);

        when(userRepository.existsByCpf(anyString())).thenReturn(true);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        mockMvc.perform(
            post(UserController.BASE_URI).contentType(MediaType.APPLICATION_JSON)
                                         .content(userDtoJson)
        )
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errors.email", equalTo("Email already exists.")))
               .andExpect(jsonPath("$.errors.cpf", equalTo("CPF already exists.")))
               .andExpect(jsonPath("$.errors.birthDate", equalTo("Birth date is not valid.")));

        verify(userService, never()).save(any(User.class));
        verify(userRepository).existsByCpf(anyString());
        verify(userRepository).existsByEmail(anyString());
    }

    @Test
    void index() throws Exception {
        User user1 = new User(1L, "User1", "user1@example.com", "84848518423", "01/01/1991");
        User user2 = new User(2L, "User2", "user2@example.com", "96642015029", "02/02/1992");

        when(userService.findAll()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get(UserController.BASE_URI))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$.[0].id", equalTo(1)))
               .andExpect(jsonPath("$.[0].name", equalTo("User1")))
               .andExpect(jsonPath("$.[0].email", equalTo("user1@example.com")))
               .andExpect(jsonPath("$.[0].cpf", equalTo("84848518423")))
               .andExpect(jsonPath("$.[0].birthDate", equalTo("01/01/1991")));

        verify(userService).findAll();
    }

    @Test
    void deleteNotSupported() throws Exception {
        mockMvc.perform(delete(UserController.BASE_URI)).andExpect(status().isMethodNotAllowed());
    }

    @Test
    void putNotSupported() throws Exception {
        mockMvc.perform(put(UserController.BASE_URI)).andExpect(status().isMethodNotAllowed());
    }

    @Test
    void patchNotSupported() throws Exception {
        mockMvc.perform(patch(UserController.BASE_URI)).andExpect(status().isMethodNotAllowed());
    }

    @Test
    @Disabled
    void userComics() {}

}
