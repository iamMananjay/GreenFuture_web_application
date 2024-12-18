package com.example.greenfuture.controller;

import com.example.greenfuture.dto.ReqRes;
import com.example.greenfuture.model.Idea;
import com.example.greenfuture.service.IdeaService;
import com.example.greenfuture.service.UsersManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private UsersManagementService usersManagementService;
    @Autowired
    private IdeaService ideaService;


    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes req){
        return ResponseEntity.ok(usersManagementService.login(req));
    }



}







