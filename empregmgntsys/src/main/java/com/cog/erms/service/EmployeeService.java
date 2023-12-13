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
import java.util.Optional;
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
    public Employee update(Employee employee, Long employeeId){
            Optional<Employee> updatedEmployeeOpt = employeeRepository.findById(employeeId);
            Employee updatedEmployee = null;
            if(updatedEmployeeOpt.isPresent()){
                Employee returnedEmployee = updatedEmployeeOpt.get();

                returnedEmployee.setFirstName(employee.getFirstName());
                returnedEmployee.setLastName(employee.getLastName());
                returnedEmployee.setMiddleName(employee.getMiddleName());
                returnedEmployee.setAddresses(employee.getAddresses());
                returnedEmployee.setDesignations(employee.getDesignations());

                updatedEmployee  = save(returnedEmployee);
            }
            else {
                throw new EmployeeNotFoundException("employee not exist with ID : "+employeeId);
            }
            return updatedEmployee;
    }
    public Employee updateEmployeeByLastName(String lname,Long employeeId){
        Employee updatedEmpByLName=null;
        Optional<Employee> opt = employeeRepository.findById(employeeId);
        if(opt.isPresent()){
            updatedEmpByLName = opt.get();
            updatedEmpByLName.setLastName(lname);

            updatedEmpByLName = employeeRepository.save(updatedEmpByLName);
        }
        return updatedEmpByLName;
    }
    @Cacheable("EmpFnameCache")
    public List<Employee> getByFirstName(String fname,Integer pageNumber, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Employee> byFirstNamePage = employeeRepository.findByFirstName(fname, pageable);
        List<Employee> pageSizeEmpListByFname = byFirstNamePage.getContent();
        System.out.println("************getByFirstName**************************");
        return pageSizeEmpListByFname;
    }
    public Employee getEmpById(Long id){
        Optional<Employee> byId = employeeRepository.findById(id);
        //return  byId.isPresent() ? byId.get() : byId.orElse(null);
        return byId.get();
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

