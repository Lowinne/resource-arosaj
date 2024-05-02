package com.epsi.arosaj.web.controller.v2;

import com.epsi.arosaj.persistence.dto.MessageDto;
import com.epsi.arosaj.persistence.dto.UserDto;
import com.epsi.arosaj.persistence.dto.UserPublicDto;
import com.epsi.arosaj.persistence.dto.mapper.MessageMapper;
import com.epsi.arosaj.persistence.dto.mapper.UserMapper;
import com.epsi.arosaj.persistence.model.Message;
import com.epsi.arosaj.persistence.model.Role;
import com.epsi.arosaj.persistence.model.utilisateur;
import com.epsi.arosaj.service.UserService;
import com.epsi.arosaj.web.exception.UserNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/v2")
public class UserControllerV2 {
    private static final Logger logger = LoggerFactory.getLogger(UserControllerV2.class);

    @Autowired
    private UserService userService;


    @Operation(summary = "Get all users")
    @GetMapping(path = "/")
    public Iterable<UserPublicDto> findAllPublicUser() {
        logger.info("findAllPublicUser");
        return userService.getAllUsers();
    }

    @Operation(summary = "Get all gardien")
    @GetMapping(path = "/gardiens")
    public List<UserPublicDto> findAllPublicUserGardien() {
        logger.info("findAllPublicUserGardien");
        Iterable<utilisateur> iterableUsers = userService.findAllGardien();
        List<UserPublicDto> userPublicDtoList = new ArrayList<UserPublicDto>();
        for(utilisateur user : iterableUsers){
            userPublicDtoList.add(UserMapper.convertEntityToUserPublicDto(user));
        }
        return userPublicDtoList;
    }

    @Operation(summary = "Find a user by its pseudo")
    @GetMapping("/pseudo")
    public utilisateur findByPseudo(@Parameter(description = "pseudo of user to be searched") @RequestBody UserDto userDto) {
        String pseudo = userDto.getPseudo();
        logger.info("findByPseudo : " + pseudo);
        return userService.findUserByPseudo(pseudo, userDto.getPwd());
    }

    @Operation(summary = "Create a user ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = utilisateur.class)) }),
            @ApiResponse(responseCode = "400", description = "Find another pseudo",
                    content = @Content)})
    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public utilisateur saveUser(@Parameter(description = "user to be created") @RequestBody UserDto userDto) {
        logger.info("create Dto: " + userDto.toString());
        return userService.saveUser(userDto);
    }

    @Operation(summary = "Update a user ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found and updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = utilisateur.class)) }),
            @ApiResponse(responseCode = "400", description = "Id and User id don't match",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @PutMapping("/")
    public utilisateur updateUser(@Parameter(description = "new information for updating")@RequestBody UserDto userDto) throws AuthenticationException {
        logger.info("updateUser : " + userDto.toString());
        return userService.updateUser(userDto);
    }


    @Operation(summary = "Get all the roles ")
    @GetMapping(path = "/role/all")
    public @ResponseBody Iterable<Role> getAllRole(){
        return userService.getAllRole();
    }

    @Deprecated(forRemoval = true)
    @Operation(summary = "Delete a user (ne pas utiliser)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found and deleted"),
            @ApiResponse(responseCode = "404", description = "User not found") })
    @DeleteMapping("/")
    public void delete(@Parameter(description = "user to be deleted") @RequestBody UserDto userDto) throws AuthenticationException {
        logger.info("delete : " + userDto.toString());
        userService.delete(userDto);
    }

    @Deprecated(forRemoval = true)
    @Operation(summary = "Find a user by its id (ne pas utiliser !!!)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found ",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = utilisateur.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @GetMapping("/{id}")
    public utilisateur findOne(@Parameter(description = "id of user to be searched") @PathVariable Long id) {
        logger.info("findOne : " + id);
        return userService.findOne(id);
    }

    @PostMapping(path = "/message/send")
    @Operation(summary = "send a message with pseudo/pwd verification, Proprietaire to gardien or gardien to Proprietaire")
    public @ResponseBody MessageDto newMessage(@RequestHeader String senderPseudo, @RequestHeader String pwd, @RequestHeader String receiverPseudo, @RequestHeader String messageContent) throws JsonProcessingException {

        utilisateur sender = userService.findUserByPseudo(senderPseudo,pwd);
        if(sender != null){
            utilisateur receiver = userService.findByPseudo(receiverPseudo).get(0);
            if(receiver != null){
                Message message = new Message();
                message.setMessage(messageContent);
                message.setExpediteur(sender);
                message.setDestinataire(receiver);
                message = userService.saveMessage(message);
                return MessageMapper.convertMessageToMessageDto(message);
            } else { throw new UserNotFoundException("receiver not found"); }
        }
        throw new RuntimeException("Erreur :(");
    }

    @GetMapping(path = "/message/get")
    @Operation(summary = "get all messages of user with pseudo/pwd verification")
    public @ResponseBody List<MessageDto> getAllMessageOfUser(@RequestHeader String pseudo, @RequestHeader String pwd){
        utilisateur user = userService.findUserByPseudo(pseudo,pwd);
        List<MessageDto> listMessageOfUser = userService.getAllMessage().stream()
                .filter(message -> message.getDestinataire().getPseudo().equals(user.getPseudo()) ||
                        message.getExpediteur().getPseudo().equals(user.getPseudo()))
                .map(e -> { MessageDto messageDto = MessageMapper.convertMessageToMessageDto(e); return messageDto; })
                .collect(Collectors.toList());
        return listMessageOfUser;

    }


}
