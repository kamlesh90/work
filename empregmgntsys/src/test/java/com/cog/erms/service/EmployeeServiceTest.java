package com.cog.erms.service;

import com.cog.erms.exception.EmployeeNotFoundException;
import com.cog.erms.model.Address;
import com.cog.erms.model.Designation;
import com.cog.erms.model.Employee;
import com.cog.erms.repo.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;
    @Mock
    private EmployeeRepository employeeRepository;
    @Test
    void save() {
        Employee emp = new Employee();
        emp.setFirstName("Mohan");

        Employee savedEmp = new Employee();
        savedEmp.setEmpId(1L);

        Mockito.when(employeeRepository.save(emp)).thenReturn(savedEmp);
        employeeService.save(emp);

        Assertions.assertEquals(1L,savedEmp.getEmpId());
    }
    @Test
    void getAllEmployee(){
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

        Integer pageNumber=1;
        Integer pageSize=2;

        Pageable p = PageRequest.of(pageNumber,pageSize);
        Page<Employee> page = new PageImpl<>(listOfEmployee, p, listOfEmployee.size());

        Mockito.when(employeeRepository.findAll(p)).thenReturn(page);

        List<Employee> result = employeeService.getAll(1,2);

        Assertions.assertEquals(3,result.size());
    }
    @Test
    void updateEmployee_Success(){
        Long empId=1L;
        Employee inputEmp = new Employee();
            inputEmp.setEmpId(empId);
            inputEmp.setFirstName("Ram");
            inputEmp.setLastName("Raghu");
            inputEmp.setMiddleName("Ji");
            inputEmp.setAddresses(null);
            inputEmp.setDesignations(null);

        Employee semployee = new Employee();
            semployee.setEmpId(empId);
            semployee.setFirstName("Ram Ram");
            semployee.setLastName("Raghu");
            semployee.setMiddleName("Ji");
            semployee.setAddresses(Collections.emptyList());
            semployee.setDesignations(Collections.emptyList());

        Mockito.when(employeeRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(semployee));
        Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(inputEmp);

        Employee uemployee = employeeService.update(inputEmp, empId);

        Assertions.assertEquals(inputEmp.getFirstName(),uemployee.getFirstName());
        Assertions.assertEquals(inputEmp.getLastName(),uemployee.getLastName());
    }
    @Test
    void updateEmployee_Failure(){
        Long empId=1L;
        Employee inputEmp = new Employee();
        inputEmp.setEmpId(empId);
        inputEmp.setFirstName("Ram");
        inputEmp.setLastName("Raghu");
        inputEmp.setMiddleName("Ji");
        inputEmp.setAddresses(Collections.emptyList());
        inputEmp.setDesignations(Collections.emptyList());

        Mockito.when(employeeRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        EmployeeNotFoundException exception = Assertions.assertThrows(EmployeeNotFoundException.class,() -> {
            Employee employee = employeeService.update(inputEmp,empId);
        });

        Assertions.assertEquals("employee not exist with ID : "+1L,exception.getMessage());

    }
}
