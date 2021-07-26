package com.product.springboot.inventory.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.product.springboot.inventory.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, Long>{

	//@Query("{'category' : ?0}")
	@Query(value = "{'category': {$regex : ?0, $options: 'i'}}")
	List<Product> findProductsByCategory(String category);
}
