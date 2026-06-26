package com.example.finance_backend;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;


import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;

@Service
public class JwtService {
    private final JwtEncoder jwtEncoder;
    public JwtService(@Value("${jwt.secret}") String secret) {
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        this.jwtEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
    }

    public String generateToken(User user) {
        Instant now = Instant.now();


        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("finance-backend")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(60 * 60 * 24))
                .subject(user.getEmail())
                .build();


        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();


        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}




