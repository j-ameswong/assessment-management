package uk.ac.sheffield.team_project_team_24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

@ConfigurationPropertiesScan
@SpringBootApplication
public class TeamProjectTeam24Application {

  public static void main(String[] args) {
    SpringApplication.run(TeamProjectTeam24Application.class, args);
  }

  // @Bean
  // public CommandLineRunner commandLineRunner(UserService userService) {
  // return args -> {
  // Faker faker = new Faker();
  //
  // };
  // }

}
