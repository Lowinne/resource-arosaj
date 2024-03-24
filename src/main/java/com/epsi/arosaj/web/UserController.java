package com.epsi.arosaj.web;

import com.epsi.arosaj.persistence.model.User;
import com.epsi.arosaj.persistence.repository.UserRepository;
import com.epsi.arosaj.service.UserService;
import com.epsi.arosaj.web.exception.UserAlreadyExistsException;
import com.epsi.arosaj.web.exception.UserIdMismatchException;
import com.epsi.arosaj.web.exception.UserNotFoundException;
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
@RequestMapping("/api/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Operation(summary = "Get all users")
    @GetMapping(path = "/")
    public Iterable<User> findAll() {
        logger.info("findAll");
        return userService.findAll();
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
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @GetMapping("/{id}")
    public User findOne(@Parameter(description = "id of user to be searched") @PathVariable Long id) {
        logger.info("findOne : " + id);
        return userService.findOne(id);
    }

    @Operation(summary = "Create a user ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Find another pseudo",
                    content = @Content)})
    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Parameter(description = "user to be created") @RequestBody User user) {
        logger.info("create : " + user.toString());
        return userService.create(user);
    }

    @Operation(summary = "Delete a user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found and updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
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
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Id and User id don't match",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @PutMapping("/{id}")
    public User updateUser(@Parameter(description = "new information for updating")@RequestBody User user,@Parameter(description = "id of user to be updated") @PathVariable Long id) {
        logger.info("updateUser : " + id);
        return userService.updateUser(user,id);
    }
}
