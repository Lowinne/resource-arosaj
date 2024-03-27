package com.epsi.arosaj.persistence.dto.mapper;

import com.epsi.arosaj.persistence.dto.UserDto;
import com.epsi.arosaj.persistence.dto.UserPublicDto;
import com.epsi.arosaj.persistence.model.Role;
import com.epsi.arosaj.persistence.model.Utilisateur;
import com.epsi.arosaj.persistence.model.Ville;

public class UserMapper {
    public static UserPublicDto convertEntityToUserPublicDto(Utilisateur user) {
        UserPublicDto userPublicDto = new UserPublicDto();
        userPublicDto.setId(user.getId());
        userPublicDto.setFirstName(user.getPrenom());
        userPublicDto.setLastName(user.getNom());
        userPublicDto.setEmail(user.getEmail());
        userPublicDto.setRole(user.getRole().getRole());
        return userPublicDto;
    }

    public static Utilisateur convertUserDtoToEntity(UserDto userDto){
        Role role = new Role();
        role.setCode(userDto.getCodeRole());
        //Nom role to set in userservice
        Ville ville = new Ville();
        ville.setNom(userDto.getNomVille());
        ville.setCodePostale(userDto.getCodePostale());
        Utilisateur user = new Utilisateur();
        user.setNom(userDto.getFirstName());
        user.setPrenom(userDto.getLastName());
        user.setPseudo(userDto.getPseudo());
        user.setEmail(userDto.getEmail());
        user.setPwd(userDto.getPwd());
        user.setRue(userDto.getRue());

        user.setVille(ville);
        user.setRole(role);


        return user;
    }
}
