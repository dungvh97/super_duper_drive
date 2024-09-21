package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.util.List;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		
		// Redirect to login page, add success-msg to login.html
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","testRedirection","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
	}


	/**
	 * Write a test that verifies that an unauthorized user can only access the login and signup pages.
	 **/
	@Test
	public void unauthorizedAccessRestrictions() {
		driver.get("http://localhost:" + this.port + "/viewNote");
		Assertions.assertEquals("Login", driver.getTitle());
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}
	
	/**
	 * Write a test that signs up a new user, logs in, verifies that the home page is accessible, logs out, 
	 * and verifies that the home page is no longer accessible.
	 **/
	@Test
	public void testLogout() {
		// Create a test account
		doMockSignUp("Large File","Test","testLogout","123");
		doLogIn("LFT", "123");
		driver.get("http://localhost:" + this.port + "/home");
		WebElement buttonLogout = driver.findElement(By.id("buttonLogout"));
		buttonLogout.click();
		Assertions.assertNotEquals("Home", driver.getTitle());
		
	}
	
	/**
	 * Write a test that creates a note, and verifies it is displayed.
	 **/
	@Test
	public void testCreateNote() {
		// Create a test account
		doMockSignUp("Large File","Test","testCreateNote","123");
		doLogIn("testCreateNote", "123");
		// Create note
		createNote("testCreateNote", "noteDescription");
		// go to note tab
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();
		// Check if the note appears
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title-display")));
		Assertions.assertTrue(driver.findElement(By.id("note-title-display")).getText().contains("testCreateNote"));
	}
	
	/**
	 * Write a test that edits an existing note and verifies that the changes are displayed.
	 **/
	@Test
	public void testEditNote() {
		// Create a test account
		doMockSignUp("Large File","Test","testEditNote","123");
		doLogIn("testEditNote", "123");
		// Create note
		createNote("testEditNote", "noteDescription");

		// Do edit note
		// Go to note tab
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement notesTab= driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
		// Press on edit note button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonEditNote")));
		WebElement buttonEditNote= driver.findElement(By.id("buttonEditNote"));
		buttonEditNote.click();
		// Fill out the note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement inputTitle = driver.findElement(By.id("note-title"));
		inputTitle.click();
		inputTitle.clear();
		inputTitle.sendKeys("new-note-title");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement inputDescription = driver.findElement(By.id("note-description"));
		inputDescription.click();
		inputDescription.clear();
		inputDescription.sendKeys("new-note-title");

		// Submit the note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonNoteSave")));
		WebElement saveNote = driver.findElement(By.id("buttonNoteSave"));
		saveNote.click();
		
		// Check if the new note appears
		// Go to note tab
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title-display")));
		Assertions.assertTrue(driver.findElement(By.id("note-title-display")).getText().contains("new-note-title"));
	}
	
	/**
	 * Write a test that deletes a note and verifies that the note is no longer displayed.
	 **/
	@Test
	public void testDeleteNote() {
		// Create a test account
		doMockSignUp("Large File","Test","testDeleteNote","123");
		doLogIn("testDeleteNote", "123");
		// Create note
		createNote("testDeleteNote", "noteDescription");
		// Go to note tab
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();

		// Delete note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonDeleteNote")));
		WebElement buttonDeleteNote = driver.findElement(By.id("buttonDeleteNote"));
		buttonDeleteNote.click();
		
		// Navigation and confirm
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		WebElement noteTable = driver.findElement(By.id("userTable"));
		List<WebElement> noteList = noteTable.findElements(By.tagName("tbody"));

		Assertions.assertEquals(0, noteList.size());
	}
	
	private void createNote(String noteTitle, String noteDescription) {
		driver.get("http://localhost:" + this.port + "/home");
		
		// Go to note-tab
		WebElement notesTab= driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));

		// Press on add note button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonAddNote")));
		WebElement addNoteButton= driver.findElement(By.id("buttonAddNote"));
		addNoteButton.click();

		// Fill out the note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement inputTitle = driver.findElement(By.id("note-title"));
		inputTitle.click();
		inputTitle.sendKeys(noteTitle);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement inputDescription = driver.findElement(By.id("note-description"));
		inputDescription.click();
		inputDescription.sendKeys(noteDescription);

		// Submit the note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonNoteSave")));
		WebElement saveNote = driver.findElement(By.id("buttonNoteSave"));
		saveNote.click();
	}
	
	/**
	 * Write a test that creates a set of credentials, verifies that they are displayed, 
	 * and verifies that the displayed password is encrypted
	 **/
	@Test
	public void testCreateCredential() {
		// Create a test account
		doMockSignUp("Large File","Test","testCreateCredential","123");
		doLogIn("testCreateCredential", "123");
		// Create credential
		createCredential("url", "username", "password");

		// Go to credentials tab
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
		
		// Check if the credentials appears
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		Assertions.assertEquals(driver.findElement(By.id("table-cred-url")).getText(), "url");

		// Check if the password shown in table is encrypted
		Assertions.assertNotEquals(driver.findElement(By.id("table-cred-password")).getText(), "password");
	}
	
	/**
	 * Write a test that views an existing set of credentials, verifies that the viewable password is unencrypted, 
	 * edits the credentials, and verifies that the changes are displayed.
	 **/
	@Test
	public void testEditCredential() {
		// Create a test account
		doMockSignUp("Account","Test","testEditCredential","123");
		doLogIn("testEditCredential", "123");
		// Create credential
		createCredential("url", "username", "password");

		// Do edit credential
		// Go to tab
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		// Press on edit button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonEditCredential")));
		WebElement buttonEditCredential = driver.findElement(By.id("buttonEditCredential"));
		buttonEditCredential.click();
		
		// Verifies that the viewable password is unencrypted
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModalLabel")));
		Assertions.assertEquals(driver.findElement(By.id("credential-password")).getAttribute("value"), "password");
		
		// Fill out the credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement inputURL = driver.findElement(By.id("credential-url"));
		inputURL.click();
		inputURL.clear();
		inputURL.sendKeys("new url");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement inputUsername = driver.findElement(By.id("credential-username"));
		inputUsername.click();
		inputUsername.clear();
		inputUsername.sendKeys("new username");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement inputPassword = driver.findElement(By.id("credential-password"));
		inputPassword.click();
		inputPassword.clear();
		inputPassword.sendKeys("new password");

		// Submit the credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonCredentialSave")));
		WebElement buttonCredentialSave = driver.findElement(By.id("buttonCredentialSave"));
		buttonCredentialSave.click();
		
		// Check if the new credential appears
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		Assertions.assertEquals(driver.findElement(By.id("table-cred-url")).getText(), "new url");
	}
	
	/**
	 * Write a test that deletes an existing set of credentials and verifies that the credentials are no longer displayed.
	 **/
	@Test
	public void testDeleteCreadential() {
		// Create a test account
		doMockSignUp("Account","Test","testDeleteCreadential","123");
		doLogIn("testDeleteCreadential", "123");
		// Create credential
		createCredential("url", "username", "password");
		// Go to tab
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		// Press on delete button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonDeleteCredential")));
		WebElement buttonDeleteCredential = driver.findElement(By.id("buttonDeleteCredential"));
		buttonDeleteCredential.click();
		
		// Navigation and confirm
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		WebElement credentialTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> noteList = credentialTable.findElements(By.tagName("tbody"));

		Assertions.assertEquals(0, noteList.size());
	}
	
	private void createCredential(String url, String username, String password) {
		// Go to credentials tab
		WebElement credentialsTab= driver.findElement(By.id("nav-credentials-tab"));
		credentialsTab.click();

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		// Press on add credentials button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonAddCredential")));
		WebElement buttonAddCredential= driver.findElement(By.id("buttonAddCredential"));
		buttonAddCredential.click();

		// Fill out the credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement inputURL = driver.findElement(By.id("credential-url"));
		inputURL.click();
		inputURL.sendKeys(url);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement inputUsername = driver.findElement(By.id("credential-username"));
		inputUsername.click();
		inputUsername.sendKeys(username);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement inputPassword = driver.findElement(By.id("credential-password"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt submit the credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonCredentialSave")));
		WebElement buttonCredentialSave = driver.findElement(By.id("buttonCredentialSave"));
		buttonCredentialSave.click();
	}
}
