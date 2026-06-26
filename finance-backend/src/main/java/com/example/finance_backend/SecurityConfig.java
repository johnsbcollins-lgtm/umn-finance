package com.example.finance_backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(
                HttpSecurity http, @Value("${jwt.secret}") String secret)
            throws Exception {
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            //need this to verify/create jwt stuff
            http
                    .csrf(csrf -> csrf.disable())
                    //no cookies etc so this is fine
                    .sessionManagement(session ->
                            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                            //tells spring security not to create a session
                    )
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/auth/**").permitAll() //any /auth is good
                            .anyRequest().authenticated() //eveyrthing else dids authentication
                    )
                    //configures app as OAuth2 resource server
                    //this helps verify JWT tokens
                    .oauth2ResourceServer(oauth2 ->
                            oauth2.jwt(jwt ->
                                    jwt.decoder(NimbusJwtDecoder.withSecretKey(secretKey).build())
                                    //creates decoder for JWT tokens
                                    //withSecretKey says only trust THIS secretKey
                            )
                    );
            //finally sets SecurityFilterChain that gives Spring a security system
            //it can enforce on every request
            return http.build();
        }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
