package com.epsi.arosaj.controller;

import com.epsi.arosaj.TestUtil;
import com.epsi.arosaj.persistence.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtil testUtil;

    @Test
    @Order(1)
    public void testAddUser() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/api/user/add").content(asJsonString(testUtil.createRandomUser()))
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pseudo").exists()
                );
    }

    @Test
    @Order(2)
    public void testGetUsersById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect((MockMvcResultMatchers.jsonPath("$.id").value(1))
                );
    }

    @Test
    @Order(3)
    public void testGetUsers() throws Exception {
        mockMvc.perform(get("/api/user/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void testGetUsersByPseudo() throws Exception {
        mockMvc.perform(get("/api/user/pseudo/{pseudo}", "test"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    public void testUpdateUser() throws Exception {
        User user = testUtil.createRandomUser();
        user.setId(1);

        mockMvc.perform( MockMvcRequestBuilders
                        .put("/api/user/{id}",1).content(asJsonString(user))
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect((MockMvcResultMatchers.jsonPath("$.id").value(1)));
    }

    @Test
    @Order(6)
    public void testDeleteUsersById() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    public void testGetUsersById_NotFound() throws Exception {
        mockMvc.perform(get("/api/user/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
    public void testDeleteUsersById_NotFound() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(9)
    public void testUpdateUser_BadRequest() throws Exception {
        User user = testUtil.createRandomUser();

        mockMvc.perform( MockMvcRequestBuilders
                        .put("/api/user/"+user.getId()+1).content(asJsonString(user))
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()
                );
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
