package com.epsi.arosaj.persistence.repository;

import com.epsi.arosaj.persistence.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByPseudo(String pseudo);

    @Query(value = "SELECT COALESCE(MAX(id), 0) + 1 FROM UTILISATEUR", nativeQuery = true)
    int findLastId();
}
