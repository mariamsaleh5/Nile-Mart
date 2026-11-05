package org.route;

import java.util.Optional;
import java.util.List;
public interface ProductRepository {
    List<Product> findAll();

    Optional<Product> findBySku(String sku);

    void save(Product product);

}
