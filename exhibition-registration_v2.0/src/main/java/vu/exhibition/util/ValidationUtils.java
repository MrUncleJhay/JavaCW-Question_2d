package vu.exhibition.util;

import java.util.List;

public final class ValidationUtils {

    private static final int MIN_PHONE_DIGITS = 8;

    public static final List<String> CATEGORIES = List.of(
            "Art",
            "Science",
            "Technology",
            "Literature",
            "Photography"
    );

    private ValidationUtils() {
        // Utility class — not instantiable
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }
        long digitCount = phone.chars().filter(Character::isDigit).count();
        return digitCount >= MIN_PHONE_DIGITS;
    }

    public static boolean isValidCategory(String category) {
        return category != null && CATEGORIES.contains(category);
    }
}