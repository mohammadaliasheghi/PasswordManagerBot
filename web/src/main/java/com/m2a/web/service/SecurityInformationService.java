package com.m2a.web.service;

import com.m2a.web.entity.SecurityInformationEntity;
import com.m2a.web.enums.RoleEnum;
import com.m2a.web.mapper.SecurityInformationMapper;
import com.m2a.web.model.SecurityInformationModel;
import com.m2a.web.repository.SecurityInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

//todo feature : must be implement update username or password
@Service
public class SecurityInformationService implements UserDetailsService {

    private SecurityInformationRepository repository;
    private AuthoritiesService authoritiesService;
    private PasswordEncoder passwordEncoder;
    private PasswordManagerService passwordManagerService;

    @Override
    public SecurityInformationEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return repository.findUsersByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } catch (IncorrectResultSizeDataAccessException ex) {
            throw new UsernameNotFoundException("User not found with username: " + username, ex);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public String create(SecurityInformationModel model) {
        String validate = this.validateCreate(model);
        if (!validate.isEmpty()) return validate;
        model.setPassword(passwordEncoder.encode(model.getPassword()));
        SecurityInformationEntity entity = SecurityInformationMapper.get().modelToEntity(model);
        entity.setEnabled(true);
        try {
            SecurityInformationEntity created = repository.save(entity);
            authoritiesService.create(created.getId(), RoleEnum.ROLE_USER.getId());
            return "YourAccountHasBeenCreated";
        } catch (Exception e) {
            e.fillInStackTrace();
            return "ServerError";
        }
    }

    private String validateCreate(SecurityInformationModel model) {
        if (model.getUsername() == null)
            return "UsernameIsRequired";
        if (model.getPassword() == null)
            return "PasswordIsRequired";
        Optional<SecurityInformationEntity> entity =
                repository.findUsersByUsername(model.getUsername());
        if (entity.isPresent())
            return "UsernameIsAlreadyTaken";
        return "";
    }

    @Transactional(rollbackFor = Exception.class)
    public String delete(Long id) {
        try {
            passwordManagerService.deleteList(id);
            authoritiesService.deleteBySecurityInformationId(id);
            repository.deleteById(id);
            return "YourAccountHasBeenDeleted";
        } catch (Exception e) {
            e.fillInStackTrace();
            return "ServerError";
        }
    }

    @Autowired
    public void setRepository(SecurityInformationRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setAuthoritiesService(AuthoritiesService authoritiesService) {
        this.authoritiesService = authoritiesService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setPasswordManagerService(PasswordManagerService passwordManagerService) {
        this.passwordManagerService = passwordManagerService;
    }
}
