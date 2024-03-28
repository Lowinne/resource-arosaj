package com.epsi.arosaj.service;

import com.epsi.arosaj.persistence.model.Ville;
import com.epsi.arosaj.persistence.repository.VilleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VilleService {

    private static final Logger logger = LoggerFactory.getLogger(VilleService.class);
    @Autowired
    VilleRepository villeRepository;

    public Ville ifNotExistSave(String nomVille, String codePostale){
        logger.info("Does the ville already exists ?");
        if(!villeRepository.existsByNomAndCodePostale(nomVille,codePostale)){
            Ville ville = new Ville();
            ville.setNom(nomVille);
            ville.setCodePostale(codePostale);
            logger.info("No");
            return saveVille(ville);
        }else{
            logger.info("Yes");
            return villeRepository.getVille(nomVille, codePostale);
        }
    }

    public Ville saveVille(Ville ville){
        logger.info("Saving ville entity :"+ville.toString());
        return villeRepository.save(ville);
    }
}
