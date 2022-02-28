package com.generalstoreapp.pages;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class CheckoutPage {
    public CheckoutPage(AndroidDriver<AndroidElement> driver){
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }
    @AndroidFindBy(id = "com.androidsample.generalstore:id/productPrice")
    private List<AndroidElement> prices;
    @AndroidFindBy(id = "com.androidsample.generalstore:id/totalAmountLbl")
    private WebElement totalValue;
    @AndroidFindBy(id = "com.androidsample.generalstore:id/productName")
    private List<WebElement> products;


    public List<AndroidElement> getPrices (){
        System.out.println("trying to get prices");
        return prices;
    }

    public WebElement getTotalValue (){
        System.out.println("trying to get total value");
        return totalValue;
    }

    public List<WebElement> getProducts(){
        System.out.println("trying to get prodcuts");
        return products;
    }
}
