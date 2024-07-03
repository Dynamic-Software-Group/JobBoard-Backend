package dev.dynamic.jobboard.endpoints.requests.users;

import lombok.Data;

@Data
public class RegisterRequest {
    private final String email;
    private final String password;
    private final String username;
}
