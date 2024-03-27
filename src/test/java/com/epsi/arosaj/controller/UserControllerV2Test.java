package com.epsi.arosaj.controller;

import com.epsi.arosaj.TestUtil;
import com.epsi.arosaj.persistence.dto.UserDto;
import com.epsi.arosaj.persistence.model.Utilisateur;
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
public class UserControllerV2Test {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtil testUtil;

    private static final String ROOT_PATH = "/api/user/v2";

    public UserControllerV2Test() {
    }

    @Test
    @Order(1)
    public void testAddUser() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .post(ROOT_PATH + "/add").content(asJsonString(testUtil.createRandomUserDto()))
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
                        .get(ROOT_PATH + "/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect((MockMvcResultMatchers.jsonPath("$.id").value(1))
                );
    }

    @Test
    @Order(3)
    public void testGetUsers() throws Exception {
        mockMvc.perform(get(ROOT_PATH + "/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void testGetUsersByPseudo() throws Exception {
        mockMvc.perform(get(ROOT_PATH + "/pseudo/{pseudo}", "test"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    public void testUpdateUser() throws Exception {
        Utilisateur user = testUtil.createRandomUser();
        user.setId(1);

        mockMvc.perform( MockMvcRequestBuilders
                        .put(ROOT_PATH + "/{id}",1).content(asJsonString(user))
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect((MockMvcResultMatchers.jsonPath("$.id").value(1)));
    }

    @Test
    @Order(6)
    public void testDeleteUsersById() throws Exception {
        mockMvc.perform(delete(ROOT_PATH + "/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    public void testGetUsersById_NotFound() throws Exception {
        mockMvc.perform(get(ROOT_PATH + "/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
    public void testDeleteUsersById_NotFound() throws Exception {
        mockMvc.perform(delete(ROOT_PATH + "/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(9)
    public void testUpdateUser_BadRequest() throws Exception {
        Utilisateur user = testUtil.createRandomUser();

        mockMvc.perform( MockMvcRequestBuilders
                        .put(ROOT_PATH + "/" + user.getId()+1).content(asJsonString(user))
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()
                );
    }

    @Test
    @Order(10)
    public void testAddUser2() throws Exception {
        UserDto userDto = testUtil.createRandomUserDto();
        userDto.setPseudo("test1");

        mockMvc.perform( MockMvcRequestBuilders
                        .post(ROOT_PATH + "/add").content(asJsonString(userDto))
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pseudo").exists()
                );
    }

    @Test
    @Order(11)
    public void testAddUser3_SamePseudo_BadRequest() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .post(ROOT_PATH + "/add").content(asJsonString(testUtil.createRandomUserDto()))
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()
                );
    }

    @Test
    @Order(12)
    public void testAddUser3() throws Exception {
        UserDto userDto = testUtil.createRandomUserDto();
        userDto.setPseudo("test2");

        mockMvc.perform( MockMvcRequestBuilders
                        .post(ROOT_PATH + "/add").content(asJsonString(userDto))
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pseudo").exists()
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
