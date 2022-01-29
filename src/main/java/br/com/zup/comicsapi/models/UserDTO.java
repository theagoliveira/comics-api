package br.com.zup.comicsapi.models;

import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.br.CPF;

import br.com.zup.comicsapi.groups.CustomGroup;
import br.com.zup.comicsapi.groups.FieldGroup;
import br.com.zup.comicsapi.validators.BrazilDateFormat;
import br.com.zup.comicsapi.validators.UniqueCPF;
import br.com.zup.comicsapi.validators.UniqueEmail;

@GroupSequence({FieldGroup.class, CustomGroup.class, UserDTO.class})
public class UserDTO {

    private Long id;

    @NotBlank(message = "Name cannot be blank.", groups = FieldGroup.class)
    private String name;

    @NotBlank(message = "Email cannot be blank.", groups = FieldGroup.class)
    @Email(message = "Email is not valid.", groups = FieldGroup.class)
    @UniqueEmail(message = "Email already exists.", groups = CustomGroup.class)
    private String email;

    @NotBlank(message = "CPF cannot be blank.", groups = FieldGroup.class)
    @CPF(message = "CPF is not valid.", groups = FieldGroup.class)
    @UniqueCPF(message = "CPF already exists.", groups = CustomGroup.class)
    private String cpf;

    @NotBlank(message = "Birth date cannot be blank.", groups = FieldGroup.class)
    @BrazilDateFormat(message = "Birth date is not valid.", groups = CustomGroup.class)
    private String birthDate;

    public UserDTO() {}

    public UserDTO(String name, String email, String cpf, String birthDate) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.birthDate = birthDate;
    }

    public UserDTO(Long id, String name, String email, String cpf, String birthDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.birthDate = birthDate;
    }

    public Long getId() {
        return id;
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
