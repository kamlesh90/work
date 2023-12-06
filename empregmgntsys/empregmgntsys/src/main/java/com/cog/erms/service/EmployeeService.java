package com.cog.erms.service;

import com.cog.erms.exception.EmployeeNotFoundException;
import com.cog.erms.model.Address;
import com.cog.erms.model.Employee;
import com.cog.erms.repo.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        Employee updatedEmployee=null;
        if(employeeRepository.existsById(employee.getEmpId())){
            updatedEmployee=save(employee);
        }else{
            throw new EmployeeNotFoundException("Employee Not Exist with ID "+employee.getEmpId());
        }
        return updatedEmployee;
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

}
