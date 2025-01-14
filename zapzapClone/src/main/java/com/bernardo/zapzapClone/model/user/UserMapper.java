package com.bernardo.zapzapClone.model.user;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.bernardo.zapzapClone.exception.TokenSemIdException;

@Service
public class UserMapper {
/*  private String id;
    private String firstName;
    private String lastName;
    private String email;
*/
    public User toUser(Map<String,Object> token) {
        User user = new User();
        if(token.containsKey("sub")) {
            user.setId(token.get("sub").toString());
        }else{
            throw new TokenSemIdException("User id not found in token");
        }
        if(token.containsKey("given_name")) {
            user.setFirstName(token.get("given_name").toString());

        }else if(token.containsKey("nickname")) {
            user.setFirstName(token.get("nickname").toString());
        }
        if(token.containsKey("family_name")) {
            user.setLastName(token.get("family_name").toString());
        }
        user.setEmail(token.get("email").toString());

        user.setLastSeenAt(LocalDateTime.now());
        return user;
    }

    public User updateUser(User user, Jwt token) {
        User newUser = toUser(token.getClaims());
        newUser.setId(user.getId());
        return newUser;
    }

}
