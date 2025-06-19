package com.oop.motorph.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.dto.EmployeeRequestDTO;
import com.oop.motorph.dto.UserDTO;
import com.oop.motorph.dto.mapper.EmployeeDTOMapper;
import com.oop.motorph.dto.mapper.EmployeeRequestDTOMapper;
import com.oop.motorph.entity.*;
import com.oop.motorph.repository.*;

@ExtendWith(MockitoExtension.class)
public class HRManagerServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CompensationRepository compensationRepository;
    @Mock
    private GovernmentIdRepository governmentIdRepository;
    @Mock
    private EmployeeDTOMapper employeeDTOMapper;
    @Mock
    private EmployeeRequestDTOMapper employeeRequestDTOMapper;

    @InjectMocks
    private HRManagerService hrManagerService;

    // Test constants
    private static final Long EMPLOYEE_NUMBER = 10001L;
    private static final String USERNAME = "johndoe";
    private static final String LAST_NAME = "Doe";
    private static final String FIRST_NAME = "John";
    private static final String BIRTHDATE = "1990-01-01";
    private static final String ADDRESS = "123 Street";
    private static final String PHONE_NUMBER = "1234567890";
    private static final String EMPLOYMENT_STATUS = "Regular";
    private static final String POSITION = "Developer";
    private static final String DEPARTMENT = "Manager";
    private static final String SSS_ID = "SSS123";
    private static final String TIN_ID = "TIN123";
    private static final String PAGIBIG_ID = "PAGIBIG123";
    private static final String PHILHEALTH_ID = "PHILHEALTH123";
    private static final Double BASIC_SALARY = 50000.0;
    private static final Double RICE_SUBSIDY = 1000.0;
    private static final Double PHONE_ALLOWANCE = 1000.0;
    private static final Double CLOTHING_ALLOWANCE = 1000.0;
    private static final Double GROSS_SEMI_MONTHLY_RATE = 1000.0;
    private static final Double HOURLY_RATE = 1000.0;

    private Employee employee;
    private EmployeeDTO employeeDTO;
    private EmployeeRequestDTO employeeRequest;
    private UserDTO userDTO;

    /**
     * Initializes common test data.
     */
    @BeforeEach
    void setUp() {
        PersonalInfo personalInfo = new PersonalInfo(LAST_NAME, FIRST_NAME, BIRTHDATE, ADDRESS, PHONE_NUMBER);
        EmploymentInfo employmentInfo = new EmploymentInfo(EMPLOYMENT_STATUS, POSITION, DEPARTMENT);
        GovernmentIds governmentIds = new GovernmentIds(SSS_ID, TIN_ID, PAGIBIG_ID, PHILHEALTH_ID);
        Compensation compensation = new Compensation(null, BASIC_SALARY, RICE_SUBSIDY, PHONE_ALLOWANCE,
                CLOTHING_ALLOWANCE, GROSS_SEMI_MONTHLY_RATE, HOURLY_RATE);

        employee = new Employee();
        employee.setEmployeeNumber(EMPLOYEE_NUMBER);
        employee.setUsername(USERNAME);
        employee.setPersonalInfo(personalInfo);
        employee.setEmploymentInfo(employmentInfo);
        employee.setGovernmentIds(governmentIds);
        employee.setCompensation(compensation);

        employeeDTO = new EmployeeDTO(employee);
        userDTO = new UserDTO(USERNAME);
        employeeRequest = new EmployeeRequestDTO(EMPLOYEE_NUMBER, userDTO, personalInfo, employmentInfo, governmentIds,
                compensation);
    }

    /**
     * Mocks save-related dependencies used in create and update methods.
     */
    private void mockSaveDependencies() {
        when(governmentIdRepository.save(employee.getGovernmentIds())).thenReturn(employee.getGovernmentIds());
        when(compensationRepository.save(employee.getCompensation())).thenReturn(employee.getCompensation());
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeDTOMapper.apply(employee)).thenReturn(employeeDTO);
    }

    /**
     * Tests retrieval of all employees.
     */
    @Test
    void testGetAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee));
        when(employeeDTOMapper.apply(employee)).thenReturn(employeeDTO);

        List<EmployeeDTO> result = hrManagerService.getAllEmployees();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(employeeDTO, result.get(0));
    }

    /**
     * Tests successful creation of an employee.
     */
    @Test
    void testCreateEmployee() {
        when(employeeRequestDTOMapper.toEmployee(employeeRequest)).thenReturn(employee);
        when(userRepository.existsByUsername(employee.getUsername())).thenReturn(false);
        mockSaveDependencies();

        EmployeeDTO result = hrManagerService.createEmployee(employeeRequest);

        assertNotNull(result);
        assertEquals(employeeDTO, result);
    }

    /**
     * Tests creation of an employee with an existing username.
     */
    @Test
    void testCreateEmployee_UsernameExists() {
        when(employeeRequestDTOMapper.toEmployee(employeeRequest)).thenReturn(employee);
        when(userRepository.existsByUsername(employee.getUsername())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> hrManagerService.createEmployee(employeeRequest));
    }

    /**
     * Tests successful update of an existing employee.
     */
    @Test
    void testUpdateEmployee() {
        when(userRepository.existsByUsername(employeeRequest.user().username())).thenReturn(false);
        when(employeeRepository.findByEmployeeNumber(EMPLOYEE_NUMBER)).thenReturn(Optional.of(employee));
        mockSaveDependencies();

        EmployeeDTO result = hrManagerService.updateEmployee(EMPLOYEE_NUMBER, employeeRequest);

        assertNotNull(result);
        assertEquals(employeeDTO, result);
    }

    /**
     * Tests update of a non-existent employee.
     */
    @Test
    void testUpdateEmployee_NotFound() {
        when(userRepository.existsByUsername(employeeRequest.user().username())).thenReturn(false);
        when(employeeRepository.findByEmployeeNumber(EMPLOYEE_NUMBER)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> hrManagerService.updateEmployee(EMPLOYEE_NUMBER, employeeRequest));
    }

    /**
     * Tests successful deletion of an employee.
     */
    @Test
    void testDeleteEmployee() {
        when(employeeRepository.findByEmployeeNumber(EMPLOYEE_NUMBER)).thenReturn(Optional.of(employee));
        when(employeeDTOMapper.apply(employee)).thenReturn(employeeDTO);

        EmployeeDTO result = hrManagerService.deleteEmployeeById(EMPLOYEE_NUMBER);

        assertNotNull(result);
        assertEquals(employeeDTO, result);
        verify(employeeRepository).deleteById(any());
    }

    /**
     * Tests deletion of a non-existent employee.
     */
    @Test
    void testDeleteEmployee_NotFound() {
        when(employeeRepository.findByEmployeeNumber(EMPLOYEE_NUMBER)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> hrManagerService.deleteEmployeeById(EMPLOYEE_NUMBER));
    }
}
