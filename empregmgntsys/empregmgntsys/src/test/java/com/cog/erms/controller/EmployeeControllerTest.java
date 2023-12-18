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
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.print.attribute.standard.Media;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    @Test
    @DisplayName("success scenario test is for to update the employee by last name")
    void updateEmployeeByLastName(){
        Employee employee = new Employee();
        employee.setFirstName("test-employee");

        Mockito.when(employeeService.updateEmployeeByLastName(Mockito.anyString(),Mockito.anyLong())).thenReturn(employee);

        ResponseEntity<Employee> responseEntity = employeeController.updateEmployeeByLastName("dummy last name", 1L);

        Assertions.assertEquals(employee.getLastName(),responseEntity.getBody().getLastName());
        Assertions.assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
    }
    @Test
    @DisplayName("save list of employee at once")
    void saveAllEmployees(){
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

        Mockito.when(employeeService.saveAll(Mockito.anyList())).thenReturn(listOfEmployee);
        ResponseEntity<List<Employee>> responseEntity = employeeController.registerEmployees(listOfEmployee);

        Assertions.assertEquals(HttpStatus.CREATED.value(),responseEntity.getStatusCode().value());
        Assertions.assertEquals(3,responseEntity.getBody().size());
    }
    @Test
    @DisplayName("Test is for update employee")
    void updateEmployee(){
        Employee employee = new Employee(1L,"ShriSeetaRam","Raghu","Ji", Collections.emptyList(),Collections.emptyList());
        Employee e = new Employee();

        Mockito.when(employeeService.update(e,1L)).thenReturn(employee);
        ResponseEntity<String> stringResponseEntity = employeeController.updateEmployee(e, 1L);

        Assertions.assertEquals("Employee updated !! with ID : "+1L,stringResponseEntity.getBody());
        Assertions.assertEquals(HttpStatus.OK.value(),stringResponseEntity.getStatusCode().value());
    }
    @Test
    @DisplayName("test is for get employee by id")
    void getEmployeeById(){
        Employee employee = new Employee(1L,"ShriSeetaRam","Raghu","Ji", Collections.emptyList(),Collections.emptyList());

        Mockito.when(employeeService.getEmpById(1L)).thenReturn(employee);

        ResponseEntity<Employee> responseEntity = employeeController.getEmployeeById(1L);

        Assertions.assertEquals(1L,responseEntity.getBody().getEmpId());
        Assertions.assertEquals("ShriSeetaRam",responseEntity.getBody().getFirstName());
        Assertions.assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());

    }
    @Test
    @DisplayName("test to illustrate get Employees by first name")
    void getEmployeeByFirstName(){
        List<Employee> employeeList = Arrays.asList(new Employee(1L,"Ram","Ji","Raghu",Collections.emptyList(),Collections.emptyList()));

        Mockito.when(employeeService.getByFirstName("Ram",0,2)).thenReturn(employeeList);

        ResponseEntity<List<Employee>> responseEntity = employeeController.getByFirstName("Ram",0,2);

        Assertions.assertEquals(1L,responseEntity.getBody().get(0).getEmpId());
        Assertions.assertEquals(1,responseEntity.getBody().size());
        Assertions.assertEquals("Ram",responseEntity.getBody().get(0).getFirstName());
        Assertions.assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
    }
    @Test
    @DisplayName("test to illustrate get Employees by address city")
    void getEmployeeByAddress(){
        List<Address> listOfAddressKrishna = new ArrayList<>();
        listOfAddressKrishna.add(new Address(33L,"Vrindavan"));
        listOfAddressKrishna.add(new Address(44L,"UP"));

        List<Employee> employeeList = Arrays.asList(new Employee(1L,"Ram","Ji","Raghu",listOfAddressKrishna,Collections.emptyList()));

        Mockito.when(employeeService.getByAddress("Vrindavan")).thenReturn(employeeList);

        ResponseEntity<List<Employee>> responseEntity = employeeController.getByAddress("Vrindavan");

        Assertions.assertEquals(1L,responseEntity.getBody().get(0).getEmpId());
        Assertions.assertEquals(1,responseEntity.getBody().size());
        Assertions.assertEquals("Ram",responseEntity.getBody().get(0).getFirstName());
        Assertions.assertEquals("Vrindavan",responseEntity.getBody().get(0).getAddresses().get(0).getCity());
        Assertions.assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
    }
    @Test
    @DisplayName("test to illustrate get Employees by address city not present")
    void getEmployeeByAddress_IsNotPresent(){

        Mockito.when(employeeService.getByAddress("multai")).thenReturn(Collections.emptyList());

        ResponseEntity<List<Employee>> responseEntity = employeeController.getByAddress("multai");

        Assertions.assertEquals(Collections.emptyList(),responseEntity.getBody());
    }
    @Test
    @DisplayName("Test is for export data in excel")
    void exportToExcel() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("Test Data".getBytes());

        Mockito.when(employeeService.getAllInExcel()).thenReturn(byteArrayInputStream);

        ResponseEntity<Resource> resourceResponseEntity = employeeController.exportToExcel();

        // Verify that the getAllInExcel method is called on the mocked employeeService
        Mockito.verify(employeeService,Mockito.times(1)).getAllInExcel();

        //Assert the response details
        Assertions.assertEquals(HttpStatus.OK, resourceResponseEntity.getStatusCode());
        Assertions.assertEquals("attachment; filename=EmployeesExcelData", resourceResponseEntity.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0));
        Assertions.assertEquals(MediaType.parseMediaType("application/vnd.ms-excel"),resourceResponseEntity.getHeaders().getContentType());

        //Assert the content of the InputStreamResource
        InputStreamResource file = (InputStreamResource) resourceResponseEntity.getBody();
        InputStream actualInputStream = file.getInputStream();
        byte[] actualBytes = new byte[byteArrayInputStream.available()];
        actualInputStream.read(actualBytes);

        Assertions.assertEquals("Test Data",new String(actualBytes));
    }
}