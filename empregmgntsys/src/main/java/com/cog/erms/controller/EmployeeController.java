package com.cog.erms.controller;

import com.cog.erms.model.Employee;
import com.cog.erms.service.EmployeeService;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/api/v1/employee")
@Tag(name = "EmployeeController", description = "EmployeeController for perform rest api operations")
public class EmployeeController {
    //http://localhost:8080/swagger-ui/index.html : swagger url
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/bulk")
    @Operation(
            summary = "api for employee registration bulk",
            description = "api for employee registration bulk"
    )
    public ResponseEntity<String> registerEmployees(@RequestBody @JsonProperty("employees") List<Employee> employees){
        List<Employee> savedEmp = employeeService.save(employees);
        return ResponseEntity.ok("Employees saved successfully!!");
    }
    @PostMapping
    @Operation(
            summary = "api for employee registration",
            description = "api for employee registration"
    )
    public ResponseEntity<Employee> registerEmployee(@RequestBody Employee employee){
        Employee saveEmployee = employeeService.save(employee);
        ResponseEntity<Employee> responseEntity = new ResponseEntity<>(saveEmployee, HttpStatus.CREATED);
        return responseEntity;
    }
    @PutMapping("/update/{empId}")
    @Operation(
            summary = "api for employee updation",
            description = "api for employee updation"
    )
    public ResponseEntity<String> updateEmployee(@RequestBody Employee employee, @PathVariable("empId") Long employeeId){
        Employee update = employeeService.update(employee,employeeId);
        return  ResponseEntity.ok().body("Employee updated !! with ID : "+update.getEmpId());
    }
    @Operation(
            summary = "api for update employee by last name",
            description = "api for update employee by last name"
    )
    @PatchMapping("updateByLastName/{empId}")
    public ResponseEntity<Employee> updateEmployeeByLastName(@RequestParam(value = "lastName") String lastname, @PathVariable("empId") Long employeeId){
        Employee updatedEmployeeByLName = employeeService.updateEmployeeByLastName(lastname, employeeId);
        ResponseEntity<Employee> responseEntity = new ResponseEntity<>(updatedEmployeeByLName, HttpStatus.OK);
        return responseEntity;
    }
    @Operation(
            summary = "api for get employee by ID",
            description = "api for get employee by ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<Employee>  getByEmployeeId(@PathVariable Long id){
        Employee empById = employeeService.getEmpById(id);
        return ResponseEntity.ok().body(empById);
    }
    @Operation(
            summary = "api for get employee by first name",
            description = "api for get employee by first name"
    )
    @GetMapping("/firstname/{firstName}")
    public ResponseEntity<List<Employee>> getByFirstName(@PathVariable("firstName") String firstName,
                                                         @RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
                                                         @RequestParam(value = "pageSize", defaultValue = "2", required = false) Integer pageSize
    ){
        List<Employee> listOfEmpsByfname = employeeService.getByFirstName(firstName,pageNumber,pageSize);
        return ResponseEntity.ok(listOfEmpsByfname);
    }
    @GetMapping("/address/{addresses}")
    @Operation(
            summary = "api for get employee by address",
            description = "api for get employee by address"
    )
    public ResponseEntity<List<Employee>> getByAddress(@PathVariable @JsonProperty("addresses") String addresses){
        List<Employee> listOfEmpsByAddress = employeeService.getByAddress(addresses);
        return ResponseEntity.ok(listOfEmpsByAddress);
    }
    @GetMapping("/all")
    @Operation(
            summary = "api for get all employee",
            description = "api for get all employee"
    )
    public ResponseEntity<List<Employee>> getAll(@RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
                                                 @RequestParam(value = "pageSize", defaultValue = "2", required = false) Integer pageSize){
        List<Employee> allEmp = employeeService.getAll(pageNumber, pageSize);
        return ResponseEntity.ok(allEmp);
    }
    @GetMapping("/excel")
    @Operation(
            summary = "api for export employee data to excel",
            description = "api for export employee data to excel"
    )
    public ResponseEntity<Resource> exportToExcel() throws IOException {
        String filename="EmployeesExcelData";

        ByteArrayInputStream allInExcel = employeeService.getAllInExcel();
        InputStreamResource file = new InputStreamResource(allInExcel);

        ResponseEntity<Resource> body = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)//we have attachment file and name is this
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);

        return body;
    }
}
