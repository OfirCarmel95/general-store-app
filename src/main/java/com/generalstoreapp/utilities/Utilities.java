package com.generalstoreapp.utilities;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;


public class Utilities {
    AndroidDriver<AndroidElement> driver;
    JavascriptExecutor jse;

    public Utilities(AndroidDriver<AndroidElement> driver){
        this.driver = driver;
        this.jse = (JavascriptExecutor)this.driver;
    }

    public void scrollToText(String text){
        driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector())" +
                                                                        ".scrollIntoView(text(\"" + text + "\"))");
    }

    public WebDriverWait getWaitElement(){
        WebDriverWait wait = new WebDriverWait(driver, 3);
        return wait;
    }

    public void setBrowserstackTestName(String testName){
        this.jse.executeScript("browserstack_executor: {\"action\": \"setSessionName\", \"arguments\": {\"name\":\"" + testName + "\" }}");
    }

    public void markBrowserstackTestPassed(String reason){
        this.jse.executeScript(
                "browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"" + reason + "\"}}"
        );
    }

    public void markBrowserstackTestFailed(String reason){
        this.jse.executeScript(
                "browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"" + reason +"\"}}"
        );
    }
}
