package br.com.zup.comicsapi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    private final static LocalDate TEST_DATE = LocalDate.of(2022, 01, 31);

    @Mock
    UserRepository userRepository;

    @Mock
    Clock clock;

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
    void findComicsByIdDiscounted() {
        Clock fixedClock = Clock.fixed(
            TEST_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()
        );
        Comic comic1 = new Comic(
            1L, "Title1", new BigDecimal("10.00"), new HashSet<>(), "12345678900", "A first comic"
        );
        Set<Comic> comics = Set.of(comic1);
        User user1 = new User(1L, "User1", "user1@example.com", "12345678900", "01/01/1991");

        user1.setComics(comics);

        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        List<Comic> userComics = userService.findComicsById(1L);

        assertNotNull(userComics);
        assertEquals(1, userComics.size());
        assertEquals(true, userComics.get(0).getDiscounted());
        assertEquals(new BigDecimal("9.00"), userComics.get(0).getPrice());
    }

    @Test
    void findComicsByIdNotDiscounted() {
        Clock fixedClock = Clock.fixed(
            TEST_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()
        );
        Comic comic1 = new Comic(
            1L, "Title1", new BigDecimal("10.00"), new HashSet<>(), "12345678902", "A first comic"
        );
        Set<Comic> comics = Set.of(comic1);
        User user1 = new User(1L, "User1", "user1@example.com", "12345678900", "01/01/1991");

        user1.setComics(comics);

        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        List<Comic> userComics = userService.findComicsById(1L);

        assertNotNull(userComics);
        assertEquals(1, userComics.size());
        assertEquals(false, userComics.get(0).getDiscounted());
        assertEquals(new BigDecimal("10.00"), userComics.get(0).getPrice());
    }

}
