package com.example.HRMS.request;

import com.example.HRMS.model.OffBoarding;
import com.example.HRMS.utils.StatusUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EmployeeRequest {

    //general information
    private Long id;

    @NotNull(message = "Employee Name must not be null")
    @Size(min = 1,max = 15,message = "Employee Name is mandatory. Please fill it in")
    private String employeeName;

    private String employeeId;

    @NotNull(message = "Gender must not be null")
    private Long gender;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "UTC")
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$", message = "DateOfBirth should be in the format of dd-MM-yyyy")
    private String dateOfBirth;

    @NotNull(message = "Mobile Number must not be null")
    @Pattern(regexp = "^[6-9]\\d{9}$",message = "Provide a valid mobile number")
    private String mobileNumber;

    @NotNull(message = "EmailId must not be null")
    @Email(message = "Provide a valid Email")
    private String emailId;

    //employeeInformation
    @NotNull(message = "Department must not be null")
    private Long department;

    @NotNull(message = "Designation must not be null")
    private Long designation;

    @NotNull(message = "ReportTo must not be null")
    private Long reportTo;

    @NotNull(message = "Provide a date you Joined")
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$", message = "DateOfJoining should be in the format dd-MM-yyyy")
    private String dateOfJoining;

    @NotNull(message = "Branch must not be null")
    private Long branch;

    @NotNull(message = "EmployeeType must not be null")
    private Long employeeType;

    @NotNull(message = "YearOfExperience should not be null")
    @Size(min = 1,max = 15,message = "YearOfExperience is mandatory. Please fill it in")
    private String yearOfExperience;

    @NotNull(message = "CoreSkills must not be null")
    private List<Long> coreSkills;

    private String additionalSkill;

    //contact information
    @Pattern(regexp = "^[6-9]\\d{9}$",message = "Provide a valid Alternative Mobile number")
    private String alternativeNumber;

    @NotNull(message = "Current Address must not be null")
    @NotEmpty(message = "Current Address must not be empty")
    @NotBlank(message = "Current Address must not be blank")
    private String currentAddress;

    @NotNull(message = "Permanent Address must not be null")
    @NotEmpty(message = "Permanent Address must not be empty")
    @NotBlank(message = "Permanent Address must not be blank")
    private String permanentAddress;

    @NotNull(message = "Provide a personalEmailId ")
    @Email(message = "Provide a valid Personal Email")
    private String personalEmailId;

    //AccessInfo
    @NotNull(message = "Role Id Should Not be null")
    private Long roleId;

    @Email(message = "Provide a valid Official Email-2")
    private  String officialEmail;

    @NotNull(message = "Password must not be null")
    @NotEmpty(message = "Password must not be Empty")
    @NotBlank(message = "Password must not be Blank")
    private String password;

    //personal info
    @NotNull(message = "Martial Status must not be null")
    private Long martialStatus;

    @NotNull(message = "Blood group must be null")
    @NotEmpty(message = "Blood group must not be Empty")
    @NotBlank(message = "Blood group must not be Blank")
    private String bloodGroup;

    private List<Long> languages;

    //educational info
    @NotNull(message = "UG field must not be null")
    @NotEmpty(message = "UG field must not be empty")
    @NotBlank(message = "UG field must not be blank")
    private String ug;

    private String pg;

    //emergency contact
    @NotNull(message = "Emergency Contact Name must not be null")
    @Size(min = 1,max = 50,message = "Emergency Contact Name is mandatory. Please fill it in")
    private  String emergencyContactName;

    @NotNull(message = "Relationship must not be null")
    @Size(min = 1,max = 50,message = "Relationship is mandatory. Please fill it in")
    private String relationship;

    @NotNull(message = "Phone number not be null")
    @Pattern(regexp = "^[6-9]\\d{9}$",message = "Provide a valid phone number")
    private String emergencyPhoneNumber;

    private Integer isActive = StatusUtils.ACTIVE;

    private OnboardingRequest onboarding;

    private String bankName;

    private String bankAccountNo;

    private String bankAddress;

    private String ifscCode;

    private String ctc;

    private Long payrollId;

    private OffBoardingRequest offBoarding;
}
