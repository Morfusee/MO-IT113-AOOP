package com.oop.motorph.dto.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.oop.motorph.dto.LeaveRequestDTO;
import com.oop.motorph.entity.LeaveRequest;

/**
 * Maps between {@link LeaveRequest} entities and {@link LeaveRequestDTO}
 * objects.
 * This mapper facilitates the conversion of leave request data for API exposure
 * and for updating existing leave request records.
 */
@Service
public class LeaveRequestDTOMapper implements Function<LeaveRequest, LeaveRequestDTO> {

        /**
         * Converts a {@code LeaveRequest} entity to a {@code LeaveRequestDTO} using
         * direct constructor invocation. It constructs the employee's full name
         * by concatenating first and last names from the associated
         * {@link com.oop.motorph.entity.Employee} entity.
         *
         * @param leaveRequest The {@link LeaveRequest} entity to map.
         * @return A new {@link LeaveRequestDTO} containing the mapped leave request
         *         data.
         */
        @Override
        public LeaveRequestDTO apply(LeaveRequest leaveRequest) {
                return new LeaveRequestDTO(
                                leaveRequest.getId(),
                                leaveRequest.getEmployeeNum(),
                                leaveRequest.getEmployee().getPersonalInfo().getFirstName() + " "
                                                + leaveRequest.getEmployee().getPersonalInfo().getLastName(),
                                leaveRequest.getStartDate(),
                                leaveRequest.getEndDate(),
                                leaveRequest.getNotes(),
                                leaveRequest.getLeaveType(),
                                leaveRequest.getStatus());
        }

        /**
         * Converts a {@code LeaveRequest} entity to a {@code LeaveRequestDTO} using
         * a builder pattern. This method provides an alternative, more readable way
         * to construct the DTO, especially useful when there are many fields.
         * It also constructs the employee's full name from the associated employee
         * entity.
         *
         * @param entity The {@link LeaveRequest} entity to map.
         * @return A new {@link LeaveRequestDTO} built from the entity data.
         */
        public LeaveRequestDTO toDTO(LeaveRequest entity) {
                return LeaveRequestDTO.builder()
                                .id(entity.getId())
                                .employeeNum(entity.getEmployeeNum())
                                .employeeName(entity.getEmployee().getPersonalInfo().getFirstName() + " "
                                                + entity.getEmployee().getPersonalInfo().getLastName())
                                .startDate(entity.getStartDate())
                                .endDate(entity.getEndDate())
                                .notes(entity.getNotes())
                                .leaveType(entity.getLeaveType())
                                .status(entity.getStatus())
                                .build();
        }

        /**
         * Updates an existing {@link LeaveRequest} entity with data from a
         * {@link LeaveRequestDTO}.
         * This method is used for patching or putting updates to a leave request
         * record,
         * applying the changes from the DTO to the persistent entity.
         *
         * @param leaveRequestToUpdate The existing {@link LeaveRequest} entity to be
         *                             updated.
         * @param leaveRequest         The {@link LeaveRequestDTO} containing the
         *                             updated fields.
         */
        public void updateEntity(LeaveRequest leaveRequestToUpdate, LeaveRequestDTO leaveRequest) {
                leaveRequestToUpdate.setStartDate(leaveRequest.startDate());
                leaveRequestToUpdate.setEndDate(leaveRequest.endDate());
                leaveRequestToUpdate.setNotes(leaveRequest.notes());
                leaveRequestToUpdate.setLeaveType(leaveRequest.leaveType());
                leaveRequestToUpdate.setStatus(leaveRequest.status());
        }
}