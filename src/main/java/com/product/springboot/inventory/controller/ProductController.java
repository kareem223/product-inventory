package com.product.springboot.inventory.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.product.springboot.inventory.exception.ResourceNotFoundException;
import com.product.springboot.inventory.model.Product;
import com.product.springboot.inventory.repository.ProductRepository;
import com.product.springboot.inventory.service.SequenceGeneratorService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v2")
public class ProductController {
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	@GetMapping("/products")
	public List<Product> getAllProducts(@RequestParam(required = false) String category) {
		if(category!=null) {
			return productRepository.findProductsByCategory(category);
		}
		return productRepository.findAll();
	}

	@Cacheable(value = "products", key = "#productobjId")
	@GetMapping("/products/{id}")
	public Product getProductById(@PathVariable(value = "id") Long productobjId)
			throws ResourceNotFoundException {
		Product product = productRepository.findById(productobjId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + productobjId));
		return product;
	}
	
//	@GetMapping("/products/{category}")
//	public ResponseEntity<List<Product>> getProductById(@PathVariable(value = "category") String category)
//			throws ResourceNotFoundException {
//		System.out.println("inside getProductById..."+category);
//		List<Product> product = productRepository.findProductsByCategory(category);
//		System.out.println("product:::"+product);
//		if(product.size()>0) {
//			return ResponseEntity.ok().body(product);
//		}
//		else {
//			new ResourceNotFoundException("Product not found for this category :: " + category);
//		}
//		return null;
//	}

	@PostMapping("/products")
	public Product createProduct(@Valid @RequestBody Product product) {
		product.setId(sequenceGeneratorService.generateSequence(Product.SEQUENCE_NAME));
		return productRepository.save(product);
	}

	@CachePut(value = "products", key = "#productobjId")
	@PutMapping("/products/{id}")
	public Product updateProduct(@PathVariable(value = "id") Long productobjId,
			@Valid @RequestBody Product productDetails) throws ResourceNotFoundException {
		Product product = productRepository.findById(productobjId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + productobjId));

		product.setProductId(productDetails.getProductId());
		product.setProductName(productDetails.getProductName());
		product.setDescription(productDetails.getDescription());
		product.setCategory(productDetails.getCategory());
		product.setUnits(productDetails.getUnits());
		final Product updatedProduct = productRepository.save(product);
		return updatedProduct;
	}

	@CacheEvict(value = "products", key = "#productobjId")
	@DeleteMapping("/products/{id}")
	public Map<String, Boolean> deleteProduct(@PathVariable(value = "id") Long productobjId)
			throws ResourceNotFoundException {
		Product product = productRepository.findById(productobjId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + productobjId));

		productRepository.delete(product);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
