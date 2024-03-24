package com.epsi.arosaj;

import com.epsi.arosaj.persistence.model.Role;
import com.epsi.arosaj.persistence.model.User;
import com.epsi.arosaj.persistence.model.Ville;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
@Service
public class TestUtil {

    private static final String API_ROOT = "http://localhost:8080/api/user";

    public String API_ROOT () {
        return API_ROOT;
    }

    public User createRandomUser() {

        Role role = new Role();
        role.setCode("P");
        role.setRole("Proprietaire");

        Ville ville = new Ville();
        ville.setNom("Massy");
        ville.setCodePostale("91300");

        User user = new User();
        user.setNom(randomAlphabetic(10));
        user.setPrenom(randomAlphabetic(10));
        user.setPseudo(randomAlphabetic(10));
        user.setEmail(randomAlphabetic(10));
        user.setPwd(randomAlphabetic(15));
        user.setRue(randomAlphabetic(10));
        user.setRole(role);
        user.setVille(ville);

        return user;
    }

    public String createUserAsUri(User user) {
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(user)
                .post(API_ROOT + "/add");

        return API_ROOT + "/" + response.jsonPath().get("id");
    }

}
