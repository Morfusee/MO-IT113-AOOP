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
    Optional<Employee> findByEmployeeNumber(Long employeeNumber);


    Employee save(EmployeeDTO employee);


    @Query("SELECT e.employeeNumber FROM Employee e")
    List<Long> findAllEmployeeNumbers();
}





