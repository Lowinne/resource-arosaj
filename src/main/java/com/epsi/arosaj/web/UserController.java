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
        return userService.findAll();
    }

    @GetMapping("/pseudo/{pseudo}")
    public List findByPseudo(@PathVariable String pseudo) {
        logger.info("findByPseudo : " + pseudo);
        return userService.findByPseudo(pseudo);
    }

    @GetMapping("/{id}")
    public User findOne(@PathVariable Long id) {
        logger.info("findOne : " + id);
        return userService.findOne(id);
    }

    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        logger.info("create : " + user.toString());
        return userService.create(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.info("delete : " + id);
        userService.delete(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestBody User user, @PathVariable Long id) {
        logger.info("updateUser : " + id);
        return userService.updateUser(user,id);
    }
}
