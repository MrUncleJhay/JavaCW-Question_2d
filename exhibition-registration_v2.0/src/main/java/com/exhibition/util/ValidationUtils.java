package com.exhibition.util;

public class ValidationUtils {
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.contains("@") && email.contains(".");
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        String digits = phone.replaceAll("\\D", "");
        return digits.length() >= 8;
    }

    public static boolean isValidCategory(String category) {
        return isNotEmpty(category) && !category.equalsIgnoreCase("Select Category");
    }
}
