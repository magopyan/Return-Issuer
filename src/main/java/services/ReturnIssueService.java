package services;

import backoffice_credentials.BackOfficeUser;
import backoffice_credentials.Package;
import enums.AutoAcceptMode;
import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import utils.FormOpener;

import java.io.File;
import java.util.List;

public class ReturnIssueService {


    private static WebDriver webDriver;

    private static String driverAvailable = "AVAILABLE";
    private static String returnPickReason = "private_reason_key_device_issue";

    static {  // set up ChromeDriver, executes only once

        try {
            String directory = System.getProperty("user.dir") + File.separator + "chromedriver.exe";
            System.setProperty("webdriver.chrome.driver", directory);
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            webDriver = new ChromeDriver(options);
        }
        catch (Exception e) {
            FormOpener.openAlert("Error", e.getMessage() + "\r\n Please contact the developer!");
        }
    }


    // Main Method combining all the other methods in this class
    public static String issueReturn(Package returnPackage) throws NoSuchElementException {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String url = returnPackage.getPackageUrl();
        webDriver.get(url);
        String actualUrl = webDriver.getCurrentUrl();
        if (url.equals(actualUrl) == false) { // if we cannot directly reach the package page, we have to log in
            BOLogin();
            if (actualUrl.equals(webDriver.getCurrentUrl())) { // if the new URL is still the same login page
                FormOpener.openAlert("BO login failed", "Unable to log into BackOffice, please edit your login info!");
                return "";
            }
        }
        if (fromPackageGoToEditDriver(true) == true) {
            boolean initiallyOff = switchDriverAutoAccept(AutoAcceptMode.on); // if the driver's Auto Accept is Off
            createReturnJob(returnPackage.getReturnReason());
            markReturnAsPicked();

            if (initiallyOff) {
                fromPackageGoToEditDriver(false);
                switchDriverAutoAccept(AutoAcceptMode.off);
            }
            double time = stopWatch.getTime();
            return "Return took " + time/1000 + "s";
        }
        else
            return "";
    }


    private static void BOLogin() throws NoSuchElementException {

        BackOfficeUser user = BackOfficeUser.getBOUserInstance();
        WebElement emailField = webDriver.findElement(By.id("admin_user_email"));
        WebElement passwordField = webDriver.findElement(By.id("admin_user_password"));
        WebElement loginButton = webDriver.findElement(By.name("commit"));

        emailField.sendKeys(user.getEmail());
        passwordField.sendKeys(user.getPassword());
        loginButton.click();
        // then automatic redirect to Package page
    }


    private static boolean fromPackageGoToEditDriver(boolean checkIfInvitable) throws NoSuchElementException {

        WebElement driversTable = webDriver.findElement(By.xpath("//*[@id=\"main_content\"]/div[2]/div/table/tbody"));
        List<WebElement> rows = driversTable.findElements(By.tagName("tr"));

        try {
            WebElement driverHref = webDriver.findElement
                    (By.xpath("/html/body/div[1]/div[4]/div[1]/div/div[2]/div/table/tbody/tr["+rows.size()+"]/td[3]/a"));
            driverHref.click();
        }
        catch (Exception e) {
            FormOpener.openAlert("No driver assigned", "There is no driver assigned to this order!");
            return false;
        }

        if(checkIfInvitable == true) {
            WebElement availability = webDriver.findElement(By.xpath("/html/body/div[1]/div[4]/div[2]/div[1]/div/div/table/tbody/tr[2]/td/span"));
            if(driverAvailable.equals(availability.getText()) == false) {
                FormOpener.openAlert("Driver unavailable", "The driver has gone offline and as unavailable for a return!");
                return false;
            }
        }

        WebElement editDriverButton = webDriver.findElement(By.xpath("//*[@id=\"titlebar_right\"]/div/span[1]/a"));
        editDriverButton.click();
        return true;
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

        WebElement dropdownList = webDriver.findElement(By.id("driver_active_admin_auto_accept_mode"));
        Select select = new Select(dropdownList);
        WebElement selectedOption = select.getFirstSelectedOption();

        boolean turnBackOffLater = false;
        if(!selectedOption.getText().equals(switchTo.toString())) {
            turnBackOffLater = true;
            select.selectByValue(switchTo.toString());
            WebElement commitDriverButton = webDriver.findElement(By.xpath("//*[@id=\"driver_submit_action\"]/input"));
            commitDriverButton.click();
        }

        if(turnBackOffLater) { // if successfully switched from OFF to ON, return to package page
            webDriver.navigate().back();
            webDriver.navigate().back();
            webDriver.navigate().back();
        }
        else {   // if driver already has the wished AA mode, return to package page
            webDriver.navigate().back();
            webDriver.navigate().back();
        }
        return turnBackOffLater;
    }


    private static void createReturnJob(String reason) throws NoSuchElementException {

        WebElement packageReturnButton = webDriver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div/span[6]/a"));
        packageReturnButton.click();

        WebElement radioButtonChoice = webDriver.findElement(By.id(reason));
        radioButtonChoice.click();

        // SAFE MODE OFF - will issue a return
        WebElement issueReturnButton = webDriver.findElement(By.name("commit"));
        issueReturnButton.click();

        // SAFE MODE ON - for testing, goes to the return page but doesn't launch the return
        // webDriver.navigate().back();
    }


    private static void markReturnAsPicked() {

        try{
            WebElement deliveriesTable = webDriver.findElement(By.xpath("/html/body/div[1]/div[4]/div[1]/div/div[3]/div/table/tbody"));
            List<WebElement> rows = deliveriesTable.findElements(By.tagName("tr"));
            WebElement returnPickupRow = rows.get(rows.size() - 2);

            WebElement markAsPickedButton = returnPickupRow.findElement(By.id("task_modal_link"));
            markAsPickedButton.click();
            WebElement radioButtonChoice = webDriver.findElement(By.id(returnPickReason));
            radioButtonChoice.click();
            WebElement doneButton = webDriver.findElement(By.name("commit"));
            doneButton.click();
        }
        catch (Exception e) {
            FormOpener.openAlert("Error", e.getMessage() + "\r\n Please contact the developer!");
        }
    }
}
