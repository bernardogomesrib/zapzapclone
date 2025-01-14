package com.bernardo.zapzapClone.model.user;

import java.util.Optional;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSynchronizer {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public void synchronizeUser(Jwt token) {
        log.info("Synchronizing user with idp");
        getEmail(token).ifPresent(email -> {
            User user = userMapper.toUser(token.getClaims());
            userRepository.save(user);
        });
    }
    public Optional<String> getEmail(Jwt token) {
        return Optional.ofNullable(token.getClaim("email"));
    }


}
