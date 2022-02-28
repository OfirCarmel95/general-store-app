package com.generalstoreapp.tests;
import com.generalstoreapp.base.base;
import com.generalstoreapp.pages.CheckoutPage;
import com.generalstoreapp.pages.FormPage;
import com.generalstoreapp.pages.ProductsPage;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.IOException;
import java.util.List;

public class ProductsSumTest extends base {

    private static AndroidDriver<AndroidElement> driver;
    @BeforeMethod
    public void testSetup() throws Exception {
        killAllNodes();
        startServer();
        this.driver = runCapabilities();
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
        Assert.assertEquals(String.valueOf(productsPriceSum), totalValue);
    }
}
