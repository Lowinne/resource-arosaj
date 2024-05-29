package com.epsi.arosaj.controller;

import com.epsi.arosaj.TestUtil;
import com.epsi.arosaj.persistence.model.AuthRequest;
import com.epsi.arosaj.persistence.model.Utilisateur;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.epsi.arosaj.controller.ControllerV2Test.asJsonString;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ControllerV3Test {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtil testUtil;

    private static final String ROOT_PATH_USER = "/api/user/v2";
    private static final String ROOT_PATH_PLANTE = "/api/plante/v2";
    private static final String ROOT_PATH_AUTH = "/api/public";

    private String JWT;

    @BeforeAll
     void init() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("test");
        authRequest.setPassword("12345");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post(ROOT_PATH_AUTH + "/login")
                        .content(asJsonString(authRequest))
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.jwt").exists())
                .andReturn(); // Ensure the result is returned here

        // Extract the JWT from the response
        String responseContent = result.getResponse().getContentAsString();

        JWT = JsonPath.read(responseContent, "$.jwt");
    }

    @Test
    @Order(1)
    public void testAddUser() throws Exception {

        Utilisateur user = testUtil.createRandomUser();

        mockMvc.perform(MockMvcRequestBuilders
                        .post(ROOT_PATH_AUTH + "/add")
                        .header("nom",user.getNom())
                        .header("prenom", user.getPrenom())
                        .header("pseudo", user.getPseudo())
                        .header("email", user.getEmail())
                        .header("rue", user.getRue())
                        .header("codeRole",user.getRole().getCode())
                        .header("nomVille", user.getVille().getNom())
                        .header("codePostale", user.getVille().getCodePostale())
                        .header("pwd", user.getPwd()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pseudo").exists()
        );
    }

}
