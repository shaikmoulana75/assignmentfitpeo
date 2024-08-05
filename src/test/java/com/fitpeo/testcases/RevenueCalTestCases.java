package com.fitpeo.testcases;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RevenueCalTestCases {

	WebDriver driver;

	@BeforeMethod
	public void launchBrowser() {

		driver = new ChromeDriver();
		driver.get("https://fitpeo.com/");
		driver.manage().window().maximize();
	}

	@Test(priority = 1)
	public void openRevenueCalPage() throws IOException {
		driver.findElement(By.xpath("//div[contains(text(),'Revenue')]")).click();
		// Refresh page
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		// Find slider
		WebElement element = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class, 'MuiSlider-root')]")));
        
		//Scroll to element
		jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);

		if (element.isDisplayed()) {
			
			// Take Screenshot to verify
			TakesScreenshot screenshot = ((TakesScreenshot) driver);
			File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
			File destFile = new File(System.getProperty("user.dir") + "/screenshots/" + "scrollverify.png");
			FileUtils.copyFile(srcFile, destFile);
			System.out.println("Slider visible and took screenshot");		
		}
		else {
			System.out.println("Not scrolled");
		}

	}

	@Test(priority = 2)
	public void verifySlider() {
		driver.findElement(By.xpath("//div[contains(text(),'Revenue')]")).click();
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		WebElement element = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class, 'MuiSlider-root')]")));
		jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
		System.out.println("Width of slider=" + element.getSize().getWidth());

		int sliderWidth = element.getSize().getWidth(); // Width of the slider in pixels
		int minValue = 0;
		int maxValue = 2000;
		int targetValue = 820; 

		// Calculate the target position in pixels
		int positionPx = (int) (((double) (targetValue - minValue) / (maxValue - minValue)) * sliderWidth);
		
		WebElement slidePoint = driver
				.findElement(By.xpath("(//*[contains(@class, 'MuiSlider-root')]/child::span)[3]/input"));
		
		WebElement firstSlide =driver.findElement(By.xpath("(//*[contains(@class, 'MuiSlider-root')]/span)[2]"));
		int handleWidth = firstSlide.getSize().getWidth();
		System.out.println(handleWidth);
		Actions actions = new Actions(driver);
		actions.clickAndHold(slidePoint).moveByOffset(positionPx - handleWidth / 2, 0).release().perform();
	}

	@Test(priority = 3)
	public void updateTextInput() throws InterruptedException {
		driver.findElement(By.xpath("//div[contains(text(),'Revenue')]")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[contains(@class, 'MuiSlider-root')]/following-sibling::div/div/input")));

		element.sendKeys(Keys.BACK_SPACE);
		element.sendKeys(Keys.BACK_SPACE);
		element.sendKeys(Keys.BACK_SPACE);

		Thread.sleep(3000);
		element.sendKeys("560" + Keys.CONTROL.ENTER);
		WebElement slidePoint = driver
				.findElement(By.xpath("(//*[contains(@class, 'MuiSlider-root')]/child::span)[3]/input"));

		String actVal = slidePoint.getAttribute("value");
		System.out.println(actVal);

		String exVal = "560";

		// Verify input updated to 560		
		Assert.assertEquals(actVal, exVal, "Not set to 560");

	}

	@Test(priority = 4)
	public void checkboxValidate() throws InterruptedException {

		driver.findElement(By.xpath("//div[contains(text(),'Revenue')]")).click();
		driver.get("https://www.fitpeo.com/revenue-calculator");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[contains(@class, 'MuiSlider-root')]/following-sibling::div/div/input")));
		element.click();

		element.sendKeys(Keys.BACK_SPACE);
		element.sendKeys(Keys.BACK_SPACE);
		element.sendKeys(Keys.BACK_SPACE);

		Thread.sleep(3000);
		element.sendKeys("820" + Keys.CONTROL.ENTER);

		WebElement cb1 = driver
				.findElement(By.xpath("//p[contains(text(),'CPT-99091')]/parent::div/descendant::input"));
		cb1.click();
		WebElement cb2 = driver
				.findElement(By.xpath("//p[contains(text(),'CPT-99453')]/parent::div/descendant::input"));
		cb2.click();
		WebElement cb3 = driver
				.findElement(By.xpath("//p[contains(text(),'CPT-99454')]/parent::div/descendant::input"));
		cb3.click();
		WebElement cb4 = driver
				.findElement(By.xpath("//p[contains(text(),'CPT-99474')]/parent::div/descendant::input"));
		cb4.click();

		// verify checkboxes are checked
		boolean cb1checked = cb1.isSelected();
		boolean cb2checked = cb2.isSelected();
		boolean cb3checked = cb3.isSelected();
		boolean cb4checked = cb4.isSelected();

		Assert.assertTrue(cb1checked, "Not checked");
		Assert.assertTrue(cb2checked, "Not checked");
		Assert.assertTrue(cb3checked, "Not checked");
		Assert.assertTrue(cb4checked, "Not checked");

		// validating total reimbursement amount
		WebElement amountTotal = driver.findElement(By.xpath(
				"(//*[contains(@class, 'MuiSlider-root')]/ancestor::div[3]/preceding-sibling::div/child::div/div)[3]/p[2]"));

		String actAmount = amountTotal.getText();
		System.out.println(actAmount);
		String expAmount = "$110700";
		Assert.assertEquals(actAmount, expAmount);

		Thread.sleep(3000);
		// Validating on header

		WebElement headerAmount = driver
				.findElement(By.xpath("(//p[contains(text(),'CPT-99454')]/ancestor::div[3]/header/div/p)[4]/p"));
		String actTotalAmount = headerAmount.getText();
		System.out.println(actTotalAmount);
		String expTotalAmount = "$110700";
		Assert.assertEquals(actTotalAmount, expTotalAmount);
	}

	@AfterMethod
	public void quitBroswer() {
		driver.quit();
	}

}
