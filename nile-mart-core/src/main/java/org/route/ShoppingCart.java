package org.route;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
@Component
@Scope("prototype")
@Getter
@Setter
public class ShoppingCart {

    private final ProductService productService;
    private final int maxQuantityPerProduct;

    private int cartNumber;
    private Customer customer;

    private final Map<String, Integer> lines = new HashMap<>();

    public ShoppingCart(
            ProductService productService,
            @Value("${store.max-quantity-per-product}") int maxQuantityPerProduct) {

        this.productService = productService;
        this.maxQuantityPerProduct = maxQuantityPerProduct;
    }
    public void addItem(String sku, int qty) {

        // CART-01: Quantity must be positive
        if (qty <= 0) {
            throw new StoreException("Quantity must be greater than zero.");
        }

        // Find the product
        Product product = productService.findBySku(sku);

        // CART-02: Product must be active and in stock
        if (!product.isActive() || product.getStockQuantity() <= 0) {
            throw new StoreException("Product is not available.");
        }

        // Get current quantity in cart
        int currentQuantity = lines.getOrDefault(sku, 0);

        // Calculate the new total quantity
        int newQuantity = currentQuantity + qty;

        // CART-03: Check quantity limit and available stock
        int allowedQuantity = Math.min(
                maxQuantityPerProduct,
                product.getStockQuantity()
        );

        if (newQuantity > allowedQuantity) {
            throw new StoreException(
                    "Maximum allowed quantity is " + allowedQuantity
            );
        }

        // Add or update product quantity
        lines.put(sku, newQuantity);
    }
    public BigDecimal subtotal() {

        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<String, Integer> entry : lines.entrySet()) {

            String sku = entry.getKey();
            int qty = entry.getValue();

            total = total.add(productService.getPrice(sku, qty));
        }

        return total;
    }
    public boolean isEmpty() {
        return lines.isEmpty();
    }
    public void clear() {
        lines.clear();
    }
}