package testcases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import dao.OrderProcessorRepositoryImpl;
import entity.Customer;
import entity.Product;
import java.util.*;
import exception.ProductNotFoundException;
import exception.UserNotFoundException;

class OrderProcessorRepositoryImplTest {
	private OrderProcessorRepositoryImpl orderProcessor=new OrderProcessorRepositoryImpl();
	
	
	@Test
	void testCreateProduct() {
		  Product product = new Product(109, "Laptop4", 1500.0, "Gaming Laptop", 10);
        boolean result = orderProcessor.createProduct(product);
        assertTrue(result, "Product should be created successfully.");
	}
	
	@Test
	void testCreateCustomer() {
		Customer cust=new Customer(110,"johndoe2", "password@111", "User");
		boolean iscustcreated= orderProcessor.createCustomer(cust);
		 assertTrue(iscustcreated, "User should be created successfully.");
		
	}
	
	@Test
	void testaddToCart() {
		Product product = new Product(105, "Laptop2", 1500.0, "Gaming Laptop", 10);
		Customer cust=new Customer(106,"johndoe", "password@12345", "User");
		boolean result= orderProcessor.addToCart(cust, product, 1);
		assertTrue(result, "Product is successfully added to cart");
	}
	@Test
	void testPlaceOrder() {
		Customer cust=new Customer(106,"johndoe", "password@12345", "User");
		Product product = new Product(105, "Laptop2", 1500.0, "Gaming Laptop", 10);
		List<Map<Product, Integer>> products = new ArrayList<>();
        Map<Product, Integer> productMap = new HashMap<>();
        productMap.put(product, 2);
        products.add(productMap);
        
        boolean result = orderProcessor.placeOrder(cust, products, "123 Main St1");
        assertTrue(result, "Order should be placed successfully.");
	}
	
	@Test
	void testdeleteproduct()
	{
		 assertThrows(ProductNotFoundException.class, () -> {
	            orderProcessor.deleteProduct(104);
	        }, "Exception should be thrown when product ID is invalid.");
	}
	
	@Test
	void testdeletecustomer()
	{
		 assertThrows(UserNotFoundException.class, () -> {
	            orderProcessor.deleteCustomer(104);
	        }, "Exception should be thrown when product ID is invalid.");
	
	}

	

}


