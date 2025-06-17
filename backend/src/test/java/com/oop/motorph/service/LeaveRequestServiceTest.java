package com.oop.motorph.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.oop.motorph.dto.LeaveRequestDTO;
import com.oop.motorph.dto.mapper.LeaveRequestDTOMapper;
import com.oop.motorph.entity.Employee;
import com.oop.motorph.entity.LeaveRequest;
import com.oop.motorph.entity.PersonalInfo;
import com.oop.motorph.repository.LeaveRequestRepository;
import jakarta.persistence.EntityManager;

@ExtendWith(MockitoExtension.class)
public class LeaveRequestServiceTest {

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private LeaveRequestDTOMapper leaveRequestDTOMapper;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private LeaveRequestService leaveRequestService;

    // Test constants
    private static final Long EMPLOYEE_NUM = 10001L;
    private static final Long LEAVE_REQUEST_ID = 1L;
    private static final Long NON_EXISTENT_ID = 999L;
    private static final String EMPLOYEE_LAST_NAME = "Doe";
    private static final String EMPLOYEE_FIRST_NAME = "John";
    private static final String EMPLOYEE_BIRTHDATE = "1990-01-01";
    private static final String EMPLOYEE_ADDRESS = "123 Street";
    private static final String EMPLOYEE_PHONE = "1234567890";
    private static final Timestamp START_DATE_VALID = Timestamp.valueOf("2024-01-27 17:45:37");
    private static final Timestamp END_DATE_VALID = Timestamp.valueOf("2024-02-27 17:45:37");
    private static final Timestamp START_DATE_INVALID = Timestamp.valueOf("2024-02-27 17:45:37");
    private static final Timestamp END_DATE_INVALID = Timestamp.valueOf("2024-01-27 17:45:37");
    private static final String NOTES = "Test notes";
    private static final String LEAVE_TYPE = "Sick Leave";
    private static final String STATUS_PENDING = "Pending";
    private static final String EMPLOYEE_FULL_NAME = "John Doe";

    private LeaveRequest leaveRequest;
    private LeaveRequestDTO leaveRequestDTO;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setEmployeeNumber(EMPLOYEE_NUM);
        employee.setPersonalInfo(new PersonalInfo(EMPLOYEE_LAST_NAME, EMPLOYEE_FIRST_NAME, EMPLOYEE_BIRTHDATE,
                EMPLOYEE_ADDRESS, EMPLOYEE_PHONE));

        leaveRequest = new LeaveRequest(
                EMPLOYEE_NUM,
                START_DATE_VALID,
                END_DATE_VALID,
                NOTES,
                LEAVE_TYPE,
                STATUS_PENDING);
        leaveRequest.setId(LEAVE_REQUEST_ID.intValue());
        leaveRequest.setEmployee(employee);

        leaveRequestDTO = LeaveRequestDTO.builder()
                .id(LEAVE_REQUEST_ID.intValue())
                .employeeNum(EMPLOYEE_NUM)
                .employeeName(EMPLOYEE_FULL_NAME)
                .startDate(START_DATE_VALID)
                .endDate(END_DATE_VALID)
                .notes(NOTES)
                .leaveType(LEAVE_TYPE)
                .status(STATUS_PENDING)
                .build();
    }

    private void assertLeaveRequestDTO(LeaveRequestDTO expected, LeaveRequestDTO actual) {
        assertNotNull(actual);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.employeeNum(), actual.employeeNum());
        assertEquals(expected.employeeName(), actual.employeeName());
        assertEquals(expected.startDate(), actual.startDate());
        assertEquals(expected.endDate(), actual.endDate());
        assertEquals(expected.notes(), actual.notes());
        assertEquals(expected.leaveType(), actual.leaveType());
        assertEquals(expected.status(), actual.status());
    }

    @Test
    void testGetLeaveRequestById() {
        when(leaveRequestRepository.findById(LEAVE_REQUEST_ID)).thenReturn(Optional.of(leaveRequest));
        when(leaveRequestDTOMapper.apply(leaveRequest)).thenReturn(leaveRequestDTO);

        LeaveRequestDTO result = leaveRequestService.getLeaveRequestById(LEAVE_REQUEST_ID);

        assertLeaveRequestDTO(leaveRequestDTO, result);
    }

    @Test
    void testGetLeaveRequestById_NotFound() {
        when(leaveRequestRepository.findById(NON_EXISTENT_ID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> leaveRequestService.getLeaveRequestById(NON_EXISTENT_ID));
    }

    @Test
    void testGetLeaveRequestByEmployeeNumAndStatus() {
        List<LeaveRequest> leaveRequests = Arrays.asList(leaveRequest);
        when(leaveRequestRepository.findByEmployeeNumAndStatus(EMPLOYEE_NUM, STATUS_PENDING)).thenReturn(leaveRequests);
        when(leaveRequestDTOMapper.apply(leaveRequest)).thenReturn(leaveRequestDTO);

        List<LeaveRequestDTO> results = leaveRequestService.getLeaveRequestByEmployeeNum(EMPLOYEE_NUM, STATUS_PENDING);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertLeaveRequestDTO(leaveRequestDTO, results.get(0));
    }

    @Test
    void testCreateLeaveRequest() {
        when(leaveRequestRepository.save(leaveRequest)).thenReturn(leaveRequest);
        when(leaveRequestDTOMapper.toDTO(leaveRequest)).thenReturn(leaveRequestDTO);

        LeaveRequestDTO result = leaveRequestService.createLeaveRequest(leaveRequest);

        assertLeaveRequestDTO(leaveRequestDTO, result);
    }

    @Test
    void testCreateLeaveRequest_InvalidDates() {
        LeaveRequest invalidRequest = new LeaveRequest(
                EMPLOYEE_NUM,
                START_DATE_INVALID,
                END_DATE_INVALID,
                NOTES,
                LEAVE_TYPE,
                STATUS_PENDING);

        assertThrows(RuntimeException.class, () -> leaveRequestService.createLeaveRequest(invalidRequest));
    }

    @Test
    void testUpdateLeaveRequest() {
        when(leaveRequestRepository.findById(LEAVE_REQUEST_ID.intValue())).thenReturn(Optional.of(leaveRequest));
        when(leaveRequestRepository.save(leaveRequest)).thenReturn(leaveRequest);
        when(leaveRequestDTOMapper.toDTO(leaveRequest)).thenReturn(leaveRequestDTO);

        LeaveRequestDTO result = leaveRequestService.updateLeaveRequest(leaveRequestDTO);

        assertLeaveRequestDTO(leaveRequestDTO, result);
    }

    @Test
    void testDeleteLeaveRequestById() {
        when(leaveRequestRepository.findById(LEAVE_REQUEST_ID)).thenReturn(Optional.of(leaveRequest));
        when(leaveRequestDTOMapper.apply(leaveRequest)).thenReturn(leaveRequestDTO);

        LeaveRequestDTO result = leaveRequestService.deleteLeaveRequestById(LEAVE_REQUEST_ID);

        assertLeaveRequestDTO(leaveRequestDTO, result);
        verify(leaveRequestRepository).deleteById(LEAVE_REQUEST_ID);
    }
}