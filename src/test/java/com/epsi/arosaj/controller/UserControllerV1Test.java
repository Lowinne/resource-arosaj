package com.epsi.arosaj.controller;

import com.epsi.arosaj.TestUtil;
import com.epsi.arosaj.persistence.model.Utilisateur;
import org.apache.catalina.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.epsi.arosaj.controller.UserControllerV2Test.asJsonString;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerV1Test {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtil testUtil;

    private static final String ROOT_PATH = "/api/user/v1";

    @Test
    @Order(1)
    public void testAddUser() throws Exception {

        Utilisateur user = testUtil.createRandomUser();

        mockMvc.perform( MockMvcRequestBuilders
                        .post(ROOT_PATH + "/add")
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

    @Test
    @Order(2)
    public void testGetUser() throws Exception {

        Utilisateur user = testUtil.createRandomUser();

        mockMvc.perform(MockMvcRequestBuilders
                        .get(ROOT_PATH + "/get")
                        .header("pseudo", user.getPseudo())
                        .header("pwd", user.getPwd()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect((MockMvcResultMatchers.jsonPath("$.pseudo").value("Test"))
                );
    }
}
