package dev.dynamic.jobboard.endpoints.requests.users;

import dev.dynamic.jobboard.model.enums.EmploymentType;
import lombok.Data;

@Data
public class AddExperienceRequest {
    private String jobTitle;
    private EmploymentType employmentType;
    private Long startDate;
    private Long endDate;
    private String description;
    private Long businessId;
}
