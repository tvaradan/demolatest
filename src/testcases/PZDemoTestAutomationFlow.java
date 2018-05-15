package testcases;

import java.io.IOException;
import java.util.HashMap;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import pages.POSPage;

import utility.Initialize;

public class PZDemoTestAutomationFlow extends Initialize {

	Initialize init;
	String strTCName = this.getClass().getSimpleName();
	
	
	@Parameters({ "platform" , "browser", "description" })
	@BeforeTest
	public void intiTest(String platform, String browser, String description) throws IOException{
		init = new Initialize();
		init.startTest(platform, browser, strTCName, description);
	}
	
	@Test(dataProvider="dbDataProvider",dataProviderClass=Initialize.class)
	public void runTest(HashMap<String, Object> dataMap) throws Exception {
		mapData = dataMap;
		POSPage posPage = new POSPage();
		posPage.checkHeader();
		posPage.checkCrustSection();
		posPage.placeOrder();
	}


	@AfterMethod
	public void getResult(ITestResult result){
		init.checkResult(result);
	}
	
	
	@AfterTest
    public void tearDown() throws IOException {
		init.endTest();
    }

}
