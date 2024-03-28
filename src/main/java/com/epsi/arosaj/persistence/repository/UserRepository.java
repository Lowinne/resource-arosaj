package com.epsi.arosaj.persistence.repository;

import com.epsi.arosaj.persistence.model.Utilisateur;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<Utilisateur, Long> {

    List<Utilisateur> findByPseudo(String pseudo);

    @Query(value = "SELECT COALESCE(MAX(id), 0) + 1 FROM Utilisateur", nativeQuery = true)
    int findLastId();

    @Query("SELECT u FROM Utilisateur u WHERE u.pseudo = :pseudo AND u.pwd = :pwd")
    Utilisateur findUserByPseudo(@Param("pseudo") String pseudo, @Param("pwd") String pwd);
}
