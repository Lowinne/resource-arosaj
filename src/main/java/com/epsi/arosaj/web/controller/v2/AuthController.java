package com.epsi.arosaj.web.controller.v2;

import com.epsi.arosaj.config.utility.JwtUtil;
import com.epsi.arosaj.persistence.model.AuthRequest;
import com.epsi.arosaj.persistence.model.AuthResponse;
import com.epsi.arosaj.persistence.model.MyUserDetails;
import com.epsi.arosaj.persistence.model.Utilisateur;
import com.epsi.arosaj.service.MyUserDetailsService;
import com.epsi.arosaj.service.RoleService;
import com.epsi.arosaj.service.UserService;
import com.epsi.arosaj.service.VilleService;
import com.epsi.arosaj.web.exception.FindAnotherPseudoException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private VilleService villeService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> createToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new Exception("Invalid username or password", e);
        }

        final MyUserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new AuthResponse(jwt, userDetails.getUser()));
    }

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

}
