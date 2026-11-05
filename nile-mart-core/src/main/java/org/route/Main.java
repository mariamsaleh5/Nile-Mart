package org.route;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static Governorate askGovernorate(Scanner scanner) {

        System.out.println("Available governorates:");

        for (Governorate governorate : Governorate.values()) {
            System.out.println(governorate);
        }

        System.out.print("Enter governorate: ");

        String input = scanner.nextLine().trim().toUpperCase();

        try {
            return Governorate.valueOf(input);
        } catch (IllegalArgumentException e) {
            throw new StoreException("Invalid governorate: " + input);
        }
    }
    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        Scanner scanner = new Scanner(System.in);

        Map<Integer, ShoppingCart> carts = new HashMap<>();

        int nextCartNumber = 1;

        try {

            boolean running = true;

            while (running) {

                System.out.println("\n========== NILEMART ==========");
                System.out.println("1. Show products");
                System.out.println("2. Create a cart");
                System.out.println("3. Add a product to a cart");
                System.out.println("4. Show a cart");
                System.out.println("5. Check out a cart");
                System.out.println("0. Exit");

                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                try {

                    switch (choice) {

                        case 1:
                            ProductService productService = context.getBean(ProductService.class);
                            for (Product product : productService.findAll()) {
                                System.out.println(
                                        product.getSku() + " | "
                                                + product.getName() + " | "
                                                + product.getCategory() + " | "
                                                + product.getPrice() + " EGP | "
                                                + "Stock: " + product.getStockQuantity()
                                );
                            }
                            break;


                        case 2:
                            // Create a cart
                            ShoppingCart cart = context.getBean(ShoppingCart.class);

                            cart.setCartNumber(nextCartNumber);

                            System.out.print("Enter customer name: ");
                            String name = scanner.nextLine();

                            System.out.print("Enter customer phone: ");
                            String phone = scanner.nextLine();

                            System.out.print("Enter customer email: ");
                            String email = scanner.nextLine();

                            Customer customer = new Customer(name, phone, email);

                            cart.setCustomer(customer);

                            carts.put(nextCartNumber, cart);

                            System.out.println("Cart created successfully. Cart number: " + nextCartNumber);

                            nextCartNumber++;

                            break;


                        case 3:
                            System.out.print("Enter cart number: ");
                            int cartNumber = scanner.nextInt();
                            scanner.nextLine();

                            ShoppingCart selectedCart = carts.get(cartNumber);

                            if (selectedCart == null) {
                                throw new StoreException("Cart not found: " + cartNumber);
                            }

                            System.out.print("Enter product SKU: ");
                            String sku = scanner.nextLine();

                            System.out.print("Enter quantity: ");
                            int quantity = scanner.nextInt();
                            scanner.nextLine();

                            selectedCart.addItem(sku, quantity);

                            System.out.println("Product added successfully.");
                            break;

                        case 4:
                            System.out.print("Enter cart number: ");
                            int cartNumberToShow = scanner.nextInt();
                            scanner.nextLine();

                            ShoppingCart cartToShow = carts.get(cartNumberToShow);

                            if (cartToShow == null) {
                                throw new StoreException("Cart not found: " + cartNumberToShow);
                            }

                            System.out.println("\nCart Number: " + cartToShow.getCartNumber());
                            System.out.println("Customer: " + cartToShow.getCustomer().fullName());

                            for (Map.Entry<String, Integer> entry : cartToShow.getLines().entrySet()) {

                                String sku1 = entry.getKey();
                                int quantity1 = entry.getValue();

                                Product product = context.getBean(ProductService.class).findBySku(sku1);

                                System.out.println(
                                        product.getName()
                                                + " | SKU: " + sku1
                                                + " | Quantity: " + quantity1
                                                + " | Price: " + product.getPrice()
                                );
                            }

                            System.out.println("Subtotal: " + cartToShow.subtotal() + " EGP");
                            break;



                        case 5:
                            System.out.print("Enter cart number: ");
                            int cartNumberToCheckout = scanner.nextInt();
                            scanner.nextLine();

                            ShoppingCart cartToCheckout = carts.get(cartNumberToCheckout);

                            if (cartToCheckout == null) {
                                throw new StoreException("Cart not found: " + cartNumberToCheckout);
                            }

                            CheckoutService checkoutService =
                                    context.getBean(CheckoutService.class);

                            Order order = checkoutService.placeOrder(
                                    cartToCheckout,
                                    askGovernorate(scanner)
                            );

                            System.out.println("\nOrder placed successfully!");
                            System.out.println("Order Number: " + order.orderNumber());
                            System.out.println("Subtotal: " + order.subtotal() + " EGP");
                            System.out.println("VAT: " + order.vat() + " EGP");
                            System.out.println("Shipping: " + order.shippingFee() + " EGP");
                            System.out.println("Total: " + order.total() + " EGP");

                            break;


                        case 0:
                            running = false;
                            break;

                        default:
                            System.out.println("Invalid option. Try again.");
                    }

                } catch (StoreException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

        } finally {
            context.close();
            scanner.close();
        }
    }
}