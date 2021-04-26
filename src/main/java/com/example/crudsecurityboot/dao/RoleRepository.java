package com.example.crudsecurityboot.dao;

import com.example.crudsecurityboot.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;



public interface RoleRepository extends JpaRepository<Role, Long> {

}
