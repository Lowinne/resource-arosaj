package com.epsi.arosaj.web.controller.v2;

import com.epsi.arosaj.persistence.dto.ConseilDto;
import com.epsi.arosaj.persistence.dto.MessageDto;
import com.epsi.arosaj.persistence.dto.mapper.MessageMapper;
import com.epsi.arosaj.web.exception.UserNotFoundException;
import com.epsi.arosaj.web.message.ResponseFile;
import com.epsi.arosaj.persistence.model.*;
import com.epsi.arosaj.service.PhotoService;
import com.epsi.arosaj.service.PlanteService;
import com.epsi.arosaj.service.UserService;
import com.epsi.arosaj.web.exception.ParameterMistakeException;
import com.epsi.arosaj.web.exception.UnauthorizedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

//lux
@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/api/plante/v2")
public class PlanteControllerV2 {
    //planteController V1
    //PhotoController V1
    private static final Logger logger = LoggerFactory.getLogger(PlanteControllerV2.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PlanteService planteService;

    @Autowired
    private PhotoService photoService;

    @PostMapping(path = "/add")
    @Operation(summary = "add plant to a user and it's first image with pseudo/pwd verification")
    public @ResponseBody Plante addPlante(@RequestHeader String pseudo, @RequestHeader String userPwd, @RequestHeader String nom, @RequestHeader String desc, @RequestParam("file") MultipartFile file ){
        if (nom == null || pseudo == null || userPwd == null || desc == null || file == null){
            logger.error("addPlante: One or more parameters are null");
            throw new ParameterMistakeException("One or more parameters needed null, file needed too");
        }else {
            try{
                Utilisateur user = userService.findUserByPseudo(pseudo,userPwd);
                if(!user.getRole().getCode().equals(TypeEnum.PROPIETAIRE)){
                    logger.error("addPlante: Role invalid");
                    throw new UnauthorizedException("Invalid user role");
                }else{
                    logger.info("addPlante : Trying to save plante of user : " + user.toString());

                    Plante plante = planteService.savePlante(user,nom,desc);

                    if(plante != null){
                        // Upload the image
                        try{
                            photoService.uploadImage(file,user,plante.getId());
                        } catch (RuntimeException e){
                            logger.error("Fail saving first image of plant", e);
                        }
                        return plante;
                    }
                }
            } catch (Exception e) {
                logger.error("addPlante: Erreur lors de la sauvegarde de la plante");
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Error processing");
    }

    @PostMapping(path = "/upload")
    @Operation(summary = "Upload a photo for a plant id, with pseudo/pwd verification")
    public @ResponseBody Photo uploadPhotoOfPlante(@RequestParam("file") MultipartFile file, @RequestHeader String pseudo, @RequestHeader String userPwd, @RequestHeader Long planteId ){
        if (planteId == null || pseudo == null || userPwd == null || userPwd == null || file == null) {
            logger.error("addPhotoOfPlante: One or more parameters are null");
            throw new ParameterMistakeException("One or more parameters needed null, file needed too");
        }
        try {
            Utilisateur user = userService.findUserByPseudo(pseudo,userPwd);

            // Upload the image
            return photoService.uploadImage(file,user,planteId);

        } catch (IOException e) {
            throw new RuntimeException("Failed saving the photo of plante of user");
        }
    }

    @GetMapping("/images")
    @Operation(summary = "Find a list of photo for a plant id, with pseudo/pwd verification")
    public @ResponseBody List<ResponseFile> getPhotoOfPlant(@RequestHeader String pseudo, @RequestHeader String userPwd, @RequestHeader Long planteId) {
        Utilisateur user = userService.findUserByPseudo(pseudo, userPwd);
        try {
            List<Photo> photoList = planteService.getAllPhotoOfPlante(planteId);
            List<ResponseFile> files = photoList.stream()
                    .map(photo -> {
                        String fileDownloadUri = ServletUriComponentsBuilder
                                .fromCurrentContextPath()
                                .path("/files/")
                                .path(String.valueOf(photo.getId()))
                                .toUriString();
                        return new ResponseFile(
                                photo.getName(),
                                fileDownloadUri,
                                photo.getType(),
                                photo.getData().length);
                    })
                    .collect(Collectors.toList());

            return files; // Return the list of ResponseFileDto
        } catch (Exception e) {
            logger.error("Retrieving image failed: ", e);
            throw new RuntimeException("Something happened :( when finding photos of plant");
        }
    }

    @PostMapping(path = "/botaniste/conseil/add")
    @Operation(summary = "Ajoute un conseil à une plante avec verification pseudo/pwd ")
    public @ResponseBody ConseilDto addConseil(@RequestHeader String botanistePseudo, @RequestHeader String pwd, @RequestHeader Long planteId, @RequestHeader String conseil) throws JsonProcessingException {
        Utilisateur user = userService.findUserByPseudo(botanistePseudo,pwd);
        if(user.getRole().getCode().equals(TypeEnum.BOTANISTE)){
            Plante plante = planteService.getPlante(planteId);
            return planteService.saveConseil(user, plante, conseil);
        }
        throw new RuntimeException("Something hapenned :( when saving conseil");
    }

    @GetMapping(path = "/conseils")
    @Operation(summary = "find conseil associated to a plant with pseudo/pwd verification")
    public @ResponseBody List<ConseilDto> getConseil(@RequestHeader Long planteId){
        return planteService.getConseil(planteId);
    }




}
