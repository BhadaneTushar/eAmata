package utilities;

import com.github.javafaker.Faker;

public class TestDataGenerator {

    protected Faker faker = new Faker();


    public String generateRandomFirstName() {
        return faker.name().firstName();
    }

    public String generateCompanyName() {
        return faker.company().name().replaceAll("[^a-zA-Z ]", "");
    }

    public String generateRandomLastName() {
        return faker.name().lastName();
    }

    public String generateRandomEmail() {
        return faker.internet().emailAddress();
    }

    public String generateEmail(String prefix) {
        return "tushar.bhadane+" + prefix + faker.number().digits(2) + "@thinkitive.com";
    }

    public String generatePhoneNumber() {
        return faker.number().digits(10);
    }

    public String generateRandomNPI() {
        return faker.number().digits(10);
    }

    public String generateRandomSubDomain() {
        return faker.regexify("[a-z]{3,4}");
    }

    public String generateAddressLine1() {
        return faker.address().streetAddress();
    }

    public String generateAddressLine2() {
        return faker.address().secondaryAddress();
    }

    public String generateCity() {
        return faker.address().city();
    }

    public String generateCountry() {
        return faker.address().country();
    }

    public String generateZipCode() {
        return faker.number().digits(5);
    }
}
