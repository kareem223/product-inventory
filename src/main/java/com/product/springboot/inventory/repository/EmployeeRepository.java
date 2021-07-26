package com.product.springboot.inventory.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.product.springboot.inventory.model.Employee;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, Long>{

}
