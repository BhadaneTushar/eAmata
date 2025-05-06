package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObject.ProviderGroupPage;
import testBase.BaseClass;
import utilities.LoginUtils;
import utilities.TestDataGenerator;

public class TC006_EditProviderGroup extends BaseClass {
    private ProviderGroupPage providerGroupPage;
    private String updatedName;

    @BeforeMethod
    @Description("Setup test prerequisites")
    public void setUp() {
        LoginUtils.loginAsSuperAdmin();
        providerGroupPage = new ProviderGroupPage(getDriver());
        TestDataGenerator dataGenerator = new TestDataGenerator();

        // Generate updated test data
        updatedName = dataGenerator.generateCompanyName();
    }

    @Test(priority = 1, groups = { "smoke", "regression" })
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify editing a provider group with valid data")
    public void editProviderGroup() {
        providerGroupPage.editProviderGroup(updatedName);
        Assert.assertEquals(providerGroupPage.getSuccessMessage(), "Provider group updated successfully!",
                "Provider group was not updated successfully");
    }

    @Test(priority = 2, groups = { "regression" })
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify validation when updating provider group with empty name")
    public void testEmptyNameValidation() {
        providerGroupPage.editProviderGroup("");
        Assert.assertEquals(providerGroupPage.getNameRequiredError(), "Name is required",
                "Name required error message does not match");
    }
}