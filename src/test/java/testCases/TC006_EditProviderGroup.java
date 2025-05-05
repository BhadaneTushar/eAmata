package testCases;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObject.ProviderGroupPage;
import testBase.BaseClass;
import utilities.TestDataGenerator;

public class TC006_EditProviderGroup extends BaseClass {

    private ProviderGroupPage providerGroupPage;
    private String newProviderGroupName;

    @BeforeMethod
    public void setUp() {
        super.setUp();
        TestDataGenerator data = new TestDataGenerator();
        providerGroupPage = new ProviderGroupPage();
        newProviderGroupName = data.generateCompanyName();
    }

    @Test(priority = 1, groups = { "smoke", "regression" })
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify SuperAdmin can successfully edit a provider group")
    public void editProviderGroupWithValidDetails() {
        providerGroupPage.editProviderGroup(newProviderGroupName);
        String actualMessage = providerGroupPage.getSuccessMessage();
        String expectedMessage = "Provider group updated successfully!";
        Assert.assertEquals(actualMessage, expectedMessage, "Provider group update failed");
    }

    @Test(priority = 2, groups = { "regression" })
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify error message when provider group name is empty")
    public void editProviderGroupWithEmptyName() {
        providerGroupPage.editProviderGroup("");
        String actualError = providerGroupPage.getNameRequiredError();
        String expectedError = "Name is required";
        Assert.assertEquals(actualError, expectedError, "Empty name validation failed");
    }
}
