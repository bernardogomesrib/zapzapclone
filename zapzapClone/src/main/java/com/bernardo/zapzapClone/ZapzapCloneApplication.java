package com.bernardo.zapzapClone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@EnableJpaAuditing
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                implicit = @OAuthFlow(
                        authorizationUrl = "http://localhost:6969/realms/zapzap/protocol/openid-connect/auth",
                        tokenUrl = "http://localhost:6969/realms/zapzap/protocol/openid-connect/token"
                )

        ),
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@SecurityScheme(name = "keycloak", type = SecuritySchemeType.OAUTH2, bearerFormat = "JWT", scheme="bearer",in = SecuritySchemeIn.HEADER,
	flows = @OAuthFlows(password = @OAuthFlow(
		tokenUrl = "http://localhost:6969/realms/zapzap/protocol/openid-connect/token",
		authorizationUrl = "http://localhost:6969/realms/zapzap/protocol/openid-connect/auth"))
		
)
public class ZapzapCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZapzapCloneApplication.class, args);
	}

}
