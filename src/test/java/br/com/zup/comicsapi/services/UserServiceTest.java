package br.com.zup.comicsapi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.zup.comicsapi.models.User;
import br.com.zup.comicsapi.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void save() {
        User userToSave = new User("User", "user@example.com", "12345678900", "01/01/1991");
        User returnedUser = new User(1L, "User", "user@example.com", "12345678900", "01/01/1991");

        when(userRepository.save(any(User.class))).thenReturn(returnedUser);
        User savedUser = userService.save(userToSave);

        assertNotNull(savedUser);
        assertEquals(1L, savedUser.getId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void findAll() {
        User user1 = new User("User1", "user1@example.com", "12345678900", "01/01/1991");
        User user2 = new User("User2", "user2@example.com", "98765432100", "09/09/1999");
        List<User> returnedUsers = new ArrayList<>(List.of(user1, user2));

        when(userRepository.findAll()).thenReturn(returnedUsers);
        List<User> users = userService.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }

}
