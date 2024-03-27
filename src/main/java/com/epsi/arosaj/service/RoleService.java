package com.epsi.arosaj.service;

import com.epsi.arosaj.persistence.model.Role;
import com.epsi.arosaj.persistence.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private RoleRepository roleRepository;

    public Role saveRole(Role role){
        logger.info("Saving a role : " + role.toString());
        return roleRepository.save(role);
    }

    public Role getRoleInTable(String code){
        logger.info("Searching a role by code : " + code);
        if(roleRepository.findByCodeBool(code)){
            return getRole(code);
        }
        else {
            logger.info("Code not found :"+code+". P by default");
            return getRole("P");
        }
    }

    public Role getRole(String code){
        logger.info("Searching a role by code : " + code);
        return roleRepository.findByCode(code);
    }

    public Iterable<Role> getAllRole() {
        return roleRepository.findAll();
    }
}
