package com.generalstoreapp.tests;
import com.generalstoreapp.base.base;
import com.generalstoreapp.pages.FormPage;
import com.generalstoreapp.pages.ProductsPage;
import com.generalstoreapp.utilities.Utilities;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Properties;

public class FillFormTest extends base {

    AndroidDriver<AndroidElement> driver;
    Utilities u;
    String capabilities;

    @BeforeMethod
    public void testSetup() throws Exception {
        killAllNodes();
        startServer();
        this.driver = runCapabilities();
        Properties prop = readPropertiesFile(System.getProperty("user.dir") + "/src/main/resources//global.properties");
        this.capabilities = (String) prop.get("capabilities");
        // Setting name of the test for browserstack
        if(this.capabilities.equalsIgnoreCase("cloud")){
            this.u = new Utilities(this.driver);
            u.setBrowserstackTestName("Fill Form Test");
        }
    }

    @AfterMethod
    public void testTearDown() {
        this.driver.quit();
        tearDown();
    }

    @Test(dataProvider = "getExcelData", dataProviderClass = com.generalstoreapp.dataprovider.GetExcelData.class)
    public void fillForm(String country, String name, String gender) throws Exception {
        FormPage fp = new FormPage(driver);
        ProductsPage pp = new ProductsPage(driver);
        fp.clickOnDropdown();
        fp.selectCountry(country);
        fp.fillName(name);
        fp.clickOnGenderOption(gender);
        fp.submitForm();
        Thread.sleep(500);
        try {
            Assert.assertEquals(pp.getPageTitle(), "Products");
            if(this.capabilities.equalsIgnoreCase("cloud"))
                this.u.markBrowserstackTestPassed("form filled successfully");
        }catch (AssertionError e){
            String toastMessage = fp.getToastMessage();
            if(this.capabilities.equalsIgnoreCase("cloud"))
                this.u.markBrowserstackTestFailed("name field is required");
            throw new Exception(toastMessage);
        }
    }

}
