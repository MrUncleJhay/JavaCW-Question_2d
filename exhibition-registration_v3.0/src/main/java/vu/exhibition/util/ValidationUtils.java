package vu.exhibition.util;

import java.util.List;

/**
 * Stateless validation helpers for participant registration input.
 * <p>
 * Every method here is a pure predicate — no Swing, no dialogs, no
 * persistence. The UI layer decides what to do with a failed check
 * (e.g. which specific error dialog to show); this class only answers
 * "is this value valid?".
 */
public final class ValidationUtils {

    /** Minimum number of digit characters a phone number must contain. */
    private static final int MIN_PHONE_DIGITS = 8;

    /** Canonical list of valid exhibition categories, in display order. */
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

    /**
     * A name is valid if it is non-null and contains at least one
     * non-whitespace character.
     */
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    /**
     * Minimal email check per spec: the string must contain both
     * {@code '@'} and {@code '.'}. This is intentionally simple, not
     * full RFC 5322 validation — swap in a regex here later if
     * stricter checking is ever needed.
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    /**
     * A phone number is valid if it contains at least {@link #MIN_PHONE_DIGITS}
     * digit characters. Formatting characters (spaces, dashes, '+',
     * parentheses) are ignored, so "+256 700-123456" and "0700123456"
     * are both judged on their digit count, not their raw length.
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }
        long digitCount = phone.chars().filter(Character::isDigit).count();
        return digitCount >= MIN_PHONE_DIGITS;
    }

    /**
     * A category is valid only if it exactly matches one of
     * {@link #CATEGORIES}. This guards against a null or placeholder
     * combo-box selection reaching the database.
     */
    public static boolean isValidCategory(String category) {
        return category != null && CATEGORIES.contains(category);
    }
}
