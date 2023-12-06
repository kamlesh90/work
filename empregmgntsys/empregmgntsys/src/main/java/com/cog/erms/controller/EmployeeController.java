package com.cog.erms.controller;

import com.cog.erms.model.Address;
import com.cog.erms.model.Employee;
import com.cog.erms.service.EmployeeService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/bulk")
    public ResponseEntity<String> registerEmployees(@RequestBody @JsonProperty("employees") List<Employee> employees){
        employeeService.save(employees);
        return ResponseEntity.ok("Employees saved successfully!!");
    }
    @PostMapping
    public ResponseEntity<Void> registerEmployee(@RequestBody @JsonProperty("employees") Employee employee){
        employeeService.save(employee);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/update")
    public ResponseEntity<String> updateEmployee(@RequestBody Employee employee){
        employeeService.update(employee);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/firstname/{firstName}")
    public ResponseEntity<List<Employee>> getByFirstName(@PathVariable("firstName") String firstName,
                                                         @RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
                                                         @RequestParam(value = "pageSize", defaultValue = "2", required = false) Integer pageSize
    ){
        List<Employee> listOfEmpsByfname = employeeService.getByFirstName(firstName,pageNumber,pageSize);
        return ResponseEntity.ok(listOfEmpsByfname);
    }
    @GetMapping("/address/{addresses}")
    public ResponseEntity<List<Employee>> getByAddress(@PathVariable @JsonProperty("addresses") String addresses){
        List<Employee> listOfEmpsByAddress = employeeService.getByAddress(addresses);
        return ResponseEntity.ok(listOfEmpsByAddress);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAll(@RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
                                                 @RequestParam(value = "pageSize", defaultValue = "2", required = false) Integer pageSize){
        List<Employee> allEmp = employeeService.getAll(pageNumber, pageSize);
        return ResponseEntity.ok(allEmp);
    }
}
