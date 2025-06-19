package com.oop.motorph.dto;

import com.oop.motorph.entity.Employee;

/**
 * A Data Transfer Object (DTO) that encapsulates an {@link Employee} entity.
 * This record class is used to pass employee data, often for responses,
 * by directly wrapping the {@code Employee} domain object.
 *
 * @param employee The {@link Employee} entity contained within this DTO.
 */
public record EmployeeDTO(
                Employee employee) {

}