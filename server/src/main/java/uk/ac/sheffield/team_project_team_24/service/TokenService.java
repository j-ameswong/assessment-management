package uk.ac.sheffield.team_project_team_24.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import uk.ac.sheffield.team_project_team_24.dto.TokenDTO;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public TokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
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

    public Authentication parseToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            String username = jwt.getSubject();
            String scope = jwt.getClaimAsString("scope");
            Collection<GrantedAuthority> authorities = Arrays.stream(scope.split(" "))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        } catch (JwtException e) {
            return null; // invalid token
        }
    }
}
