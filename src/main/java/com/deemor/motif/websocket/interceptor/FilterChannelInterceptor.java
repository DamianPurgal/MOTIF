package com.deemor.motif.websocket.interceptor;

import com.deemor.motif.exception.BusinessException;
import com.deemor.motif.security.configuration.JwtConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

import javax.crypto.SecretKey;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class FilterChannelInterceptor extends ChannelInterceptorAdapter {

    private final JwtConfiguration jwtConfiguration;
    private final SecretKey secretKeyAccessToken;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            try {
                log.info(headerAccessor.getMessageHeaders().toString());
                String authorizationHeader = headerAccessor.getNativeHeader("Authorization").get(0).toString();
                log.info(authorizationHeader);
                String token = authorizationHeader.replace(jwtConfiguration.getTokenPrefix(), "");
                log.info(token);

                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(secretKeyAccessToken)
                        .build()
                        .parseClaimsJws(token);

                Claims body = claimsJws.getBody();
                String username = body.getSubject();

                log.info("USER: " + username + " SUBSCRIBED WEBSOCKET");

                String path = headerAccessor.getDestination() == null ? "" : headerAccessor.getDestination();
                if (isUserSpecificPath(path)) {
                    log.info("PATH: " + path);
                    String usernameFromPath = getUsernameFromPath(path).orElse("");
                    log.info("USERNAME FROM PATH: " + usernameFromPath);
                    if (!username.toLowerCase().equals(usernameFromPath)) {
                        throw new BusinessException(HttpStatus.FORBIDDEN, "FORBIDDEN");
                    }
                }

            } catch (BusinessException businessException) {
                throw businessException;
            } catch (Exception e) {
                throw new BusinessException(HttpStatus.BAD_REQUEST, "CANNOT SUBSCRIBE");
            }
        }
        return message;
    }

    private Optional<String> getUsernameFromPath(String path) {
        if (!isUserSpecificPath(path)) {
            return Optional.empty();
        }
        path = path.toLowerCase();
        String[] pathSegments = path.split("/");

        if (pathSegments.length >= 2) {
            return Optional.of(pathSegments[2]);
        } else {
            return Optional.empty();
        }
    }

    private boolean isUserSpecificPath(String path) {
        return path.toLowerCase().startsWith("/user");
    }

}