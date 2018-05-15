package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.remote.html5.RemoteLocationContext;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;

public class Initialize {

	public static String relativePath;
	public static String strPlatform;
	public static String strBrowser;
	public static Properties prop_Global = new Properties();
	public static WebDriver driver = null;
	public static ExtentReports extentReports = null;
	public static ExtentTest logger;
	public static boolean blnHtmlFlag;
	public static String strExeStartTime = "";
	public static Connection goConn;
	public static Statement goQuery;
	public static ResultSet goResSet;
	//For using data across business components	
	public static HashMap<String, Object> mapData = new HashMap<String, Object>();
	public static String strScreenshot_path = "";
	public static boolean blnTcflag = true;
	public static int imgCounter = 1;
	public static String testName;
	public String homeUrl= prop_Global.getProperty("ApplicationUrl");
	
	
	
	/***********************************************************
	//Method Name: getDriver
	//Description: Initializing the WebDriver for Execution
	//Created By: Cognizant Technology Solution
	//Return Type: WebDriver
	//Date of Creation: 01/09/2017
	************************************************************/
	public WebDriver getDriver(String Platform, String BrowserName) {
		
		//Storing Platform and Browser to global Variables
		strPlatform = Platform;
		strBrowser = BrowserName;	
		
		if (strPlatform.equalsIgnoreCase("Desktop"))
		{
			String exePath;
			File tmpFile;
			
			switch(strBrowser.toLowerCase())
			{
			case "firefox":
				//Getting GeckoDriver Path from global Properties file
				exePath = prop_Global.getProperty("GeckoDriverPath");
				tmpFile = new File(exePath);
				
				//If GeckoDriver binary Not found in the Path then setting up relative Path
				if(!tmpFile.exists()) {
					exePath = relativePath + "/src/resources/geckodriver.exe";
				}		
				System.setProperty("webdriver.gecko.driver", exePath);
				driver = new FirefoxDriver();
				break;
				
			case "chrome":			
				//Getting ChromeDriver Path from global Properties file
				//exePath = prop_Global.getProperty("ChromeDriverPath");
				//tmpFile = new File(exePath);
				
				//If ChromeDriver binary Not found in the Path then setting up relative Path
				//if(!tmpFile.exists()) {
					exePath = relativePath + "/src/resources/chromedriver.exe";
				//Changed the path
					//exePath = "/usr/local/bin/chromedriver";
				//exePath = "/usr/local/bin/chromedriver";
				//}		
				System.setProperty("webdriver.chrome.driver", exePath);
				ChromeOptions options = new ChromeOptions();
				Map<String, Object> prefs = new HashMap<String, Object>();
				prefs.put("credentials_enable_service", false);
				prefs.put("profile.password_manager_enabled", false);

				options.setExperimentalOption("prefs", prefs);

				//options.addArguments("--enable-strict-powerful-feature-restrictions");
				//options.addArguments("--disable-geolocation");
				driver = new ChromeDriver(options);
				break;
				
			case "internetexplorer":	
				//Getting IEDriver Path from global Properties file
				exePath = prop_Global.getProperty("InternetExplorerDriverPath");
				tmpFile = new File(exePath);
				
				//If IEDriver binary Not found in the Path then setting up relative Path
				if(!tmpFile.exists()) {
					exePath = relativePath + "/src/resources/IEDriverServer.exe";
				}			
				System.setProperty("webdriver.ie.driver", exePath);
				
				DesiredCapabilities caps = DesiredCapabilities.internetExplorer(); 
				caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true); 
				caps.setBrowserName("Internet Explorer");
				caps.setVersion("9");
				caps.setCapability(CapabilityType.VERSION, "8");
				
				driver = new InternetExplorerDriver(caps);
				break;
				
			default:
				System.out.println("Please mention the Browser properly in Global Properties Excel file");
				driver.quit();
			}//End Of Switch block
		}
		else if (strPlatform.equalsIgnoreCase("Android"))
		{
			String s = "";
			String strDeviceId = "";
			try
		        {
 		            Process p = Runtime.getRuntime().exec("adb devices");
 		            
 		            // read the output from the command
 		            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream())); 		            
 		            while ((s = stdInput.readLine()) != null) {
 		                System.out.println(s);
 	 	 				if (s.contains("	device"))
 	 	 				{
 	 	 					strDeviceId = s.replace("	device", "");
 	 	 					strDeviceId.trim();
 	 	 					System.out.println("Device Id captured as: " + strDeviceId);
 	 	 					break;
 	 	 				}
 		            }
 		            
 		            if (strDeviceId.length() <= 0)
 		            {
 		            	System.out.println("Device NOT Found..No Android Devices attached with the system");
 		            }

 		            
 		            //System.exit(0);
 		        }
 		        catch (IOException e) {
 		            System.out.println("exception happened - here's what I know: ");
 		            e.printStackTrace();
 		            System.exit(-1);
 		        }
			
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("browsername","chrome");
		    capabilities.setCapability("device","Android");
		    //capabilities.setCapability("deviceName", prop_Global.getProperty("Device_Name"));
		    capabilities.setCapability("deviceName", strDeviceId);
		    //capabilities.setCapability("platformversion", prop_Global.getProperty("Device_Platform"));
		    capabilities.setCapability("deviceType","phone");
		    capabilities.setCapability("platformName","Android");
		    //capabilities.setCapability("automationName","Android");
		    capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");

		    capabilities.setCapability("app-package", prop_Global.getProperty("Application_Package_Name"));
		    
		    
		    ChromeOptions androidOptions = new ChromeOptions();
		    
			Map<String, Object> prefsAnd = new HashMap<String, Object>();
			prefsAnd.put("credentials_enable_service", false);
			prefsAnd.put("profile.password_manager_enabled", false);
			    
			//--disable-search-geolocation-disclosure
			  
			prefsAnd.put("profile.default_content_settings.geolocation", 1);
			//prefs.put("profile.default_content_settings.geolocation", 2);
			//prefs.put("profile.default_content_settings_values.geolocation", 2);
			
			//androidOptions.setExperimentalOption("prefs", prefsAnd);
			
			/*JSONObject jsonObject = new JSONObject();
			jsonObject.put("profile.default_content_settings.geolocation", 1);
			options.setExperimentalOption("prefs", jsonObject);*/
			//androidOptions.addArguments("--enable-strict-powerful-feature-restrictions");
			//options.addArguments("--disable-geolocation");
		    
		    androidOptions.addArguments("--enable-strict-powerful-feature-restrictions");
		    androidOptions.addArguments("--disable-geolocation");
		    androidOptions.addArguments("--disable-search-geolocation-disclosure");
		    androidOptions.addArguments("--disable-notifications");
		    
		    /*
		    Map prefs = new HashMap<String, Object>();
		    prefs.put("profile.default_content_setting_values.geolocation", 1); // 1:allow 2:block
		    
		    androidOptions.setExperimentalOption("prefs", prefs);*/
		    
		    
		    
		    capabilities.setCapability(ChromeOptions.CAPABILITY, androidOptions);
		    try {
				driver = new AndroidDriver(new URL(prop_Global.getProperty("AppiumURL")), capabilities);
				//driver = new RemoteWebDriver(new URL(prop_Global.getProperty("AppiumURL")), capabilities);
				//((RemoteLocationContext)driver).setLocation(new Location(37.774929, -122.419416, 0));
				//((AndroidDriver)driver).setLocation(new Location(49, 123, 10));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    

			    
		}
		else if (strPlatform.equalsIgnoreCase("iOS"))
		{
		
			DesiredCapabilities iOScapability = new DesiredCapabilities();
			iOScapability.setCapability("platformName", "iOS");
			iOScapability.setCapability("platformVersion", "10.0.2");
			iOScapability.setCapability("browserName", "Safari");
			iOScapability.setCapability("udid", "accea2b8110749ca7f7b4e97317742f1d1b0e8f1");
			iOScapability.setCapability("deviceName", "iPhone");
			iOScapability.setCapability("fullReset", false);
			iOScapability.setCapability("startIWDP", true);
			iOScapability.setCapability("autoAcceptAlerts", true);
			iOScapability.setCapability("autoGrantPermissions", true);
			iOScapability.setCapability("recreateChromeDriverSessions", true);
			iOScapability.setCapability("waitForAppScript", "$.delay(1000); $.acceptAlert();");

			//String s = "ios_webkit_debug_proxy -c 563492996a8c3da0fe7a141e928f81ba09ec2784:27753";
			//s= "xcodebuild build test -project /Applications/Appium.app/Contents/Resources/app/node_modules/appium/node_modules/appium-xcuitest-driver/WebDriverAgent/WebDriverAgent.xcodeproj -scheme WebDriverAgentRunner -destination id=563492996a8c3da0fe7a141e928f81ba09ec2784 -configuration Debug";			
		    
			try 
			{
		    	driver = new RemoteWebDriver(new URL(prop_Global.getProperty("AppiumURL")), iOScapability);
		    	
		    	for(String winHandle : driver.getWindowHandles())
		    		driver.switchTo().window(winHandle);
			}
			catch (MalformedURLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("Please mention the Platform properly in testData Excel file");
		}
		

		return driver;
	}

	/***********************************************************
	//Method Name: getxPath
	//Description: Getting the Xpath from the xml file
	//Created By: Cognizant Technology Solution
	//Return Type: String
	//Date of Creation: 01/09/2017
	************************************************************/	
	public String getxPath(String strKeyword)
	{
		String strValue = "";
		//String statement = "Select * From xPathRepository where Object='"+ strKeyword +"'";
		
	      try
	      {
	    	  //Setting up relative Path for xPathRepository XML File
	    	  File inputFile = new File(relativePath + "/src/pages/xPathRepository.xml");
	          
	    	  DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    	  DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    	  Document doc = dBuilder.parse(inputFile);
	    	  doc.getDocumentElement().normalize();
	          
	    	  if (doc.getDocumentElement().getNodeName().equals("xPathNode"))
	    	  {
		          NodeList nList = doc.getElementsByTagName("object");
		          //System.out.println("----------------------------");
		          for (int temp = 0; temp < nList.getLength(); temp++) {
		             Node nNode = nList.item(temp);
		             //System.out.println("\nCurrent Element :" + nNode.getNodeName());
		             
		             if (nNode.getNodeName().equals("object"))
		             {
			             if (nNode.getNodeType() == Node.ELEMENT_NODE)
			             {
			            	 Element eElement = (Element) nNode;
				             //System.out.println("Student roll no : " + eElement.getAttribute("name"));
				             
				             if (eElement.getAttribute("name").equals(strKeyword))
				             {
					             //System.out.println("First Name : " + eElement.getElementsByTagName("desktop").item(0).getTextContent());
					             //System.out.println("Last Name : " + eElement.getElementsByTagName("mobile").item(0).getTextContent());
					             if (strPlatform.equalsIgnoreCase("Desktop"))
					             {
					            	 strValue = eElement.getElementsByTagName("desktop").item(0).getTextContent();
					             }
					             else
					             {
					            	 strValue = eElement.getElementsByTagName("mobile").item(0).getTextContent();
					             }
					             break;
				             }
				             else
				             {
				            	 //throw new Exception("No element found for object - " + strKeyword);
				            	 continue;
				             }			             

			             }
		             }
		             

		          }
	          }
	          else
	          {
	        	  throw new Exception("Please specify the root element as [xPathNode]");
	          }	          
	          

	       }
	      catch (Exception ex)
	      {
	    	  //addStepDetails("Object Identification", "An error occured in locating the object [" + strKeyword + "]", "FAIL");
	          System.out.println("An error occured in locating the object: " + ex.getMessage());
	          return ex.getMessage();
	      }
		
		return strValue;
	}	
	
	/***********************************************************
	//Method Name: getScreenshot
	//Description: Captures screenshot using relative Path
	//Created By: Cognizant Technology Solution
	//Return Type: String
	//Date of Creation: 01/09/2017
	************************************************************/	
/*    public void getScreenshot(WebDriver driver) throws IOException{
            File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String strDate = dateFormat.format(date);
            System.out.println(strDate);
            String strDate = getDate("yyyyMMddHHmmss");
            strScreenshot_path = relativePath + "/screenshots/SC_" + strDate + ".png";
            //The below method will save the screen shot in the specified path with specified name
            FileUtils.copyFile(scrFile, new File(strScreenshot_path));
            //return strScreenshot_path;
    }*/
	
    public void getScreenshot(WebDriver driver) throws IOException
    {
    	File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE); 
        String strImagePath = ""; //path of screenshot
        
        imgCounter++;
	    
	if (System.getProperty("os.name").contains("Windows"))
    	 {
		strImagePath = ".\\ExecutionResults\\Results_" + strExeStartTime +"\\screenshots\\SC_" + imgCounter + ".png";
		strScreenshot_path = ".\\screenshots\\SC_" + imgCounter + ".png";
    	 }
    	 else
    	 {
    		strImagePath = "./ExecutionResults/Results_" + strExeStartTime +"/screenshots/SC_" + imgCounter + ".png";
		strScreenshot_path = "./screenshots/SC_" + imgCounter + ".png";
    	 }
        
        
            
        //The below method will save the screen shot in the specified path with specified name
        FileUtils.copyFile(scrFile, new File(strImagePath));                       
    }
	

	/***********************************************************
	//Method Name: getDate
	//Description: Provides Current date in the given Pattern
	//Created By: Cognizant Technology Solution
	//Return Type: String
	//Date of Creation: 01/09/2017
	************************************************************/    
    public String getDate(String dtFormat)
    {
    	DateFormat dateFormat = new SimpleDateFormat(dtFormat);
    	//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date);
        //System.out.println(strDate);
    	
    	return strDate;
    }
    
	/***********************************************************
	//Method Name: setRelativePath
	//Description: sets the relative path
	//Created By: Cognizant Technology Solution
	//Return Type: String
	//Date of Creation: 01/09/2017
	************************************************************/
	public String setRelativePath()
	{
		relativePath = new File(System.getProperty("user.dir")).getAbsolutePath();
		if(relativePath.contains("components")) {
			relativePath = new File(System.getProperty("user.dir")).getParent();
		}
		if(relativePath == ""){
			System.out.println("The project path not found and has not been set properly");
		}
		return relativePath;
	}
	
	/***********************************************************
	//Method Name: waiting
	//Description: halts the execution for the specified miliseconds
	//Created By: Cognizant Technology Solution
	//Return Type: String
	//Date of Creation: 01/09/2017
	************************************************************/	
	public void waiting(int intTime) {
		int TimeLagMultiplier = 1;
		try
		{
			TimeLagMultiplier = Integer.parseInt(prop_Global.getProperty("TimeLagMultiplier"));
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage().toString());
		}
		int waitTime = TimeLagMultiplier * intTime;
		long t0, t1;
        t0 = System.currentTimeMillis();
        do {
              t1 = System.currentTimeMillis();
        } while (t1 - t0 < waitTime);
	}
	
	/***********************************************************
	//Method Name: getRandomNum
	//Description: generates a Random Integer value based on the given max and min numbers
	//Created By: Cognizant Technology Solution
	//Return Type: Integer
	//Date of Creation: 18/09/2017
	************************************************************/
	
	public Integer getRandomNum()
	{
		Random rand = new Random();
		int randomNum = 0;
		int maxNum = 10;
		int minNum = 1;
		
		randomNum = rand.nextInt(maxNum - minNum + 1) + minNum;
		return randomNum;
	}
	
	
	
	/***********************************************************
	//Method Name: locateElement(String objProperty, String strSelectType)
	//Description: scroll to a definite position in the screen
	//Parameters: objProperty - property(xpath/ID) of the object
    //            strSelectType - the method used to identify the object (ID/xpath)
	//Created By: Cognizant Technology Solution
	//Return Type: String
	//Date of Creation: 01/09/2017
	 * @throws IOException 
	 * @throws ExecuteException 
	************************************************************/ 
     public void locateElement(WebDriver driver, String objProperty, String strSelectType, int index) throws ExecuteException, IOException{
      
    	 int x = 0;
    	 int y = 0;
    	 List<WebElement> ElemList = null;
    	 JavascriptExecutor js = (JavascriptExecutor)driver;
		 //js.executeScript("window.scrollTo(0,0)", "");
		 waiting(500);
    	 switch(strSelectType){
           
    	 case "id":
    		 ElemList=driver.findElements(By.id(objProperty));
        	   for (int i=0;i<ElemList.size(); i++)
        	   {
        		   Locatable hoverItem = (Locatable) driver.findElement(By.id(objProperty));
        		   x = hoverItem.getCoordinates().onPage().getX();
        		   y = hoverItem.getCoordinates().onPage().getY();
        	   }//end of For Loop
                  break;
           case "xpath":
                  ElemList=driver.findElements(By.xpath(objProperty));
                  
                  if (index <= ElemList.size())
                  {
                	  Locatable hoverItem = (Locatable) ElemList.get(index);
                	  //x = hoverItem.getCoordinates().onPage().getX();
                	  y = hoverItem.getCoordinates().onPage().getY();
                  }
                  break;
    	 }//end of Switch block
    	 waiting(1000);
    	 //((JavascriptExecutor)driver).executeScript("window.scrollBy("+x+",0)");
    	 //((JavascriptExecutor)driver).executeScript("window.scrollBy(0,"+y+");");
         //Actions builder = new Actions(driver);
         //builder.moveToElement(ElemList.get(index)).perform();

    	 waiting(500);
    	 int height = ((Long) js.executeScript("return window.innerHeight || document.body.clientHeight")).intValue() ;
    	 int scrollheight = 0;
    	 if (height<y)
    	 {
    		 scrollheight = y-height+100;
    		 System.out.println("Element is NOT visible..hence scrolling");
    		 js.executeScript("window.scrollBy(0, "+scrollheight+")", "");
    	 }
    	 else
    	 {
    		 System.out.println("element is already visible");
    		 
 			if (strPlatform.equals("Desktop"))
 			{
 				/*scrollheight = y-height+100;
 				CommandLine cmd = new CommandLine("adb");
 				cmd.addArgument("shell", false).addArgument("dumpsys", false).addArgument("input_method", false).addArgument("|", false).addArgument("grep", false).addArgument("mInputShown", false);
 				DefaultExecutor exec = new DefaultExecutor();
 				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 				PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
 				exec.setStreamHandler(streamHandler);
 				exec.execute(cmd);
 				String strStatus = outputStream.toString();
 				System.out.println(strStatus);
 				if (strStatus.contains("mInputShown=true"))
 				{
 					System.out.println("Mobile Keyboard is displayed..hence scrolling");
 					js.executeScript("window.scrollBy(0, "+scrollheight+")", "");
 				}*/
 			}
 			else if (strPlatform.equals("Android"))
 			{
 				scrollheight = y-height+100;
 				String s = null;

 		        try {

 		            Process p = Runtime.getRuntime().exec("adb shell dumpsys input_method | grep mInputShown");
 		            
 		            // read the output from the command
 		            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream())); 		            
 		            System.out.println("Here is the standard output of the command:\n");
 		            while ((s = stdInput.readLine()) != null) {
 		                System.out.println(s);
 	 	 				if (s.contains("mInputShown=true"))
 	 	 				{
 	 	 					System.out.println("Mobile Keyboard is displayed..hence scrolling");
 	 	 					js.executeScript("window.scrollBy(0, "+scrollheight+")", "");
 	 	 				}
 		            }
 		            
 		            // read any errors from the attempted command
 		            /*BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
 		            System.out.println("Here is the standard error of the command (if any):\n");
 		            while ((s = stdError.readLine()) != null) {
 		                System.out.println(s);
 		            }*/
 		            

 		            
 		            //System.exit(0);
 		        }
 		        catch (IOException e) {
 		            System.out.println("exception happened - here's what I know: ");
 		            e.printStackTrace();
 		            System.exit(-1);
 		        }
 			}
    		 
    	 }
			
    	 
     }
     

 	/******************************************************************************************
 	//Method Name: waitForObject
 	//Description: Halts the execution until the specified time and handle alerts
 	//Created By: Cognizant Technology Solution
 	//Date of Creation: 12/01/2017
 	//******************************************************************************************/
 	
 	public static String waitForObject(String strType, int inPutTime, String strValue) {
 		int TimeLagMultiplier = 1;
 		try
 		{
 			TimeLagMultiplier = Integer.parseInt(prop_Global.getProperty("TimeLagMultiplier"));
 		}
 		catch(Exception ex)
 		{
 			ex.printStackTrace();
 		}		

 		int waitTime = TimeLagMultiplier * inPutTime;
 		WebDriverWait wait = new WebDriverWait(driver, waitTime);
 		
 	    if(strType.contains("Alert"))
 	    {
 		    try {		        
 		        wait.until(ExpectedConditions.alertIsPresent());
 		        Alert alert = driver.switchTo().alert();
 		        String strText = alert.getText();
 		        if(strText.contains(strValue))
 		        {
 					alert.accept();
 					System.out.println("Alert Text: " +strText);
 					return "true";
 		        }
 		        else
 		        {
 		        	alert.accept();
 					System.out.println("Alert Text does not match : " + strText);
 					return strText;
 		        }

 		    } catch (Exception e) {
 		        System.out.println(e.getMessage());
 				logger.log(LogStatus.FAIL, "Alert Not Found", "Page is taking long time to load");
 				return e.getStackTrace().toString();
 		    }
 	    }
 	    else
 	    {
 			try
 			{
 		    	if(strType.contains("xpath"))
 				{
 					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(strValue)));
 					System.out.println("element found with given xpath");
 					return "true";
 				}
 				else
 				{
 					System.out.println("Error in calling [waitForObject] method..Please check the parameters");
 					return "false";
 				}
 		    	
 			} catch (Exception e) {        
 				System.out.println("Loading took too much time..Unable to find element within the specified time");
 				System.out.println(e.getMessage());
 				return "false";
 			}
 	    	

 	    }
 		
 	}
     
     
     
     
 	/***********************************************************
 	//Method Name: getStoreDetails
 	//Description: Getting the Store Details from the xml file
 	//Created By: Cognizant Technology Solution
 	//Return Type: String
 	//Date of Creation: 12/09/2017
 	************************************************************/	
     public String[] getStoreDetails(String strInput)
     {
    	 String strValue[] = new String[4];
 		
    	 try
    	 {
    		 //Setting up relative Path for StoreDetails XML File
    		 File inputFile = new File(relativePath + "/testData/GlobalData.xml");
 	          
    		 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    		 DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    		 Document doc = dBuilder.parse(inputFile);
 	    	 doc.getDocumentElement().normalize();
 	          
 	    	 if (doc.getDocumentElement().getNodeName().equals("globalData"))
 	    	 {
 	    		 NodeList nList = doc.getElementsByTagName("storeSearch");
 	    		 Element elTmp = (Element) nList.item(0);
 	    		 nList = elTmp.getElementsByTagName("store");
 	  			  

 	    		 for (int temp = 0; temp < nList.getLength(); temp++)
 	    		 {
 	    			 Node nNode = nList.item(temp);
 		           
 	    			 if (nNode.getNodeName().equals("store"))
 	    			 {
 	    				 if (nNode.getNodeType() == Node.ELEMENT_NODE)
 	    				 {
 	    					 Element eElement = (Element) nNode;
 			        	   
 	    					 if (eElement.getAttribute("param").equals(strInput))
 	    					 {
 	    						 strValue [0] = eElement.getElementsByTagName("address").item(0).getTextContent();
 	    						 strValue [1] = eElement.getElementsByTagName("city").item(0).getTextContent();
 	    						 strValue [2] = eElement.getElementsByTagName("state").item(0).getTextContent();
 	    						 strValue [3] = eElement.getElementsByTagName("zipCode").item(0).getTextContent();
 	    						 break;
 	    					 }
 	    					 else
 	    					 {
 	    						 //throw new Exception("No element found for object - " + strKeyword);
 	    						 continue;
 	    					 }//End Of third If		             

 	    				 }//End Of second If
 	    			 }//End Of first If
 		             

 	    		 }////End Of For Loop
 	    	 }
 	    	 else
 	    	 {
 	    		 throw new Exception("Please specify the root element as [storeSearch]");
 	    	 }//End of Parent if         
 	          

 		}
 		catch (Exception ex)
 	    {
 			//addStepDetails("Object Identification", "An error occurred in locating the object [" + strKeyword + "]", "FAIL");
 			System.out.println("An error occurred in getting Store Details from xml File: " + ex.getStackTrace());
 	        logger.log(LogStatus.FAIL, "An error occurred in getting Store Details from xml File", ex.getStackTrace().toString());
 	        return null;
 	    }
 		
 		return strValue;
 	}
     
     
 	/***********************************************************
 	//Method Name: getItemfromGlobalData
 	//Description: Component to fetch the item names from Global Data xml
 	//Created By:  Cognizant Technology Solution
 	//Return Type: ArrayList<String>
 	//Date of Creation: 20/09/2017
 	************************************************************/	
 	
 	public ArrayList<String> getItemfromGlobalData ()
 	{
 		//String strValue[] = {};
 		ArrayList<String> strValue = new ArrayList<String>();
 		
 		try
 		{
 			//Setting up relative Path for Global Data XML File
 			String inputFile = relativePath + "/testData/GlobalData.xml";
 	          
 			//Loading the xml file
 			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
 	    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
 	    	Document doc = dBuilder.parse(inputFile);
 	    	doc.getDocumentElement().normalize();
 	          
 	    	//Checking if root node is globalData
 	    	if (doc.getDocumentElement().getNodeName().equals("globalData"))
 	    	{
 	    		NodeList nList = doc.getElementsByTagName("foodItems");
 	    		Element elTmp = (Element) nList.item(0);
 	    		nList = elTmp.getElementsByTagName("item");
 	    		System.out.println(nList.getLength());
 	    		
	    		for (int temp = 0; temp < nList.getLength(); temp++)
	    		{
	    			Element e = (Element) nList.item(temp);
 	      			strValue.add(temp, elTmp.getElementsByTagName("item").item(temp).getTextContent());
	    		}
 	        }
 	        else
 	        {
 	        	throw new Exception("Please specify the root element as [globalData]");
 	        }

 		}
 	    catch (Exception ex)
 	    {
 	    	System.out.println("An error occured in getting Item Names: " + ex.getMessage());
 	    }
 		
 		return strValue;
 	}
 	
 	
 	/***********************************************************
 	//Method Name: getCardDetails
 	//Description: Getting the Credit Card Details from the xml file
 	//Created By: Cognizant Technology Solution
 	//Return Type: String
 	//Date of Creation: 16/10/2017
 	************************************************************/	
     public String[] getCardDetails(String strInput)
     {
    	 String strValue[] = new String[5];
 		
    	 try
    	 {
    		 //Setting up relative Path for StoreDetails XML File
    		 File inputFile = new File(relativePath + "/testData/GlobalData.xml");
 	          
    		 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    		 DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    		 Document doc = dBuilder.parse(inputFile);
 	    	 doc.getDocumentElement().normalize();
 	          
 	    	 if (doc.getDocumentElement().getNodeName().equals("globalData"))
 	    	 {
 	    		 NodeList nList = doc.getElementsByTagName("creditCard");
 	    		 Element elTmp = (Element) nList.item(0);
 	    		 nList = elTmp.getElementsByTagName("card");
 	  			  

 	    		 for (int temp = 0; temp < nList.getLength(); temp++)
 	    		 {
 	    			 Node nNode = nList.item(temp);
 		           
 	    			 if (nNode.getNodeName().equals("card"))
 	    			 {
 	    				 if (nNode.getNodeType() == Node.ELEMENT_NODE)
 	    				 {
 	    					 Element eElement = (Element) nNode;
 			        	   
 	    					 if (eElement.getAttribute("param").equals(strInput))
 	    					 {
 	    						 strValue [0] = eElement.getElementsByTagName("cardName").item(0).getTextContent();
 	    						 strValue [1] = eElement.getElementsByTagName("cardNum").item(0).getTextContent();
 	    						 strValue [2] = eElement.getElementsByTagName("cardExp").item(0).getTextContent();
 	    						 strValue [3] = eElement.getElementsByTagName("cardCVV").item(0).getTextContent();
 	    						 strValue [4] = eElement.getElementsByTagName("cardZip").item(0).getTextContent();
 	    						 break;
 	    					 }
 	    					 else
 	    					 {
 	    						 //throw new Exception("No element found for object - " + strKeyword);
 	    						 continue;
 	    					 }//End Of third If		             

 	    				 }//End Of second If
 	    			 }//End Of first If
 		             

 	    		 }////End Of For Loop
 	    	 }
 	    	 else
 	    	 {
 	    		 throw new Exception("Please specify the root element as [storeSearch]");
 	    	 }//End of Parent if         
 	          

 		}
 		catch (Exception ex)
 	    {
 			//addStepDetails("Object Identification", "An error occurred in locating the object [" + strKeyword + "]", "FAIL");
 			System.out.println("An error occurred in getting Store Details from xml File: " + ex.getStackTrace());
 	        logger.log(LogStatus.FAIL, "An error occurred in getting Store Details from xml File", ex.getStackTrace().toString());
 	        return null;
 	    }
 		
 		return strValue;
 	}
 	

	/***********************************************************
	//Method Name: getNewEmail
	//Description: Component to fetch the emailAddress from ~/createAccount/email tag of Global Data xml
	//Created By:  Cognizant Technology Solution
	//Return Type: None
	//Date of Creation: 15/09/2017
	************************************************************/	
	
	public String getNewEmail ()
	{
		String strValue = "";
		
		try
		{
			//Setting up relative Path for Global Data XML File
			String inputFile = relativePath + "/testData/GlobalData.xml";
	          
			//Loading the xml file
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    	Document doc = dBuilder.parse(inputFile);
	    	doc.getDocumentElement().normalize();
	          
	    	//Checking if root node is globalData
	    	if (doc.getDocumentElement().getNodeName().equals("globalData"))
	    	{
	    		NodeList nList = doc.getElementsByTagName("createAccount");
	    		for (int temp = 0; temp < nList.getLength(); temp++) {       
	    			Element e = (Element) nList.item(temp);
	      			strValue = e.getElementsByTagName("email").item(0).getTextContent();
	      			System.out.println(strValue);
	      			break;
	    		}
	        }
	        else
	        {
	        	throw new Exception("Please specify the root element as [globalData]");
	        }
	    	
	    	//Method call for increasing the numeric value of email by 1
	    	changeEmailData(inputFile, strValue);

		}
	    catch (Exception ex)
	    {
	    	System.out.println("An error occured in locating the object: " + ex.getMessage());
	        return ex.getMessage();
	    }
		
		return strValue;
	}

	
	/***********************************************************
	//Method Name: changeEmailData(String inputFile, String strText)
	//Description: Component for increasing the numeric value of email by 1 in ~/createAccount/email tag of Global Data xml
	//Created By:  Cognizant Technology Solution
	//Return Type: None
	//Date of Creation: 15/09/2017
	************************************************************/	
	
	public void changeEmailData(String inputFile, String strText)
	{
		try
		{
			//Increasing the digit in email by 1
		    StringBuilder numericPart = new StringBuilder();
		    for (int i = 0; i < strText.length(); i++) {
		        if (Character.isDigit(strText.charAt(i))) {
		        	numericPart.append(strText.charAt(i));
		            //System.out.println(str.charAt(i) + " is a digit.");
		        } else {
		            //System.out.println(str.charAt(i) + " not a digit.");
		        }
		    }
		    Integer newNum = Integer.parseInt(numericPart.toString())+1;
		    String strNewEmail = strText.replace(numericPart, newNum.toString());
		    System.out.println("New Number: " + strNewEmail);
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	  	  	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	  	  	Document doc = dBuilder.parse(inputFile);

			
			// Get the createAccount element by tag name directly
			Node createAccount = doc.getElementsByTagName("createAccount").item(0);		
			
			// loop the createAccount child node
			NodeList nList = createAccount.getChildNodes();

			for (int i = 0; i < nList.getLength(); i++) {
				Node node = nList.item(i);

				// get the email element, and update the value
				if ("email".equals(node.getNodeName())) {
					node.setTextContent(strNewEmail);
				}
			}			
			
			// writing the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer;
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(inputFile));
			transformer.transform(source, result);
		}
		catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		catch (SAXException sae) {
			sae.printStackTrace();
		}
	}
	
	 	
     
     
     
     
  	/***********************************************************
 	//Method Name: startExtentReport(String reportName)
 	//Description: initializing HTML Extent Report
 	//Parameters: reportName - name 
 	//Created By: Cognizant Technology Solution
 	//Return Type: ExtentReports
 	//Date of Creation: 01/09/2017
 	************************************************************/
	 
     public ExtentReports startExtentReport(String ReportName) {
 		//ExtentReports(String filePath,Boolean replaceExisting) 
 		//filepath - path of the file, in .htm or .html format - path where your report needs to generate. 
 		//replaceExisting - Setting to overwrite (TRUE) the existing file or append to it
 		//True (default): the file will be replaced with brand new markup, and all existing data will be lost. Use this option to create a brand new report
 		//False: existing data will remain, new tests will be appended to the existing report. If the the supplied path does not exist, a new file will be created.
    	 
    	 if (System.getProperty("os.name").contains("Windows"))
    	 {
    		 extentReports = new ExtentReports (relativePath +"\\ExecutionResults\\Results_" + strExeStartTime + "\\" + ReportName + ".html", blnHtmlFlag);
    	 }
    	 else
    	 {
    		 extentReports = new ExtentReports (relativePath +"/ExecutionResults/Results_" + strExeStartTime + "/" + ReportName + ".html", blnHtmlFlag);
    	 }
    	 
    	 extentReports
 			//.addSystemInfo("Host Name", "SoftwareTestingMaterial")
 			.addSystemInfo("Environment", "QA")
 			.addSystemInfo("Platform", "Desktop")
 			.addSystemInfo("User Name", "Cognizant QA Team");
 			//loading the external xml file (i.e., extent-config.xml) which was placed under the base directory
    	 extentReports.loadConfig(new File(relativePath + "/extent-config.xml"));
    	 
    	 //Setting the TestCase Pass Flag to true. It will be marked as false if any method fails
    	 blnTcflag = true;
 		
    	 return extentReports;
     }
     

   	/***********************************************************
  	//Method Name: startTest(String Platform, String Browser, String TestCase, String Description)
  	//Description: Setting up environment for Test Case execution
  	//Created By: Cognizant Technology Solution
  	//Return Type: ExtentReports
  	//Date of Creation: 14/09/2017
  	************************************************************/
     
     public void startTest(String Platform, String Browser, String TestCase, String Description)
     {
    	 try
    	 {
        	 //Method call for initializing HTML Extent Report
    		 startExtentReport("PZI_POS_Demo_" + Platform + Browser + "Report");
        	 
        	 //Starting Extent Test
        	 logger = extentReports.startTest(TestCase, "TEST DESCRIPTION - " + Description);
        	 System.out.println("Executing " + TestCase);
     		
        	 //Calling method for Launching the browser based on Platform mentioned in TestNG xml
        	 getDriver(Platform, Browser);
     		
        	 //Setting up Implicit wait time
        	 driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
     		
        	 logger.log(LogStatus.INFO, "Browser Initiation", Browser + " Browser started in " + Platform + " Platform");		

        	 //Launching the application URL
        	 driver.get(prop_Global.getProperty("ApplicationUrl"));
    		
        	 if (strPlatform.equalsIgnoreCase("Desktop"))
        	 {
            	 //Maximizing browser Window after Page Loads
            	 driver.manage().window().maximize();
        	 }
        	 
        	 if (strPlatform.equalsIgnoreCase("iOS"))
        	 {
            	 waiting(3000);
        	 }
        	 
        	 try {
     			if (System.getProperty("os.name").contains("Windows"))
     				FileUtils.copyFile(new File(".\\src\\resources\\logo.png"), new File(".\\ExecutionResults\\Results_" + strExeStartTime +"\\screenshots\\logo.png"));
     			else
     				FileUtils.copyFile(new File("./src/resources/logo.png"), new File("./ExecutionResults/Results_" + strExeStartTime +"/screenshots/logo.png"));
     		} catch (IOException e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		}

    		
        	 logger.log(LogStatus.PASS, "Launch Application", "Launch URL is successfull");
        	 

        	 
    	 }
    	 catch(Exception e)
    	 {
    		 e.printStackTrace();
    	 }
		
     }
     
     
    /***********************************************************
   	//Method Name: endTest
   	//Description: Cleaning up after completion of Test Case Execution
   	//Created By: Cognizant Technology Solution
   	//Return Type: ExtentReports
   	//Date of Creation: 14/09/2017
   	************************************************************/     
     
     public void endTest()
     {
 		// ending test
 		//endTest(logger) : It ends the current test and prepares to create HTML report
    	 extentReports.endTest(logger);
 		
 		// writing everything to document
 		//flush() - to write or update test information to your report. 
    	 extentReports.flush();
    	 extentReports.close();
         
 		System.out.println("Finishing Test Case");
 		driver.quit();
 		
 		//Setting the flag to false so that the Next Test Case comes under same HTML Report
 		blnHtmlFlag = false;
     }
     
     
 	/****************************************************************
 	Method Name: EstConnection(String strDBName)
 	Description: gets the test database connection object
 	Parameters: strDBName: name of the file
 	Example: EstConnection("DataSheet.csv")
 	Return Type: Connection object
 	Created By: Cognizant Technology Solution
 	Date of Creation: 04/30/2013
 	 * @throws ClassNotFoundException 
 	*****************************************************************/
 	public static Connection EstConnection(String strDBName) throws SQLException, ClassNotFoundException{	
 		
 		Class.forName("org.relique.jdbc.csv.CsvDriver");
 		Connection obj = DriverManager.getConnection("jdbc:relique:csv:" + strDBName);
 		return obj;
 	}
     
 	@BeforeSuite
 	public void setup () throws ClassNotFoundException, SQLException, IOException
 	{
		//Setting up the relative Path to Project root Directory
		relativePath = setRelativePath();
		
		//loading the Global Settings properties file
		FileInputStream objFile;

		objFile = new FileInputStream(relativePath + "/Global_Settings.properties");
		prop_Global.load(objFile);
		
		//Setting flag to true to generate new HTML report for every run..This needs to be set to false at @AfterTest Method of each testcase
		blnHtmlFlag = true;
		
		//Getting Datetime during beginning of Exe to apend after HTML Report
		strExeStartTime = getDate("yyyyMMddHHmmss");
		
		//Establishing connection and create the test database object of csv Data file
		String strDataPath = relativePath + "/testData";
		goConn = EstConnection(strDataPath);
 	}
 	
	/***********************************************************
	//Method Name: dbDataProvider
	//Description: sets data set for each and every test case
	//Created By: Cognizant Technology Solution
	//Return Type: Object [][]
	//Date of Creation: 09/04/2017
     * @throws ClassNotFoundException 
	************************************************************/
 	
	@BeforeClass
	@DataProvider(name="dbDataProvider",parallel = true)
    public Object[][] dbDataProvider (ITestContext context) throws SQLException, IOException, ClassNotFoundException{
		
		//This line will fetch the name of the test case the thread will be currently running, inorder to pass it onto the query
		testName = context.getCurrentXmlTest().getName();
		
		// ****************************************************To be put into global **************************************
		//String strDataPath = "D:\\Automation\\eCommPOM_V1\\data";
		//Class.forName("org.relique.jdbc.csv.CsvDriver");
		//Connection goConn = DriverManager.getConnection("jdbc:relique:csv:" + strDataPath);
		//*****************************************************************************************************************
		
		goQuery = goConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
		//The following variable goResSet should contain the result set with all the separate lines against that test case
		goResSet = goQuery.executeQuery("SELECT * from Data where TestCase='" + testName + "'");
		
        //get the number of columns in the result set
        ResultSetMetaData rsmd = goResSet.getMetaData();
        int intColNum = rsmd.getColumnCount();
        
        List<HashMap<String,String>> objArrList = new ArrayList<>();
        HashMap<String,String> dataRows = new HashMap<String,String>();
        
        while(goResSet.next()){
            for(int iCount = 2 ; iCount <= intColNum; iCount++){
                dataRows.put(rsmd.getColumnName(iCount), goResSet.getString(iCount));
            }
            objArrList.add(dataRows);
        }
        
        if (objArrList.size() != 0)
        {
            //Putting the hash map into an Object Array
        	Object[][] returnObject = new Object[objArrList.size()][1];
            for(int iIndex = 0; iIndex < objArrList.size(); iIndex ++)
            {
            	returnObject[iIndex][0] = objArrList.get(iIndex);
            }
            
            //This object needs to be passed onto the tests
    		return returnObject;
        }
        else
        {
        	System.out.println("Please create entry for Test Case [" + testName + "] in the test Data csv");
        	logger.log(LogStatus.ERROR, "Test Data Error", "No entry found for Test Case [" + testName + "] in the test Data csv");
        	
        	return new Object[0][1];
        }
		
	}

	
	
  	/***********************************************************
 	//Method Name: checkResult(ITestResult result)
 	//Description: Catching the uncaught errors and exceptions
 	//Created By: Cognizant Technology Solution
 	//Return Type: ExtentReports
 	//Date of Creation: 11/09/2017
 	************************************************************/	
	

	
	public void checkResult(ITestResult result){
		
		if (result.getStatus() == ITestResult.SUCCESS)
		{
			try {
				getScreenshot(driver);
				logger.log(LogStatus.INFO, "Closing Application", logger.addScreenCapture(strScreenshot_path));
			}catch (Exception e) {
				logger.log(LogStatus.FATAL, "Error occured in capturing Screenshot", e.getStackTrace().toString());
			}
		}
		else if(result.getStatus() == ITestResult.FAILURE)
		{
			if(result.getThrowable().getMessage().length()>60 || result.getThrowable().getMessage().contains("java.lang") || result.getThrowable().getMessage().contains("org.openqa"))
			{
				logger.log(LogStatus.ERROR, "Error", "Message [ Unexpected/Unhandled error encountered ]");
			}
			else if(result.getThrowable().getMessage().contains("java.lang"))
			{
				logger.log(LogStatus.FAIL, "Failure Reason", "Message [ Handled error encountered ]");
			}
			else
			{
				logger.log(LogStatus.FAIL, "Failure Reason", "Message: [ "+result.getThrowable().getMessage()+" ]");
			}
			
			
			try {
				getScreenshot(driver);
				logger.log(LogStatus.FAIL, "Fail Screenshot", logger.addScreenCapture(strScreenshot_path));
			}catch (Exception e) {
				logger.log(LogStatus.FATAL, "Error occured in capturing Screenshot", e.getStackTrace().toString());
			}
			
		}else if(result.getStatus() == ITestResult.SKIP){
			logger.log(LogStatus.SKIP, "Skipped", "Test Case Skipped is "+result.getName());
		}
	}
	
 	
	@AfterSuite
	public void killDriver(){
		driver.quit();
		
		/*Calendar calendar = new GregorianCalendar();
		TimeZone timeZone = calendar.getTimeZone();
		
		System.out.println(timeZone);
		
		if (timeZone.toString().contains("America"))
			try {
				AfterScript.moveResults(relativePath + "/ExecutionResults/", "Results_" + strExeStartTime);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	}
	
    /***********************************************************
   //Method Name: isElementPresent
   //Description: Verify the element is present or not
   //Created By: Cognizant Technology Solution
   //Return Type: ExtentReports
   //Date of Creation: 11/09/2017
   ************************************************************/     
    public boolean isElementPresent(int timeout_in_second, String obj_xpath) {
     boolean foundElement = false;
     WebDriverWait wait = new WebDriverWait(driver, timeout_in_second /* timeout in seconds */);
     try {
                 wait.until(ExpectedConditions.elementToBeClickable(By.xpath(obj_xpath)));
                 foundElement = true;
           }catch (Exception e) {
                 foundElement = false;
           }
     return foundElement;
    }
    
	
	//changes
	
	/***********************************************************
 	//Method Name: bringElementIntoView
 	//Description: bringing any element to the visible screen using Actions class
 	//Created By: Cognizant Technology Solution
 	//Return Type: void
 	//Date of Creation: 28/12/2017
 	************************************************************/
	
	public void bringElementIntoView(WebElement element){
 		Actions actions = new Actions(driver);
 		actions.moveToElement(element);
 		actions.perform();
 	}
	
	/***********************************************************
 	//Method Name: returnToHome
 	//Description: navigate back to home page
 	//Created By: Cognizant Technology Solution
 	//Return Type: void
 	//Date of Creation: 28/12/2017
	 * @throws InterruptedException 
 	************************************************************/

	
	public void returnToHome() throws InterruptedException{
		//driver.navigate().to(homeUrl);
		if(strPlatform.equalsIgnoreCase("Desktop"))
		{
			driver.findElement(By.xpath("//a[@class='logo']/img[@title='Taco Bell']")).click();
		}
		else
		{
			driver.findElement(By.xpath("//a[@class='logo']/img[@title='logo']")).click();
		}
		Thread.sleep(1000);
	}
	
	/***********************************************************
 	//Method Name: bringIntoViewAndClick
 	//Description: scrolls from top of page to end till element is visible and then clicks on it
 	//Created By: Cognizant Technology Solution
 	//Return Type: void
 	//Date of Creation: 28/12/2017
	 * @throws InterruptedException 
 	************************************************************/

	public void bringIntoViewAndClick(WebElement e){
		int flag=0,i=0;
		((JavascriptExecutor)driver).executeScript("window.scrollTo(0,0)");
		((JavascriptExecutor)driver).executeScript("window.scrollTo(0,0)");
		waiting(2000);
		while(flag!=1 && i<100){
			try{
				i++;
				e.click();
				flag=1;
			}
			catch(UnknownError er){
				System.out.println("Not clickable....");
				er.printStackTrace();
				
				JavascriptExecutor jse = (JavascriptExecutor)driver;
				jse.executeScript("window.scrollBy(0,200)", "");
			}
			catch(ElementNotVisibleException ex){
				System.out.println("Not visible...");
				ex.printStackTrace();
				//((JavascriptExecutor)driver).executeScript("window.scrollTo(0,0)");
				JavascriptExecutor jse = (JavascriptExecutor)driver;
				jse.executeScript("window.scrollBy(0,200)", "");
			}
			catch(Exception ex){
				System.out.println("Some other exception:");
				//ex.printStackTrace();
				JavascriptExecutor jse = (JavascriptExecutor)driver;
				jse.executeScript("window.scrollBy(0,250)", "");
			}
			
		}
	}
	
	
	/***********************************************************
 	//Method Name: closePopup
 	//Description: Closes any popup if it appears
 	//Created By: Cognizant Technology Solution
 	//Return Type: void
 	//Date of Creation: 18/01/2018
	 * @throws IOException 
 	************************************************************/
	public int closePopup(){
		try{
			/*if(waitForObject("xpath", 10, "//div[@id='cboxLoadedContent']").equals("true")){
				String popupText=driver.findElement(By.xpath("//div[@id='cboxLoadedContent']")).getText();
				System.out.println("Popup detected");
				getScreenshot(driver);
				logger.log(LogStatus.WARNING, "HandlingPopup", "Closed popup ["+popupText+"]");
				logger.log(LogStatus.WARNING, "Popup Screenshot", logger.addScreenCapture(strScreenshot_path));
				driver.findElement(By.xpath("//div[@id='cboxLoadedContent']//button[contains(.,'OK')]")).click();
				return 1;
			}
			else{
				System.out.println("No popup opened");
				return 0;
			}*/
			if(waitForObject("xpath", 8, "//div[@id='cboxLoadedContent']").equals("true"))
			{
				String popupText=driver.findElement(By.xpath("//div[@id='cboxLoadedContent']")).getText();
				
				System.out.println("Popup detected");
				getScreenshot(driver);
				logger.log(LogStatus.WARNING, "Handling Popup", "popup detected ["+popupText+"]");
				logger.log(LogStatus.WARNING, "Popup Screenshot", logger.addScreenCapture(strScreenshot_path));
				
				//Anchor PopUp Handled
				if(waitForObject("xpath", 1, "//div[@id='cboxLoadedContent']//a").equals("true"))
				{
					driver.findElement(By.xpath("//div[@id='cboxLoadedContent']//a")).click();
				}				
				//Button PopUp Handled
				else if(waitForObject("xpath", 1, "//div[@id='cboxLoadedContent']//button").equals("true"))
				{
					driver.findElement(By.xpath("//div[@id='cboxLoadedContent']//button")).click();
				}
				//Trying to click on the close icon
				else if(waitForObject("xpath", 1, "//div[@id='cboxLoadedContent']//i[@class='icon-close']").equals("true"))
				{
					driver.findElement(By.xpath("//div[@id='cboxLoadedContent']//i[@class='icon-close']")).click();
				}
				//PopUp Escaped
				else
				{
					driver.findElement(By.xpath("//div[@id='cboxLoadedContent']")).sendKeys(Keys.ESCAPE);
					//need to check if escape is working without using button or anchor
				}
				
				//Checking whether the popup is really gone
				if(!waitForObject("xpath", 5, "//div[@id='cboxLoadedContent']").equals("true"))
				{
					System.out.println("Popup handled successfully");
					getScreenshot(driver);
					logger.log(LogStatus.PASS, "Popup Handled", "Popup handled successfully");
					logger.log(LogStatus.PASS, "Popup Screenshot", logger.addScreenCapture(strScreenshot_path));
					return 1;
				}
				else
				{
					System.out.println("Unable to handle Popup");
					throw new Error("Unable to handle Popup");
				}
				
			}
			
			else
			{
				System.out.println("PopUp did not appear");
				return 0;
			}
			
			
		}
		catch(Exception ex){
			throw new Error("Unable to handle Popup");
			//return 0;
		}
	}
	

	
	
	//ignore
	 public boolean isWebElementVisible(WebElement w) {
		    Dimension weD = w.getSize();
		    Point weP = w.getLocation();
		    Dimension d = driver.manage().window().getSize();

		    int x = d.getWidth()-20;
		    int y = d.getHeight()-20;
		    int x2 = weD.getWidth() + weP.getX();
		    int y2 = weD.getHeight() + weP.getY();
		    
		    return x2 <= x && y2 <= y;
		  
	 }
	 
	 public void bringIntoViewAnd(WebElement e){
			int flag=0;
			waiting(2000);
			((JavascriptExecutor)driver).executeScript("window.scrollTo(0,0)");
			waiting(2000);
			while(flag!=1){
				try{
					//e.click();
					if(isWebElementVisible(e)){
						System.out.println("Element found");
						flag=1;
						break;
					}
					
					else{
						System.out.println("Scrolling");
						JavascriptExecutor jse = (JavascriptExecutor)driver;
						jse.executeScript("window.scrollBy(0,100)", "");
						flag=0;
					}
				}
				/*catch(UnknownError er){
					System.out.println("Not clickable....");
					er.printStackTrace();
					
					JavascriptExecutor jse = (JavascriptExecutor)driver;
					jse.executeScript("window.scrollBy(0,200)", "");
				}
				catch(ElementNotVisibleException ex){
					System.out.println("Not visible...");
					ex.printStackTrace();
					//((JavascriptExecutor)driver).executeScript("window.scrollTo(0,0)");
					JavascriptExecutor jse = (JavascriptExecutor)driver;
					jse.executeScript("window.scrollBy(0,200)", "");
				}*/
				catch(Exception ex){
					System.out.println("Some other exception:");
					//ex.printStackTrace();
					JavascriptExecutor jse = (JavascriptExecutor)driver;
					jse.executeScript("window.scrollBy(0,250)", "");
				}
				finally{
					waiting(1000);
				}
				
			}
		}
	 
	 public void showElement(WebElement e){
		 
		 while(!isWebElementVisible(e)){
			 System.out.println("Scrolling.....");
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("window.scrollBy(0,250)", "");
		 }
	 }

	 
	 public String getAlertConfirmation() {       
		    try{
		        Alert alert = driver.switchTo().alert();
		        String alertText = alert.getText();
		        alert.accept();
		        return alertText;       
		    }catch(Exception ex){
		    	return "";
		    }
		}
	 
	 
	 public static int IsAlertPresent() {
	      WebDriverWait wait = new WebDriverWait(driver, 10);
	  if(wait.until(ExpectedConditions.alertIsPresent())==null)
	     return 0;
	  else
	     return 1;
	   }
	
	
}
