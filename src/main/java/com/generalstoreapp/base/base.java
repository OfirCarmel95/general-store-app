package com.generalstoreapp.base;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.*;
import java.net.ServerSocket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import io.github.cdimascio.dotenv.Dotenv;


public class base {
    private static AppiumDriverLocalService service;
    private static AndroidDriver<AndroidElement> driver;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");


    public AppiumDriverLocalService startServer(){
        boolean flag = checkIfServerIsRunning(4723);
        if(!flag){
            service = AppiumDriverLocalService.buildDefaultService();
            service.start();
        }
        return service;
    }

    public static boolean checkIfServerIsRunning(int port){
        boolean isServerRunning = false;
        ServerSocket serverSocket;
        try{
            serverSocket = new ServerSocket(port);
            serverSocket.close();
        }catch (IOException e){
        // if control comes here, then it means that the port is in use
            isServerRunning = true;
        }finally {
            serverSocket = null;
        }
        return isServerRunning;
    }

    public void killAllNodes() throws IOException, InterruptedException {
        Runtime.getRuntime().exec("taskkill /F /IM node.exe");
        Thread.sleep(3000);
    }
    public static void startEmulator() throws IOException, InterruptedException {
        Runtime.getRuntime().exec(System.getProperty("user.dir") + "/src/main/resources/startEmulator.bat");
        Thread.sleep(30000);
    }

    public static String getScreenshot(String testName) throws IOException {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Date date = new Date();
        String screenshotPath = System.getProperty("user.dir") + "/FailedTestsScreenshots/" + testName + "_" + formatter.format(date) + ".png";
        FileUtils.copyFile(scrFile, new File(screenshotPath));
        return screenshotPath;
    }

    public static Properties readPropertiesFile(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        Properties prop = new Properties();
        prop.load(fis);
        return prop;
    }

   public static AndroidDriver<AndroidElement> setUpCloud() throws Exception {
       Dotenv dotenv = Dotenv
               .configure()
               .directory("src/test/resources/Config")
               .filename(".browserstack")
               .load();
       JSONParser parser = new JSONParser();
       JSONObject config = (JSONObject) parser.parse(new FileReader("src/test/resources/Config/browserstack.conf.json"));
       DesiredCapabilities capabilities = new DesiredCapabilities();
       JSONArray envs = (JSONArray) config.get("environments");
       Map<String, String> envCapabilities = (Map<String, String>) envs.get(0);
       Iterator it = envCapabilities.entrySet().iterator();
       while (it.hasNext()) {
           Map.Entry pair = (Map.Entry)it.next();
           capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
       }
       Map<String, String> commonCapabilities = (Map<String, String>) config.get("capabilities");
       it = commonCapabilities.entrySet().iterator();
       while (it.hasNext()) {
           Map.Entry pair = (Map.Entry)it.next();
           if(capabilities.getCapability(pair.getKey().toString()) == null){
               capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
           }
       }
       String username = dotenv.get("BROWSERSTACK_USERNAME");
       String accessKey = dotenv.get("BROWSERSTACK_ACCESS_KEY");
       String app = dotenv.get("BROWSERSTACK_APP_ID");
       if(app != null && !app.isEmpty()) {
           capabilities.setCapability("app", app);
       }
       driver = new AndroidDriver(new URL("http://"+username+":"+accessKey+"@"+config.get("server")+"/wd/hub"), capabilities);
       return driver;
   }

    public static AndroidDriver<AndroidElement> setUp (String appName) throws IOException, InterruptedException {
        Properties prop = readPropertiesFile(System.getProperty("user.dir") + "/src/main/resources/global.properties");
        DesiredCapabilities cap = new DesiredCapabilities();
        String deviceName = (String) prop.get("device");
        if(deviceName.contains("emulator")){
            startEmulator();
        }
        File appDir = new File(System.getProperty("user.dir")+ "/src/main/resources");
        File app = new File(appDir, (String) prop.get(appName));
        cap.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        cap.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
        cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), cap);
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        return driver;
    }

    public static AndroidDriver<AndroidElement> runCapabilities() throws Exception {
        Properties prop = readPropertiesFile(System.getProperty("user.dir") + "/src/main/resources/global.properties");
        String capabilities = (String) prop.get("capabilities");
        if(capabilities.equalsIgnoreCase("cloud")){
            return setUpCloud();
        } else if (capabilities.equalsIgnoreCase("local")){
            return setUp("GeneralStoreApp");
        }else {
            throw new Exception("Invalid capabilities value");
        }
    }

    public void tearDown() {
        if(service != null)
            service.stop();
    }
}
