package com.epsi.arosaj.persistence.repository;

import com.epsi.arosaj.persistence.model.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    @Query("SELECT r FROM Role r WHERE r.code = :code")
    Role findByCode(@Param("code") String code);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Role r WHERE r.code = :code")
    boolean findByCodeBool(@Param("code") String code);
}

