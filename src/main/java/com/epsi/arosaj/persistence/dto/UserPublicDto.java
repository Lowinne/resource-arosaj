package com.epsi.arosaj.persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPublicDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;

}
