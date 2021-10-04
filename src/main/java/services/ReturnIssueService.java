package services;

import backoffice_credentials.BackOfficeUser;
import backoffice_credentials.Package;
import enums.AutoAcceptMode;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import utils.FormOpener;

public class ReturnIssueService {


    private static WebDriver driver;

    static {  // set up ChromeDriver, executes only once

        System.setProperty("webdriver.chrome.driver", "D:\\Return_Issuer\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    // Main Method combining all the other methods in this class
    public static boolean issueReturn(Package returnPackage) throws NoSuchElementException
    {

        String url = returnPackage.getPackageUrl();
        driver.get(url);

        String actualUrl = driver.getCurrentUrl();
        if(url.equals(actualUrl) == false)  // if we cannot directly reach the package page, we have to log in
            BOLogin();
        if(actualUrl.equals(driver.getCurrentUrl())) // if the new URL is still the same login page
            FormOpener.openAlert("BO login failed", "Unable to log into BackOffice, please edit your login info!");

        fromPackageGoToEditDriver();
        boolean initiallyOff = switchDriverAutoAccept(AutoAcceptMode.on); // if the driver's Auto Accept is Off
        createReturnJob(returnPackage.getReturnReason());

        if(initiallyOff) {
            fromPackageGoToEditDriver();
            switchDriverAutoAccept(AutoAcceptMode.off);
        };
        return true;
    }


    private static void BOLogin() throws NoSuchElementException {

        BackOfficeUser user = BackOfficeUser.getBOUserInstance();
        WebElement emailField = driver.findElement(By.id("admin_user_email"));
        WebElement passwordField = driver.findElement(By.id("admin_user_password"));
        WebElement loginButton = driver.findElement(By.name("commit"));

        emailField.sendKeys(user.getEmail());
        passwordField.sendKeys(user.getPassword());
        loginButton.click();
        // then automatic redirect to Package page
    }


    private static void fromPackageGoToEditDriver() throws NoSuchElementException {

        WebElement driverHref = driver.findElement(By.xpath("/html/body/div[1]/div[4]/div[1]/div/div[2]/div/table/tbody/tr/td[3]/a"));
        driverHref.click();

        WebElement editDriverButton = driver.findElement(By.xpath("//*[@id=\"titlebar_right\"]/div/span[1]/a"));
        editDriverButton.click();
    }


    private static boolean switchDriverAutoAccept(AutoAcceptMode switchTo) throws NoSuchElementException {

        /*
        If driver is OFF:
        1. Turn AA on
        2. Go back 3 times
        3. Issue return
        4. If AA is still on, turn off

        If driver is ON:
        1. Don't change AA
        2. Go back 2 times
        3. Issue return
        */

        WebElement dropdownList = driver.findElement(By.id("driver_active_admin_auto_accept_mode"));
        Select select = new Select(dropdownList);
        WebElement selectedOption = select.getFirstSelectedOption();

        boolean turnBackOffLater = false;
        if(!selectedOption.getText().equals(switchTo.toString())) {
            turnBackOffLater = true;
            select.selectByValue(switchTo.toString());
            WebElement commitDriverButton = driver.findElement(By.xpath("//*[@id=\"driver_submit_action\"]/input"));
            commitDriverButton.click();
        }

        if(turnBackOffLater) { // if successfully switched from OFF to ON, return to package page
            driver.navigate().back();
            driver.navigate().back();
            driver.navigate().back();
        }
        else {   // if driver already has the wished AA mode, return to package page
            driver.navigate().back();
            driver.navigate().back();
        }

        return turnBackOffLater;
    }


    private static void createReturnJob(String reason) throws NoSuchElementException {

        WebElement packageReturnButton = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div/span[6]/a"));
        packageReturnButton.click();

        WebElement radioButtonChoice = driver.findElement(By.id(reason));
        radioButtonChoice.click();

        // SAFE MODE OFF - will issue a return
        WebElement issueReturnButton = driver.findElement(By.name("commit"));
        issueReturnButton.click();

        // SAFE MODE ON - for testing
        //driver.navigate().back();
    }
}
