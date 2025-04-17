package com.m2a.bot.service;

import com.m2a.bot.entity.TempEntity;
import com.m2a.bot.repository.TempRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TempService {

    private final TempRepository repository;

    @Transactional(rollbackFor = Exception.class)
    public void create(String username, String token, String state) {
        repository.save(new TempEntity(username, token, state, 1, null));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(TempEntity entity) {
        repository.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String username) {
        TempEntity entity = this.findByUsername(username);
        if (entity != null && entity.getId() != null)
            repository.deleteById(entity.getId());
    }

    @Transactional(readOnly = true)
    public TempEntity findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Long getSecurityInformationIdByUsername(String username) {
        return repository.getSecurityInformationIdByUsername(username);
    }
}
