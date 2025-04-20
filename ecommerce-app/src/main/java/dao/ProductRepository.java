package dao;

import java.util.List;
import entity.Product;

public interface ProductRepository {
    List<Product> getAllProducts();
    Product getProductById(int id);
    void addProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(int id);
}
