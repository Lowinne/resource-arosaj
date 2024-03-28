package com.epsi.arosaj.persistence.repository;

import com.epsi.arosaj.persistence.model.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

}
