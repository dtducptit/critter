package com.udacity.jdnd.course3.critter.dto;

import lombok.*;


/**
 * Represents the form that customer request and response data takes. Does not map
 * to the database directly.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private long id;
    private String name;
    private String phoneNumber;
    private String notes;
}
