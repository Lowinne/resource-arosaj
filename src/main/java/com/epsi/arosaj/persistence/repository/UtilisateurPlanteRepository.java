package com.epsi.arosaj.persistence.repository;

import com.epsi.arosaj.persistence.model.UtilisateurPlante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UtilisateurPlanteRepository extends JpaRepository<UtilisateurPlante, Long> {
    @Query("SELECT u FROM UtilisateurPlante u WHERE u.proprietaire.id = :userid ")
    List<UtilisateurPlante> findByUserId(@Param("userid") String idU);
}
