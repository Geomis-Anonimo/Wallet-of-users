package br.com.wallet.config;

import br.com.wallet.security.JwtTokenProvider;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.time.Duration;

@Configuration
public class JwtConfig {

    @Value("${security.jwt.secret}")
    private String base64Secret;

    @Value("${security.jwt.expiration-seconds:3600}")
    private long expirationSeconds;

    @Bean
    public SecretKey jwtSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(SecretKey secretKey) {
        return new JwtTokenProvider(secretKey, Duration.ofSeconds(expirationSeconds));
    }
}