package com.product.springboot.inventory.springboot2jpacrudexample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import com.product.springboot.inventory.Application;
import com.product.springboot.inventory.model.Product;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIntegrationTest {
	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void contextLoads() {

	}

	@Test
	@Ignore
	public void testGetAllProducts() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/employees",
				HttpMethod.GET, entity, String.class);
		
		assertNotNull(response.getBody());
	}

	@Test
	@Ignore
	public void testGetProductById() {
		Product product = restTemplate.getForObject(getRootUrl() + "/products/1", Product.class);
		System.out.println(product.getProductName());
		assertNotNull(product);
	}

	@Test
	@Ignore
	public void testCreateProduct() {
		Product product = new Product();
		product.setProductId("a45");
		product.setProductName("aircraft");
		product.setCategory("dsfdf");
		product.setDescription("dsfjhs jkhfsk");
		product.setUnits(4);

		ResponseEntity<Product> postResponse = restTemplate.postForEntity(getRootUrl() + "/product", product, Product.class);
		assertNotNull(postResponse);
		assertNotNull(postResponse.getBody());
	}

	@Test
	@Ignore
	public void testUpdateProduct() {
		int id = 1;
		Product product = restTemplate.getForObject(getRootUrl() + "/products/" + id, Product.class);
		product.setProductId("a454");
		product.setProductName("aircraf4t");
		product.setCategory("dsfd4f");
		product.setDescription("dsfjh4s jkhfsk");
		product.setUnits(5);

		restTemplate.put(getRootUrl() + "/products/" + id, product);

		Product updatedProduct = restTemplate.getForObject(getRootUrl() + "/products/" + id, Product.class);
		assertNotNull(updatedProduct);
	}

	@Test
	@Ignore
	public void testDeleteProduct() {
		int id = 2;
		Product product = restTemplate.getForObject(getRootUrl() + "/products/" + id, Product.class);
		assertNotNull(product);

		restTemplate.delete(getRootUrl() + "/products/" + id);

		try {
			product = restTemplate.getForObject(getRootUrl() + "/products/" + id, Product.class);
		} catch (final HttpClientErrorException e) {
			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}
}
