package com.browserstack;

import org.openqa.selenium.By;

import org.testng.annotations.Test;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.*;

public class BStackSampleTest extends BrowserStackTest {


	@Test
	public void TC_01_Url() throws InterruptedException {
			driver.get("https://aurascan.io/");
			Thread.sleep(3000);
			driver.findElement(By.cssSelector("span.fw-normal")).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath("//input[@type='email']")).sendKeys("hainam11993@gmail.com");
			driver.findElement(By.xpath("//input[@class='ng-untouched ng-pristine ng-invalid']")).sendKeys("Hainam12@");
			driver.findElement(By.xpath("//button[@class='button button-pill button-flat w-100 btn-login button--lg border-radius--sm']")).click();
			//assert.assertEquals(driver.findElement(By.xpath("//div[text()='Profile settings']")).getText(),"Profile settings");
			}
}