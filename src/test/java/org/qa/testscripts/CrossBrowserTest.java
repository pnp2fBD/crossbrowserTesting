package org.qa.testscripts;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CrossBrowserTest {
	
	WebDriver driver;
	
	@BeforeTest
	@Parameters({"browser", "website"})
	public void setup(String browser,String website){
				
		System.out.println("browser " + browser);
		if(browser.equalsIgnoreCase("chrome")){
			System.setProperty("webdriver.chrome.driver", "c:\\sw\\chromedriver.exe");
			driver = new ChromeDriver();			
		}else if (browser.equalsIgnoreCase("firefox")){
			//System.setProperty("webdriver.gecko.driver", "c:\\sw\\geckodriver.exe");
			System.setProperty("webdriver.firefox.marionette","C:\\geckodriver.exe");
			driver = new FirefoxDriver();			
		}
		
		System.out.println("website " + website);
		driver.get("https:\\www." + website);
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
	}
	
	@Test(priority=1)
	public void execute(){
		
		String sSearchtxt = "//*[@id='twotabsearchtextbox']";
		String sSearchBtn = "//*[@id='nav-search']/form/div[2]/div/input";
		String sSearchedItem = "//*[@id='result_0']/div/div/div/div[2]/div[2]/div[1]/a/h2";
		String sAddtoCart = "//*[@id='add-to-cart-button']";
		String sMainCartbtn = "//*[@id='nav-cart']";
		String sProductTitle = "//span[@class='a-size-medium sc-product-title a-text-bold']";
				//*[@id='activeCartViewForm']/div[2]/div/div[4]/div/div[1]/div/div/div[2]/ul/li[1]/span/a/span"
		     	//*[@id="activeCartViewForm"]/div[1]/div[1]/div[2]/div/div/div/div[4]/div/div[1]/div/div/div[2]/ul/li[1]/span/a/span
		
		String sproductPrice = "//*[@id='activeCartViewForm']//span[@class='a-size-medium a-color-price sc-price sc-white-space-nowrap sc-product-price sc-price-sign a-text-bold']";
		String sproductQty = "//select[@name='quantity']";
		
				
		String sFinalPrice = "//*[@id='gutterCartViewForm']/div[3]/div/div/div[1]/p/span/span[2]";
		
		String sStringtoSearch = "Apple iPhone";
		
		driver.findElement(By.xpath(sSearchtxt)).sendKeys(sStringtoSearch);
		System.out.println("Entered the Searched criteria ");
		
		driver.findElement(By.xpath(sSearchBtn)).click();
		System.out.println("clicked the search button");
		
		driver.findElement(By.xpath(sSearchedItem)).click();
		System.out.println("clicked the searched Item");
		
		driver.findElement(By.xpath(sAddtoCart)).click();
		System.out.println("clicked the AddtoCart Button");
		
		driver.findElement(By.xpath(sMainCartbtn)).click();
		System.out.println("clicked main cart button");
		
		String sActualproductText = driver.findElement(By.xpath(sProductTitle)).getText();
		System.out.println(" Actual Product Title is : " + sActualproductText);
		
		String sActualproductPrice = driver.findElement(By.xpath(sproductPrice)).getText();
		System.out.println(" Actual Product Price is :" + sActualproductPrice);
		
		String sActualfinalPrice = driver.findElement(By.xpath(sFinalPrice)).getText();
		System.out.println(" Actual Final Price is :" + sActualfinalPrice);
		
		Select oSelect = new Select(driver.findElement(By.xpath(sproductQty)));				
		String sActualproductQty = oSelect.getFirstSelectedOption().getText();
		System.out.println(" Actual Product Qty " + sActualproductQty.trim());
		
		oSelect.selectByIndex(1);
		System.out.println("Selected the Qty value to 2:");
		
		waitforelementIgnoreException(driver.findElement(By.xpath(sproductQty)));
		
		sActualproductQty = oSelect.getFirstSelectedOption().getText();
		System.out.println("Updated Actual Product Qty " + sActualproductQty.trim());
		
		// verify the updated final price
		
		waitforelementIgnoreException(driver.findElement(By.xpath(sFinalPrice)));
		
		waitforelementByText(By.xpath(sFinalPrice),sActualproductPrice);
		
		//driver.manage().timeouts().implicitlyWait(10, arg1)
		
		String sActualfinalPrice2 = driver.findElement(By.xpath(sFinalPrice)).getText();
		
		System.out.println(" Updated Actual Final Price " + sActualfinalPrice2);
		
		
		
	}
	
	@AfterTest
	public void teardown(){
		if (driver != null)
			driver.quit();
	}
	
	public void waitforelementByText(By element,String sActualfinalPrice){
		WebDriverWait wait = new WebDriverWait(driver,40);
		//wait.until(ExpectedConditions.attributeContains(element,"Text","Value"));
		//wait.until(ExpectedConditions.textToBePresentInElement(element, element.getText()));
		//	elementToBeSelected(element));
		wait.until(ExpectedConditions.invisibilityOfElementWithText(element, sActualfinalPrice));
		System.out.println("In method waitforelementByText"); 
	}
	
	@SuppressWarnings("deprecation")
	public void waitforelementIgnoreException(WebElement element){
		Wait wait = new FluentWait(driver).withTimeout(30, TimeUnit.SECONDS).pollingEvery(5, TimeUnit.SECONDS).ignoring(StaleElementReferenceException.class,NoSuchElementException.class);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		System.out.println("In method waitforelementIgnoreException");
	}
	

}
