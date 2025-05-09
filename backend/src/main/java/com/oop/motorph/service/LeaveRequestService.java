package com.oop.motorph.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oop.motorph.dto.LeaveRequestDTO;
import com.oop.motorph.dto.mapper.LeaveRequestDTOMapper;
import com.oop.motorph.entity.LeaveRequest;
import com.oop.motorph.repository.LeaveRequestRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private LeaveRequestDTOMapper leaveRequestDTOMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public LeaveRequestDTO getLeaveRequestById(Long leaveRequestId) {
        return leaveRequestRepository.findById(leaveRequestId).stream()
                .map(leaveRequestDTOMapper)
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Leave request not found."));
    }

    public List<LeaveRequestDTO> getLeaveRequestByEmployeeNum(Long employeeNum, String status) {
        return leaveRequestRepository.findByEmployeeNumAndStatus(employeeNum, status).stream()
                .map(leaveRequestDTOMapper)
                .collect(Collectors.toList());
    }

    public List<LeaveRequestDTO> getLeaveRequestByStatus(String status) {
        return leaveRequestRepository.findByStatus(status).stream().map(leaveRequestDTOMapper)
                .collect(Collectors.toList());
    }

    public LeaveRequestDTO createLeaveRequest(LeaveRequest leaveRequest) {

        if (leaveRequest.getStartDate().after(leaveRequest.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date.");
        }

        LeaveRequest savedLeaveRequest = leaveRequestRepository.save(leaveRequest);

        /*
         * Clear the persistence context to ensure that the fields
         * of the returned object are up-to-date
         * Solves the employee null issue
         */
        entityManager.refresh(leaveRequest);

        return leaveRequestDTOMapper.toDTO(savedLeaveRequest);
    }

    public LeaveRequestDTO updateLeaveRequest(LeaveRequestDTO leaveRequest) {
        // Get the leave request by ID
        LeaveRequest leaveRequestToUpdate = leaveRequestRepository.findById(leaveRequest.id()).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Leave request not found."));

        // Update the leave request
        leaveRequestDTOMapper.updateEntity(leaveRequestToUpdate, leaveRequest);

        // Save the updated leave request
        LeaveRequest updatedLeaveRequest = leaveRequestRepository.save(leaveRequestToUpdate);

        // Return the updated leave request
        return leaveRequestDTOMapper.toDTO(updatedLeaveRequest);
    }

    public LeaveRequestDTO deleteLeaveRequestById(Long leaveRequestId) {
        // Get the leave request by ID
        LeaveRequestDTO leaveRequest = leaveRequestRepository.findById(leaveRequestId).stream()
                .map(leaveRequestDTOMapper)
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Leave request not found."));

        // Delete the leave request
        leaveRequestRepository.deleteById(leaveRequestId);

        // Return the deleted leave request
        return leaveRequest;
    }
}
