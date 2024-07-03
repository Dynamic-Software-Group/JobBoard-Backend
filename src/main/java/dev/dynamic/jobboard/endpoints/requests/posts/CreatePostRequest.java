package dev.dynamic.jobboard.endpoints.requests.posts;

import dev.dynamic.jobboard.model.enums.EmploymentType;
import dev.dynamic.jobboard.model.enums.LocationType;
import dev.dynamic.jobboard.model.enums.Tags;
import lombok.Data;

import java.util.List;

@Data
public class CreatePostRequest {
    private String title;
    private String description;
    private EmploymentType employmentType;
    private LocationType locationType;
    private String location;
    private int maxSalary;
    private int minSalary;
    private String currency;
    private List<Tags> tags;
}
