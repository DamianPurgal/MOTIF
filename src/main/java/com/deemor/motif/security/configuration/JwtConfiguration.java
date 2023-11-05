package com.deemor.motif.security.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfiguration {

    private String secretKeyAccessToken;
    private String secretKeyRefreshToken;
    private String tokenPrefix;
    private Integer accessTokenExpirationTimeInDays;
    private Integer refreshTokenExpirationTimeInDays;

}
