package dev.dynamic.jobboard.endpoints.requests.businesses;

import dev.dynamic.jobboard.model.enums.Socials;
import lombok.Data;

import java.util.Map;

@Data
public class UpdateBusinessRequest {
    private String avatar;
    private String businessEmail;
    private String description;
    private String name;
    private Map<Socials, String> socials;
    private String website;
}
