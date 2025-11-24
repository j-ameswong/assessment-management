package uk.ac.sheffield.team_project_team_24.domain.assessment;

import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Assessment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assessment {

    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long assessmentId;

    @Column(unique = true, nullable = false)
    private String assessmentName;

    @Enumerated(EnumType.STRING)
    private AssessmentType assessmentType;

    @Enumerated(EnumType.STRING)
    private AssessmentStatus status;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    @ManyToOne
    @JoinColumn(name = "setter_id")
    private User setter;

    @ManyToOne
    @JoinColumn(name = "checker_id")
    private User checker;

    @ManyToOne
    @JoinColumn(name = "external_examiner_id")
    private User externalExaminer;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssessmentStageLog> log;

    private LocalDateTime releaseDate;

    private LocalDateTime deadline;

    private LocalDateTime examDate;

    @Column(length = 2000)
    private String checkerFeedback; // Do not save the content

    @Column(length = 2000)
    private String setterResponse; // setter responses to external examiner
}
