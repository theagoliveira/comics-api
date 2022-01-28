package br.com.zup.comicsapi.services;

import java.util.List;

import org.springframework.stereotype.Service;

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

}
