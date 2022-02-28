package com.generalstoreapp.tests;
import com.generalstoreapp.base.base;
import com.generalstoreapp.pages.FormPage;
import com.generalstoreapp.pages.ProductsPage;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.testng.Assert;
import org.testng.annotations.*;

public class FillFormTest extends base {

    AndroidDriver<AndroidElement> driver;

    @BeforeMethod
    public void testSetup() throws Exception {
        killAllNodes();
        startServer();
        this.driver = runCapabilities();
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
        }catch (AssertionError e){
            String toastMessage = fp.getToastMessage();
            throw new Exception(toastMessage);
        }
    }

}
