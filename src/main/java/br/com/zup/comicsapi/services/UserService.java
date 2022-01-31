package br.com.zup.comicsapi.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.zup.comicsapi.models.Comic;
import br.com.zup.comicsapi.models.User;
import br.com.zup.comicsapi.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<Comic> findComicsByUserId(Long userId) {
        List<Comic> userComics = userRepository.findComicsByUserId(userId);
        Integer dayOfWeekValue = LocalDate.now().getDayOfWeek().getValue();

        for (Comic comic : userComics) {
            String isbn = comic.getIsbn();
            int lastIsbnDigit = isbn.charAt(isbn.length() - 1) - 48;
            boolean shouldBeDiscounted = lastIsbnDigit == (2 * dayOfWeekValue - 1)
                    || lastIsbnDigit == (2 * dayOfWeekValue - 2);

            comic.setDiscounted(shouldBeDiscounted);
        }

        return userComics;
    }

}
