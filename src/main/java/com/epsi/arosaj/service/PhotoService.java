package com.epsi.arosaj.service;

import com.epsi.arosaj.persistence.model.Photo;
import com.epsi.arosaj.persistence.model.PhotoPlante;
import com.epsi.arosaj.persistence.model.Utilisateur;
import com.epsi.arosaj.persistence.repository.PhotoPlanteRepository;
import com.epsi.arosaj.persistence.repository.PhotoRepository;
import com.epsi.arosaj.persistence.repository.PlanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private PlanteRepository planteRepository;
    @Autowired
    private PhotoPlanteRepository photoPlanteRepository;
    public Photo uploadImage(byte[] imageData, Utilisateur user, Long planteId) {
        PhotoPlante photoPlante = new PhotoPlante();
        photoPlante.setPlante(planteRepository.findById(planteId).get());

        Photo photo = new Photo();
        photo.setData(imageData);
        photo.setUtilisateur(user);
        photo.setPhotoPlante(photoPlante);

        photoPlante.getPhotoList().add(photo);

        photoPlante = photoPlanteRepository.save(photoPlante);
        Optional<Photo> optionalPhoto = photoPlante.getPhotoList()
                .stream()
                .filter(p -> p.getUtilisateur().getId() == user.getId() && p.getData() == imageData)
                .findFirst();

        photo = photoRepository.save(optionalPhoto.get());
        if(photo == null){
            throw new RuntimeException("Saving image failed");
        }

        return photo;
    }
}
