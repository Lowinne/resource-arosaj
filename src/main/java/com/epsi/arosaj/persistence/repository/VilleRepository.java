package com.epsi.arosaj.persistence.repository;

import com.epsi.arosaj.persistence.model.Ville;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VilleRepository extends CrudRepository<Ville, Integer> {
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Ville v WHERE v.nom = :nom AND v.codePostale = :codePostale")
    boolean existsByNomAndCodePostale(@Param("nom") String nomVille, @Param("codePostale") String codePostale);

    @Query("SELECT v FROM Ville v WHERE v.nom = :nom AND v.codePostale = :codePostale")
    Ville getVille(@Param("nom") String nomVille, @Param("codePostale") String codePostale);
}
