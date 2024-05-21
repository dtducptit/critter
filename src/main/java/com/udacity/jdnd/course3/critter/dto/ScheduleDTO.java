package com.udacity.jdnd.course3.critter.dto;

import com.udacity.jdnd.course3.critter.common.enums.EmployeeSkill;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Represents the form that schedule request and response data takes. Does not map
 * to the database directly.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {
    private long id;
    private List<Long> employeeIds;
    private List<Long> petIds;
    private LocalDate date;
    private Set<EmployeeSkill> activities;
}
