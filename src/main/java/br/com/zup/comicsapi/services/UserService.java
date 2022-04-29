package br.com.zup.comicsapi.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import br.com.zup.comicsapi.models.Comic;
import br.com.zup.comicsapi.models.User;
import br.com.zup.comicsapi.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Clock clock;

    public UserService(UserRepository userRepository, Clock clock) {
        this.userRepository = userRepository;
        this.clock = clock;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    // Could receive an user directly instead of an userId
    public List<Comic> findComicsById(Long userId) {
        Set<Comic> userComics = userRepository.findById(userId).get().getComics();
        Integer dayOfWeekValue = LocalDate.now(clock).getDayOfWeek().getValue();

        for (Comic comic : userComics) {
            String isbn = comic.getIsbn();
            int lastIsbnDigit = isbn.charAt(isbn.length() - 1) - 48;
            boolean shouldBeDiscounted = lastIsbnDigit == (2 * dayOfWeekValue - 1)
                    || lastIsbnDigit == (2 * dayOfWeekValue - 2);

            if (shouldBeDiscounted) {
                comic.setDiscounted(true);
                comic.setPrice(
                    comic.getPrice()
                         .multiply(new BigDecimal("0.90"))
                         .setScale(2, RoundingMode.HALF_EVEN)
                );
            }

        }

        return new ArrayList<>(userComics);
    }

}
