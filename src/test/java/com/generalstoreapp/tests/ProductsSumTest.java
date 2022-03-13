package com.generalstoreapp.tests;
import com.generalstoreapp.base.base;
import com.generalstoreapp.pages.CheckoutPage;
import com.generalstoreapp.pages.FormPage;
import com.generalstoreapp.pages.ProductsPage;
import com.generalstoreapp.utilities.Utilities;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class ProductsSumTest extends base {

    private static AndroidDriver<AndroidElement> driver;
    Utilities u;
    String capabilities;

    @BeforeMethod
    public void testSetup() throws Exception {
        killAllNodes();
        startServer();
        this.driver = runCapabilities();
        Properties prop = readPropertiesFile(System.getProperty("user.dir") + "/src/main/resources/global.properties");
        this.capabilities = (String) prop.get("capabilities");
        if(this.capabilities.equalsIgnoreCase("cloud")){
            // Setting name of the test for browserstack
            this.u = new Utilities(this.driver);
            u.setBrowserstackTestName("Products Sum Test");
        }
    }

    @AfterMethod
    public void testTearDown() throws Exception {
        this.driver.quit();
        tearDown();
    }

    @Test(dataProvider = "getExcelData", dataProviderClass = com.generalstoreapp.dataprovider.GetExcelData.class)
    public void totalValidation(String country, String name, String gender) throws IOException, InterruptedException {
        FormPage fp = new FormPage(driver);
        fp.clickOnDropdown();
        fp.selectCountry(country);
        fp.fillName(name);
        fp.clickOnGenderOption(gender);
        fp.submitForm();
        ProductsPage pp = new ProductsPage(driver);
        pp.addMultipleProductsToCart();
        pp.clickOnCartButton();
        Thread.sleep(2000);
        double productsPriceSum = 0;
        CheckoutPage cp = new CheckoutPage(driver);
        List<AndroidElement> prices =  cp.getPrices();
        for (AndroidElement price : prices)
            productsPriceSum += Double.valueOf(price.getText().substring(1));
        String totalValue = cp.getTotalValue().getText().substring(2);
        try {
            Assert.assertEquals(String.valueOf(productsPriceSum), totalValue);
            if(this.capabilities.equalsIgnoreCase("cloud"))
                this.u.markBrowserstackTestPassed("products sum match");
        }catch (AssertionError e){
            if(this.capabilities.equalsIgnoreCase("cloud"))
                this.u.markBrowserstackTestFailed("products sum don't match");
            throw e;
        }
    }
}
