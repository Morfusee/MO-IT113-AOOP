package com.oop.motorph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oop.motorph.entity.Compensation;

@Repository
public interface CompensationRepository extends JpaRepository<Compensation, Long> {

}
