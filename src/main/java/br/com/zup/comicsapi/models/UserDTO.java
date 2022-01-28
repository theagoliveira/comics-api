package br.com.zup.comicsapi.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.br.CPF;

import br.com.zup.comicsapi.validators.BrazilDateFormat;
import br.com.zup.comicsapi.validators.UniqueCPF;
import br.com.zup.comicsapi.validators.UniqueEmail;

public class UserDTO {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    @UniqueEmail
    private String email;

    @NotBlank
    @CPF
    @UniqueCPF
    private String cpf;

    @NotBlank
    @BrazilDateFormat
    private String birthDate;

}
