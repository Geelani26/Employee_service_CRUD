package com.example.HRMS.config.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignInRequest {

    @NotNull(message = "Email Should not be null")
    @NotBlank(message = "Email Should not be blank")
    @NotEmpty(message = "Email Should not be empty")
    private String email;

    @NotNull(message = "Password Should not be null")
    @NotBlank(message = "Password Should not be blank")
    @NotEmpty(message = "Password Should not be empty")
    private String password;
}
