package com.oop.motorph.utils;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oop.motorph.dto.LeaveRequestDTO;
import com.oop.motorph.dto.mapper.LeaveRequestDTOMapper;
import com.oop.motorph.entity.LeaveRequest;
import com.oop.motorph.repository.LeaveRequestRepository;
import com.oop.motorph.service.LeaveRequestService;

@Component
public class LeaveRequestTestUtils {

    @Autowired
    private LeaveRequestDTOMapper leaveRequestDTOMapper;

    @Autowired
    private LeaveRequestService leaveRequestService;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    public LeaveRequestDTO createAndSaveLeaveRequest() {
        LeaveRequest leaveRequest = new LeaveRequest(10001L, Timestamp.valueOf("2024-01-27 17:45:37"),
                Timestamp.valueOf("2024-02-27 17:45:37"), "",
                "Sick Leave", "Pending");

        return leaveRequestService.createLeaveRequest(leaveRequest);
    }

    public LeaveRequestDTO updateLeaveRequest(LeaveRequestDTO leaveRequest) {
        LeaveRequestDTO updatedLeaveRequest = new LeaveRequestDTO(leaveRequest.id(), leaveRequest.employeeNum(),
                leaveRequest.employeeName(), leaveRequest.startDate(), leaveRequest.endDate(), leaveRequest.notes(),
                leaveRequest.leaveType(), "Denied");

        return leaveRequestService.updateLeaveRequest(updatedLeaveRequest);
    }

    public LeaveRequestDTO deleteLeaveRequest(LeaveRequestDTO savedLeaveRequest) {
        // Delete leave request by id
        leaveRequestService
                .deleteLeaveRequestById(Long.valueOf(savedLeaveRequest.id()));

        // Find leave request by id
        return leaveRequestRepository.findById(Long.valueOf(savedLeaveRequest.id())).stream()
                .map(leaveRequestDTOMapper)
                .collect(Collectors.toList())
                .stream()
                .findFirst().orElse(null);
    }
}
