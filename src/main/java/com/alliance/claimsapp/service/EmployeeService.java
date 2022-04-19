package com.alliance.claimsapp.service;

import com.alliance.claimsapp.model.Employee;
import com.alliance.claimsapp.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public Employee registerEmployeeService(Employee employee){

        if(employeeRepository.findByEmail(employee.getEmail()).isPresent()){
            throw new IllegalStateException("Email already exist");
        }
        employee.setUserStatus("active");
        return employeeRepository.save(employee);
    }

    public Employee loginEmployeeService(String email, String password){
        Employee employee = employeeRepository.findByEmailAndPassword(email, password);
        if(employeeRepository.existsById(employee.getId())) {
            if(!employee.getUserStatus().equals("deleted"))
                return employee;
        }
        throw new IllegalStateException("User not found!");
    }


    public Employee editSessionEmployeePasswordService(Long id, String password){
        Employee sessionEmployee = null;
        if(employeeRepository.findById(id).isPresent()){
            sessionEmployee = employeeRepository.findById(id).get();
            sessionEmployee.setPassword(password);
        } else {
            throw new IllegalStateException("User not found");
        }
        return employeeRepository.save(sessionEmployee);
    }

    public String validateSessionEmployeePasswordService(Long id, String password) {
        Employee sessionEmployee = null;
        if(employeeRepository.findById(id).isPresent()){
            sessionEmployee = employeeRepository.findById(id).get();
            if(sessionEmployee.getPassword().equals(password)){
                return "true";
            } else {
                return "false";
            }
        } else {
            throw new IllegalStateException("User not found");
        }
    }

    public List<Employee> getAllEmployeeService(){
        return employeeRepository.findAllByUserStatus("active");
    }

    public Employee deleteEmployeeService(Long id){
        Employee oldUser = null;
        if(employeeRepository.findById(id).isPresent()){
            oldUser = employeeRepository.findById(id).get();
            oldUser.setUserStatus("deleted");
        } else {
            throw new IllegalStateException("User not found");
        }
        return employeeRepository.save(oldUser);
    }

    public Employee editSessionNameService(Long id, String firstName, String lastName){
        Employee sessionEmployee = employeeRepository.findById(id).get();
        sessionEmployee.setLastName(lastName);
        sessionEmployee.setFirstName(firstName);

        return employeeRepository.save(sessionEmployee);
    }

}
