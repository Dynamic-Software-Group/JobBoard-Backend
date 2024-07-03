package dev.dynamic.jobboard.endpoints.requests.users;

import lombok.Data;

@Data
public class LoginRequest {
    public String email;
    public String password;
}
