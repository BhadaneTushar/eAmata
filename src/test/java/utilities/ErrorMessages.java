package utilities;

/**
 * Utility class containing all error messages used in the application.
 * This helps maintain consistency in error messages across test cases.
 */
public class ErrorMessages {
    // Provider Group Error Messages
    public static final String NAME_REQUIRED = "Name is required";
    public static final String EMAIL_INVALID = "Invalid email address";
    public static final String PHONE_INVALID = "Invalid phone number. It must be 10 digits.";
    public static final String NPI_INVALID = "Must be 10 digits";
    public static final String NPI_REQUIRED = "NPI is required";
    public static final String PHONE_REQUIRED = "Phone is required";
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String ADDRESS_REQUIRED = "Line 1 is required";
    public static final String SUBDOMAIN_INVALID = "Subdomain must only contain lowercase letters, numbers, and hyphens, and must not start or end with a hyphen.";
    public static final String SUBDOMAIN_REQUIRED = "Sub domain field is required";

    // Staff Error Messages
    public static final String FIRST_NAME_REQUIRED = "First Name is required";
    public static final String LAST_NAME_REQUIRED = "Last Name is required";

    // Location Error Messages
    public static final String LOCATION_NAME_REQUIRED = "Name is mandatory";
    public static final String LOCATION_PHONE_INVALID = "Invalid phone number. Please use +91, +1, or +61 followed by 10 to 11 digits, allowing hyphens.";
    public static final String LOCATION_EMAIL_REQUIRED = "Email id is mandatory";
    public static final String LOCATION_EMAIL_INVALID = "Invalid email format";
    public static final String ZIP_CODE_REQUIRED = "Zip code is required";

    // Login Error Messages
    public static final String LOGIN_EMAIL_INVALID = "Invalid email address";
    public static final String LOGIN_PASSWORD_INVALID = "Password must be 8+ characters, with at least one uppercase, one lowercase, one number, and one special character. No spaces.";

    // Nurse Error Messages
    public static final String NURSE_FIRST_NAME_REQUIRED = "First Name is required";
    public static final String NURSE_LAST_NAME_REQUIRED = "Last Name is required";
    public static final String NURSE_EMAIL_INVALID = "Invalid email format";
    public static final String NURSE_PHONE_INVALID = "Invalid phone number. It must be 10 digits.";
}