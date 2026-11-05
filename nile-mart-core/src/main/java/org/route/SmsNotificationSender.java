package org.route;

import org.springframework.stereotype.Component;

@Component
public class SmsNotificationSender implements NotificationSender {

    @Override
    public void send(Customer customer, String message) {
        System.out.println(
                "SMS sent to " + customer.phone() + ": " + message
        );
    }
}