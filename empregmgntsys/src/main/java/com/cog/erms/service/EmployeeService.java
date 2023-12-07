package com.cog.erms.service;

import com.cog.erms.exception.EmployeeNotFoundException;
import com.cog.erms.helper.ExcelHelper;
import com.cog.erms.model.Address;
import com.cog.erms.model.Employee;
import com.cog.erms.repo.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    public List<Employee> save(List<Employee> employee){
        List<Employee> employees = employeeRepository.saveAll(employee);
        return employees;
    }
    public Employee save(Employee employee){
         Employee semployee = employeeRepository.save(employee);
        return semployee;
    }
    public Employee update(Employee employee){
        Employee returnedEmployee=null;
        if(employeeRepository.existsById(employee.getEmpId())){
            Employee updatedEmployee = employeeRepository.findByEmpId(employee.getEmpId());
            updatedEmployee.setEmpId(employee.getEmpId());
            updatedEmployee.setFirstName(employee.getFirstName());
            updatedEmployee.setLastName(employee.getLastName());
            updatedEmployee.setMiddleName(employee.getMiddleName());
            updatedEmployee.setAddresses(employee.getAddresses());
            updatedEmployee.setDesignations(employee.getDesignations());
             returnedEmployee=save(updatedEmployee);

        }else{
            throw new EmployeeNotFoundException("Employee Not Exist with ID "+employee.getEmpId());
        }
        return returnedEmployee;
    }
    @Cacheable("EmpFnameCache")
    public List<Employee> getByFirstName(String fname,Integer pageNumber, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Employee> byFirstNamePage = employeeRepository.findByFirstName(fname, pageable);
        List<Employee> pageSizeEmpListByFname = byFirstNamePage.getContent();
        System.out.println("************getByFirstName**************************");
        return pageSizeEmpListByFname;
    }
    @Cacheable("EmpAddCache")
    public List<Employee> getByAddress(String addresses){
        List<Employee> list = employeeRepository.findByAddressesCity(addresses);
        return list;
    }
    @Cacheable("EmpGetALlCache")
    public List<Employee> getAll(Integer pageNumber, Integer pageSize){
        Pageable p = PageRequest.of(pageNumber,pageSize);
        Page<Employee> allEmplist = employeeRepository.findAll(p);
        List<Employee> pageSizeEmplist = allEmplist.getContent();
        return pageSizeEmplist;
    }
    @Cacheable("EmpGetALlCache")
    public ByteArrayInputStream getAllInExcel() throws IOException {
        List<Employee> allEmp = employeeRepository.findAll();
        ByteArrayInputStream byteArrayInputStream = ExcelHelper.dataToExcel(allEmp);

        return byteArrayInputStream;
    }

}
