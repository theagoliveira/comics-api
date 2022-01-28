package br.com.zup.comicsapi.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.br.CPF;

import br.com.zup.comicsapi.validators.BrazilDateFormat;
import br.com.zup.comicsapi.validators.UniqueCPF;
import br.com.zup.comicsapi.validators.UniqueEmail;

public class UserDTO {

    @NotBlank(message = "Name cannot be blank.")
    private String name;

    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Email is not valid.")
    @UniqueEmail(message = "Email already exists.")
    private String email;

    @NotBlank(message = "CPF cannot be blank.")
    @CPF(message = "CPF is not valid.")
    @UniqueCPF(message = "CPF already exists.")
    private String cpf;

    @NotBlank(message = "Birth date cannot be blank.")
    @BrazilDateFormat(message = "Birth date is not valid.")
    private String birthDate;

    public UserDTO() {}

    public UserDTO(String name, String email, String cpf, String birthDate) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getBirthDate() {
        return birthDate;
    }

}
