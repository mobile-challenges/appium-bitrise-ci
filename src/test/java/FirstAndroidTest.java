import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.appium.Eyes;
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

@SuppressWarnings("rawtypes")
public class FirstAndroidTest {

    AppiumDriver driver;
    private static final By addPlant = By.id("add_plant");
    private static final By add = By.id("fab");
    Eyes eyes = new Eyes();

    @BeforeTest
    public void setUp() throws MalformedURLException {
        String apiKey = System.getenv("APPLITOOLS_API_KEY");
        eyes.setApiKey(apiKey);
        String batchName = null;
        String batchId   = System.getenv("APPLITOOLS_BATCH_ID");
        BatchInfo batchInfo = new BatchInfo(batchName);
        batchInfo.setId(batchId);
        eyes.setBatch(batchInfo);
        eyes.setForceFullPageScreenshot(false);

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("platformVersion", "9");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("app", System.getenv("BITRISE_APK_PATH"));
        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), caps);
    }

    @Test
    public void add_plant_test() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        eyes.open(driver, "Sunflower", "Add My Plant");
        MobileElement plantList = (MobileElement) driver.findElementByAccessibilityId("Plant list");
        MobileElement myGarden = (MobileElement) driver.findElementByAccessibilityId("My garden");
        wait.until(ExpectedConditions.elementToBeClickable(plantList)).click();
        eyes.checkWindow("Plant list",false);
        wait.until(ExpectedConditions.elementToBeClickable(myGarden)).click();
        eyes.checkWindow("My Empty Garden",false);
        wait.until(ExpectedConditions.presenceOfElementLocated(addPlant)).click();
        List<MobileElement> listElements = driver.findElements(By.id("plant_item_title"));
        for (MobileElement el : listElements) {
            if (el.getText().equalsIgnoreCase("Avocado")) {
                el.click();
                break;
            }
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(add)).click();
        driver.navigate().back();
        driver.findElementByAccessibilityId("My garden").click();
        By plant_name = By.id("plant_name");
        wait.until(ExpectedConditions.presenceOfElementLocated(plant_name));
        eyes.checkWindow("Avocado",false);
        Assert.assertTrue(driver.findElement(plant_name).getAttribute("text")
                .equalsIgnoreCase("Avocado"));
        eyes.close();
    }

    @AfterTest
    public void tearDown() {
        if (null != driver) {
            driver.quit();
            eyes.abortIfNotClosed();
        }
    }
}