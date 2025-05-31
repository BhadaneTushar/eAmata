package utilities;

import com.github.javafaker.Faker;
import utilities.LoggerUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Utility class for generating test data.
 * Provides methods for generating both valid and invalid test data with
 * enhanced validation.
 */
public class TestDataGenerator {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PHONE_PATTERN = "^\\d{10}$";
    private static final String NPI_PATTERN = "^\\d{10}$";
    private static final String SUBDOMAIN_PATTERN = "^[a-z0-9-]+$";
    private static final String ZIP_CODE_PATTERN = "^\\d{5}(-\\d{4})?$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private static final String URL_PATTERN = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$";
    private static final String DATE_PATTERN = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\\d{4}$";

    protected Faker faker = new Faker();
    private Random random = new Random();

    /**
     * Generates a list of random first names.
     * 
     * @param count The number of names to generate
     * @return List of random first names
     */
    public List<String> generateRandomFirstNames(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> generateRandomFirstName())
                .collect(Collectors.toList());
    }

    /**
     * Generates a list of random last names.
     * 
     * @param count The number of names to generate
     * @return List of random last names
     */
    public List<String> generateRandomLastNames(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> generateRandomLastName())
                .collect(Collectors.toList());
    }

    /**
     * Generates a list of random email addresses.
     * 
     * @param count The number of emails to generate
     * @return List of random email addresses
     */
    public List<String> generateRandomEmails(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> generateRandomEmail())
                .collect(Collectors.toList());
    }

    /**
     * Generates a random password that meets security requirements.
     * 
     * @return A secure password
     */
    public String generateSecurePassword() {
        String password;
        do {
            password = faker.internet().password(8, 16, true, true, true);
        } while (!Pattern.matches(PASSWORD_PATTERN, password));
        LoggerUtils.debug("Generated secure password");
        return password;
    }

    /**
     * Generates a random URL.
     * 
     * @return A valid URL
     */
    public String generateRandomUrl() {
        String url = faker.internet().url();
        LoggerUtils.debug("Generated URL: " + url);
        return url;
    }

    /**
     * Generates a random date in MM/DD/YYYY format.
     * 
     * @return A date string
     */
    public String generateRandomDate() {
        String date = String.format("%02d/%02d/%04d",
                random.nextInt(12) + 1,
                random.nextInt(28) + 1,
                2000 + random.nextInt(23));
        LoggerUtils.debug("Generated date: " + date);
        return date;
    }

    /**
     * Generates a list of test data objects.
     * 
     * @param count The number of objects to generate
     * @return List of test data objects
     */
    public List<Map<String, String>> generateTestDataObjects(int count) {
        List<Map<String, String>> testData = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Map<String, String> data = new HashMap<>();
            data.put("firstName", generateRandomFirstName());
            data.put("lastName", generateRandomLastName());
            data.put("email", generateRandomEmail());
            data.put("phone", generatePhoneNumber());
            data.put("npi", generateRandomNPI());
            data.put("subdomain", generateRandomSubDomain());
            data.put("addressLine1", generateAddressLine1());
            data.put("addressLine2", generateAddressLine2());
            data.put("city", generateCity());
            data.put("zipCode", generateZipCode());
            data.put("state", generateState());
            testData.add(data);
        }
        LoggerUtils.debug("Generated " + count + " test data objects");
        return testData;
    }

    /**
     * Generates a random state name.
     * 
     * @return A state name
     */
    public String generateState() {
        String state = faker.address().state();
        LoggerUtils.debug("Generated state: " + state);
        return state;
    }

    /**
     * Validates a date string.
     * 
     * @param date The date to validate
     * @return true if date is valid
     */
    public boolean isValidDate(String date) {
        return date != null && Pattern.matches(DATE_PATTERN, date);
    }

    /**
     * Validates a URL.
     * 
     * @param url The URL to validate
     * @return true if URL is valid
     */
    public boolean isValidUrl(String url) {
        return url != null && Pattern.matches(URL_PATTERN, url);
    }

    /**
     * Validates a password.
     * 
     * @param password The password to validate
     * @return true if password is valid
     */
    public boolean isValidPassword(String password) {
        return password != null && Pattern.matches(PASSWORD_PATTERN, password);
    }

    /**
     * Generates a random first name.
     * 
     * @return Random first name
     */
    public String generateRandomFirstName() {
        String firstName = faker.name().firstName();
        LoggerUtils.debug("Generated first name: " + firstName);
        return firstName;
    }

    /**
     * Generates a company name with special characters removed.
     * 
     * @return Clean company name
     */
    public String generateCompanyName() {
        String companyName = faker.company().name().replaceAll("[^a-zA-Z ]", "");
        LoggerUtils.debug("Generated company name: " + companyName);
        return companyName;
    }

    /**
     * Generates a random last name.
     * 
     * @return Random last name
     */
    public String generateRandomLastName() {
        String lastName = faker.name().lastName();
        LoggerUtils.debug("Generated last name: " + lastName);
        return lastName;
    }

    /**
     * Generates a random valid email address.
     * 
     * @return Valid email address
     */
    public String generateRandomEmail() {
        String email = faker.internet().emailAddress();
        LoggerUtils.debug("Generated email: " + email);
        return email;
    }

    /**
     * Generates an email with a specific prefix.
     * 
     * @param prefix The email prefix
     * @return Generated email
     */
    public String generateEmail(String prefix) {
        String email = "tushar.bhadane+" + prefix + faker.number().digits(2) + "@thinkitive.com";
        LoggerUtils.debug("Generated prefixed email: " + email);
        return email;
    }

    /**
     * Generates a valid 10-digit phone number.
     * 
     * @return Valid phone number
     */
    public String generatePhoneNumber() {
        String phone = faker.number().digits(10);
        LoggerUtils.debug("Generated phone number: " + phone);
        return phone;
    }

    /**
     * Generates a valid 10-digit NPI number.
     * 
     * @return Valid NPI number
     */
    public String generateRandomNPI() {
        String npi = faker.number().digits(10);
        LoggerUtils.debug("Generated NPI: " + npi);
        return npi;
    }

    /**
     * Generates a valid subdomain.
     * 
     * @return Valid subdomain
     */
    public String generateRandomSubDomain() {
        String subdomain = faker.regexify("[a-z]{3,4}");
        LoggerUtils.debug("Generated subdomain: " + subdomain);
        return subdomain;
    }

    /**
     * Generates a valid address line 1.
     * 
     * @return Valid address line 1
     */
    public String generateAddressLine1() {
        String address = faker.address().streetAddress();
        LoggerUtils.debug("Generated address line 1: " + address);
        return address;
    }

    /**
     * Generates a valid address line 2.
     * 
     * @return Valid address line 2
     */
    public String generateAddressLine2() {
        String address = faker.address().secondaryAddress();
        LoggerUtils.debug("Generated address line 2: " + address);
        return address;
    }

    /**
     * Generates a valid city name.
     * 
     * @return Valid city name
     */
    public String generateCity() {
        String city = faker.address().city();
        LoggerUtils.debug("Generated city: " + city);
        return city;
    }

    /**
     * Generates a valid country name.
     * 
     * @return Valid country name
     */
    public String generateCountry() {
        String country = faker.address().country();
        LoggerUtils.debug("Generated country: " + country);
        return country;
    }

    /**
     * Generates a valid ZIP code.
     * 
     * @return Valid ZIP code
     */
    public String generateZipCode() {
        String zipCode = faker.number().digits(5);
        LoggerUtils.debug("Generated ZIP code: " + zipCode);
        return zipCode;
    }

    /**
     * Generates an invalid email address.
     * 
     * @return Invalid email address
     */
    public String generateInvalidEmail() {
        String invalidEmail = faker.regexify("[a-z]{5}@[a-z]{3}"); // Missing domain
        LoggerUtils.debug("Generated invalid email: " + invalidEmail);
        return invalidEmail;
    }

    /**
     * Generates an invalid phone number.
     * 
     * @return Invalid phone number
     */
    public String generateInvalidPhoneNumber() {
        String invalidPhone = faker.number().digits(5); // Less than 10 digits
        LoggerUtils.debug("Generated invalid phone: " + invalidPhone);
        return invalidPhone;
    }

    /**
     * Generates an invalid NPI number.
     * 
     * @return Invalid NPI number
     */
    public String generateInvalidNPI() {
        String invalidNPI = faker.number().digits(5); // Less than 10 digits
        LoggerUtils.debug("Generated invalid NPI: " + invalidNPI);
        return invalidNPI;
    }

    /**
     * Generates an invalid subdomain.
     * 
     * @return Invalid subdomain
     */
    public String generateInvalidSubdomain() {
        String invalidSubdomain = faker.regexify("[A-Z]{3}"); // Uppercase letters
        LoggerUtils.debug("Generated invalid subdomain: " + invalidSubdomain);
        return invalidSubdomain;
    }

    /**
     * Validates an email address.
     * 
     * @param email The email to validate
     * @return true if email is valid
     */
    public boolean isValidEmail(String email) {
        return email != null && Pattern.matches(EMAIL_PATTERN, email);
    }

    /**
     * Validates a phone number.
     * 
     * @param phone The phone number to validate
     * @return true if phone number is valid
     */
    public boolean isValidPhoneNumber(String phone) {
        return phone != null && Pattern.matches(PHONE_PATTERN, phone);
    }

    /**
     * Validates an NPI number.
     * 
     * @param npi The NPI number to validate
     * @return true if NPI number is valid
     */
    public boolean isValidNPI(String npi) {
        return npi != null && Pattern.matches(NPI_PATTERN, npi);
    }

    /**
     * Validates a subdomain.
     * 
     * @param subdomain The subdomain to validate
     * @return true if subdomain is valid
     */
    public boolean isValidSubdomain(String subdomain) {
        return subdomain != null && Pattern.matches(SUBDOMAIN_PATTERN, subdomain);
    }

    /**
     * Validates a ZIP code.
     * 
     * @param zipCode The ZIP code to validate
     * @return true if ZIP code is valid
     */
    public boolean isValidZipCode(String zipCode) {
        return zipCode != null && Pattern.matches(ZIP_CODE_PATTERN, zipCode);
    }
}
