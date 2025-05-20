package com.example.HRMS.controller;


import com.example.HRMS.request.EmployeeFilterRequest;
import com.example.HRMS.request.EmployeeRequest;
import com.example.HRMS.response.ApiResponse;
import com.example.HRMS.response.ConstantResponse;
import com.example.HRMS.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @PatchMapping("/add")
    public ResponseEntity<ApiResponse<Object>> addDetails(@RequestBody @Valid EmployeeRequest employeeRequest){
        return employeeService.addDetails(employeeRequest);
    }
    @PostMapping("/getAll")
    public ResponseEntity<ApiResponse<Object>> getAllDetails(@RequestBody EmployeeFilterRequest request){
        return employeeService.getAllDetails(request);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getDetailsById(@PathVariable Long id){
        return employeeService.getDetailsById(id);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteDetailsById(@PathVariable Long id){
        return employeeService.deleteDetailsById(id);
    }
}
