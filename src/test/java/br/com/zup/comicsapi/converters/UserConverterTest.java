package br.com.zup.comicsapi.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.zup.comicsapi.models.User;
import br.com.zup.comicsapi.models.UserDTO;

public class UserConverterTest {

    UserConverter userConverter;

    @BeforeEach
    void setUp() {
        userConverter = new UserConverter();
    }

    @Test
    void testNullParameterToDto() {
        assertNull(userConverter.toDto(null));
    }

    @Test
    void testNullParameterToEntity() {
        assertNull(userConverter.toEntity(null));
    }

    @Test
    void testEmptyObjectToDto() {
        assertNotNull(userConverter.toDto(new User()));
    }

    @Test
    void testEmptyObjectToEntity() {
        assertNotNull(userConverter.toEntity(new UserDTO()));
    }

    @Test
    void convertToDto() {
        User user = new User("User", "user@example.com", "12345678900", "01/01/1991");

        UserDTO userDto = userConverter.toDto(user);

        assertNotNull(userDto);
        assertEquals("User", userDto.getName());
        assertEquals("user@example.com", userDto.getEmail());
        assertEquals("12345678900", userDto.getCpf());
        assertEquals("01/01/1991", userDto.getBirthDate());
    }

    @Test
    void convertToEntity() {
        UserDTO userDto = new UserDTO("User", "user@example.com", "12345678900", "01/01/1991");

        User user = userConverter.toEntity(userDto);

        assertNotNull(user);
        assertEquals("User", user.getName());
        assertEquals("user@example.com", user.getEmail());
        assertEquals("12345678900", user.getCpf());
        assertEquals("01/01/1991", user.getBirthDate());
    }

}
