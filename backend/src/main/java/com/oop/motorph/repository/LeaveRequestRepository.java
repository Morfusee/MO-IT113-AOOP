package com.oop.motorph.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oop.motorph.entity.LeaveRequest;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployeeNum(Long employeeNum);

    Optional<LeaveRequest> findById(Integer id);

    List<LeaveRequest> findByStatus(String status);

    List<LeaveRequest> findByEmployeeNumAndStatus(Long employeeNum, String status);
}
