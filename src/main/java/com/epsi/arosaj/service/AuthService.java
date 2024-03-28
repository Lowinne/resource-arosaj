package com.epsi.arosaj.service;

import com.epsi.arosaj.persistence.model.Plante;
import com.epsi.arosaj.persistence.model.Utilisateur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private PlanteService planteService;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    public boolean isUserAuth(Long id, String pwd){
        logger.info("isUserAuth: Verification mot de passe");
        Utilisateur user = userService.getUserByIdV1(id);
        return user.getPwd().equals(pwd);
    }

    public boolean isUserPlanteAuth(Long id, String pwd, Long planteId){
        logger.info("isUserPlanteAuth: Verification mot de passe et propriete plante");
        if(isUserAuth(id,pwd)){
            Plante plante = planteService.getPlante(planteId);
            return plante.getUtilisateurPlante().getProprietaire().getId() == id;
        }
        logger.info("isUserPlanteAuth: Mot de passe ou propriete plante non valide");
        return false;
    }
}
