package dao;

import entity.Customer;
import entity.Product;
import java.util.List;
import java.util.Map;
import exception.ProductNotFoundException;
import exception.UserNotFoundException;


public interface OrderProcessorRepository {

	List<Product> viewallProducts();
    List<Customer> viewallCustomer();
    boolean createProduct(Product product);

    boolean createCustomer(Customer customer);

    boolean deleteProduct(int productId) throws ProductNotFoundException;

    boolean deleteCustomer(int customerId) throws UserNotFoundException ;

    boolean addToCart(Customer customer, Product product, int quantity);

    boolean removeFromCart(Customer customer, Product product);

    List<Product> getAllFromCart(Customer customer);

    boolean placeOrder(Customer customer, List<Map<Product, Integer>> products, String shippingAddress);

    List<Map<Product, Integer>> getOrdersByCustomer(int customerId);
}
