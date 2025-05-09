package com.oop.motorph.dto.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.oop.motorph.dto.LeaveRequestDTO;
import com.oop.motorph.entity.LeaveRequest;

@Service
public class LeaveRequestDTOMapper implements Function<LeaveRequest, LeaveRequestDTO> {

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

        public void updateEntity(LeaveRequest leaveRequestToUpdate, LeaveRequestDTO leaveRequest) {
                leaveRequestToUpdate.setStartDate(leaveRequest.startDate());
                leaveRequestToUpdate.setEndDate(leaveRequest.endDate());
                leaveRequestToUpdate.setNotes(leaveRequest.notes());
                leaveRequestToUpdate.setLeaveType(leaveRequest.leaveType());
                leaveRequestToUpdate.setStatus(leaveRequest.status());
        }
}
