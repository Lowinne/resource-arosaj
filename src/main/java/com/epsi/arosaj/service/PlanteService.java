package com.epsi.arosaj.service;

import com.epsi.arosaj.persistence.model.*;
import com.epsi.arosaj.persistence.repository.PhotoPlanteRepository;
import com.epsi.arosaj.persistence.repository.PlanteRepository;
import com.epsi.arosaj.persistence.repository.UtilisateurPlanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlanteService {
    @Autowired
    private PlanteRepository planteRepository;
    @Autowired
    private UtilisateurPlanteRepository utilisateurPlanteRepository;
    @Autowired
    private PhotoPlanteRepository photoPlanteRepository;
    @Autowired
    private UserService userService;

    public boolean doesPlanteExist(Long id){
        return planteRepository.existsById(id);
    }
    public Plante getPlante(Long id){
        return planteRepository.findById(id).get();
    }

    public List<Photo> getAllPhotoOfPlante(Long id){
        Plante plante = getPlante(id);
        String idS = Long.toString(plante.getId());
        List<Photo> photoList = new ArrayList<Photo>();
        List<PhotoPlante> photoPlante = photoPlanteRepository.findByPlanteId(idS);
        for(PhotoPlante pp : photoPlante){
            photoList.add(pp.getPhotoList().get(0));
        }
        return photoList;
    }

    public List<Plante> getAllPlanteOfUser(Long id) {
        Utilisateur user = userService.getUserByIdV1(id);
        String idU = Long.toString(user.getId());
        List<Plante> planteList = new ArrayList<Plante>();
        List<UtilisateurPlante> utilisateurPlanteList = utilisateurPlanteRepository.findByUserId(idU);
        for(UtilisateurPlante ut : utilisateurPlanteList){
            planteList.add(ut.getPlanteList().get(0));
        }
        return planteList;
        //utilisateurPlante.getPlanteList();

    }
}
