package com.epsi.arosaj.controller;

import com.epsi.arosaj.TestUtil;
import com.epsi.arosaj.persistence.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ControllerV2Test {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtil testUtil;

    private static final String ROOT_PATH_USER = "/api/user/v2";
    private static final String ROOT_PATH_PLANTE = "/api/plante/v2";

    public ControllerV2Test() {
    }

    @Test
    @Order(1)
    public void testAddUser() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .post(ROOT_PATH_USER + "/add")
                        .content(asJsonString(testUtil.createRandomUserDto()))
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pseudo").exists()
                );
    }

    //@Test
    //@Order(2)
    public void testGetUsersById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(ROOT_PATH_USER + "/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect((MockMvcResultMatchers.jsonPath("$.id").value(1))
                );
    }

    @Test
    @Order(3)
    public void testGetUsers() throws Exception {
        mockMvc.perform(get(ROOT_PATH_USER + "/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void testGetUsersByPseudo() throws Exception {

        mockMvc.perform(get(ROOT_PATH_USER + "/pseudo")
                .content(asJsonString(testUtil.createRandomUserDto()))
                .contentType(APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    public void testUpdateUser() throws Exception {
        UserDto user = testUtil.createRandomUserDto();
        user.setEmail("boris@provider.com");

        mockMvc.perform( MockMvcRequestBuilders
                        .put(ROOT_PATH_USER + "/" ).content(asJsonString(user))
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect((MockMvcResultMatchers.jsonPath("$.email").value("boris@provider.com")));
    }

    @Test
    @Order(6)
    public void testDeleteUsers() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .delete(ROOT_PATH_USER + "/" ).content(asJsonString(testUtil.createRandomUserDto()))
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //@Test
    //@Order(7)
    public void testGetUsersById_NotFound() throws Exception {
        mockMvc.perform(get(ROOT_PATH_USER + "/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
    public void testDeleteUsers_NotFound() throws Exception {
        UserDto userDto = testUtil.createRandomUserDto();
        userDto.setPseudo("xxxxx");

        mockMvc.perform( MockMvcRequestBuilders
                        .delete(ROOT_PATH_USER + "/" ).content(asJsonString(userDto))
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(9)
    public void testUpdateUser_BadRequest() throws Exception {
        UserDto user = testUtil.createRandomUserDto();
        user.setPseudo("wtv else");

        mockMvc.perform( MockMvcRequestBuilders
                        .put(ROOT_PATH_USER + "/" ).content(asJsonString(user))
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound()
                );
    }

    @Test
    @Order(10)
    public void testAddUser2() throws Exception {
        UserDto userDto = testUtil.createRandomUserDto();
        userDto.setPseudo("test1");
        userDto.setPwd("1234");

        mockMvc.perform( MockMvcRequestBuilders
                        .post(ROOT_PATH_USER + "/add").content(asJsonString(userDto))
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pseudo").exists()
                );
    }

    @Test
    @Order(11)
    public void testAddUser3_SamePseudo_BadRequest() throws Exception {
        UserDto userDto = testUtil.createRandomUserDto();
        userDto.setPseudo("test1");

        mockMvc.perform( MockMvcRequestBuilders
                        .post(ROOT_PATH_USER + "/add").content(asJsonString(userDto))
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest()
                );
    }

    @Test
    @Order(12)
    public void testAddUser3Botaniste() throws Exception {
        UserDto userDto = testUtil.createRandomUserDto();
        userDto.setPseudo("test2");
        userDto.setPwd("1234");
        userDto.setCodeRole("B");

        mockMvc.perform( MockMvcRequestBuilders
                        .post(ROOT_PATH_USER + "/add").content(asJsonString(userDto))
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pseudo").exists()
                );
    }

    @Test
    @Order(13)
    public void testAddUser4Gardien() throws Exception {
        UserDto userDto = testUtil.createRandomUserDto();
        userDto.setPseudo("test3");
        userDto.setPwd("1234");
        userDto.setCodeRole("G");

        mockMvc.perform( MockMvcRequestBuilders
                        .post(ROOT_PATH_USER + "/add").content(asJsonString(userDto))
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pseudo").exists()
                );
    }
    //Plante Controller
    @Test
    @Order(14)
    public void testAddPlante() throws Exception {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        mockMvc.perform( MockMvcRequestBuilders
                        .multipart(ROOT_PATH_PLANTE + "/add").file(file)
                        .header("pseudo","test1")
                        .header("userPwd", "1234")
                        .header("nom", "Rose")
                        .header("desc", "Une rose fatigue mais qui sent tres bon")
                        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").exists()
                );
    }

    @Test
    @Order(15)
    public void testAddPhotoPlante() throws Exception {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        mockMvc.perform( MockMvcRequestBuilders
                        .multipart(ROOT_PATH_PLANTE + "/upload").file(file)
                        .header("pseudo","test1")
                        .header("userPwd", "1234")
                        .header("planteId", 1)
                )
                .andDo(print())
                .andExpect(status().isOk()
                );
    }

    @Test
    @Order(16)
    public void testAddPhotoPlanteToSameUser() throws Exception {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        mockMvc.perform( MockMvcRequestBuilders
                        .multipart(ROOT_PATH_PLANTE + "/upload").file(file)
                        .header("pseudo","test1")
                        .header("userPwd", "1234")
                        .header("planteId", 1)
                )
                .andDo(print())
                .andExpect(status().isOk()
                );
    }

    @Test
    @Order(17)
    public void testAddPlante2() throws Exception {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        mockMvc.perform( MockMvcRequestBuilders
                        .multipart(ROOT_PATH_PLANTE + "/add").file(file)
                        .header("pseudo","test1")
                        .header("userPwd", "1234")
                        .header("nom", "Chene")
                        .header("desc", "Planté par mon grand père")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").exists()
                );
    }

    @Test
    @Order(18)
    public void testGetPhotoOfPlante() throws Exception {

        mockMvc.perform( MockMvcRequestBuilders
                        .get(ROOT_PATH_PLANTE + "/images")
                        .header("pseudo","test1")
                        .header("userPwd", "1234")
                        .header("planteId", "1")
                )
                .andDo(print())
                .andExpect(status().isOk()

                );
    }

    @Test
    @Order(19)
    public void testaddConseilOfPlante() throws Exception {
        //Botaniste
        mockMvc.perform( MockMvcRequestBuilders
                        .post(ROOT_PATH_PLANTE + "/botaniste/conseil")
                        .header("botanistePseudo","test2")
                        .header("pwd", "1234")
                        .header("planteId", "1")
                        .header("conseil","Faut l'aroser mon reuf")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.conseil").exists()
                );
    }

    @Test
    @Order(20)
    public void testaddConseilOfPlante2() throws Exception {
        //Botaniste
        mockMvc.perform( MockMvcRequestBuilders
                        .post(ROOT_PATH_PLANTE + "/botaniste/conseil")
                        .header("botanistePseudo","test2")
                        .header("pwd", "1234")
                        .header("planteId", "1")
                        .header("conseil","Je repete, il faut l'aroser mon reuf")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.conseil").exists()
                );
    }

    @Test
    @Order(21)
    public void testGetConseilsOfPlante() throws Exception {
        //Botaniste
        mockMvc.perform( MockMvcRequestBuilders
                        .get(ROOT_PATH_PLANTE + "/conseils")
                        .header("botanistePseudo","test2")
                        .header("pwd", "1234")
                        .header("planteId", "1")
                        .header("conseil","Je repete, il faut l'aroser mon reuf")
                )
                .andDo(print())
                .andExpect(status().isOk()
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
