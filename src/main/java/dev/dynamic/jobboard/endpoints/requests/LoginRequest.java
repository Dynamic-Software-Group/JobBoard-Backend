package dev.dynamic.jobboard.endpoints.requests;

import lombok.Data;

@Data
public class LoginRequest {
    public String email;
    public String password;
}
