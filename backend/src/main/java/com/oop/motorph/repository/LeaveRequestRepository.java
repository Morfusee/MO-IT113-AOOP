package com.oop.motorph.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oop.motorph.entity.LeaveRequest;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    /**
     * Retrieves all leave requests submitted by a specific employee.
     * 
     * @param employeeNum The employee number.
     * @return List of LeaveRequest records.
     */
    List<LeaveRequest> findByEmployeeNum(Long employeeNum);

    /**
     * Retrieves a leave request by its unique ID.
     * 
     * @param id The leave request ID.
     * @return An Optional containing the LeaveRequest if found.
     */
    Optional<LeaveRequest> findById(Integer id);

    /**
     * Retrieves all leave requests with a specific status.
     * 
     * @param status The status to filter by (e.g. "Pending", "Approved", "Denied").
     * @return List of LeaveRequest records matching the status.
     */
    List<LeaveRequest> findByStatus(String status);

    /**
     * Retrieves leave requests for a specific employee that match a given status.
     * 
     * @param employeeNum The employee number.
     * @param status      The leave request status.
     * @return List of LeaveRequest records.
     */
    List<LeaveRequest> findByEmployeeNumAndStatus(Long employeeNum, String status);
}
