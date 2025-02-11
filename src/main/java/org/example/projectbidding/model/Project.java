package org.example.projectbidding.model;

import org.example.projectbidding.exception.DateOutOfRangeException;
import org.example.projectbidding.exception.EmptyFieldException;

import java.time.LocalDate;

/**
 * Represents a project in the project bidding system.
 * <p>
 * A project has a name, description, start date, and end date.
 * This class extends {@link Entity} and implements {@link Validatable} to ensure
 * proper validation before a project is used.
 */
public final class Project extends Entity implements Validatable {
    private String projectName;
    private String projectDescription;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;

    private Project(ProjectBuilder builder) {
        super(builder.id);
        this.projectName = builder.projectName;
        this.projectDescription = builder.projectDescription;
        this.projectStartDate = builder.projectStartDate;
        this.projectEndDate = builder.projectEndDate;
    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public LocalDate getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(LocalDate projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public LocalDate getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(LocalDate projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    public static class ProjectBuilder {
        private Long id;
        private String projectName;
        private String projectDescription;
        private LocalDate projectStartDate;
        private LocalDate projectEndDate;


        public ProjectBuilder setId(Long id) {
            this.id = id;
            return this;
        }


        public ProjectBuilder setProjectName(String projectName) {
            this.projectName = projectName;
            return this;
        }


        public ProjectBuilder setProjectDescription(String projectDescription) {
            this.projectDescription = projectDescription;
            return this;
        }


        public ProjectBuilder setProjectStartDate(LocalDate projectStartDate) {
            this.projectStartDate = projectStartDate;
            return this;
        }


        public ProjectBuilder setProjectEndDate(LocalDate projectEndDate) {
            this.projectEndDate = projectEndDate;
            return this;
        }


        public Project build() {
            return new Project(this);
        }
    }

    /**
     * Validates the project details to ensure all required fields are properly set.
     * <p>
     * - Ensures that the project name, description, start date, and end date are not null. <br>
     * - Checks that the end date is not before or equal to the start date. <br>
     * - Ensures that the start date is not in the past. <br>
     *
     * @throws EmptyFieldException If any required field is empty.
     * @throws DateOutOfRangeException If the start date is in the past or the end date is before the start date.
     */
    @Override
    public void validate() throws EmptyFieldException {
        if(projectName == null || projectDescription == null || projectStartDate == null || projectEndDate == null) {
            throw new EmptyFieldException("One of the fields is empty!");
        }
        if (projectEndDate.isBefore(projectStartDate) || projectEndDate.isEqual(projectStartDate) || projectStartDate.isBefore(LocalDate.now())) {
            throw new DateOutOfRangeException("End date cannot be before start date or in the past!");
        }
    }


}
