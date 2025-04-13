package com.m2a.web.repository;

import com.m2a.web.entity.PasswordManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordManagerRepository extends JpaRepository<PasswordManagerEntity, Long> {
}
