package com.generalstoreapp.tests;
import com.generalstoreapp.base.base;
import com.generalstoreapp.pages.CheckoutPage;
import com.generalstoreapp.pages.FormPage;
import com.generalstoreapp.pages.ProductsPage;
import com.generalstoreapp.utilities.Utilities;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddToCartTest extends base {
    private static AndroidDriver<AndroidElement> driver;
    Utilities u;
    String capabilities;

    @BeforeMethod
    public void testSetup() throws Exception {
        killAllNodes();
        startServer();
        this.driver = runCapabilities();
        Properties prop = readPropertiesFile(System.getProperty("user.dir") + "/src/main/resources//global.properties");
        this.capabilities = (String) prop.get("capabilities");
        if(this.capabilities.equalsIgnoreCase("cloud")){
            this.u = new Utilities(this.driver);
            u.setBrowserstackTestName("Add To Cart Test");
        }
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
        try {
            pp.addProductToCart(productName);
        }catch (NoSuchElementException e){
            if(this.capabilities.equalsIgnoreCase("cloud"))
                this.u.markBrowserstackTestFailed(productName + "doesnt exist in products page");
            throw e;
        }
        pp.clickOnCartButton();
        List<String> productNames = new ArrayList<>();
        int productsCount = cp.getProducts().size();
        for (int i = 0; i < productsCount; i++) {
            List<WebElement> products = cp.getProducts();
            productNames.add(products.get(i).getText());
        }
        try {
            Assert.assertTrue(productNames.contains(productName));
            if(this.capabilities.equalsIgnoreCase("cloud"))
                this.u.markBrowserstackTestPassed("product added to cart successfully");
        }catch (AssertionError e){
            if(this.capabilities.equalsIgnoreCase("cloud"))
                this.u.markBrowserstackTestFailed(productName + " not in checkout page");
            throw e;
        }

    }
}
