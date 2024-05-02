package com.epsi.arosaj.service;

import com.epsi.arosaj.persistence.model.Photo;
import com.epsi.arosaj.persistence.model.PhotoPlante;
import com.epsi.arosaj.persistence.model.utilisateur;
import com.epsi.arosaj.persistence.repository.PhotoPlanteRepository;
import com.epsi.arosaj.persistence.repository.PhotoRepository;
import com.epsi.arosaj.persistence.repository.PlanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private PlanteRepository planteRepository;
    @Autowired
    private PhotoPlanteRepository photoPlanteRepository;
    public Photo uploadImage(MultipartFile file, utilisateur user, Long planteId) throws IOException {


        PhotoPlante photoPlante = new PhotoPlante();
        photoPlante.setPlante(planteRepository.findById(planteId).get());

        Photo photo = new Photo();
        photo.setData(file.getBytes());
        photo.setType(file.getContentType());
        photo.setName(StringUtils.cleanPath(file.getOriginalFilename()));
        photo.setUtilisateur(user);
        photo.setPhotoPlante(photoPlante);

        photoPlante.getPhotoList().add(photo);

        photoPlante = photoPlanteRepository.save(photoPlante);
        Optional<Photo> optionalPhoto = photoPlante.getPhotoList()
                .stream()
                .filter(p -> {
                    try {
                        return p.getUtilisateur().getId() == user.getId() && p.getData() == file.getBytes();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .findFirst();

        photo = photoRepository.save(optionalPhoto.get());
        if(photo == null){
            throw new RuntimeException("Saving image failed");
        }

        return photo;
    }

    public Photo getPhoto(Long id){
        return photoRepository.findById(id).get();
    }

    public Iterable<Photo> getAllFiles() {
        return photoRepository.findAll() ;
    }

}
