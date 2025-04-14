package com.m2a.bot.repository;

import com.m2a.bot.entity.TempEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempRepository extends JpaRepository<TempEntity, Long> {

    TempEntity findByUsername(String username);
}
