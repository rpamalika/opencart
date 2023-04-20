package testBase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;//logging

public class BaseClass {
	
	public static WebDriver driver;//make it static so that you can use same instance in ExtentReportManager
	
	public Logger logger;//for logging
	
	public ResourceBundle rb;//to read config.properties
	
	@BeforeClass(groups= {"Master","Sanity","Regression"})//Step 8:Groups added
	@Parameters("browser")//getting browser parameter from testng.xml
	public void setup(String br) {
		//to remove the heading in chrome page when automation is running like "chrome is being controlled by..."
		//ChromeOptions options=new ChromeOptions();
		//options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
		rb = ResourceBundle.getBundle("config");// Load config.properties
		logger=LogManager.getLogger(this.getClass());	//logging
		//launch right browser based on parameter
		//driver=new ChromeDriver(options);
		if(br.equals("chrome")) {
			WebDriverManager.chromedriver().setup();

		driver=new ChromeDriver();
		}
		else if(br.equals("edge")) {
			WebDriverManager.edgedriver().setup();

			driver=new EdgeDriver();
		}
		else
		{
			WebDriverManager.chromedriver().setup();
			driver=new ChromeDriver();
		}
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		driver.get("https://demo.opencart.com/index.php");
		driver.manage().window().maximize();
	}
    @AfterClass(groups= {"Master","Sanity","Regression"})
    
    public void teardown() {
    	driver.quit();
    	
    }
    
    //To generate random values for testing the email, username and password fields
    
    public String randomString() {
    	String generatedString=RandomStringUtils.randomAlphabetic(5);
    	return generatedString;
    }
    public String randomNumber() {
    	String generatedNumber=RandomStringUtils.randomNumeric(10);
    	return generatedNumber;
    }
    public String randomAlphaNumeric() {
    	String st= RandomStringUtils.randomAlphabetic(4);
    	String num= RandomStringUtils.randomNumeric(3);
    	return(st+"@"+num);
    }
    public String captureScreen(String tname) throws IOException {

		String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
				
		TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
		File source = takesScreenshot.getScreenshotAs(OutputType.FILE);
		String destination = System.getProperty("user.dir") + "\\screenshots\\" + tname + "_" + timeStamp + ".png";

		try {
			FileUtils.copyFile(source, new File(destination));
		} catch (Exception e) {
			e.getMessage();
		}
		return destination;

}
}
