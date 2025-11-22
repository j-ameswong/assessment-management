package uk.ac.sheffield.team_project_team_24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@ConfigurationPropertiesScan
@SpringBootApplication
public class TeamProjectTeam24Application {

  public static void main(String[] args) {
    SpringApplication.run(TeamProjectTeam24Application.class, args);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/h2-console/**").permitAll()
            .anyRequest().authenticated())
        .csrf(csrf -> csrf.disable())
        .headers(headers -> headers.frameOptions().disable());

    return http.build();
  }
  // @Bean
  // public CommandLineRunner commandLineRunner(UserService userService) {
  // return args -> {
  // Faker faker = new Faker();
  //
  // };
  // }
}
