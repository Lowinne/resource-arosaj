package com.epsi.arosaj.persistence.repository;

import com.epsi.arosaj.persistence.model.Photo;
import org.springframework.data.repository.CrudRepository;

public interface PhotoRepository extends CrudRepository<Photo,Long> {
}
