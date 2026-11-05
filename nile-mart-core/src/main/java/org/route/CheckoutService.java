package org.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final ProductService productService;
    private final ShippingQuoteService shippingQuoteService;
    private final TaxCalculator taxCalculator;
    private final OrderRegistry orderRegistry;
    private final List<NotificationSender> notificationSenders;

    public Order placeOrder(ShoppingCart cart, Governorate governorate) {

        // Step 1: Check if cart is empty
        if (cart.isEmpty()) {
            throw new StoreException("Cannot checkout an empty cart.");
        }

        for (Map.Entry<String, Integer> entry : cart.getLines().entrySet()) {

            String sku = entry.getKey();
            int quantity = entry.getValue();

            Product product = productService.findBySku(sku);

            if (product.getStockQuantity() < quantity) {
                throw new StoreException("Insufficient stock for: " + sku);
            }
        }
        BigDecimal subtotal=cart.subtotal();
        BigDecimal vat=taxCalculator.calculate(subtotal);
        BigDecimal shippingFee = shippingQuoteService.quote(
                governorate,
                subtotal
        );
        BigDecimal total = subtotal
                .add(vat)
                .add(shippingFee);
        for (Map.Entry<String, Integer> entry : cart.getLines().entrySet()) {

            String sku = entry.getKey();
            int quantity = entry.getValue();

            productService.reduceStock(sku, quantity);
        }
        Order order = new Order(
                orderRegistry.createOrderNumber(),
                cart.getCustomer(),
                governorate,
                new HashMap<>(cart.getLines()),
                subtotal,
                vat,
                shippingFee,
                total
        );

        orderRegistry.save(order);
        String message = "Your order " + order.orderNumber()
                + " has been placed. Total: " + total + " EGP";

        for (NotificationSender sender : notificationSenders) {
            sender.send(cart.getCustomer(), message);
        }
        cart.clear();

        return order;
    }
}