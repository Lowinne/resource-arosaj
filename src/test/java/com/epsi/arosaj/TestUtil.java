package com.epsi.arosaj;

import com.epsi.arosaj.persistence.dto.UserDto;
import com.epsi.arosaj.persistence.model.Role;
import com.epsi.arosaj.persistence.model.Utilisateur;
import com.epsi.arosaj.persistence.model.Ville;
import com.epsi.arosaj.service.RoleService;
import com.epsi.arosaj.service.UserService;
import com.epsi.arosaj.service.VilleService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
@Service
public class TestUtil {

    @Autowired
    private RoleService roleService;

    @Autowired
    private VilleService villeService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String API_ROOT = "http://localhost:8080/api/user";

    public String API_ROOT () {
        return API_ROOT;
    }

    public Utilisateur createRandomUser() {
        Role role = new Role();
        role.setCode("P");
        role.setRole("Proprietaire");

        Ville ville = new Ville();
        ville.setNom("Massy");
        ville.setCodePostale("91300");

        Utilisateur user = new Utilisateur();
        user.setNom(randomAlphabetic(10));
        user.setPrenom(randomAlphabetic(10));
        user.setPseudo("Test");
        user.setEmail(randomAlphabetic(10));
        user.setPwd("Test");
        user.setRue(randomAlphabetic(10));
        user.setRole(role);
        user.setVille(ville);


        return user;
    }

    public UserDto createRandomUserDto(){
        UserDto userDto = new UserDto();
        userDto.setCodeRole("P");
        userDto.setNomVille("Massy");
        userDto.setCodePostale("91300");
        userDto.setFirstName("P");
        userDto.setLastName("P");
        userDto.setPseudo("P");
        userDto.setEmail("P");
        userDto.setPwd("P");
        userDto.setRue("P");


        return userDto;
    }

    public String createUserAsUri(Utilisateur user) {
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(user)
                .post(API_ROOT + "/add");

        return API_ROOT + "/" + response.jsonPath().get("id");
    }

    public Utilisateur GetUserTest(){
        Utilisateur user4 = new Utilisateur();
        user4.setNom("PRINCE");
        user4.setPrenom("Boris");
        user4.setEmail("test");
        user4.setPseudo("test");
        user4.setPwd(passwordEncoder.encode("12345"));
        user4.setRue("10 rue de la fournes");
        user4.setRole(roleService.getRoleInTable("P"));
        user4.setVille(villeService.ifNotExistSave("Paris", "75012"));
        return user4;
    }
}
