package com.epsi.arosaj.web.controller.v1;

import com.epsi.arosaj.persistence.model.Message;
import com.epsi.arosaj.persistence.repository.MessageRepository;
import com.epsi.arosaj.persistence.repository.RoleRepository;
import com.epsi.arosaj.persistence.repository.VilleRepository;
import com.epsi.arosaj.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/sms/v1")
public class MessageControllerV1  {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private VilleRepository villeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    ObjectMapper objectMapper = new ObjectMapper();

    //methode qui doit prendre des int id plutot que des full user
    @PostMapping(path = "/add")
    public @ResponseBody Message newMessage(@RequestBody Message message) throws JsonProcessingException {
        message = messageRepository.save(message);
        return message;
    }

}

