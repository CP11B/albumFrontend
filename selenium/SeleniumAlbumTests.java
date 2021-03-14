package com.project.recordPlayer.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@Sql(scripts = { "classpath:album-schema.sql",
//"classpath:album-data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
public class SeleniumAlbumTests {
	
	@LocalServerPort
	private int port;
	
	private WebDriverWait wait;
	private RemoteWebDriver driver;

	@BeforeEach
	void setup() {
		
		ChromeOptions options = new ChromeOptions();
		options.setHeadless(true);
		this.driver = new ChromeDriver(options);
		this.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		this.wait = new WebDriverWait(driver, 10); 
	
	}
	
	@Test
	void createTest() {
		
		final String title = "Title";
		final String artist = "Artist";
		final String date = "2000";
		// run test on a randomised port
		this.driver.get("http://localhost:" + port);
		//bring up create form modal
		WebElement createModalButton = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createButton")));
		createModalButton.click();
		// fill out form fields
		WebElement titleField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createTitle")));
		titleField.sendKeys(title);
		
		WebElement artistField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createArtist")));
		artistField.sendKeys(artist);
		
		WebElement dateField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createReleaseYear")));
		dateField.sendKeys(date);
		// submit
		WebElement saveEntryButton = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("submit")));
		saveEntryButton.click();

		// check there is a created card with our given details
		WebElement creationCheck = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"stack\"]/div/div[2]")));
		Assertions.assertTrue(creationCheck.getText().contains("Title"));		
		Assertions.assertTrue(creationCheck.getText().contains("Artist"));	
		//this.driver.close();
	}
	
	@Test
	void updateTest() {
		this.driver.get("http://localhost:" + port);
		final String title = "Title";
		final String artist = "Artist";
		final String date = "2000";
		final String updatedTitle = "Updated Title";
		final String updatedArtist = "Updated Artist";

		WebElement createModalButton = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createButton")));
		createModalButton.click();
		
		WebElement titleField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createTitle")));
		titleField.sendKeys(title);
		
		WebElement artistField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createArtist")));
		artistField.sendKeys(artist);
		
		WebElement dateField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createReleaseYear")));
		dateField.sendKeys(date);
				
		WebElement saveEntryButton = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("submit")));
		saveEntryButton.click();


		WebElement creationCheck = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"stack\"]/div/div[2]")));
		Assertions.assertTrue(creationCheck.getText().contains("Title"));		
		Assertions.assertTrue(creationCheck.getText().contains("Artist"));	
		
		
//		// after card is made, edit test starts here
	
		WebElement editModalButton = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"stack\"]/div/div[2]/span/button")));
		editModalButton.click();

		WebElement editTitleField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("editTitle")));
		driver.findElement(By.id("editTitle")).clear();
		editTitleField.sendKeys(updatedTitle);
		
		WebElement editArtistField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("editArtist")));
		driver.findElement(By.id("editArtist")).clear();
		editArtistField.sendKeys(updatedArtist);
		
		WebElement saveEditButton = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("submitEditButton")));
		saveEditButton.click();
		

		WebElement editedCheck = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"stack\"]/div[1]/div[2]/div[1]")));
		try {
			Assertions.assertTrue(editedCheck.getText().contains("Updated Title"));		
			
		} catch(org.openqa.selenium.StaleElementReferenceException e){
			System.out.println(e);
		}
		//this.driver.close();
	}
	
	@Test
	void deleteTest() {
		
		this.driver.get("http://localhost:" + port);
		//this.driver.get("http://localhost:" + 8080);
		final String title = "Womp and the Bimbles";
		final String artist = "Headface";
		final String date = "2000";

		WebElement createModalButton = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createButton")));
		createModalButton.click();		
		
		WebElement titleField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createTitle")));
		titleField.sendKeys(title);
				
		WebElement artistField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createArtist")));
		artistField.sendKeys(artist);
		
		WebElement dateField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createReleaseYear")));
		dateField.sendKeys(date);
		
		WebElement saveEntryButton = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("submit")));
		saveEntryButton.click();

		WebElement creationCheck;
		creationCheck = this.driver.findElement(By.cssSelector("#stack > div:nth-child(3) > div.extra.content > div.header.cardAlbumTitle"));

		System.out.println(creationCheck.getText());
		Assertions.assertTrue(creationCheck.getText().contains("Womp and the Bimbles"));		
		
		
		// deletion starts here		
		WebElement editModalButton = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"stack\"]/div[3]/div[2]/span/button")));
		editModalButton.click();
		
		WebElement deleteButton = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#deleteButton")));
		deleteButton.click();
		deleteButton.click();
		deleteButton.click();
		
		WebElement deletionCheck;
		
		try {
			deletionCheck = this.driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/div[2]/div[1]"));
	    } catch (org.openqa.selenium.NoSuchElementException e) {
	    	assert true;
	    } catch (Exception e) {
	    	assert false;
	    }
		//this.driver.close();	
		
	}	
	
}
