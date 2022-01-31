package br.com.zup.comicsapi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.zup.comicsapi.models.Comic;
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
        User user1 = new User(1L, "User1", "user1@example.com", "12345678900", "01/01/1991");
        User user2 = new User(2L, "User2", "user2@example.com", "98765432100", "09/09/1999");
        List<User> returnedUsers = new ArrayList<>(List.of(user1, user2));

        when(userRepository.findAll()).thenReturn(returnedUsers);
        List<User> users = userService.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }

    @Test
    void findComicsByUserId() {
        Comic comic1 = new Comic(
            1L, "Title1", 10.0, new HashSet<>(), "12345678900", "A first comic"
        );
        Comic comic2 = new Comic(
            2L, "Title2", 10.0, new HashSet<>(), "12345678902", "A second comic"
        );

        String isbn1 = comic1.getIsbn();
        String isbn2 = comic2.getIsbn();
        int lastIsbnDigit1 = isbn1.charAt(isbn1.length() - 1) - 48;
        int lastIsbnDigit2 = isbn2.charAt(isbn2.length() - 1) - 48;
        Integer dayOfWeekValue = LocalDate.now().getDayOfWeek().getValue();

        boolean shouldBeDiscounted1 = lastIsbnDigit1 == (2 * dayOfWeekValue - 1)
                || lastIsbnDigit1 == (2 * dayOfWeekValue - 2);
        boolean shouldBeDiscounted2 = lastIsbnDigit2 == (2 * dayOfWeekValue - 1)
                || lastIsbnDigit2 == (2 * dayOfWeekValue - 2);

        when(userRepository.findComicsByUserId(anyLong())).thenReturn(List.of(comic1, comic2));
        List<Comic> userComics = userService.findComicsByUserId(1L);

        assertNotNull(userComics);
        assertEquals(2, userComics.size());
        assertEquals(shouldBeDiscounted1, userComics.get(0).getDiscounted());
        assertEquals(shouldBeDiscounted2, userComics.get(1).getDiscounted());
        verify(userRepository).findComicsByUserId(anyLong());
    }

}
