package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.dto.ScheduleDTO;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        List<Employee> employees = employeeRepository.findAllById(scheduleDTO.getEmployeeIds());
        List<Pet> pets = petRepository.findAllById(scheduleDTO.getPetIds());
        Schedule schedule = new Schedule();
        schedule.setDate(scheduleDTO.getDate());
        schedule.setActivities(scheduleDTO.getActivities());
        schedule.setPets(pets);
        schedule.setEmployees(employees);
        Schedule result = scheduleRepository.save(schedule);
        List<Long> petIds = result.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        List<Long> employeeIds = result.getEmployees().stream().map(Employee::getId).collect(Collectors.toList());
        return new ScheduleDTO(result.getId(), employeeIds, petIds, result.getDate(), result.getActivities());
    }
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return getScheduleDTOS(schedules);
    }
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        Pet pet = petRepository.getOne(petId);
        List<Schedule> schedules = scheduleRepository.findAllByPetsContaining(pet);
        return getScheduleDTOS(schedules);
    }
    private List<ScheduleDTO> getScheduleDTOS(List<Schedule> list) {
        return list.stream().map(s -> new ScheduleDTO(
                s.getId(),
                s.getEmployees().stream().map(Employee::getId).collect(Collectors.toList()),
                s.getPets().stream().map(Pet::getId).collect(Collectors.toList()),
                s.getDate(),
                s.getActivities()
        )).collect(Collectors.toList());
    }
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        List<Schedule> list = scheduleRepository.findAllByEmployeesContaining(employee);
        return getScheduleDTOS(list);
    }
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Pet> pets = petRepository.findByOwerId(customerId);
        List<Schedule> list = scheduleRepository.findAllByPetsIn(pets);
        return getScheduleDTOS(list);
    }
}
