package com.fitpeo.testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;



public class HomepageTestCases{
	
	WebDriver driver;
	
	@BeforeMethod
	public void launchBrowser() {
		driver.get("https://fitpeo.com/");
		driver.manage().window().maximize();
	}
	
	@Test
	public void navigateToHomepage() {
		driver.findElement(By.xpath("//div[@class='satoshi MuiBox-root css-1s5s2kl']")).click();
		String currentURL = driver.getCurrentUrl();
		System.out.println(currentURL);
		String expURL = "https://www.fitpeo.com/home";
		Assert.assertEquals(currentURL, expURL,"Not navigated to homepage");
		
	}
	@Test(dependsOnMethods = "navigateToHomepage")
	public WebDriver navigateToRevenueCal() {
		driver.findElement(By.xpath("//div[contains(text(),'Revenue')]")).click();
		String curUrl = driver.getCurrentUrl();
		String exUrl ="https://www.fitpeo.com/revenue-calculator";
		Assert.assertEquals(curUrl, exUrl,"Not navigated to revenue page");
		return driver;
		
	}
	
	@AfterMethod
	public void closeBrowser() {
		
		driver.quit();
	}
	
}
