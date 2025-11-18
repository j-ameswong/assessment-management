package uk.ac.sheffield.team_project_team_24.domain;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class StaffID implements Serializable {
    private long userID;
    private String moduleCode;
    private String roleName;

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        StaffID staffID = (StaffID) object;
        return getUserID() == staffID.getUserID() && java.util.Objects.equals(getModuleCode(), staffID.getModuleCode()) && java.util.Objects.equals(getRoleName(), staffID.getRoleName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserID(), getModuleCode(), getRoleName());
    }
}