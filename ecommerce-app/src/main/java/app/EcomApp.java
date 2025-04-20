package app;

import dao.OrderProcessorRepositoryImpl;
import entity.Customer;
import entity.Product;

import java.util.*;



public class EcomApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OrderProcessorRepositoryImpl repository = new OrderProcessorRepositoryImpl();

        System.out.println("Welcome to the E-Commerce App");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Create Customer");
            System.out.println("2. Create Product");
            System.out.println("3. Delete Product");
            System.out.println("4. Delete Customer");
            System.out.println("5. Add to Cart");
            System.out.println("6. Place Order");
            System.out.println("7. View Orders by Customer");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    scanner.nextLine();
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    Customer customer = new Customer(0, name, email, password);
                    boolean created = repository.createCustomer(customer);
                    System.out.println(created ? "Customer created!" : "Error creating customer.");
                    break;

                case 2:
                    scanner.nextLine();
                    System.out.print("Product Name: ");
                    String productName = scanner.nextLine();
                    System.out.print("Price: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Description: ");
                    String desc = scanner.nextLine();
                    System.out.print("Stock: ");
                    int stock = scanner.nextInt();
                    Product product = new Product(0, productName, price, desc, stock);
                    boolean productCreated = repository.createProduct(product);
                    System.out.println(productCreated ? "Product created!" : "Error creating product.");
                    break;

                case 3:
                    System.out.print("Enter Product ID to delete: ");
                    int prodId = scanner.nextInt();
                    boolean deleted = repository.deleteProduct(prodId);
                    System.out.println(deleted ? "Product deleted!" : "Error deleting product.");
                    break;

                case 4:
                    System.out.print("Enter Customer ID to delete: ");
                    int custId = scanner.nextInt();
                    boolean custDeleted = repository.deleteCustomer(custId);
                    System.out.println(custDeleted ? "Customer deleted!" : "Error deleting customer.");
                    break;

                case 5:
                    System.out.print("Enter Customer ID: ");
                    int custID = scanner.nextInt();
                    System.out.print("Enter Product ID: ");
                    int productID = scanner.nextInt();
                    System.out.print("Enter Quantity: ");
                    int qty = scanner.nextInt();
                    Customer cartCustomer = new Customer();
                    cartCustomer.setCustomerId(custID);
                    Product cartProduct = new Product();

                    cartProduct.setProductId(productID);
                    boolean added = repository.addToCart(cartCustomer, cartProduct, qty);
                    System.out.println(added ? "Added to cart!" : "Error adding to cart.");
                    break;

                case 6:
                    System.out.print("Enter Customer ID: ");
                    int placeCustId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Shipping Address: ");
                    String address = scanner.nextLine();
                    Customer placingCustomer = new Customer();
                    placingCustomer.setCustomerId(placeCustId);
                    List<Map<Product, Integer>> cartItems = repository.getAllFromCart(placingCustomer);
                    boolean orderPlaced = repository.placeOrder(placingCustomer, cartItems, address);
                    System.out.println(orderPlaced ? "Order placed successfully!" : "Error placing order.");
                    break;

                case 7:
                    System.out.print("Enter Customer ID: ");
                    int viewCustId = scanner.nextInt();
                    List<Map<Product, Integer>> orders = repository.getOrdersByCustomer(viewCustId);
                    for (Map<Product, Integer> order : orders) {
                        for (Map.Entry<Product, Integer> entry : order.entrySet()) {
                            Product p = entry.getKey();
                            System.out.println("Product: " + p.getName() + ", Quantity: " + entry.getValue());
                        }
                    }
                    break;

                case 8:
                    System.out.println("Exiting app.");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
