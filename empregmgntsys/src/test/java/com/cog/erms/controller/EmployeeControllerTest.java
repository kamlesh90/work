package com.cog.erms.controller;

import com.cog.erms.model.Address;
import com.cog.erms.model.Designation;
import com.cog.erms.model.Employee;
import com.cog.erms.service.EmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {
    @InjectMocks
    private EmployeeController employeeController;
    @Mock
    private EmployeeService employeeService;
    @Test
    @DisplayName("This test is for register single employee")
    void registerEmployee() {
        Employee e = new Employee();
        e.setFirstName("Ram");

        Employee savedEmployee = new Employee();
        savedEmployee.setEmpId(1L);

        //here we dont want real call rather then we want mockito to do this for us, when i am calling service.save then return dummy/proxy object
        //do not make the actual call for employeeService's methods inside controller, rather return dummy object
        Mockito.when(employeeService.save(e)).thenReturn(savedEmployee);

        ResponseEntity<Employee> employeeResponseEntity = employeeController.registerEmployee(e);

        Assertions.assertNotNull(employeeResponseEntity.getBody().getEmpId());
        Assertions.assertEquals(HttpStatus.CREATED.value(), employeeResponseEntity.getStatusCode().value());
    }
    @Test
    @DisplayName("Test illustrate the operation for get all employee")
    void getAll(){
        List<Employee> listOfEmployee = new ArrayList<>();

        List<Address> listOfAddressRam = new ArrayList<>();
                listOfAddressRam.add(new Address(11L,"Ayodhya"));
                listOfAddressRam.add(new Address(22L,"UP"));
        List<Designation> listOfDesignationRam = new ArrayList<>();
                listOfDesignationRam.add(new Designation(12L,"DEV"));
                listOfDesignationRam.add(new Designation(13L,"QA"));

        List<Address> listOfAddressKrishna = new ArrayList<>();
                listOfAddressKrishna.add(new Address(33L,"Vrindavan"));
                listOfAddressKrishna.add(new Address(44L,"UP"));
        List<Designation> listOfDesignationKrishna = new ArrayList<>();
                listOfDesignationKrishna.add(new Designation(32L,"DEV-1"));
                listOfDesignationKrishna.add(new Designation(43L,"QA-1"));

        List<Address> listOfAddressJohn = new ArrayList<>();
                listOfAddressJohn.add(new Address(55L,"New york"));
                listOfAddressJohn.add(new Address(66L,"America"));
        List<Designation> listOfDesignationJohn = new ArrayList<>();
                listOfDesignationJohn.add(new Designation(52L,"DEV-2"));
                listOfDesignationJohn.add(new Designation(62L,"QA-2"));


        Employee ram     = new Employee(1L,"Ram","Ji","Raghu",listOfAddressRam,listOfDesignationRam);
        Employee krishna = new Employee(2L,"Krishna","yadu","kanha",listOfAddressKrishna,listOfDesignationKrishna);
        Employee john    = new Employee(3L,"John","Scott","jim",listOfAddressJohn,listOfDesignationJohn);

        listOfEmployee.add(ram);
        listOfEmployee.add(krishna);
        listOfEmployee.add(john);

        int pageNum=1;
        int pageSize=2;

        Mockito.when(employeeService.getAll(pageNum,pageSize)).thenReturn(listOfEmployee);

        ResponseEntity<List<Employee>> responseEntity = employeeController.getAll(pageNum, pageSize);
        System.out.println("SIZE = "+responseEntity.getBody().size());

        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertEquals(3,responseEntity.getBody().size());
        Assertions.assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
    }
}