package com.example.HRMS.service;


import com.example.HRMS.model.*;
import com.example.HRMS.model.Enum;
import com.example.HRMS.repository.*;
import com.example.HRMS.request.EmployeeFilterRequest;
import com.example.HRMS.request.EmployeeRequest;
import com.example.HRMS.request.OffBoardingRequest;
import com.example.HRMS.request.OnboardingRequest;
import com.example.HRMS.response.ApiResponse;
import com.example.HRMS.response.ConstantResponse;
import com.example.HRMS.response.EmployeeGetAllDto;
import com.example.HRMS.utils.ConstantMessage;
import com.example.HRMS.utils.PageingUtil;
import com.example.HRMS.utils.StatusConstants;
import com.example.HRMS.utils.StatusUtils;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EnumRepository enumRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private PageingUtil pagingUtil;

    private static final String EMPLOYEE = "Employee ";
    @Autowired
    private PayrollRepository payrollRepository;
    @Autowired
    private OnBoardingRepository onBoardingRepository;
    @Autowired
    private FileUploadRepository fileUploadRepository;
    @Autowired
    private OffBoardingRepository offBoardingRepository;

    public ResponseEntity<ApiResponse<Object>> addDetails(EmployeeRequest employeeRequest) {
        Employee employee;
        String message;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        if (employeeRequest.getId() == null) {
            employee = new Employee();
            message = EMPLOYEE + ConstantMessage.ADD;
            employee.setEmployeeId(setEmployeeId());
        } else {
            employee = employeeRepository.findById(employeeRequest.getId()).orElse(null);
            if (employee == null) {
                throw new RuntimeException("Invalid Id");
            }
            message = EMPLOYEE + ConstantMessage.UPDATED;

            Optional<Employee> duplicateEmployeeId = employeeRepository.findByEmployeeIdAndIdNot(employeeRequest.getEmployeeId(),employeeRequest.getId());
            if(duplicateEmployeeId.isPresent()) {
                throw new RuntimeException("Employee Id Already Exists!");
            }

            if(employeeRequest.getEmployeeId() != null && !Objects.equals(employee.getEmployeeId(), employeeRequest.getEmployeeId()) && employeeRequest.getEmployeeId().startsWith("STS") && employeeRequest.getEmployeeId().length() == 6){
                employee.setEmployeeId(employeeRequest.getEmployeeId());
            }

            if(employeeRequest.getPayrollId() != null){
                Payroll payroll = payrollRepository.findById(employeeRequest.getPayrollId()).orElseThrow(() -> new RuntimeException("Invalid Payroll Id."));
                employee.setPayroll(payroll);
            }

            employee.setBankName(employeeRequest.getBankName());
            employee.setBankAccountNo(employeeRequest.getBankAccountNo());
            employee.setBankAddress(employeeRequest.getBankAddress());
            employee.setIfscCode(employeeRequest.getIfscCode());
            employee.setCtc(employeeRequest.getCtc());

            if(employeeRequest.getOnboarding() != null){
                OnBoarding onBoarding;
                OnboardingRequest onboardingReq = employeeRequest.getOnboarding();
                if(onboardingReq.getId() == null){
                    onBoarding = new OnBoarding();
                }else{
                    onBoarding = onBoardingRepository.findById(onboardingReq.getId()).orElseThrow(() -> new RuntimeException("Invalid OnBoarding Id to Update."));
                }
                if(Objects.equals(onboardingReq.getIsProbation(), true)){
                    onBoarding.setIsProbation(onboardingReq.getIsProbation());
                    if(onboardingReq.getProbationMonths() != null){
                        Enum probationMonths = enumRepository.findByIdAndEnumType(onboardingReq.getProbationMonths(),"probation_months").orElseThrow(() -> new RuntimeException("Invalid ProbationMonths Id."));
                        onBoarding.setProbationMonths(probationMonths);
                    }
                    LocalDate probationStartDate = LocalDate.parse(onboardingReq.getProbationStartDate(), formatter);
                    onBoarding.setProbationStartDate(probationStartDate);
                    LocalDate probationEndDate = LocalDate.parse(onboardingReq.getProbationEndDate(), formatter);
                    onBoarding.setProbationEndDate(probationEndDate);
                }else{
                    onBoarding.setIsProbation(onboardingReq.getIsProbation());
                }
                onBoarding.setIsShenllWorkspace(onboardingReq.getIsShenllWorkspace());
                onBoarding.setIsWorkspaceEmail(onboardingReq.getIsWorkspaceEmail());
                onBoarding.setIsGmail(onboardingReq.getIsGmail());

                if (onboardingReq.getEmployeeDocs() != null && !onboardingReq.getEmployeeDocs().isEmpty()) {
                    List<FileUpload> employeeDocsList = onboardingReq.getEmployeeDocs().stream()
                            .map(id -> fileUploadRepository.findById(id)
                                    .orElseThrow(() -> new RuntimeException("Invalid Onboarding EmployeeDocument Id")))
                            .collect(Collectors.toList());
                    onBoarding.setEmployeeDocs(employeeDocsList);
                }
                if (onboardingReq.getGovtIdentificationDocs() != null && !onboardingReq.getGovtIdentificationDocs().isEmpty()) {
                    List<FileUpload> govtIdentificationList = onboardingReq.getGovtIdentificationDocs().stream()
                            .map(id -> fileUploadRepository.findById(id)
                                    .orElseThrow(() -> new RuntimeException("Invalid Onboarding GovtIdentificationDocument Id")))
                            .collect(Collectors.toList());
                    onBoarding.setGovtIdentificationDocs(govtIdentificationList);
                }
                if (onboardingReq.getOrganizationDocs() != null && !onboardingReq.getOrganizationDocs().isEmpty()) {
                    List<FileUpload> organizationDocsList = onboardingReq.getOrganizationDocs().stream()
                            .map(id -> fileUploadRepository.findById(id)
                                    .orElseThrow(() -> new RuntimeException("Invalid Onboarding OrganizationDocument Id")))
                            .collect(Collectors.toList());
                    onBoarding.setOrganizationDocs(organizationDocsList);
                }
                if (onboardingReq.getLastOrganizationDocs() != null && !onboardingReq.getLastOrganizationDocs().isEmpty()) {
                    List<FileUpload> lastOrganizationDocsList = onboardingReq.getLastOrganizationDocs().stream()
                            .map(id -> fileUploadRepository.findById(id)
                                    .orElseThrow(() -> new RuntimeException("Invalid Onboarding LastOrganizationDocument Id")))
                            .collect(Collectors.toList());
                    onBoarding.setLastOrganizationDocs(lastOrganizationDocsList);
                }
                onBoardingRepository.save(onBoarding);
                employee.setOnBoarding(onBoarding);
            }

            if(employeeRequest.getOffBoarding() != null) {
                OffBoarding offBoarding;
                OffBoardingRequest offboardingReq = employeeRequest.getOffBoarding();
                if(offboardingReq.getId() == null){
                    offBoarding = new OffBoarding();
                }else{
                    offBoarding = offBoardingRepository.findById(offboardingReq.getId()).orElseThrow(() -> new RuntimeException("Invalid OffBoarding Id to Update."));
                }

                if(offboardingReq.getEmployeeStatus() != null){
                    Enum offBoardingStatus = enumRepository.findByIdAndEnumType(offboardingReq.getEmployeeStatus(), "offboarding_employee_status").orElseThrow(() -> new RuntimeException("Invalid OffBoarding Employee Id."));
                    offBoarding.setEmployeeStatus(offBoardingStatus);

                    if(offBoardingStatus.getName().equalsIgnoreCase("Non Working") || offBoardingStatus.getName().equalsIgnoreCase("On Notice Period")){

                        LocalDate noticePeriodStartDate = LocalDate.parse(offboardingReq.getNoticePeriodStartDate(), formatter);
                        offBoarding.setNoticePeriodStartDate(noticePeriodStartDate);
                        LocalDate noticePeriodEndDate = LocalDate.parse(offboardingReq.getNoticePeriodEndDate(), formatter);
                        offBoarding.setNoticePeriodEndDate(noticePeriodEndDate);
                        LocalDate relievedDate = LocalDate.parse(offboardingReq.getRelievedDate(), formatter);
                        offBoarding.setRelievedDate(relievedDate);

                        offBoarding.setIsShenllWorkspace(offboardingReq.getIsShenllWorkspace());
                        offBoarding.setIsWorkspaceEmail(offboardingReq.getIsWorkspaceEmail());
                        offBoarding.setIsGmail(offboardingReq.getIsGmail());

                        if (offboardingReq.getEmployeeDocs() != null && !offboardingReq.getEmployeeDocs().isEmpty()) {
                            List<FileUpload> employeeDocsList = offboardingReq.getEmployeeDocs().stream()
                                    .map(id -> fileUploadRepository.findById(id)
                                            .orElseThrow(() -> new RuntimeException("Invalid Offboarding EmployeeDocument Id")))
                                    .collect(Collectors.toList());
                            offBoarding.setEmployeeDocs(employeeDocsList);
                        }
                        if (offboardingReq.getGovtIdentificationDocs() != null && !offboardingReq.getGovtIdentificationDocs().isEmpty()) {
                            List<FileUpload> govtIdentificationList = offboardingReq.getGovtIdentificationDocs().stream()
                                    .map(id -> fileUploadRepository.findById(id)
                                            .orElseThrow(() -> new RuntimeException("Invalid Offboarding GovtIdentificationDocument Id")))
                                    .collect(Collectors.toList());
                            offBoarding.setGovtIdentificationDocs(govtIdentificationList);
                        }
                        if (offboardingReq.getOrganizationDocs() != null && !offboardingReq.getOrganizationDocs().isEmpty()) {
                            List<FileUpload> organizationDocsList = offboardingReq.getOrganizationDocs().stream()
                                    .map(id -> fileUploadRepository.findById(id)
                                            .orElseThrow(() -> new RuntimeException("Invalid Offboarding OrganizationDocument Id")))
                                    .collect(Collectors.toList());
                            offBoarding.setOrganizationDocs(organizationDocsList);
                        }
                        if (offboardingReq.getLastOrganizationDocs() != null && !offboardingReq.getLastOrganizationDocs().isEmpty()) {
                            List<FileUpload> lastOrganizationDocsList = offboardingReq.getLastOrganizationDocs().stream()
                                    .map(id -> fileUploadRepository.findById(id)
                                            .orElseThrow(() -> new RuntimeException("Invalid Offboarding LastOrganizationDocument Id")))
                                    .collect(Collectors.toList());
                            offBoarding.setLastOrganizationDocs(lastOrganizationDocsList);
                        }
                    }
                }
                offBoardingRepository.save(offBoarding);
                employee.setOffBoarding(offBoarding);
            }
        }

        //generalInformation
        employee.setEmployeeName(employeeRequest.getEmployeeName());
        Enum gender = enumRepository.findByIdAndEnumType(employeeRequest.getGender(),"gender").orElseThrow(() -> new RuntimeException("Invalid Gender Id"));
        employee.setGender(gender);

        if(employeeRequest.getDateOfBirth() != null && !employeeRequest.getDateOfBirth().isEmpty()) {
            LocalDate dateOfBirth = LocalDate.parse(employeeRequest.getDateOfBirth(), formatter);
            employee.setDateOfBirth(dateOfBirth);
        }
        employee.setMobileNumber(employeeRequest.getMobileNumber());
        employee.setEmail(employeeRequest.getEmailId());

        //employeeInformation
        Enum department =  enumRepository.findByIdAndEnumType(employeeRequest.getDepartment(),"department").orElseThrow(() -> new RuntimeException("Invalid Department Id"));
        employee.setDepartment(department);

        Enum designation = enumRepository.findByIdAndEnumType(employeeRequest.getDesignation(),"designation").orElseThrow(() -> new RuntimeException("Invalid Designation Id"));
        employee.setDesignation(designation);

        employee.setReportTo(employeeRequest.getReportTo());
        if(employeeRequest.getDateOfJoining() != null && !employeeRequest.getDateOfJoining().isEmpty()) {
            LocalDate dateOfJoining = LocalDate.parse(employeeRequest.getDateOfJoining(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            employee.setDateOfJoining(dateOfJoining);
        }
        Enum branch = enumRepository.findByIdAndEnumType(employeeRequest.getBranch(),"branch").orElseThrow(() -> new RuntimeException("Invalid Branch Id"));
        employee.setBranch(branch);

        Enum employeeType = enumRepository.findByIdAndEnumType(employeeRequest.getEmployeeType(),"employee_type").orElseThrow(() -> new RuntimeException("Invalid EmployeeType Id"));
        employee.setEmployeeType(employeeType);

        employee.setYearOfExperience(employeeRequest.getYearOfExperience());
        if(!employeeRequest.getCoreSkills().isEmpty()){
            Set<Enum> coreSkills = new HashSet<>();
            for(Long id : employeeRequest.getCoreSkills()){
                Enum skill = enumRepository.findByIdAndEnumType(id, "core_skills").orElseThrow(() -> new RuntimeException("Invalid Language Id"));
                coreSkills.add(skill);
            }
            employee.setCoreSkills(coreSkills);
        }
        employee.setAdditionalSkill(employeeRequest.getAdditionalSkill());

        //contactInformation
        employee.setAlternativeNumber(employeeRequest.getAlternativeNumber());
        employee.setCurrentAddress(employeeRequest.getCurrentAddress());
        employee.setPermanentAddress(employeeRequest.getPermanentAddress());
        employee.setPersonalEmailId(employeeRequest.getPersonalEmailId());

        //accessInfo
        Set<Role> roleSet = new HashSet<>();
        Role role = roleRepository.findById(employeeRequest.getRoleId()).orElseThrow(() -> new RuntimeException("Invalid Role Id"));
        roleSet.add(role);
        employee.setRoles(roleSet);
        employee.setOfficialEmail(employeeRequest.getOfficialEmail());
        employee.setPassword(encoder.encode(employeeRequest.getPassword()));

        //personalInfo
        Enum maritalStatus = enumRepository.findByIdAndEnumType(employeeRequest.getMartialStatus(),"marital_status").orElse(null);
        if (maritalStatus == null){
            throw new RuntimeException("Invalid MaritalStatus Id");
        }else {
            employee.setMaritalStatus(maritalStatus);
        }
        employee.setBloodGroup(employeeRequest.getBloodGroup());
        if(employeeRequest.getLanguages() != null && !employeeRequest.getLanguages().isEmpty()) {
            Set<Enum> languageList = new HashSet<>();
            for(Long id : employeeRequest.getLanguages()){
                Enum language = enumRepository.findByIdAndEnumType(id,"languages").orElseThrow(() -> new RuntimeException("Invalid Language Id"));
                languageList.add(language);
            }
            employee.setLanguages(languageList);
        }

        //educationalInfo
        employee.setUg(employeeRequest.getUg());
        employee.setPg(employeeRequest.getPg());

        //emergencyContact
        employee.setEmergencyContactName(employeeRequest.getEmergencyContactName());
        employee.setRelationship(employeeRequest.getRelationship());
        employee.setEmergencyPhoneNumber(employeeRequest.getEmergencyPhoneNumber());
        employee.setIsActive(employeeRequest.getIsActive());

        employeeRepository.save(employee);
        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), message));
    }

    private String setEmployeeId() {
        Optional<Employee> latestEmployee = employeeRepository.findTopByOrderByEmployeeIdDesc();
        int runningNumber = 1;
        if (latestEmployee.isPresent()) {
            String latestEmployeeId = latestEmployee.get().getEmployeeId();
            if (latestEmployeeId.startsWith("STS")) {
                String numberPart = latestEmployeeId.substring(3);
                runningNumber = Integer.parseInt(numberPart) + 1;
            }
        }
        String formattedRunningNumber = String.format("%03d", runningNumber);
        return "STS" + formattedRunningNumber;
    }


    public ResponseEntity<ApiResponse<Object>> getAllDetails(EmployeeFilterRequest request) {
        Pageable pageable = pagingUtil.getPageable(request.getPageNo(), request.getPageSize(), request.getSortAs(), request.getSortOn());
        Specification<Employee>employeeSpecification  = (root, query, criteriaBuilder) -> {
            Predicate predicate =  criteriaBuilder.conjunction();

            if (request.getEmployeeId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("employeeId"), "%" + request.getEmployeeId() + "%"));
            }
            if (request.getEmployeeName() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("employeeName"), "%" + request.getEmployeeName() + "%"));
            }
            if (request.getDepartment() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("department").get("id"), request.getDepartment()));
            }
            if (request.getBranch() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("branch").get("id"), request.getBranch()));
            }
            if (request.getEmployeeType() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("employeeType").get("id"), request.getEmployeeType()));
            }
            if (request.getIsActive() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isActive"), request.getIsActive()));
            }else {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isActive"), StatusUtils.ACTIVE));
            }

            return predicate;
        };
        Page<Employee> employees = employeeRepository.findAll(employeeSpecification,pageable);

        if(employees.isEmpty()){
            return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), new ArrayList<>()));
        }

        Integer activeCount = 0;
        Integer inActiveCount = 0;
        Integer contractCount = 0;
        Integer totalEmployee = employeeRepository.findAll().size();

        List<EmployeeGetAllDto> dtoResponseList = new ArrayList<>();
        for (Employee emp : employees) {
            Integer isActive = emp.getIsActive();
            if (Objects.equals(isActive,1)) activeCount++;
            else if (Objects.equals(isActive,0)) inActiveCount++;

            if ("Contract".equals(emp.getEmployeeType() != null && emp.getEmployeeType().getName() != null ? emp.getEmployeeType().getName() : null)) {
                contractCount++;
            }

            EmployeeGetAllDto dto = new EmployeeGetAllDto(emp.getId(), emp.getEmployeeId(), emp.getEmployeeName(), emp.getEmail(), emp.getMobileNumber(), emp.getDepartment() != null && emp.getDepartment().getName() != null ? emp.getDepartment().getName() : null, emp.getIsActive() != null ? emp.getIsActive() : null);
            dtoResponseList.add(dto);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("content", dtoResponseList);
        response.put("totalEmployee", totalEmployee);
        response.put("activeCount", activeCount);
        response.put("inActiveCount", inActiveCount);
        response.put("contractCount", contractCount);
        response.put("pageable", employees.getPageable());
        response.put("last", employees.isLast());
        response.put("total_pages", employees.getTotalPages());
        response.put("total_elements", employees.getTotalElements());
        response.put("size", employees.getSize());
        response.put("number", employees.getNumber());
        response.put("sort", employees.getSort());
        response.put("number_of_elements", employees.getNumberOfElements());
        response.put("first", employees.isFirst());
        response.put("empty", employees.isEmpty());

        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), response));
    }

    public ResponseEntity<ApiResponse<Object>> getDetailsById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Invalid Employee Id"));

        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), employee));
    }

    public ResponseEntity<ApiResponse<Object>> deleteDetailsById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Invalid Employee Id"));
        employee.setIsActive(StatusUtils.INACTIVE);
        employeeRepository.save(employee);
        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), EMPLOYEE + ConstantMessage.DELETED));
    }
}
