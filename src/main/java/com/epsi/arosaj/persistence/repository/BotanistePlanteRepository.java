package com.epsi.arosaj.persistence.repository;

import com.epsi.arosaj.persistence.model.BotanistePlante;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BotanistePlanteRepository extends CrudRepository<BotanistePlante, Long> {
    @Query("SELECT b FROM BotanistePlante b WHERE b.plante.id = :planteid")
    List<BotanistePlante> findByPlante(@Param("planteid") Long idPlante);
}
