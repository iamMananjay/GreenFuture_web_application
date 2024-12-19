package com.example.greenfuture.controller;

import com.example.greenfuture.dto.ReqRes;
import com.example.greenfuture.model.Idea;
import com.example.greenfuture.model.Users;
import org.springframework.security.core.Authentication;

import com.example.greenfuture.service.IdeaService;
import com.example.greenfuture.service.JWTUtils;
import com.example.greenfuture.service.UserService;
import com.example.greenfuture.service.UsersManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AuthController {
    private JWTUtils jwtUtils;


    @Autowired
    private UsersManagementService usersManagementService;
    @Autowired
    private IdeaService ideaService;
    private UserService userService;



    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes req){
        return ResponseEntity.ok(usersManagementService.login(req));
    }


    @GetMapping("/auth/get-profile")
    public ResponseEntity<ReqRes> getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes response = usersManagementService.getMyInfo(email);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }


}







