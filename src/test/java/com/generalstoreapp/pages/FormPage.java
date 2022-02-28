package com.generalstoreapp.pages;
import com.generalstoreapp.utilities.Utilities;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class FormPage {

    AndroidDriver<AndroidElement> driver;
    Utilities u;

    public FormPage(AndroidDriver<AndroidElement> driver){
        this.driver = driver;
        this.u = new Utilities(driver);
        PageFactory.initElements(new AppiumFieldDecorator(this.driver), this);
    }
    @AndroidFindBy(id = "com.androidsample.generalstore:id/nameField")
    private WebElement nameField;
    @AndroidFindBy(id = "com.androidsample.generalstore:id/radioFemale")
    private WebElement femaleOption;
    @AndroidFindBy(id = "com.androidsample.generalstore:id/radioMale")
    private WebElement maleOption;
    @AndroidFindBy(id = "android:id/text1")
    private WebElement countryDropdown;
    @AndroidFindBy(id = "com.androidsample.generalstore:id/btnLetsShop")
    private WebElement letsShopButton;
    @AndroidFindBy(xpath = "//android.widget.Toast[1]")
    private WebElement toastMessage;

    public void clickOnDropdown(){
        System.out.println("trying to get country dropdown");
        countryDropdown.click();
    }

    public void selectCountry(String country){
        System.out.println("trying to select country");
        u.scrollToText(country);
        WebElement countryNameElement = driver.findElementByXPath("//*[@text=\'" + country + "\']");
        countryNameElement.click();
    }

    public void fillName(String name){
        System.out.println("trying to fill name field");
        nameField.sendKeys(name);
        driver.hideKeyboard();
    }


    public void clickOnGenderOption(String gender){
        if(gender.equalsIgnoreCase("Male")){
            System.out.println("trying to click on male option");
            maleOption.click();
        }else {
            System.out.println("trying to click on female option");
            femaleOption.click();
        }
    }

    public void submitForm(){
        System.out.println("trying to click on let's shop button");
        letsShopButton.click();
    }

    public String getToastMessage(){
        return toastMessage.getAttribute("name");
    }

}
