package dev.dynamic.jobboard.endpoints.requests;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String username;
    private String avatar;
    private String bio;
}
