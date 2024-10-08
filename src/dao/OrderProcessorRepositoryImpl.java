package dao;

import entity.Customer;
import entity.Product;
//import entity.Order;
import exception.ProductNotFoundException;
import exception.UserNotFoundException;
//import exception.OrderNotFoundException;
import util.DBConnUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrderProcessorRepositoryImpl implements OrderProcessorRepository {

	
	@Override
	public List<Customer> viewallCustomer() {
	    List<Customer> customers = new ArrayList<>();
	    String query = "Select * from customers";

	    try (Connection connection = DBConnUtil.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query);
	         ResultSet resultSet = preparedStatement.executeQuery()) {

	        while (resultSet.next()) {
	            Customer customer = new Customer(
	                resultSet.getInt("customer_id"),
	                resultSet.getString("name"),
	                resultSet.getString("email"),
	                resultSet.getString("password")
	            );
	            customers.add(customer);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return customers;
	}

	@Override
	public List<Product> viewallProducts() {
	    List<Product> products = new ArrayList<>();
	    String query = "Select * from products";

	    try (Connection connection = DBConnUtil.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query);
	         ResultSet resultSet = preparedStatement.executeQuery()) {

	        while (resultSet.next()) {
	            Product product = new Product(
	                resultSet.getInt("product_id"),
	                resultSet.getString("name"),
	                resultSet.getDouble("price"),
	                resultSet.getString("description"),
	                resultSet.getInt("stockQuantity")
	            );
	            products.add(product);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return products;
	}

	
    @Override
    public boolean createProduct(Product product) {
        try (Connection connection = DBConnUtil.getConnection()) {
            String query = "Insert into products (product_id,name, price, description, stockQuantity) values (?,?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, product.getProductId());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setString(4, product.getDescription());
            preparedStatement.setInt(5, product.getStockQuantity());

            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createCustomer(Customer customer) {
        try (Connection connection = DBConnUtil.getConnection()) {
            String query = "Insert into customers (customer_id,name, email, password) values (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customer.getCustomerId());
            preparedStatement.setString(2, customer.getName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getPassword());

            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteProduct(int productId) throws ProductNotFoundException{
        try (Connection connection = DBConnUtil.getConnection()) {
        	

            String query = "Delete from products where product_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, productId);

            int result = preparedStatement.executeUpdate();
            
            if (result == 0) {
                throw new ProductNotFoundException("Product with ID " + productId + " not found.");
            }
            
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCustomer(int customerId) throws UserNotFoundException{
        try (Connection connection = DBConnUtil.getConnection()) {
        	

            String query = "Delete from customers where customer_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customerId);

            int result = preparedStatement.executeUpdate();
            
            if (result == 0) {
                throw new UserNotFoundException("Customer with ID " + customerId + " not found.");
            }
            
            return result > 0;
        } 
        catch ( SQLException e) {
             e.printStackTrace();
        	return false;
        }
    }

    
    @Override
    public boolean addToCart(Customer customer, Product product, int quantity) {
        try (Connection connection = DBConnUtil.getConnection()) {
           
            String checkQuery = "Select quantity from cart where customer_id = ? and product_id = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setInt(1, customer.getCustomerId());
            checkStatement.setInt(2, product.getProductId());

            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
               
                int existingQuantity = resultSet.getInt("quantity");

                String updateQuery = "Update cart set quantity = ? where customer_id = ? and product_id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setInt(1, existingQuantity + quantity); // Add the new quantity to the existing quantity
                updateStatement.setInt(2, customer.getCustomerId());
                updateStatement.setInt(3, product.getProductId());

                int result = updateStatement.executeUpdate();
                return result > 0;
            } else {
              
                String insertQuery = "Insert into cart (customer_id, product_id, quantity) values (?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, customer.getCustomerId());
                insertStatement.setInt(2, product.getProductId());
                insertStatement.setInt(3, quantity);

                int result = insertStatement.executeUpdate();
                return result > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean removeFromCart(Customer customer, Product product) {
        try (Connection connection = DBConnUtil.getConnection()) {
           
            String checkQuantityQuery = "Select quantity from cart where customer_id = ? and product_id = ?";
            PreparedStatement checkQuantityStatement = connection.prepareStatement(checkQuantityQuery);
            checkQuantityStatement.setInt(1, customer.getCustomerId());
            checkQuantityStatement.setInt(2, product.getProductId());
            
            ResultSet resultSet = checkQuantityStatement.executeQuery();
            
           
            if (resultSet.next()) {
                int currentQuantity = resultSet.getInt("quantity");
                
                if (currentQuantity > 1) {
                   
                    String updateQuantityQuery = "Update cart set quantity = quantity - 1 where customer_id = ? and product_id = ?";
                    PreparedStatement updateQuantityStatement = connection.prepareStatement(updateQuantityQuery);
                    updateQuantityStatement.setInt(1, customer.getCustomerId());
                    updateQuantityStatement.setInt(2, product.getProductId());
                    
                    int updateResult = updateQuantityStatement.executeUpdate();
                    return updateResult > 0;
                    
                } else {
                 
                    String deleteProductQuery = "DELETE FROM cart WHERE customer_id = ? AND product_id = ?";
                    PreparedStatement deleteProductStatement = connection.prepareStatement(deleteProductQuery);
                    deleteProductStatement.setInt(1, customer.getCustomerId());
                    deleteProductStatement.setInt(2, product.getProductId());
                    
                    int deleteResult = deleteProductStatement.executeUpdate();
                    return deleteResult > 0;
                }
            } else {
            
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("Error " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    
    @Override
    public List<Product> getAllFromCart(Customer customer) {
        List<Product> cartProducts = new ArrayList<>();
        
       
        try (Connection connection = DBConnUtil.getConnection()) {
            
         
            String query = "SELECT p.product_id, p.name, p.price, p.description, c.quantity " +
                           "FROM products p " +
                           "INNER JOIN cart c ON p.product_id = c.product_id " +
                           "WHERE c.customer_id = ?";
            
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customer.getCustomerId()); 
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // Iterate over the result set and create product objects
            while (resultSet.next()) {
                Product product = new Product(
                    resultSet.getInt("product_id"),       
                    resultSet.getString("name"),           
                    resultSet.getDouble("price"),         
                    resultSet.getString("description"),  
                    resultSet.getInt("quantity")       
                );
                cartProducts.add(product);
            }
            
        } catch (SQLException e) {
         
            System.err.println("Error while fetching products from the cart: " + e.getMessage());
            e.printStackTrace();
        }
        
        return cartProducts;
    }


    
    @Override
    public boolean placeOrder(Customer customer, List<Map<Product, Integer>> products, String shippingAddress) {
        try (Connection connection = DBConnUtil.getConnection()) {
            connection.setAutoCommit(false);

            // Insert into orders table
            String orderQuery = "Insert into orders (customer_id, order_date, total_price, shipping_address) values (?, NOW(), ?, ?)";
            PreparedStatement orderStatement = connection.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS);
            double totalPrice = calculateTotalPrice(products);
            orderStatement.setInt(1, customer.getCustomerId());
            orderStatement.setDouble(2, totalPrice);
            orderStatement.setString(3, shippingAddress);

            int result = orderStatement.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = orderStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);

                  
                    for (Map<Product, Integer> entry : products) {
                        for (Map.Entry<Product, Integer> productEntry : entry.entrySet()) {
                            Product product = productEntry.getKey();
                            int quantity = productEntry.getValue();
                            String orderItemQuery = "Insert into order_items (order_id, product_id, quantity) values (?, ?, ?)";
                            PreparedStatement orderItemStatement = connection.prepareStatement(orderItemQuery);
                            orderItemStatement.setInt(1, orderId);
                            orderItemStatement.setInt(2, product.getProductId());
                            orderItemStatement.setInt(3, quantity);
                            orderItemStatement.executeUpdate();
                        }
                    }

               
                    String clearCartQuery = "DELETE FROM cart WHERE customer_id = ?";
                    PreparedStatement clearCartStatement = connection.prepareStatement(clearCartQuery);
                    clearCartStatement.setInt(1, customer.getCustomerId());
                    clearCartStatement.executeUpdate();

              
                    connection.commit();
                    return true;
                }
            }

            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


   
    @Override
    public List<Map<Product, Integer>> getOrdersByCustomer(int customerId) {
        List<Map<Product, Integer>> orders = new ArrayList<>();
        try (Connection connection = DBConnUtil.getConnection()) {
            String query = "SELECT o.order_id, p.product_id, p.name, oi.quantity FROM orders o " +
                           "JOIN order_items oi ON o.order_id = oi.order_id " +
                           "JOIN products p ON p.product_id = oi.product_id " +
                           "WHERE o.customer_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customerId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");

                Product product = new Product();
                product.setProductId(productId);
                product.setName(productName);

                Map<Product, Integer> orderItems = new HashMap<>();
                orderItems.put(product, quantity);
                orders.add(orderItems);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

   
    private double calculateTotalPrice(List<Map<Product, Integer>> products) {
        double totalPrice = 0.0;
        for (Map<Product, Integer> entry : products) {
            for (Map.Entry<Product, Integer> productEntry : entry.entrySet()) {
                Product product = productEntry.getKey();
                int quantity = productEntry.getValue();
                totalPrice += product.getPrice() * quantity;
            }
        }
        return totalPrice;
    }
}
