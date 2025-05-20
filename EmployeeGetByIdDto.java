package com.example.HRMS.response;

import com.example.HRMS.model.*;
import com.example.HRMS.model.Enum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeGetByIdDto {

    private Long id;

    private String employeeName;

    private String employeeId;

    private String gender;

    private String dateOfBirth;

    private String mobileNumber;

    private String email;

    private String department;

    private String designation;

    private Long reportTo;

    private String dateOfJoining;

    private String branch;

    private String employeeType;

    private String yearOfExperience;

    private Set<String> coreSkills;

    private String additionalSkill;

    private String alternativeNumber;

    private String currentAddress;

    private String permanentAddress;

    private String personalEmailId;

    private Set<String> roles;

    private String officialEmail;

    private String password;

    private String maritalStatus;

    private String bloodGroup;

    private Set<String> languages;

    private String ug;

    private String pg;

    private  String emergencyContactName;

    private String relationship;

    private String emergencyPhoneNumber;

    private Integer isActive;

    @ManyToOne
    @JoinColumn(name = "on_boarding_id")
    private OnBoarding onBoarding;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account_no")
    private String bankAccountNo;

    @Column(name = "bank_address")
    private String bankAddress;

    @Column(name = "ifsc_code")
    private String ifscCode;

    @Column(name = "ctc")
    private String ctc;

    @ManyToOne
    @JoinColumn(name = "payroll_id")
    private Payroll payroll;

    @ManyToOne
    @JoinColumn(name = "off_boarding_id")
    private OffBoarding offBoarding;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Assets> assets;
}
