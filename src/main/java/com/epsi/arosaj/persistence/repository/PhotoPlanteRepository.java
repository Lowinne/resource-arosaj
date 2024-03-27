package com.epsi.arosaj.persistence.repository;

import com.epsi.arosaj.persistence.model.PhotoPlante;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoPlanteRepository extends CrudRepository<PhotoPlante,Long> {
    @Query("SELECT p FROM PhotoPlante p WHERE p.plante.id = :planteId")
    List<PhotoPlante> findByPlanteId(@Param("planteId") String planteId);
}
