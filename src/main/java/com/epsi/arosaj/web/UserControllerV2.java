package com.epsi.arosaj.web;

import com.epsi.arosaj.persistence.dto.UserDto;
import com.epsi.arosaj.persistence.dto.UserPublicDto;
import com.epsi.arosaj.persistence.model.Utilisateur;
import com.epsi.arosaj.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/v2")
public class UserControllerV2 {
    private static final Logger logger = LoggerFactory.getLogger(UserControllerV2.class);

    @Autowired
    private UserService userService;

    @Operation(summary = "Get all users")
    @GetMapping(path = "/")
    public Iterable<UserPublicDto> findAll() {
        logger.info("findAll");
        return userService.getAllUsers();
    }

    @Operation(summary = "Find a user by its pseudo")
    @GetMapping("/pseudo/{pseudo}")
    public List findByPseudo(@Parameter(description = "pseudo of user to be searched") @PathVariable String pseudo) {
        logger.info("findByPseudo : " + pseudo);
        return userService.findByPseudo(pseudo);
    }

    @Operation(summary = "Find a user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found ",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Utilisateur.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @GetMapping("/{id}")
    public Utilisateur findOne(@Parameter(description = "id of user to be searched") @PathVariable Long id) {
        logger.info("findOne : " + id);
        return userService.findOne(id);
    }

    @Operation(summary = "Create a user ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Utilisateur.class)) }),
            @ApiResponse(responseCode = "400", description = "Find another pseudo",
                    content = @Content)})
    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Utilisateur saveUser(@Parameter(description = "user to be created") @RequestBody UserDto userDto) {
        logger.info("create Dto: " + userDto.toString());
        return userService.saveUser(userDto);
    }

    @Operation(summary = "Delete a user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found and updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Utilisateur.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @DeleteMapping("/{id}")
    public void delete(@Parameter(description = "id of user to be deleted") @PathVariable Long id) {
        logger.info("delete : " + id);
        userService.delete(id);
    }

    @Operation(summary = "Update a user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found and updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Utilisateur.class)) }),
            @ApiResponse(responseCode = "400", description = "Id and User id don't match",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @PutMapping("/{id}")
    public Utilisateur updateUser(@Parameter(description = "new information for updating")@RequestBody Utilisateur user, @Parameter(description = "id of user to be updated") @PathVariable Long id) {
        logger.info("updateUser : " + id);
        return userService.updateUser(user,id);
    }
}
