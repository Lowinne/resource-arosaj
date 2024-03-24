package com.epsi.arosaj.service;

import com.epsi.arosaj.persistence.model.User;
import com.epsi.arosaj.persistence.repository.UserRepository;
import com.epsi.arosaj.web.exception.FindAnotherPseudoException;
import com.epsi.arosaj.web.exception.UserAlreadyExistsException;
import com.epsi.arosaj.web.exception.UserIdMismatchException;
import com.epsi.arosaj.web.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Random;
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user){
        logger.info("saveUser");
        //user.setId(getFreeId());
        logger.info(user.toString());
        return userRepository.save(user);
    }

    public int getFreeId(){
        return userRepository.findLastId();
    }

    public Iterable<User> findAll() {
        logger.info("findAll");
        return userRepository.findAll();
    }

    public List findByPseudo(String pseudo){
        logger.info("findByPseudo : " + pseudo);
        return userRepository.findByPseudo(pseudo);
    }

    public User findOne(Long id) {
        logger.info("findOne : " + id);
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public User create(User user) {
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

    public void delete(Long id) {
        logger.info("delete : " + id);
        userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
    }

    public User updateUser( User user, Long id) {
        if (user.getId() != id) {
            throw new UserIdMismatchException();
        }
        logger.info("updateUser : " + id);
        userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return userRepository.save(user);
    }

}
