package model;

import java.util.regex.Pattern;
import java.util.Optional;

public class Customer {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private final String firstName;
    private final String lastName;
    private final String email;

    public Customer(final String firstName, final String lastName, final String email) {
        this.email = Optional.of(email)
                .filter(Customer::isValidEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email: " + email));

        this.firstName = firstName;
        this.lastName = lastName;
    }

    private static boolean isValidEmail(final String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    // Getters and toString
    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return String.format("First Name: %s\nLast Name: %s\nEmail: %s", firstName, lastName, email);
    }
}
