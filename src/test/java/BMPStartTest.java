import net.lightbody.bmp.proxy.ProxyServer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class BMPStartTest {
    private final static String BASE_URL = "http://the-internet.herokuapp.com/";
    private WebDriver driver;
    private ProxyServer bmp;
    @BeforeMethod
    public void setup() throws Exception {
        bmp = new ProxyServer(5559);
        bmp.start();
        bmp.autoBasicAuthorization("the-internet.herokuapp.com", "admin", "admin");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(CapabilityType.PROXY,bmp.seleniumProxy());
        driver = new ChromeDriver(caps);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.get(BASE_URL);
        Thread.sleep(5000);
    }
    @AfterMethod
    public void tearDown() throws Exception {
        driver.quit();
        bmp.stop();
    }
    @Test
    public void bmpSimpleTest(){
        Assert.assertEquals(driver.getTitle(), "The Internet");

    }
    @Test
    public void bawTest(){
        driver.findElement(By.linkText("Basic Auth")).click();
        Assert.assertTrue(driver.findElement(By.cssSelector(".example")).getText().trim().contains("Congratulations! You must have the proper credentials."));

    }


}
