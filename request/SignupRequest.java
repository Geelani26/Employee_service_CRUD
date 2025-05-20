package com.example.HRMS.config.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignupRequest {

  @NotBlank(message = "Username should not be blank")
  @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
  private String username;

  @NotBlank(message = "Email should not be blank")
  @Size(max = 50, message = "Email should not exceed 50 characters")
  @Email(message = "Invalid email format")
  private String email;

  private Set<Long> roles;

  @NotBlank(message = "Password should not be blank")
  @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
  private String password;
}
