package com.m2a.web.controller;

import com.m2a.common.EndPoint;
import com.m2a.web.service.SecurityInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = EndPoint.SECURITY_INFORMATION)
@RequiredArgsConstructor
public class SecurityInformationController {

    private final SecurityInformationService securityInformationService;

    @DeleteMapping(value = EndPoint.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return new ResponseEntity<>(securityInformationService.delete(id), HttpStatus.OK);
    }
}
