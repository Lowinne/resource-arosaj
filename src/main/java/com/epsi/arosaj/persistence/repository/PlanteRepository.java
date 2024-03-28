package com.epsi.arosaj.persistence.repository;

import com.epsi.arosaj.persistence.model.Plante;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanteRepository extends CrudRepository<Plante, Long> {
}
