package com.example.crudsecurityboot.dao;

import com.example.crudsecurityboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public interface UserRepository extends JpaRepository<User, Long> {

}
