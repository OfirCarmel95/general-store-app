package com.generalstoreapp.pages;
import com.generalstoreapp.utilities.Utilities;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class ProductsPage {
    AndroidDriver<AndroidElement> driver;
    Utilities u;
    public ProductsPage(AndroidDriver<AndroidElement> driver){
        this.driver = driver;
        this.u = new Utilities(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }
    @AndroidFindBy(id = "com.androidsample.generalstore:id/productAddCart")
    private List<AndroidElement> addButtons;
    @AndroidFindBy(id = "com.androidsample.generalstore:id/appbar_btn_cart")
    private WebElement cartButton;
    @AndroidFindBy(id = "com.androidsample.generalstore:id/productName")
    private List <WebElement> products;
    @AndroidFindBy(id = "com.androidsample.generalstore:id/toolbar_title")
    private WebElement pageTitle;

    public void addMultipleProductsToCart(){
        System.out.println("trying to add multiple products");
        for (AndroidElement addButton : addButtons)
            addButton.click();
    }

    public void clickOnCartButton() {
        System.out.println("trying to click on cart button");
        cartButton.click();
    }

    public void addProductToCart(String productName){
        u.scrollToText(productName);
        int shoesCount = products.size();
        for (int i = 0; i < shoesCount ; i++) {
            String currProduct = products.get(i).getText();
            if(currProduct.equalsIgnoreCase(productName)) {
                addButtons.get(i).click();
                break;
            }
        }
    }

    public String getPageTitle(){
        return pageTitle.getText();
    }
}
