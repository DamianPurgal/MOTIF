package com.deemor.motif.security.configuration;

import com.deemor.motif.user.service.AppUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.deemor.motif.security.CustomAuthenticationFailureHandler;
import com.deemor.motif.security.filter.JwtAuthenticationFilter;
import com.deemor.motif.security.filter.JwtAuthorizationFilter;

import javax.crypto.SecretKey;
import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends AbstractHttpConfigurer<SecurityConfiguration, HttpSecurity> {

    private final PasswordEncoder passwordEncoder;

    private final AppUserService userService;

    private final JwtConfiguration jwtConfiguration;

    private final SecretKey secretKeyAccessToken;

    private final SecretKey secretKeyRefreshToken;

    public SecurityConfiguration(AppUserService userService,
                                 PasswordEncoder passwordEncoder,
                                 JwtConfiguration jwtConfiguration,
                                 @Qualifier("secretKeyAccessToken") SecretKey secretKeyAccessToken,
                                 @Qualifier("secretKeyRefreshToken") SecretKey secretKeyRefreshToken) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfiguration = jwtConfiguration;
        this.secretKeyAccessToken = secretKeyAccessToken;
        this.secretKeyRefreshToken = secretKeyRefreshToken;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/api/auth/register").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll();

        http.authenticationProvider(authenticationProvider());

        JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtConfiguration, secretKeyAccessToken, secretKeyRefreshToken);
        authenticationFilter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());

        http.addFilter(authenticationFilter)
                .addFilterAfter(new JwtAuthorizationFilter(jwtConfiguration, secretKeyAccessToken), JwtAuthenticationFilter.class);

        http.sessionManagement().sessionCreationPolicy(STATELESS);

        return http.cors().and().build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }

}
