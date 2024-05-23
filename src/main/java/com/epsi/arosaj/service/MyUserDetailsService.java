package com.epsi.arosaj.service;

import com.epsi.arosaj.persistence.model.MyUserDetails;
import com.epsi.arosaj.persistence.model.Utilisateur;
import com.epsi.arosaj.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Assuming you have a UserRepository for fetching user data

    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur user = userRepository.findByPseudo(username).get(0);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new MyUserDetails(user);
    }
}
