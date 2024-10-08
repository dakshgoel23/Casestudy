package main;

import dao.OrderProcessorRepository;
import dao.OrderProcessorRepositoryImpl;
import exception.UserNotFoundException;
import exception.ProductNotFoundException;
import entity.Customer;
import entity.Product;
//import util.DBConnUtil;
//import exception.ProductNotFoundException;

import java.util.*;

public class MainModule {
    
    // Simulating Admin Credentials
    private static final String ADMIN_EMAIL = "daksh@gmail.com";
    private static final String ADMIN_PASSWORD = "admin@123";
    
    // DAO for database interactions
    private static final OrderProcessorRepository orderProcessorRepo = new OrderProcessorRepositoryImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the E-commerce Application!");
        System.out.println("Are you an Admin or Customer?");
        System.out.println("1. Admin");
        System.out.println("2. Customer");

        int userType = scanner.nextInt();
        scanner.nextLine();  // consume newline character

        if (userType == 1) {
            handleAdmin();
        } else if (userType == 2) {
            handleCustomer();
        } else {
            System.out.println("Invalid choice. Please restart the application.");
        }
    }

    // Handle Admin functionality
    private static void handleAdmin() {
        System.out.println("Enter Admin Email: ");
        String email = scanner.nextLine();
        System.out.println("Enter Admin Password: ");
        String password = scanner.nextLine();

        // Admin credentials check
        if (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {
            System.out.println("Admin login successful!");
            adminMenu();
        } else {
            System.out.println("Invalid Admin credentials.");
        }
    }

    // Admin menu and functionality
    private static void adminMenu() {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. View all Products");
            System.out.println("2. View all Customers");
            System.out.println("3. Create Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Delete Customer");
            System.out.println("6. Get all Orders by Customer");
            System.out.println("7. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();  // consume newline character

            switch (choice) {
                case 1 -> viewallcustomers();
                case 2 -> viewallproducts();
                case 3 -> createProduct();
                case 4 -> deleteProduct();
                case 5 -> deleteCustomer();
                case 6 -> getAllOrdersByCustomer();
                case 7 -> {
                    System.out.println("Exiting Admin Menu.");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void viewallcustomers() {
        System.out.println("Fetching all customers...");

        // Get the list of all customers from the repository
        List<Customer> customers = orderProcessorRepo.viewallCustomer();

        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            System.out.println("List of Customers:");
            for (Customer customer : customers) {
                System.out.println("ID: " + customer.getCustomerId() +
                        ", Name: " + customer.getName() +
                        ", Email: " + customer.getEmail());
            }
        }
    }

    // Function to view all products
    private static void viewallproducts() {
        System.out.println("Fetching all products...");

        // Get the list of all products from the repository
        List<Product> products = orderProcessorRepo.viewallProducts();

        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            System.out.println("List of Products:");
            for (Product product : products) {
                System.out.println("ID: " + product.getProductId() +
                        ", Name: " + product.getName() +
                        ", Price: " + product.getPrice() +
                        ", Stock: " + product.getStockQuantity());
            }
        }
    }
    // Admin - Create a Product
    private static void createProduct() {
    	   System.out.println("Enter Product ID: ");
           int productid = scanner.nextInt();
           scanner.nextLine();
        System.out.println("Enter Product Name: ");
        String name = scanner.nextLine();
        System.out.println("Enter Product Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Product Description: ");
        String description = scanner.nextLine();
        System.out.println("Enter Stock Quantity: ");
        int stockQuantity = scanner.nextInt();
        scanner.nextLine();

        Product product = new Product(productid,name, price, description, stockQuantity);
        if (orderProcessorRepo.createProduct(product)) {
            System.out.println("Product created successfully!");
        } else {
            System.out.println("Failed to create product.");
        }
    }

    // Admin - Delete a Product
    private static void deleteProduct() {
        System.out.println("Enter Product ID to delete: ");
        int productId = scanner.nextInt();
        scanner.nextLine();
        try
        {
        if (orderProcessorRepo.deleteProduct(productId)) {
            System.out.println("Product deleted successfully!");
        } else {
            System.out.println("Failed to delete product. Check if the product exists.");
        }
        }
        catch(ProductNotFoundException e)
        {
        	System.out.println(e.getMessage());
        }
         
    }

    // Admin - Delete a Customer
    private static void deleteCustomer() {
       
        System.out.println("Enter Customer ID to delete: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();
        try
        {
        if (orderProcessorRepo.deleteCustomer(customerId)) {
            System.out.println("Customer deleted successfully!");
        } else {
            System.out.println("Failed to delete customer. Check if the customer exists.");
        }
        }
        catch(UserNotFoundException e)
        {
        	System.out.println(e.getMessage());
        }
    }

    // Admin - Get all Orders by Customer ID
    private static void getAllOrdersByCustomer() {
        System.out.println("Enter Customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();

        var orders = orderProcessorRepo.getOrdersByCustomer(customerId);
        if (orders.isEmpty()) {
            System.out.println("No orders found for this customer.");
        } else {
            System.out.println("Orders for Customer ID: " + customerId);
            orders.forEach(order -> order.forEach((product, quantity) -> 
                System.out.println("Product: " + product.getName() + ", Quantity: " + quantity)));
        }
    }

    // Handle Customer functionality
    private static void handleCustomer() {
        System.out.println("1. New Customer");
        System.out.println("2. Existing Customer");

        int customerChoice = scanner.nextInt();
        scanner.nextLine();  // consume newline character

        if (customerChoice == 1) {
            registerCustomer();
        } else if (customerChoice == 2) {
            customerLogin();
        } else {
            System.out.println("Invalid choice.");
        }
    }

    // Register a new customer
    private static void registerCustomer() {
    	 System.out.println("Enter ID: ");
         int customerid = scanner.nextInt();
         scanner.nextLine();
    	System.out.println("Enter Name: ");
        String name = scanner.nextLine();
        System.out.println("Enter Email: ");
        String email = scanner.nextLine();
        System.out.println("Enter Password: ");
        String password = scanner.nextLine();

        Customer customer = new Customer(customerid,name, email, password);
        if (orderProcessorRepo.createCustomer(customer)) {
            System.out.println("Customer registered successfully!");
            customerMenu(customer);
        } else {
            System.out.println("Failed to register customer.");
        }
    }

    // Handle existing customer login and menu
    private static void customerLogin() {
        System.out.println("Enter Customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();

        // Assuming customer lookup from DB (just a mock here)
        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        customerMenu(customer);
    }

    // Customer menu and functionality
    private static void customerMenu(Customer customer) {
        while (true) {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. Add to Cart");
            System.out.println("2. Remove from Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Place Order");
            System.out.println("5. View Orders");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();  // consume newline character

            switch (choice) {
                case 1 -> addToCart(customer);
                case 2 -> removeFromCart(customer);
                case 3 -> viewCart(customer);
                case 4 -> placeOrder(customer);
                case 5 -> viewOrders(customer);
                case 6 -> {
                    System.out.println("Exiting Customer Menu.");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
    
   

    // Customer - Add product to cart
    private static void addToCart(Customer customer) {
        System.out.println("Enter Product ID: ");
        int productId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        Product product = new Product();
        product.setProductId(productId);

        if (orderProcessorRepo.addToCart(customer, product, quantity)) {
            System.out.println("Product added to cart!");
        } else {
            System.out.println("Failed to add product to cart.");
        }
    }

    // Customer - Remove product from cart
    private static void removeFromCart(Customer customer) {
        System.out.println("Enter Product ID to remove: ");
        int productId = scanner.nextInt();
        scanner.nextLine();

        Product product = new Product();
        product.setProductId(productId);

        if (orderProcessorRepo.removeFromCart(customer, product)) {
            System.out.println("Product removed from cart!");
        } else {
            System.out.println("Failed to remove product from cart.");
        }
    }

    // Customer - View all products in cart
    private static void viewCart(Customer customer) {
        var cartProducts = orderProcessorRepo.getAllFromCart(customer);
        if (cartProducts.isEmpty()) {
            System.out.println("Cart is empty.");
        } else {
            System.out.println("Products in Cart:");
            cartProducts.forEach(product -> System.out.println(product.getName() + " - " + product.getPrice() + " - " + product.getStockQuantity()));
        }
    }

    // Customer - Place order
    private static void placeOrder(Customer customer) {
        var cartProducts = orderProcessorRepo.getAllFromCart(customer);
        if (cartProducts.isEmpty()) {
            System.out.println("Cart is empty. Cannot place order.");
            return;
        }

        System.out.println("Enter Shipping Address: ");
        String shippingAddress = scanner.nextLine();

        List<Map<Product, Integer>> orderItems = new ArrayList<>();
        cartProducts.forEach(product -> {
            Map<Product, Integer> map = new HashMap<>();
            map.put(product, 1);  // Assuming quantity = 1 for simplicity
            orderItems.add(map);
        });

        if (orderProcessorRepo.placeOrder(customer, orderItems, shippingAddress)) {
            System.out.println("Order placed successfully!");
        } else {
            System.out.println("Failed to place order.");
        }
    }

    // Customer - View Orders
    private static void viewOrders(Customer customer) {
        var orders = orderProcessorRepo.getOrdersByCustomer(customer.getCustomerId());
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            System.out.println("Orders for Customer:");
            orders.forEach(order -> order.forEach((product, quantity) -> 
                System.out.println("Product: " + product.getName() + ", Quantity: " + quantity)));
        }
    }
}
