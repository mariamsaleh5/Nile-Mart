package org.route;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

@Repository

public class InMemoryProductRepository implements ProductRepository{

    private final HashMap<String, Product> store = new HashMap<>();

    @PostConstruct
    void saveProducts() {
        Product p1 = Product.builder()
                .name("Smartphone X5 128GB")
                .sku("PH-X5-128")
                .price(new BigDecimal("16000.00"))
                .category("Electronics")
                .isActive(true)
                .stockQuantity(12)
                .build();

        Product p2 = Product.builder()
                .name("Fast Charger 20W")
                .sku("ACC-CHG-20W")
                .price(new BigDecimal("650.00"))
                .category("Electronics")
                .isActive(true)
                .stockQuantity(40)
                .build();

        Product p3 = Product.builder()
                .name("Wireless Earbuds")
                .sku("ACC-BUDS-6")
                .price(new BigDecimal("1100.00"))
                .category("Electronics")
                .isActive(true)
                .stockQuantity(8)
                .build();

        Product p4 = Product.builder()
                .name("Cotton Galabeya")
                .sku("FSH-GAL-001")
                .price(new BigDecimal("900.00"))
                .category("Fashion")
                .isActive(true)
                .stockQuantity(25)
                .build();

        Product p5 = Product.builder()
                .name("Non-stick Frying Pan 28cm")
                .sku("HOM-PAN-28")
                .price(new BigDecimal("1200.00"))
                .category("Home & Kitchen")
                .isActive(true)
                .stockQuantity(3)
                .build();

        Product p6 = Product.builder()
                .name("Wooden Spice Rack")
                .sku("HOM-SPC-RCK")
                .price(new BigDecimal("750.00"))
                .category("Home & Kitchen")
                .isActive(true)
                .stockQuantity(10)
                .build();

        Product p7 = Product.builder()
                .name("Sun Cream SPF50")
                .sku("BTY-SUN-50")
                .price(new BigDecimal("350.00"))
                .category("Beauty")
                .isActive(true)
                .stockQuantity(0)
                .build();

        Product p8 = Product.builder()
                .name("The Cairo Trilogy")
                .sku("BK-CAI-TRI")
                .price(new BigDecimal("450.00"))
                .category("Books")
                .isActive(true)
                .stockQuantity(15)
                .build();

        store.put(p1.getSku(), p1);
        store.put(p2.getSku(), p2);
        store.put(p3.getSku(), p3);
        store.put(p4.getSku(), p4);
        store.put(p5.getSku(), p5);
        store.put(p6.getSku(), p6);
        store.put(p7.getSku(), p7);
        store.put(p8.getSku(), p8);
        System.out.println("8 products loaded successfully.");

    }
    @Override
    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }
    @Override
    public Optional<Product> findBySku(String sku) {
        return Optional.ofNullable(store.get(sku));
    }
    @Override
    public void save(Product product) {
        store.put(product.getSku(), product);
    }

    @PreDestroy
    public void closeCatalog() {
        System.out.println("Catalog closed");
    }

}
