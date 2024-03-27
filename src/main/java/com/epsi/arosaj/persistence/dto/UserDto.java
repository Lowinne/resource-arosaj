package com.epsi.arosaj.persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String pseudo;
    private String pwd;
    private String rue;
    private String email;
    private String nomVille;
    private String codePostale;
    private String codeRole;
}
