package com.epsi.arosaj.service;

import com.epsi.arosaj.persistence.dto.UserDto;
import com.epsi.arosaj.persistence.dto.UserPublicDto;
import com.epsi.arosaj.persistence.dto.mapper.UserMapper;
import com.epsi.arosaj.persistence.model.Role;
import com.epsi.arosaj.persistence.model.Utilisateur;
import com.epsi.arosaj.persistence.repository.UserRepository;
import com.epsi.arosaj.web.exception.FindAnotherPseudoException;
import com.epsi.arosaj.web.exception.UserIdMismatchException;
import com.epsi.arosaj.web.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epsi.arosaj.persistence.dto.mapper.UserMapper.convertDtoToEntity;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private VilleService villeService;

    public Utilisateur saveUser(UserDto userDto){
        logger.info("saveUser");
        Utilisateur user = convertDtoToEntity(userDto);
        user.setRole(roleService.getRoleInTable(user.getRole().getCode()));
        user.setVille(villeService.ifNotExistSave(user.getVille().getNom(), user.getVille().getCodePostale()));
        logger.info(user.toString());

        if (!findByPseudo(user.getPseudo()).isEmpty()){
            throw new FindAnotherPseudoException("Entrer un autre pseudo");
        }

        return userRepository.save(user);
    }

    public Utilisateur create(Utilisateur user) {
        logger.info("create : " + user.toString());

        if (userRepository.existsById(user.getId())) {
            logger.info("Un utilisateur avec l'ID " + user.getId() + " existe déjà");
            user.setId(getFreeId());
        }

        if (!findByPseudo(user.getPseudo()).isEmpty()){
            throw new FindAnotherPseudoException("Entrer un autre pseudo");
        }

        return userRepository.save(user);
    }

    public int getFreeId(){
        return userRepository.findLastId();
    }

    public Iterable<Utilisateur> findAll() {
        logger.info("findAll");
        return userRepository.findAll();
    }

    public List findByPseudo(String pseudo){
        logger.info("findByPseudo : " + pseudo);
        return userRepository.findByPseudo(pseudo);
    }

    public Utilisateur findOne(Long id) {
        logger.info("findOne : " + id);
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public void delete(Long id) {
        logger.info("delete : " + id);
        userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
    }

    public Utilisateur updateUser(Utilisateur user, Long id) {
        if (user.getId() != id) {
            throw new UserIdMismatchException();
        }
        logger.info("updateUser : " + id);
        userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return userRepository.save(user);
    }

    public List<UserPublicDto> getAllUsers() {
        Iterable<Utilisateur> userList = userRepository.findAll();
        List<UserPublicDto> userPublicDtoList = new ArrayList<>();
        for(Utilisateur user:userList) {
            // Custom Mapper
            UserPublicDto userPublicDto = UserMapper.convertEntityToDto(user);
            userPublicDtoList.add(userPublicDto);
        }
        return userPublicDtoList;
    }

    public UserPublicDto getUserById(Long userId) {
        Optional<Utilisateur> optionalUser = userRepository.findById(userId);
        // Custom Mapper
        return UserMapper.convertEntityToDto(optionalUser.get());
    }

    //Works for the first version
    public Utilisateur getUserByIdV1(long id){
        logger.info("Searching for user by id :"+id);
        return userRepository.findById(id).get();
    }

    public Utilisateur saveUserV1(Utilisateur user){
        logger.info("Saving user");
        user.setId(getFreeId());
        logger.info(user.toString());
        return userRepository.save(user);
    }

    //Initialise les roles et les user correspondant au demarage
    @EventListener(ApplicationReadyEvent.class)
    public void initRoleV1(){
        logger.info("Initialisation of the table Role");
        Role role1 = new Role("B","Botaniste");
        Role role2 = new Role("P","Proprietaire");
        Role role3 = new Role("G","Gardien");

        roleService.saveRole(role1);
        roleService.saveRole(role2);
        roleService.saveRole(role3);

        logger.info("Initialisation of the table User");
        Utilisateur user1 = new Utilisateur();
        Utilisateur user2 = new Utilisateur();
        Utilisateur user3 = new Utilisateur();

        user1.setNom(randomAlphabetic(5));
        user1.setPrenom(randomAlphabetic(5));
        user1.setEmail(randomAlphabetic(5));
        user1.setPseudo(randomAlphabetic(5));
        user1.setPwd(randomAlphabetic(5));
        user1.setRue(randomAlphabetic(5));
        user1.setRole(roleService.getRoleInTable("P"));
        user1.setVille(villeService.ifNotExistSave(randomAlphabetic(5), "75000"));

        user2.setNom(randomAlphabetic(5));
        user2.setPrenom(randomAlphabetic(5));
        user2.setEmail(randomAlphabetic(5));
        user2.setPseudo(randomAlphabetic(5));
        user2.setPwd(randomAlphabetic(5));
        user2.setRue(randomAlphabetic(5));
        user2.setRole(roleService.getRoleInTable("B"));
        user2.setVille(villeService.ifNotExistSave(randomAlphabetic(5), "75002"));

        user3.setNom("PRINCE");
        user3.setPrenom("Boris");
        user3.setEmail("boris@provider.com");
        user3.setPseudo("Planteur");
        user3.setPwd("12345");
        user3.setRue("10 rue de la fournes");
        user3.setRole(roleService.getRoleInTable("G"));
        user3.setVille(villeService.ifNotExistSave(randomAlphabetic(5), "75012"));

        saveUserV1(user1);
        saveUserV1(user2);
        saveUserV1(user3);

    }


}
