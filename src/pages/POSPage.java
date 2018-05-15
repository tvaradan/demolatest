package pages;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.HasDeviceDetails;
import utility.Initialize;

public class POSPage extends Initialize{
	
	/*public POSPage()
	{
        new Generic().checkPageHealth();
        System.out.println(driver.getTitle());
        
        //Checking if Site is under Maintenance
        if (waitForObject("xpath", 1, getxPath("site_maintenance")).equals("true"))
         {
                  System.out.println("Site is under Maintenance");                  
                  throw new Error("Site is under Maintenance");
         }

	}*/
	
	Boolean blnMethodFlag;
	
	//Following are the objects which are used in this page
	public String strPosHead= getxPath("pos_header");
	public String strPosDet= getxPath("pos_details");
	public String strGreetings= getxPath("pos_greetings");
	
	//Buttons from Crust
	public String btThinCrust= getxPath("but_thin_crust");
	public String btDeepDish= getxPath("but_deep_dish");
	public String btNakedChk= getxPath("but_naked_chicken");
	public String btCaribbJerk= getxPath("but_carribb_jerk");
	public String btCheesyThin= getxPath("but_cheesy_thin");
	
	//Buttons from Beverages
	public String btColdCoff= getxPath("but_cold_coffee");
	public String btHotCoff= getxPath("but_hot_coff");
	public String btCoke= getxPath("but_coke");
	public String btDietCoke= getxPath("but_diet_coke");
	public String btPepsi= getxPath("but_pepsi");
	
	public String btCheckOut= getxPath("but_CheckOut");
	
	//Added Items
	//public String btColdCoff= getxPath("but_cold_coffee");

	/***********************************************************
	//Method Name: checkExistance
	//Description: Component used to validate if user navigates to checkout screen
	//Created By:  Cognizant Technology Solution
	//Return Type: None
	//Date of Creation: 15/11/2017
	 * @throws Exception 
	************************************************************/
	
	public void checkExistance (String objProperty, String strLogicalName) throws Exception
	{
		try
		{
			System.out.println("Executing Business Component - checkExistance ....");
			logger.log(LogStatus.INFO, "Business Component", "Executing checkExistance - to test for the existance of certain mandatory objects");
			
			List<WebElement> ElemList = null;
			
			ElemList=driver.findElements(By.xpath(objProperty));
			if(ElemList.size() == 1) {
				logger.log(LogStatus.PASS, "Check Existance - Is the element present?", "The element " + strLogicalName + " has been properly loaded and is visible");
				System.out.println(strLogicalName + " has been properly loaded and is visible");
			}else {
				logger.log(LogStatus.PASS, "Check Existance - Is the element present?", "The element " + strLogicalName + " has been properly loaded and is visible");
				System.out.println(strLogicalName + " has been properly loaded and is visible");
			}
			
		}catch (Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Error: "+ex.getMessage());
		}
	}
	
	/***********************************************************
	//Method Name: checkHeader
	//Description: Component used to validate the header section
	//Created By:  Cognizant Technology Solution
	//Return Type: None
	//Date of Creation: 05/12/2017
	 * @throws Exception 
	************************************************************/
	public void checkHeader() throws Exception {
		
		POSPage posPage = new POSPage();
		logger.log(LogStatus.INFO, "=======================", "============== Objects in the Header Section =============");
		posPage.checkExistance(posPage.strPosHead, "Header");
		posPage.checkExistance(posPage.strPosDet, "Details");
		posPage.checkExistance(posPage.strGreetings, "Greetings Message");
		logger.log(LogStatus.INFO, "=======================", "==========================================================");
		
	}
	
	
	/********************************************************************
	//Method Name: checkCrustSection
	//Description: Component used to validate the Crust/Toppings section
	//Created By:  Cognizant Technology Solution
	//Return Type: None
	//Date of Creation: 05/12/2017
	 * @throws Exception 
	*********************************************************************/
	public void checkCrustSection() throws Exception {
		
		POSPage posPage = new POSPage();
		logger.log(LogStatus.INFO, "=======================", "========= Objects in the Crust/Toppings Section ==========");
		posPage.checkExistance(posPage.btThinCrust, "Thin Crust button");
		posPage.checkExistance(posPage.btDeepDish, "Deep Dish button");
		posPage.checkExistance(posPage.btNakedChk, "Naked Chicken button");
		posPage.checkExistance(posPage.btCaribbJerk, "Carribbean Jerk button");
		posPage.checkExistance(posPage.btCheesyThin, "Cheesy Thin button");
		logger.log(LogStatus.INFO, "=======================", "==========================================================");
		
	}
	
	
	/********************************************************************
	//Method Name: checkBevSection
	//Description: Component used to validate the Beverages section
	//Created By:  Cognizant Technology Solution
	//Return Type: None
	//Date of Creation: 05/12/2017
	 * @throws Exception 
	*********************************************************************/
	public void checkBevSection() throws Exception {
		
		POSPage posPage = new POSPage();
		logger.log(LogStatus.INFO, "=======================", "========= Objects in the Beverages/Drinks Section ========");
		posPage.checkExistance(posPage.btColdCoff, "Cold Coffee button");
		posPage.checkExistance(posPage.btHotCoff, "Hot Coffee button");
		posPage.checkExistance(posPage.btCoke, "Coke button");
		posPage.checkExistance(posPage.btDietCoke, "Diet Coke button");
		posPage.checkExistance(posPage.btPepsi, "Pepsi button");
		logger.log(LogStatus.INFO, "=======================", "==========================================================");
		
	}
	
	/********************************************************************
	//Method Name: placeOrder
	//Description: Component used to place an order
	//Created By:  Cognizant Technology Solution
	//Return Type: None
	//Date of Creation: 05/12/2017
	 * @throws Exception 
	*********************************************************************/
	public void placeOrder() throws Exception {
		
		POSPage posPage = new POSPage();
		//Place an Order and Checkout
		logger.log(LogStatus.INFO, "=======================", "========= Place an Order and Checkout ========");
		driver.findElement(By.xpath(posPage.btCheesyThin)).click();
		waiting(5);
		if(driver.findElement(By.xpath("//li[contains(.,'Cheesy Thin')]")).isDisplayed()) {
			logger.log(LogStatus.PASS, "Order Cheesy Thin", "========= An Order for Cheesy Thin has been placed, and is present in the Order Summary ========");
		}else {
			logger.log(LogStatus.FAIL, "Order Cheesy Thin", "========= The Order was niy successfully ========");
		}
		
		driver.findElement(By.xpath(posPage.btCaribbJerk)).click();
		waiting(5);
		if(driver.findElement(By.xpath("//li[contains(.,'Carribean Jerk')]")).isDisplayed()) {
			logger.log(LogStatus.PASS, "Carribean Jerk", "========= An Order for Carribbean Jerk has been placed, and is present in the Order Summary ========");
		}else {
			logger.log(LogStatus.FAIL, "Carribean Jerk", "========= The Order was not placed successfully ========");
		}
		//driver.findElement(By.xpath(posPage..)).click();
		driver.findElement(By.xpath(posPage.btColdCoff)).click();
		waiting(5);
		if(driver.findElement(By.xpath("//li[contains(.,'Cold Coffee')]")).isDisplayed()) {
			logger.log(LogStatus.PASS, "Cold Coffee", "========= An Order for Cold Coffee has been placed, and is present in the Order Summary ========");
		}else {
			logger.log(LogStatus.FAIL, "Cold Coffee", "========= The Order was not placed successfully ========");
		}
		driver.findElement(By.xpath(posPage.btPepsi)).click();
		waiting(5);
		if(driver.findElement(By.xpath("//li[contains(.,'Pepsi')]")).isDisplayed()) {
			logger.log(LogStatus.PASS, "Pepsi", "========= An Order for Pepsi has been placed, and is present in the Order Summary ========");
		}else {
			logger.log(LogStatus.FAIL, "Pepsi", "========= The Order was not placed successfully ========");
		}
		driver.findElement(By.xpath(posPage.btCheckOut)).click();
		logger.log(LogStatus.INFO, "=======================", "==========================================================");
		
		WebDriverWait wait = new WebDriverWait(driver, 5);
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		//Accepting alert.
		alert.accept();
		waiting(2);
		
		//String strTotal = driver.findElement(By.xpath("//div[contains(.,'Total')]")).getText();
		//logger.log(LogStatus.PASS, "Pepsi", "========= " + strTotal + " ========");
	}
	
	


	
	
}
