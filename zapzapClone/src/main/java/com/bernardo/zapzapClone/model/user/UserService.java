package com.bernardo.zapzapClone.model.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    public List<UserResponse> getAllUsers(Authentication authentication) {
        return userRepository.findAllButMe(authentication.getName()).stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }
    
}
