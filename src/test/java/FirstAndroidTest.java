import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FirstAndroidTest {

    AppiumDriver driver;
    private static By addPlant = By.id("add_plant");
    private static By add = By.id("fab");

    @BeforeTest
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("platformVersion", "9");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("app", "app-debug.apk);
        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), caps);
    }

    @Test
    public void add_plant_test() {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        MobileElement plantList = (MobileElement) driver.findElementByAccessibilityId("Plant list");
        MobileElement myGarden = (MobileElement) driver.findElementByAccessibilityId("My garden");

        wait.until(ExpectedConditions.elementToBeClickable(plantList)).click();
        wait.until(ExpectedConditions.elementToBeClickable(myGarden)).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(addPlant)).click();
        List <MobileElement> listElements = driver.findElements(By.id("plant_item_title"));
        for(MobileElement el : listElements){
            if(el.getText().equalsIgnoreCase("Avocado")){
               el.click();
               break;
            }
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(add)).click();
        driver.navigate().back();
        driver.findElementByAccessibilityId("My garden").click();
        By plant_name =  By.id("plant_name");
        wait.until(ExpectedConditions.presenceOfElementLocated(plant_name));
        Assert.assertTrue(driver.findElement(plant_name).getAttribute("text")
                .equalsIgnoreCase("Avocado"));
    }

    @AfterTest
    public void tearDown() {
        if (null != driver) {
            driver.quit();
        }
    }
}
