package br.com.zup.comicsapi.converters;

import br.com.zup.comicsapi.models.UserDTO;

import org.springframework.stereotype.Component;

import br.com.zup.comicsapi.models.User;

@Component
public class UserConverter {

    public UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(user.getName(), user.getEmail(), user.getCpf(), user.getBirthDate());
    }

    public User toEntity(UserDTO userDto) {
        if (userDto == null) {
            return null;
        }

        String cpf = userDto.getCpf();
        if (cpf != null) {
            cpf = cpf.replaceAll("[^0-9]", "");
        }

        return new User(userDto.getName(), userDto.getEmail(), cpf, userDto.getBirthDate());
    }

}
