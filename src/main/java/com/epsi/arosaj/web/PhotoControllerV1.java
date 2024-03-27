package com.epsi.arosaj.web;

import com.epsi.arosaj.persistence.model.Utilisateur;
import com.epsi.arosaj.persistence.model.Photo;
import com.epsi.arosaj.persistence.repository.PhotoRepository;
import com.epsi.arosaj.service.AuthService;
import com.epsi.arosaj.service.PhotoService;
import com.epsi.arosaj.service.PlanteService;
import com.epsi.arosaj.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api/photo/v1")
public class PhotoControllerV1 {
    private static final Logger logger = LoggerFactory.getLogger(PhotoControllerV1.class);
    @Autowired
    private PhotoService photoService;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private PlanteService planteService;
    @PostMapping(path = "/upload")
    @Operation(summary = "Upload a photo for a plant id, with pseudo/pwd verification")
    public @ResponseBody String addPhotoOfPlante(@RequestParam("file") MultipartFile file, @RequestHeader String userId, @RequestHeader String userPwd, @RequestHeader String planteId ){
        try {
            long id = Long.parseLong(userId);
            long IdPlante = Long.parseLong(planteId);
            Utilisateur user = userService.getUserByIdV1(id);
            if(!authService.isUserAuth(id,userPwd)){
                logger.error("addPhotoOfPlante: Mot de passe érronné");
                return "Mot de passe érronné";
            }else{
                // Check if the file is empty
                if (file.isEmpty()) {
                    return "Please upload a file";
                }

                // Get the file data
                byte[] imageData = file.getBytes();

                // Upload the image
                photoService.uploadImage(imageData,user,IdPlante);

                return "Image uploaded successfully";
            }

        } catch (IOException e) {
            return "Failed to upload image";
        }
    }

    @GetMapping("/images")
    @Operation(summary = "Find a list of photo for a plant id, with pseudo/pwd verification")
    public @ResponseBody List<Photo> getImage(@RequestHeader Long userId, @RequestHeader String userPwd, @RequestHeader Long planteId) {
        if(authService.isUserPlanteAuth(userId,userPwd,planteId)){
            try{
                List<Photo> photoList = planteService.getAllPhotoOfPlante(planteId);
                return photoList;
            }catch(Exception e){
                logger.error("Retreving image failed :",e);
            }
        }
        return null;
    }
}
