package com.epsi.arosaj.service;

import com.epsi.arosaj.persistence.model.User;
import com.epsi.arosaj.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public User getUserById(long id){
        logger.info("getUserById : " + id);
        return userRepository.findById(id).get();
    }

}
