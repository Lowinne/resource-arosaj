package com.epsi.arosaj;

import com.epsi.arosaj.persistence.model.Role;
import com.epsi.arosaj.persistence.model.User;
import com.epsi.arosaj.persistence.model.Ville;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserLiveTest {

    private static final String API_ROOT = "http://localhost:8081/api/user";
    /*@Test
    public void whenGetAllUsers_thenOK() {
        final Response response = RestAssured.get(API_ROOT + "/");
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenGetUsersByTitle_thenOK() {
        final User user = createRandomUser();
        createUserAsUri(user);

        final Response response = RestAssured.get(API_ROOT + "/pseudo/" + user.getPseudo());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertTrue(response.as(List.class)
                .size() > 0);
    }

    @Test
    public void whenGetCreatedUserById_thenOK() {
        final User user = createRandomUser();
        final String location = createUserAsUri(user);

        final Response response = RestAssured.get(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(user.getPseudo(), response.jsonPath()
                .get("pseudo"));
    }

    @Test
    public void whenGetNotExistUserById_thenNotFound() {
        final Response response = RestAssured.get(API_ROOT + "/" + randomNumeric(4));
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    // POST
    @Test
    public void whenCreateNewUser_thenCreated() {
        final User user = createRandomUser();

        final Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(user)
                .post(API_ROOT + "/add");
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    @Test
    public void whenCreateAlreadyExistUser_thenError(){
        final User user = createRandomUser();

        final Response response1 = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(user)
                .post(API_ROOT + "/add");
        final Response response2 = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(user)
                .post(API_ROOT + "/add");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response2.getStatusCode());
    }

    @Test
    public void whenInvalidUser_thenError() {
        final User user = createRandomUser();
        user.setEmail(null);

        final Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(user)
                .post(API_ROOT + "/add");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    public void whenUpdateCreatedUser_thenUpdated() {
        final User user = createRandomUser();
        final String location = createUserAsUri(user);

        user.setId(Long.parseLong(location.split("api/user/")[1]));
        user.setEmail("newEmail");
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(user)
                .put(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        response = RestAssured.get(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("newEmail", response.jsonPath()
                .get("email"));

    }

    @Test
    public void whenDeleteCreatedUser_thenOk() {
        final User user = createRandomUser();
        final String location = createUserAsUri(user);

        Response response = RestAssured.delete(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        response = RestAssured.get(location);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }
*/
    // =============================== stub

    private User createRandomUser() {

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

    private String createUserAsUri(User user) {
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(user)
                .post(API_ROOT + "/add");

        return API_ROOT + "/" + response.jsonPath().get("id");
    }

}
