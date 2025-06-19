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

    /**
     * Retrieves a leave request by ID.
     * 
     * @param leaveRequestId The ID of the leave request.
     * @return LeaveRequestDTO if found.
     */
    public LeaveRequestDTO getLeaveRequestById(Long leaveRequestId) {
        return leaveRequestRepository.findById(leaveRequestId).stream()
                .map(leaveRequestDTOMapper)
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Leave request not found."));
    }

    /**
     * Retrieves leave requests by employee number and status.
     * 
     * @param employeeNum The employee number.
     * @param status      The status of the leave request.
     * @return List of LeaveRequestDTOs.
     */
    public List<LeaveRequestDTO> getLeaveRequestByEmployeeNum(Long employeeNum, String status) {
        return leaveRequestRepository.findByEmployeeNumAndStatus(employeeNum, status).stream()
                .map(leaveRequestDTOMapper)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all leave requests by status.
     * 
     * @param status The leave request status.
     * @return List of LeaveRequestDTOs.
     */
    public List<LeaveRequestDTO> getLeaveRequestByStatus(String status) {
        return leaveRequestRepository.findByStatus(status).stream()
                .map(leaveRequestDTOMapper)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new leave request.
     * 
     * @param leaveRequest The LeaveRequest entity to be saved.
     * @return The created LeaveRequestDTO.
     */
    public LeaveRequestDTO createLeaveRequest(LeaveRequest leaveRequest) {

        if (leaveRequest.getStartDate().after(leaveRequest.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date.");
        }

        LeaveRequest savedLeaveRequest = leaveRequestRepository.save(leaveRequest);

        /*
         * Clear the persistence context to ensure that the fields
         * of the returned object are up-to-date.
         */
        entityManager.refresh(leaveRequest);

        return leaveRequestDTOMapper.toDTO(savedLeaveRequest);
    }

    /**
     * Updates an existing leave request.
     * 
     * @param leaveRequest The updated LeaveRequestDTO.
     * @return The updated LeaveRequestDTO.
     */
    public LeaveRequestDTO updateLeaveRequest(LeaveRequestDTO leaveRequest) {
        LeaveRequest leaveRequestToUpdate = leaveRequestRepository.findById(leaveRequest.id()).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Leave request not found."));

        leaveRequestDTOMapper.updateEntity(leaveRequestToUpdate, leaveRequest);

        LeaveRequest updatedLeaveRequest = leaveRequestRepository.save(leaveRequestToUpdate);

        return leaveRequestDTOMapper.toDTO(updatedLeaveRequest);
    }

    /**
     * Deletes a leave request by ID.
     * 
     * @param leaveRequestId The ID of the leave request to delete.
     * @return The deleted LeaveRequestDTO.
     */
    public LeaveRequestDTO deleteLeaveRequestById(Long leaveRequestId) {
        LeaveRequestDTO leaveRequest = leaveRequestRepository.findById(leaveRequestId).stream()
                .map(leaveRequestDTOMapper)
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Leave request not found."));

        leaveRequestRepository.deleteById(leaveRequestId);

        return leaveRequest;
    }
}
