package org.route;

import org.springframework.stereotype.Component;

@Component
public class EmailNotificationSender implements NotificationSender {

    @Override
    public void send(Customer customer, String message) {

        if (customer.email() == null) {
            return;
        }

        System.out.println(
                "Email sent to " + customer.email() + ": " + message
        );
    }
}