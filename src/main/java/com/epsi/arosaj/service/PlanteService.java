package com.epsi.arosaj.service;

import com.epsi.arosaj.persistence.dto.ConseilDto;
import com.epsi.arosaj.persistence.dto.PlanteDto;
import com.epsi.arosaj.persistence.model.*;
import com.epsi.arosaj.persistence.repository.BotanistePlanteRepository;
import com.epsi.arosaj.persistence.repository.PhotoPlanteRepository;
import com.epsi.arosaj.persistence.repository.PlanteRepository;
import com.epsi.arosaj.persistence.repository.UtilisateurPlanteRepository;
import com.epsi.arosaj.web.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private BotanistePlanteRepository botanistePlanteRepository;


    public boolean doesPlanteExist(Long id){
        return planteRepository.existsById(id);
    }
    public Plante getPlante(Long id){
        Plante plante = planteRepository.findById(id).get();
        if(plante == null){
            throw new UserNotFoundException("Plante Not Found");
        }
        return plante;
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

    public List<PlanteDto> getAllPlante(){
        Iterable<Plante> planteIterable = planteRepository.findAll();
        if (planteIterable == null) {
            throw new UserNotFoundException("Plante Not Found");
        }
        List<PlanteDto> planteList = new ArrayList<>();
        for (Plante plante : planteIterable ) {
            PlanteDto planteDto = new PlanteDto(plante.getId(), plante.getNom(), plante.getDescription(), plante.getUtilisateurPlante().getProprietaire().getPrenom(), plante.getUtilisateurPlante().getProprietaire().getPseudo());
            planteList.add(planteDto);
        }
        return planteList;
    }

    public Plante savePlante(Utilisateur user, String nom, String desc){
        UtilisateurPlante utilisateurPlante = new UtilisateurPlante();
        utilisateurPlante.setProprietaire(user);

        Plante plante = new Plante();
        plante.setNom(nom);
        plante.setDescription(desc);
        plante.setUtilisateurPlante(utilisateurPlante);

        utilisateurPlante.getPlanteList().add(plante);

        utilisateurPlante = utilisateurPlanteRepository.save(utilisateurPlante);
        Optional<Plante> optionalPlante = utilisateurPlante.getPlanteList()
                .stream()
                .filter(p -> p.getNom().equals(nom) && p.getDescription().equals(desc))
                .findFirst();

        plante = planteRepository.save(optionalPlante.get());

        return plante;
    }

    public ConseilDto saveConseil(Utilisateur user, Plante plante, String conseil){
        BotanistePlante botanistePlante = new BotanistePlante();
        botanistePlante.setBotaniste(user);
        botanistePlante.setConseil(conseil);
        botanistePlante.setPlante(plante);
        botanistePlante = botanistePlanteRepository.save(botanistePlante);
        if (botanistePlante == null){
            throw new RuntimeException("Fail to save conseil");
        }
        ConseilDto conseilDto = new ConseilDto(botanistePlante.getBotaniste().getPseudo(), botanistePlante.getConseil());

        return conseilDto;
    }

    public List<ConseilDto> getConseil(Long idPlante){
        List<ConseilDto> conseils = new ArrayList<ConseilDto>();
        List<BotanistePlante> botanistePlanteList = botanistePlanteRepository.findByPlante(idPlante);
        for(BotanistePlante bp : botanistePlanteList){
            conseils.add(new ConseilDto(bp.getBotaniste().getPseudo(),bp.getConseil()));
        }
        if (conseils.isEmpty()){
            throw new UserNotFoundException("Pas de conseil trouvé");
        }
        return conseils;
    }


}
