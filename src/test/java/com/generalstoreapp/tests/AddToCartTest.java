package com.generalstoreapp.tests;
import com.generalstoreapp.base.base;
import com.generalstoreapp.pages.CheckoutPage;
import com.generalstoreapp.pages.FormPage;
import com.generalstoreapp.pages.ProductsPage;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;

public class AddToCartTest extends base {
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
    public void addToCart(String country, String name, String gender,String productName){
        FormPage fp = new FormPage(driver);
        ProductsPage pp = new ProductsPage(driver);
        CheckoutPage cp = new CheckoutPage(driver);
        fp.clickOnDropdown();
        fp.selectCountry(country);
        fp.fillName(name);
        fp.clickOnGenderOption(gender);
        fp.submitForm();
        pp.addProductToCart(productName);
        pp.clickOnCartButton();
        List<WebElement> products = cp.getProducts();
        List<String> productNames = new ArrayList<>();
        for (WebElement product: products) {
            productNames.add(product.getText());
        }
        Assert.assertTrue(productNames.contains(productName));
    }
}
