package uk.ac.sheffield.team_project_team_24.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
// Table that represents staff roles
// composite key uses userid, their role, module code
public class Staff {

    // Composite primary key created in StaffID class
    @EmbeddedId
    private StaffID staffID;

}