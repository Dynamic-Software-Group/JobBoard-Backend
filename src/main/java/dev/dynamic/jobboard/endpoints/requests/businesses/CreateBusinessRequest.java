package dev.dynamic.jobboard.endpoints.requests.businesses;

import lombok.Data;

@Data
public class CreateBusinessRequest {
    private String name;
    private String description;
    private String businessEmail;
    private String website;
    private String avatar;
}
