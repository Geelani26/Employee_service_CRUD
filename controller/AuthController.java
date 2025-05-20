package com.example.HRMS.config.controller;

import com.example.HRMS.config.UserDetailsImpl;
import com.example.HRMS.config.jwt.JwtService;
import com.example.HRMS.config.request.SignInRequest;
import com.example.HRMS.config.request.SignupRequest;
import com.example.HRMS.config.response.JwtResponse;
import com.example.HRMS.config.response.MessageResponse;
import com.example.HRMS.model.Employee;
import com.example.HRMS.repository.EmployeeRepository;
import com.example.HRMS.repository.EnumRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EnumRepository enumRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtService jwtService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest request) {

        Employee employee = employeeRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Invalid EmailId"));

        Authentication authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(employee.getEmployeeId(),request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken = jwtService.generateToken(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity.ok(new JwtResponse(jwtToken,
                userDetails.getUserId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }
}
