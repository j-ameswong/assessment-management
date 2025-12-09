package uk.ac.sheffield.team_project_team_24.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import uk.ac.sheffield.team_project_team_24.security.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // CORS + CSRF
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)

                // Authorisation rules
                .authorizeHttpRequests(auth -> auth
                        // login public
                        .requestMatchers("/api/auth/**").permitAll()
                        // H2 console public
                        .requestMatchers("/h2-console/**").permitAll()

                        // modules — Admin + Exams Officer only
                        .requestMatchers(HttpMethod.GET, "/api/modules").hasAnyRole("ADMIN", "EXAMS_OFFICER")
                        .requestMatchers(HttpMethod.POST, "/api/modules").hasAnyRole("ADMIN", "EXAMS_OFFICER")
                        .requestMatchers(HttpMethod.PUT, "/api/modules/**").hasAnyRole("ADMIN", "EXAMS_OFFICER")
                        .requestMatchers(HttpMethod.DELETE, "/api/modules/**").hasAnyRole("ADMIN", "EXAMS_OFFICER")

                        // module staff list
                        .requestMatchers(HttpMethod.GET, "/api/modules/*/staff")
                        .hasAnyRole("ADMIN", "EXAMS_OFFICER", "ACADEMIC_STAFF")

                        // module assessments list
                        .requestMatchers(HttpMethod.GET, "/api/modules/*/assessments")
                        .hasAnyRole("ADMIN", "EXAMS_OFFICER", "ACADEMIC_STAFF", "EXTERNAL_EXAMINER")

                        // assessments routes
                        .requestMatchers("/api/assessments/**")
                        .hasAnyRole("ADMIN",
                                "EXAMS_OFFICER",
                                "ACADEMIC_STAFF",
                                "EXTERNAL_EXAMINER")

                        // all other API endpoints need authentication
                        .requestMatchers("/api/**").authenticated()

                        // anything else allow
                        .anyRequest().permitAll()
                )

                // Stateless sessions for JWT
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // JWT filter
                .addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // allow H2 console in iframe
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:5173"));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
