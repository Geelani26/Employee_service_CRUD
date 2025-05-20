package com.example.HRMS.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employee")
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Employee extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "employee_id", unique = true, nullable = false)
    private String employeeId;

    @ManyToOne
    @JoinColumn(name = "gender")
    private Enum gender;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "UTC")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "department")
    private Enum department;

    @ManyToOne
    @JoinColumn(name = "designation")
    private Enum designation;

    @Column(name = "report_to")
    private Long reportTo;              // need to Map

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "UTC")
    @Column(name = "date_of_joining")
    private LocalDate dateOfJoining;

    @ManyToOne
    @JoinColumn(name = "branch")
    private Enum branch;

    @ManyToOne
    @JoinColumn(name = "employee_type")
    private Enum employeeType;

    @Column(name = "year_of_experience")
    private String yearOfExperience;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(  name = "employee_core_skills",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "core_skills_id"))
    private Set<Enum> coreSkills = new HashSet<>();

    @Column(name = "additional_skill")
    private String additionalSkill;

    @Column(name = "alternative_number")
    private String alternativeNumber;

    @Column(name = "current_address")
    private String currentAddress;

    @Column(name = "permanent_address")
    private String permanentAddress;

    @Column(name = "personal_email_id")
    private String personalEmailId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(  name = "employee_roles",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(name = "official_email")
    private String officialEmail;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "marital_status")
    private Enum maritalStatus;

    @Column(name = "blood_group")
    private String bloodGroup;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(  name = "employee_languages",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id"))
    private Set<Enum> languages = new HashSet<>();

    @Column(name = "ug")
    private String ug;

    @Column(name = "pg")
    private String pg;

    @Column(name = "emergency_contact_name")
    private  String emergencyContactName;

    @Column(name = "relationship")
    private String relationship;

    @Column(name = "emergency_phone_number")
    private String emergencyPhoneNumber;

    @Column(name = "is_active")
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
