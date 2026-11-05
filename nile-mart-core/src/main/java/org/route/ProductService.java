package org.route;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final PricingService pricingService;
    public List<Product> findAll() {
        return repository.findAll();
    }
    public Product findBySku(String sku) {
        return repository.findBySku(sku)
                .orElseThrow(() -> new StoreException("Product not found: " + sku));
    }
    public void reduceStock(String sku, int qty) {

        Product product = findBySku(sku);

        if (qty <= 0) {
            throw new StoreException("Quantity must be greater than zero.");
        }

        if (product.getStockQuantity() < qty) {
            throw new StoreException("Insufficient stock.");
        }

        product.setStockQuantity(product.getStockQuantity() - qty);

        repository.save(product);
    }

    public BigDecimal getPrice(String sku, int qty) {

        Product product = findBySku(sku);

        if (!product.isActive()) {
            throw new StoreException("Product isn't available.");
        }

        return pricingService.getPrice(product, qty);
    }
}
