package com.epsi.arosaj.service;

import com.epsi.arosaj.persistence.dto.UserDto;
import com.epsi.arosaj.persistence.dto.UserPublicDto;
import com.epsi.arosaj.persistence.dto.mapper.UserMapper;
import com.epsi.arosaj.persistence.model.Message;
import com.epsi.arosaj.persistence.model.Role;
import com.epsi.arosaj.persistence.model.TypeEnum;
import com.epsi.arosaj.persistence.model.utilisateur;
import com.epsi.arosaj.persistence.repository.MessageRepository;
import com.epsi.arosaj.persistence.repository.UserRepository;
import com.epsi.arosaj.persistence.util.NullChecker;
import com.epsi.arosaj.web.exception.FindAnotherPseudoException;
import com.epsi.arosaj.web.exception.ParameterMistakeException;
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

import static com.epsi.arosaj.persistence.dto.mapper.UserMapper.convertUserDtoToEntity;
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

    @Autowired
    private MessageRepository messageRepository;

    public utilisateur saveUser(UserDto userDto){
        logger.info("saveUser");
        utilisateur user = convertUserDtoToEntity(userDto);
        user.setRole(roleService.getRoleInTable(user.getRole().getCode()));
        user.setVille(villeService.ifNotExistSave(user.getVille().getNom(), user.getVille().getCodePostale()));
        logger.info(user.toString());

        if (!findByPseudo(user.getPseudo()).isEmpty()){
            throw new FindAnotherPseudoException("Entrer un autre pseudo");
        }

        if(!NullChecker.allNotNull(user)){
            throw new ParameterMistakeException("One or more field needed null");
        }

        return userRepository.save(user);
    }

    public utilisateur updateUser(UserDto userDto) {
        utilisateur userTemp = convertUserDtoToEntity(userDto);
        //Determiner si le user existe bien et est authentifié
        utilisateur user = findUserByPseudo(userTemp.getPseudo(),userTemp.getPwd());
        if(user == null){
            throw new UserNotFoundException("Le pseudo et le mot de passe ne correspondent pas");
        }
        userTemp.setId(user.getId());
        userTemp.setRole(roleService.getRoleInTable(userTemp.getRole().getCode()));
        userTemp.setVille(villeService.ifNotExistSave(userTemp.getVille().getNom(), userTemp.getVille().getCodePostale()));
        logger.info(userTemp.toString());

        return userRepository.save(userTemp);
    }

    public void delete(UserDto userDto)  {
        utilisateur userTemp = convertUserDtoToEntity(userDto);
        //Determiner si le user existe bien et est authentifié
        utilisateur user = findUserByPseudo(userTemp.getPseudo(),userTemp.getPwd());
        if(user == null){
            throw new UserNotFoundException("Le pseudo et le mot de passe ne correspondent pas");
        }
        userTemp.setId(user.getId());

        userRepository.deleteById(userTemp.getId());
    }

    public utilisateur create(utilisateur user) {
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

    public Iterable<utilisateur> findAll() {
        logger.info("findAll");
        return userRepository.findAll();
    }

    public List<utilisateur> findAllGardien() {
        logger.info("findAll");
        Iterable<utilisateur> listAllUser = findAll();
        List<utilisateur> listUser = new ArrayList<>();
        if(listAllUser != null) {
            for(utilisateur user : listAllUser){
                if(user.getRole().getCode() == TypeEnum.GARDIEN){
                    listUser.add(user);
                }
            }
        }
        if(listUser.isEmpty()){
            throw new UserNotFoundException("No gardien found");
        }
        return listUser;
    }

    public List<utilisateur> findByPseudo(String pseudo){
        logger.info("findByPseudo : " + pseudo);
        return userRepository.findByPseudo(pseudo);
    }

    public utilisateur findUserByPseudo(String pseudo, String pwd) {
        if(pwd == null){
            throw new ParameterMistakeException("Entrer un mot de passe");
        } else if (pseudo == null) {
            throw new ParameterMistakeException("Entrer un pseudo");
        }
        logger.info("findByPseudo : " + pseudo);
        utilisateur temp = userRepository.findUserByPseudo(pseudo, pwd);
        if(temp == null){
            throw new UserNotFoundException("Utilisateur non trouvé / Le mot de passe et le pseudo ne correspondent pas");
        }
        return temp;
    }

    public utilisateur findOne(Long id) {
        logger.info("findOne : " + id);
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public List<UserPublicDto> getAllUsers() {
        Iterable<utilisateur> userList = userRepository.findAll();
        List<UserPublicDto> userPublicDtoList = new ArrayList<>();
        for(utilisateur user:userList) {
            // Custom Mapper
            UserPublicDto userPublicDto = UserMapper.convertEntityToUserPublicDto(user);
            userPublicDtoList.add(userPublicDto);
        }
        return userPublicDtoList;
    }

    public UserPublicDto getUserById(Long userId) {
        Optional<utilisateur> optionalUser = userRepository.findById(userId);
        // Custom Mapper
        return UserMapper.convertEntityToUserPublicDto(optionalUser.get());
    }

    //Works for the first version
    public utilisateur getUserByIdV1(long id){
        logger.info("Searching for user by id :"+id);
        return userRepository.findById(id).get();
    }

    public utilisateur saveUserV1(utilisateur user){
        logger.info("Saving user");
        user.setId(getFreeId());
        logger.info(user.toString());
        if(userRepository.findByPseudo(user.getPseudo()) == null){
            return userRepository.save(user);
        }
       return null;
    }

    public List<Message> getAllMessage(){
        Iterable<Message> iterableMessage = messageRepository.findAll();
        List<Message> listMessage = new ArrayList<>();
        if(iterableMessage != null){

            for(Message m : iterableMessage){
                listMessage.add(m);
            }
        }else { throw new UserNotFoundException("Pas de message correspondant"); }
        return listMessage;


    }

    public Message saveMessage(Message message) {
        return message = messageRepository.save(message);
    }

    public Iterable<Role> getAllRole(){
        return roleService.getAllRole();
    }

    //Initialise les roles et les user correspondant au demarage
    @EventListener(ApplicationReadyEvent.class)
    public void initRoleV1(){
        logger.info("Initialisation of the table Role");
        Role role1 = new Role(TypeEnum.BOTANISTE,"Botaniste");
        Role role2 = new Role(TypeEnum.PROPIETAIRE,"Proprietaire");
        Role role3 = new Role(TypeEnum.GARDIEN,"Gardien");

        roleService.saveRoleNotIfExist(role1);
        roleService.saveRoleNotIfExist(role2);
        roleService.saveRoleNotIfExist(role3);

        logger.info("Initialisation of the table User");
        utilisateur user1 = new utilisateur();
        utilisateur user2 = new utilisateur();
        utilisateur user3 = new utilisateur();
        utilisateur user4 = new utilisateur();

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

        user4.setNom("PRINCE");
        user4.setPrenom("Boris");
        user4.setEmail("test");
        user4.setPseudo("test");
        user4.setPwd("12345");
        user4.setRue("10 rue de la fournes");
        user4.setRole(roleService.getRoleInTable("P"));
        user4.setVille(villeService.ifNotExistSave(randomAlphabetic(5), "75012"));

        saveUserV1(user1);
        saveUserV1(user2);
        saveUserV1(user3);
        saveUserV1(user4);

    }



}
