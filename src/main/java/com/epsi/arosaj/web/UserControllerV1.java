package com.epsi.arosaj.web;

import com.epsi.arosaj.persistence.model.BotanistePlante;
import com.epsi.arosaj.persistence.model.Plante;
import com.epsi.arosaj.persistence.model.Utilisateur;
import com.epsi.arosaj.persistence.repository.*;
import com.epsi.arosaj.service.*;
import com.epsi.arosaj.web.exception.FindAnotherPseudoException;
import com.epsi.arosaj.web.exception.UserNotFoundException;
import com.epsi.arosaj.web.exception.ParameterMistakeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/v1")
public class UserControllerV1 {

    private static final Logger logger = LoggerFactory.getLogger(UserControllerV1.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VilleRepository villeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private VilleService villeService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PlanteService planteService;

    @Autowired
    private BotanistePlanteRepository botanistePlanteRepository;

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found ",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Utilisateur.class)) }),
            @ApiResponse(responseCode = "400", description = "Parameter Missing or Pseudo already taken",
                    content = @Content) })
    @PostMapping(path = "/add")
    public @ResponseBody Utilisateur addUser(@RequestHeader String nom, @RequestHeader String prenom, @RequestHeader String pseudo, @RequestHeader String email, @RequestHeader String rue,
                                      @RequestHeader String codeRole, @RequestHeader String nomVille, @RequestHeader String codePostale, @RequestHeader String pwd) throws JsonProcessingException {
        logger.info("addUser: Adding new user endpoint");
        Utilisateur user = new Utilisateur();
        if (nom == null || prenom == null || pseudo == null || email == null ||
                rue == null || codeRole == null || nomVille == null || codePostale == null || pwd == null){
            logger.error("addUser: One or more parameters are null");
            throw new ParameterMistakeException("Il manque un ou plusieurs paramètres dans le header");
        }else {
            user.setNom(nom);
            user.setPrenom(prenom);
            user.setPseudo(pseudo);
            //TODO : Email validator && Ville validator && Password
            user.setEmail(email);
            user.setRue(rue);
            user.setPwd(pwd);
            user.setRole(roleService.getRoleInTable(codeRole));
            user.setVille(villeService.ifNotExistSave(nomVille, codePostale));
            try{
                user = userService.saveUserV1(user);
                // ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                // String json = ow.writeValueAsString(user);
                return user;
            } catch (DataIntegrityViolationException e){
                logger.error("addUser: Erreur lors de la sauvegarde de l'utilisateur",e);
            }
            throw new FindAnotherPseudoException("Le pseudo est déjà utilisé");
        }
    }

    @GetMapping(path = "/get")
    @Operation(summary = "Find a user by its pseudo and password")
    public @ResponseBody Utilisateur getUser(@RequestHeader String pseudo, @RequestHeader String pwd){
        logger.info("getUser: get the user : "+ pseudo);
        Utilisateur user = new Utilisateur();
        user = userRepository.findUserByPseudo(pseudo, pwd);
        if (user == null){throw new UserNotFoundException("Aucun utilisateur ne correspond");}
        //ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        //String json = ow.writeValueAsString(user);
        return user;

    }

    @GetMapping(path = "/plante")
    @Operation(summary = "Donne la liste des plantes d'un utilisateur")
    public @ResponseBody List<Plante> getPlanteOfUser(@RequestHeader Long idUser, @RequestHeader String pwd ){
        if(authService.isUserAuth(idUser,pwd)){
            try{
                List<Plante> planteList = planteService.getAllPlanteOfUser(idUser);
                return planteList;
            }catch (Exception e){
                logger.error("getPlanteOfUser: Retreving plantes failed :",e);
            }
        }
        return null;
    }

    @PostMapping(path = "/botaniste/conseil")
    @Operation(summary = "Ajoute un conseil à une plante")
    public @ResponseBody BotanistePlante addConseil(@RequestHeader Long botanisteId, @RequestHeader String pwd, @RequestHeader Long planteId, @RequestHeader String conseil) throws JsonProcessingException {
        if(authService.isUserAuth(botanisteId,pwd)){
            Utilisateur user = userService.getUserByIdV1(botanisteId);
            Plante plante = planteService.getPlante(planteId);
            if(user.getRole().getCode().equals("B")){
                BotanistePlante botanistePlante = new BotanistePlante();
                botanistePlante.setBotaniste(user);
                botanistePlante.setConseil(conseil);
                botanistePlante.setPlante(plante);
                botanistePlante = botanistePlanteRepository.save(botanistePlante);

                // ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                // String json = ow.writeValueAsString(botanistePlante);

                return botanistePlante;
            }else{
                throw new ParameterMistakeException("L'utilisateur n'est pas botaniste");
            }
        }else{
            throw new ParameterMistakeException("Utilisateur non autentifié : id et mdp ne conrresponent pas");
        }
    }

}
