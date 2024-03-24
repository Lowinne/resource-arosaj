package com.epsi.arosaj.web;

import com.epsi.arosaj.persistence.model.User;
import com.epsi.arosaj.persistence.repository.UserRepository;
import com.epsi.arosaj.service.UserService;
import com.epsi.arosaj.web.exception.UserAlreadyExistsException;
import com.epsi.arosaj.web.exception.UserIdMismatchException;
import com.epsi.arosaj.web.exception.UserNotFoundException;
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
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping(path = "/")
    public Iterable<User> findAll() {
        logger.info("findAll");
        return userRepository.findAll();
    }

    @GetMapping("/pseudo/{pseudo}")
    public List findByPseudo(@PathVariable String pseudo) {
        logger.info("findByPseudo : " + pseudo);
        return userRepository.findByPseudo(pseudo);
    }

    @GetMapping("/{id}")
    public User findOne(@PathVariable Long id) {
        logger.info("findOne : " + id);
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        logger.info("create : " + user.toString());

        if (userRepository.existsById(user.getId())) {
            throw new UserAlreadyExistsException("Un utilisateur avec l'ID " + user.getId() + " existe déjà");
        }

        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.info("delete : " + id);
        userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestBody User user, @PathVariable Long id) {
        if (user.getId() != id) {
            throw new UserIdMismatchException();
        }
        logger.info("updateUser : " + id);
        userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return userRepository.save(user);
    }
}
