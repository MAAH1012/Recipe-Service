package com.maahi.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maahi.recipe.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
