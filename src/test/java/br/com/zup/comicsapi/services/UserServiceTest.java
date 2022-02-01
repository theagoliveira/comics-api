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

    private final static User USER1_NO_ID = new User(
        "User1", "user1@example.com", "12345678900", "01/01/1991"
    );
    private final static User USER2 = new User(
        2L, "User2", "user2@example.com", "12345678902", "02/02/1992"
    );

    private final static LocalDate TEST_DATE_MONDAY = LocalDate.of(2022, 01, 31);
    private final static LocalDate TEST_DATE_TUESDAY = LocalDate.of(2022, 02, 01);

    private User USER1 = new User(1L, "User1", "user1@example.com", "12345678900", "01/01/1991");
    private Comic COMIC = new Comic(
        1L, "Title1", new BigDecimal("10.00"), new HashSet<>(), "12345678900", "A first comic"
    );

    @Mock
    UserRepository userRepository;

    @Mock
    Clock clock;

    @InjectMocks
    UserService userService;

    @Test
    void save() {
        when(userRepository.save(any(User.class))).thenReturn(USER1);
        User savedUser = userService.save(USER1_NO_ID);

        assertNotNull(savedUser);
        assertEquals(1L, savedUser.getId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void findAll() {
        List<User> returnedUsers = new ArrayList<>(List.of(USER1, USER2));

        when(userRepository.findAll()).thenReturn(returnedUsers);
        List<User> users = userService.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }

    @Test
    void findComicsByIdDiscounted() {
        Clock fixedClock = Clock.fixed(
            TEST_DATE_MONDAY.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
        );
        Set<Comic> comics = Set.of(COMIC);
        USER1.setComics(comics);

        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(USER1));

        List<Comic> userComics = userService.findComicsById(1L);

        assertNotNull(userComics);
        assertEquals(1, userComics.size());
        assertEquals(true, userComics.get(0).getDiscounted());
        assertEquals(new BigDecimal("9.00"), userComics.get(0).getPrice());
    }

    @Test
    void findComicsByIdNotDiscounted() {
        Clock fixedClock = Clock.fixed(
            TEST_DATE_TUESDAY.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
        );

        Set<Comic> comics = Set.of(COMIC);
        USER1.setComics(comics);

        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(USER1));

        List<Comic> userComics = userService.findComicsById(1L);

        assertNotNull(userComics);
        assertEquals(1, userComics.size());
        assertEquals(false, userComics.get(0).getDiscounted());
        assertEquals(new BigDecimal("10.00"), userComics.get(0).getPrice());
    }

}
