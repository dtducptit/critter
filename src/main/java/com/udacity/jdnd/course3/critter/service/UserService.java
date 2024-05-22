package com.udacity.jdnd.course3.critter.service;

import  com.udacity.jdnd.course3.critter.dto.CustomerDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = new Customer(customerDTO.getName(), customerDTO.getPhoneNumber(), customerDTO.getNotes());
        Customer saveCustomer = customerRepository.save(customer);
        return new CustomerDTO(saveCustomer.getId(), saveCustomer.getName(), saveCustomer.getPhoneNumber(), saveCustomer.getNotes());
    }
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(customer -> new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getNotes(),
                customer.getPhoneNumber()
        )).collect(Collectors.toList());
    }
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Optional<Pet> pet = petRepository.findById(petId);
        if (pet.isPresent()) {
            Customer customer = pet.get().getCustomer();
            return new CustomerDTO(customer.getId(), customer.getName(), customer.getPhoneNumber(), customer.getNotes());
        } else {
            throw new NoSuchElementException("Pet not found");
        }
    }
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = new Employee(employeeDTO.getName(), employeeDTO.getSkills(), employeeDTO.getDaysAvailable());
        Employee saveEmployee = employeeRepository.save(employee);
        return new EmployeeDTO(saveEmployee.getId(), saveEmployee.getName(), saveEmployee.getSkills(), saveEmployee.getDayAvailables());
    }
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new NoSuchElementException("Employee not found"));
        return new EmployeeDTO(employee.getId(), employee.getName(), employee.getSkills(), employee.getDayAvailables());
    }
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId){
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new NoSuchElementException("Employee not found"));
        employee.setDayAvailables(daysAvailable);
        employeeRepository.save(employee);
    }
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        DayOfWeek dayOfWeek = employeeDTO.getDate().getDayOfWeek();
        List<Employee> employees = employeeRepository.findByDayAvailablesContainingAndSkillsIn(dayOfWeek, employeeDTO.getSkills());
        List<Employee> filteredEmployees = employees.stream()
                .filter(employee -> employee.getSkills().containsAll(employeeDTO.getSkills()))
                .collect(Collectors.toList());
        return filteredEmployees.stream().map(employee -> new EmployeeDTO(
                employee.getId(), employee.getName(), employee.getSkills(), employee.getDayAvailables())
        ).collect(Collectors.toList());
    }
}
