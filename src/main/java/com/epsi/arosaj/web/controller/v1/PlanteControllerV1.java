package com.epsi.arosaj.web.controller.v1;

import com.epsi.arosaj.persistence.dto.ConseilDto;
import com.epsi.arosaj.persistence.model.*;
import com.epsi.arosaj.persistence.repository.*;
import com.epsi.arosaj.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/plante/v1")
public class PlanteControllerV1 {
    private static final Logger logger = LoggerFactory.getLogger(PlanteControllerV1.class);
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private PlanteRepository planteRepository;
    @Autowired
    private PhotoPlanteRepository photoPlanteRepository;
    @Autowired
    private UtilisateurPlanteRepository utilisateurPlanteRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BotanistePlanteRepository botanistePlanteRepository;
    //TODO : Factorisé les appel repository en service
    @PostMapping(path = "/add")
    @Operation(summary = "add plant to a user with it's password")
    public @ResponseBody String addPlante(@RequestHeader String userId, @RequestHeader String userPwd, @RequestHeader String nom, @RequestHeader String desc){
        if (nom == null || userId == null || userPwd == null || desc == null){
            logger.error("addPlante: One or more parameters are null");
            return "One or more parameters are null";
        }else {
            try{
                long id = Long.parseLong(userId);
                utilisateur user = userService.getUserByIdV1(id);
                if(!user.getPwd().equals(userPwd) || !user.getRole().getCode().equals("P")){
                    logger.error("addPlante: Mot de passe érronné");
                    return "Mot de passe ou role érronné";
                }else{
                    logger.info("addPlante : mot de passe valide");

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

                    planteRepository.save(optionalPlante.get());

                    return "Saved";
                }
            } catch (Exception e) {
                logger.error("addPlante: Erreur lors de la sauvegarde de la plante");
                throw new RuntimeException(e);
            }

        }


    }

    @GetMapping(path = "/conseil")
    @Operation(summary = "find conseil associated to a plant")
    public @ResponseBody List<ConseilDto> getConseil(@RequestHeader Long idPlante){
        List<ConseilDto> conseils = new ArrayList<ConseilDto>();
        List<BotanistePlante> botanistePlanteList = botanistePlanteRepository.findByPlante(idPlante);
        for(BotanistePlante bp : botanistePlanteList){
            conseils.add(new ConseilDto(bp.getBotaniste().getPseudo(),bp.getConseil()));
        }
        return conseils;
    }

}
