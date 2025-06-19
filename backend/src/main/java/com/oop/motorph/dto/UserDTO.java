package com.oop.motorph.dto;

/**
 * A Data Transfer Object (DTO) for basic user information.
 * This record class is designed to carry minimal user details,
 * primarily the username, often used for authentication requests
 * or simple user identification in responses.
 *
 * @param username The username of the user.
 */
public record UserDTO(String username) {

}