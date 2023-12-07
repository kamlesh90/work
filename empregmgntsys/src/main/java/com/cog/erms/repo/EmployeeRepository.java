package com.cog.erms.repo;

import com.cog.erms.model.Address;
import com.cog.erms.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findByFirstName(String firstName, Pageable pageable);
    List<Employee> findByAddressesCity(@Param("city") String city);
}
