package com.example.HRMS.config.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private String userId;
  private String username;
  private String email;
  private List<String> roles;

  public JwtResponse(String accessToken, String userId, String username, String email, List<String> roles) {
    this.token = accessToken;
    this.userId = userId;
    this.username = username;
    this.email = email;
    this.roles = roles;
  }
}
