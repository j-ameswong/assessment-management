package uk.ac.sheffield.team_project_team_24.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import uk.ac.sheffield.team_project_team_24.dto.TokenDTO;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public TokenDTO generateToken(Collection<? extends GrantedAuthority> authorities, String username) {
        Instant now = Instant.now();
        System.out.println("Username is: " + username);
        String scope = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(2, ChronoUnit.HOURS))
                .subject(username)
                .claim("scope", scope)
                .build();
        return new TokenDTO(this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
    }
}
