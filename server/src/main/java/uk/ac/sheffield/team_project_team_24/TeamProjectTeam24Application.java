package uk.ac.sheffield.team_project_team_24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class TeamProjectTeam24Application {

    public static void main(String[] args) {
        SpringApplication.run(TeamProjectTeam24Application.class, args);
    }

}