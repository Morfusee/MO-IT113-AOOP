package com.oop.motorph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oop.motorph.entity.GovernmentIds;

@Repository
public interface GovernmentIdRepository extends JpaRepository<GovernmentIds, Long> {

}
