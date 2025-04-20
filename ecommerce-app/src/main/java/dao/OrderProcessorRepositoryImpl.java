package dao;

import entity.Customer;
import entity.Order;
import entity.Product;
import util.DBConnUtil;

import java.sql.*;
import java.util.*;

public class OrderProcessorRepositoryImpl implements OrderProcessorRepository {

    public boolean createCustomer(Customer customer) {
        String sql = "INSERT INTO customer (name, email, password) VALUES (?, ?, ?)";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPassword());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createProduct(Product product) {
        String sql = "INSERT INTO product (name, price, description, stock) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setString(3, product.getDescription());
            pstmt.setInt(4, product.getStock());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM product WHERE product_id = ?";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM customer WHERE customer_id = ?";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addToCart(Customer customer, Product product, int quantity) {
        String sql = "INSERT INTO cart (customer_id, product_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customer.getCustomerId());
            pstmt.setInt(2, product.getProductId());
            pstmt.setInt(3, quantity);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeFromCart(Customer customer, Product product) {
        String sql = "DELETE FROM cart WHERE customer_id = ? AND product_id = ?";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customer.getCustomerId());
            pstmt.setInt(2, product.getProductId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<Product, Integer>> getAllFromCart(Customer customer) {
        List<Map<Product, Integer>> cartItems = new ArrayList<>();
        String sql = "SELECT p.*, c.quantity FROM cart c JOIN product p ON c.product_id = p.product_id WHERE c.customer_id = ?";

        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customer.getCustomerId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getInt("stock")
                );
                Map<Product, Integer> map = new HashMap<>();
                map.put(product, rs.getInt("quantity"));
                cartItems.add(map);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cartItems;
    }

    public List<Order> getAllOrders(int customerId) {
        // Stubbed method to avoid abstract type error
        return new ArrayList<>(); // Implement based on your Order table schema if needed
    }

    public boolean placeOrder(Customer customer, List<Map<Product, Integer>> cartItems, String shippingAddress) {
        String insertOrder = "INSERT INTO order_table (customer_id, order_date, total_price, shipping_address) VALUES (?, CURDATE(), ?, ?)";
        String insertOrderItem = "INSERT INTO order_item (order_id, product_id, quantity) VALUES (?, ?, ?)";

        double total = 0;
        for (Map<Product, Integer> map : cartItems) {
            for (Product p : map.keySet()) {
                total += p.getPrice() * map.get(p);
            }
        }

        try (Connection conn = DBConnUtil.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement pstmtOrder = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
            pstmtOrder.setInt(1, customer.getCustomerId());
            pstmtOrder.setDouble(2, total);
            pstmtOrder.setString(3, shippingAddress);
            pstmtOrder.executeUpdate();

            ResultSet rs = pstmtOrder.getGeneratedKeys();
            int orderId = -1;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            PreparedStatement pstmtOrderItem = conn.prepareStatement(insertOrderItem);
            for (Map<Product, Integer> map : cartItems) {
                for (Product p : map.keySet()) {
                    pstmtOrderItem.setInt(1, orderId);
                    pstmtOrderItem.setInt(2, p.getProductId());
                    pstmtOrderItem.setInt(3, map.get(p));
                    pstmtOrderItem.addBatch();
                }
            }

            pstmtOrderItem.executeBatch();
            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<Product, Integer>> getOrdersByCustomer(int customerId) {
        List<Map<Product, Integer>> orderList = new ArrayList<>();
        String sql = "SELECT p.*, oi.quantity FROM order_item oi JOIN product p ON oi.product_id = p.product_id JOIN order_table o ON oi.order_id = o.order_id WHERE o.customer_id = ?";

        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getInt("stock")
                );
                Map<Product, Integer> map = new HashMap<>();
                map.put(product, rs.getInt("quantity"));
                orderList.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderList;
    }

}
