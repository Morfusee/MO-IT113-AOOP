package com.oop.motorph.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Retrieves an employee by their unique employee number.
     * 
     * @param employeeNumber The employee number to search for.
     * @return An Optional containing the Employee if found.
     */
    Optional<Employee> findByEmployeeNumber(Long employeeNumber);

    /**
     * Saves an EmployeeDTO object to the database.
     * 
     * @param employee The EmployeeDTO object to be saved.
     * @return The saved Employee entity.
     */
    Employee save(EmployeeDTO employee);

    /**
     * Retrieves all employee numbers from the Employee table.
     * 
     * @return A list of all employee numbers.
     */
    @Query("SELECT e.employeeNumber FROM Employee e")
    List<Long> findAllEmployeeNumbers();
}
