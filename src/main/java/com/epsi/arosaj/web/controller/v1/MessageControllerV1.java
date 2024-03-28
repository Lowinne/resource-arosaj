package com.epsi.arosaj.web.controller.v1;

import com.epsi.arosaj.persistence.model.Message;
import com.epsi.arosaj.persistence.model.Role;
import com.epsi.arosaj.persistence.model.Utilisateur;
import com.epsi.arosaj.persistence.model.Ville;
import com.epsi.arosaj.persistence.repository.MessageRepository;
import com.epsi.arosaj.persistence.repository.RoleRepository;
import com.epsi.arosaj.persistence.repository.UserRepository;
import com.epsi.arosaj.persistence.repository.VilleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;

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
    private UserRepository userRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    //methode qui doit prendre des int id plutot que des full user
    @PostMapping(path = "/add")
    public @ResponseBody Message newMessage(@RequestBody Message message) throws JsonProcessingException {
        message = messageRepository.save(message);
        return message;
    }

    @GetMapping(path = "/format")
    public @ResponseBody String getFormat() throws JsonProcessingException {
        Ville ville = new Ville();
        ville.setNom("Massy");
        ville.setCodePostale("91300");
        ville.setId(villeRepository.save(ville).getId());

        Ville ville1 = new Ville();
        ville1.setNom("Paris");
        ville1.setCodePostale("75000");
        ville1.setId(villeRepository.save(ville1).getId());

        Role role = new Role();
        role.setCode("P");
        role.setRole("Proprio");
        role.setId(roleRepository.save(role).getId());

        Role role1 = new Role();
        role1.setCode("G");
        role1.setRole("Gardien");
        role1.setId(roleRepository.save(role1).getId());

        Utilisateur user = new Utilisateur();
        user.setNom("PRINCE");
        user.setPrenom("Boris");
        user.setPseudo("Planteur");
        user.setEmail("boris@rovider.fr");
        user.setRue("10 rue Marco Polo");
        user.setVille(ville);
        user.setRole(role);
        user.setId(userRepository.save(user).getId());

        Utilisateur user1 = new Utilisateur();
        user1.setNom("PRINCE");
        user1.setPrenom("Boris");
        user1.setPseudo("Planteur");
        user1.setEmail("boris@rovider.fr");
        user1.setRue("10 rue Marco Polo");
        user1.setVille(ville1);
        user1.setRole(role1);
        user1.setId(userRepository.save(user1).getId());

        Message message = new Message();
        message.setExpediteur(user);
        message.setDestinataire(user1);
        message.setMessage("Tu fais quoi la ?");
        message.setDate(new Date(2024, 02, 28));
        message.setTime(new Time(23, 36, 5));

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(message);

        return json;
    }
}

