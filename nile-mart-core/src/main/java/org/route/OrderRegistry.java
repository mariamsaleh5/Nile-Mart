package org.route;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderRegistry {

    private final List<Order> orders = new ArrayList<>();

    private int nextOrderNumber = 1;

    public String createOrderNumber() {
        return String.format("ORD-%03d", nextOrderNumber++);
    }

    public void save(Order order) {
        orders.add(order);
    }

    @PreDestroy
    public void printEndOfDaySummary() {

        BigDecimal revenue = BigDecimal.ZERO;

        for (Order order : orders) {
            revenue = revenue.add(order.total());
        }

        System.out.printf(
                "End of day: %d orders, revenue %,.2f EGP%n",
                orders.size(),
                revenue
        );
    }
}