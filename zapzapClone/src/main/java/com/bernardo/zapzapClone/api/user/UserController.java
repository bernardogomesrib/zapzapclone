package com.bernardo.zapzapClone.api.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardo.zapzapClone.model.user.UserResponse;
import com.bernardo.zapzapClone.model.user.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;


@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getMethodName(Authentication authentication) {
        return ResponseEntity.ok().body(userService.getAllUsers(authentication));
    }

    
}
