package utility;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.w3c.dom.Attr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class AfterScript {
	
    public static Connection conn = null;
    public static String url = "jdbc:sqlite://irvlabpxe/Files/Sayantan_Automation/target/DumpFromSlaves/log.db";
    
    public static void moveResults() throws IOException {
    	
    	String strDir = "";
    	String strHtmlFile = "";
    	String strDestparent = "";
    	String destination = "";
    	String strResFolder = System.getProperty("user.dir") + "/ExecutionResults";
    	String strPath = strResFolder + "/";
    	
    	//Method call for getting the Last Result folder
    	strDir = getExeResultPath(strResFolder);
    	
    	//File srcDir = new File(source);
    	File srcDir = new File(strPath + strDir);
        
    	//Destination Directory to copy to
    if (System.getProperty("os.name").contains("Windows"))
    {
    		strDestparent = "//irvlabpxe/Files/Sayantan_Automation/target/DumpFromSlaves/";
        	//destination = "//irvlabpxe/Files/Sayantan_Automation/target/" + strDir;
    }
    else
    {
        	strDestparent = "//Volumes/files/Sayantan_Automation/target/DumpFromSlaves/";
        	//destination = "smb://irvlabpxe/Files/Sayantan_Automation/test/help";//This one not working
        	//destination = "//Volumes/files/Sayantan_Automation/target/" + strDir;
        	//echo jenkins | sudo -S sudo mkdir ./mntpoint
        	//sudo mount_smbfs //guest@irvlabpxe/files ./mntpoint
    }
        
        destination = strDestparent + strDir;
        
        File destDir = new File(destination);
        
        
        //Getting all filenames and folder names in a file array
        File[] listOfFiles = srcDir.listFiles();

        for (int i = 0; i < listOfFiles.length; i++)
        {
        	if (listOfFiles[i].isFile() && listOfFiles[i].getName().contains("html"))
        	{
                strHtmlFile = listOfFiles[i].getName();
                System.out.println("File: " + strHtmlFile);
            }
        	else if (listOfFiles[i].isDirectory())
        	{
                System.out.println("Directory: " + listOfFiles[i].getName());
            }
        }
        
        //Creating Xml file by Parsing the HTML Result
        parseHtml(strPath + strDir, strHtmlFile);

        try {        	
        	//check if working.txt is present and wait until its gone
        	/*File f = new File(strDestparent + strDir + "/working.txt");
        	int tmp = 0;
        	while (f.exists())
        	{
        		tmp ++;
        		System.out.println("waiting");
        		Thread.sleep(15000);
        		if (tmp == 59)
        			break;
        	}*/
        	//Creating file working.txt and updating log file
        	/*FileOutputStream out = new FileOutputStream(strDestparent + strDir + "/working.txt");
        	System.out.println("working.txt created");
        	out.close();*/
        	
            //updating Log.txt file as starting the copy
        	/*updateLog(strDestparent + "log.txt", "Start:" + strDir);
        	System.out.println("log updated");*/
        	
            System.out.println("Started Copying results to pxe");
        	FileUtils.copyDirectory(srcDir, destDir);
            System.out.println("Folders Copied Successfully from Slave");
            
            //updating Log.txt file as copy is finished
            /*updateLog(strDestparent + "log.txt", "Finish:" + strDir);
            System.out.println("log updated");*/
            
			//Deleting file working.txt
            /*File f2 = new File(strDestparent + "working.txt");
			f2.delete();
			System.out.println("working.txt deleted");*/
			
			//Creating File SlaveCopy_done.txt
        	FileOutputStream out = new FileOutputStream(strDestparent + strDir + "/SlaveCopy_done.txt");
        	System.out.println("SlaveCopy_done.txt created");
        	out.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    	
        //parseHtml(destination, strHtmlFile);
    	
    	
    }

    public static String getExeResultPath(String strDir)
    {
		//File srcDir = new File(strPath + strDir);
		File srcDir = new File(strDir);
		
        //Getting all filenames and folder names in a file array
        File[] listOfFiles = srcDir.listFiles();
        
        Arrays.sort(listOfFiles, new Comparator<File>()
        {
            public int compare(File f1, File f2)
            {
                return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
            }
        });
        
        for(int i=0, length=Math.min(listOfFiles.length, 2); i<length; i++) {
            System.out.print("Directory: " + listOfFiles[i].getName());
            System.out.println("  Modified: " + new Date(listOfFiles[i].lastModified()));
            if (! listOfFiles[i].getName().contains("DS_Store"))
            		return listOfFiles[i].getName();
        }
        
        return null;

    }
    
    public static void parseHtml (String Dir, String HtmlFile)
    {
    	Integer countTotalTc = 0;
    	Integer countPassTc = 0;
    	Integer countFailTc = 0;
    	Integer countOtherTc = 0;
    	Integer countTotalStep = 0;
    	Integer countPassStep = 0;
    	Integer countFailStep = 0;
    	Integer countOtherStep = 0;
    	String strSlaveName = ""; //this needs to be come from jenkins master Or slave?
    	String strStartTime = "";
    	String strEndTime = "";
    	String strDuration = "";
    	String strUserName = "";
    	String strOS = "";
    	String strJavaVersion = "";
    	String strHostName = "";
    	String strEnv = "";
    	String strPlatform = "";
    	String strApplicationType = "";
    	
    	try {
        	//File input = new File("C:/Users/sxm6492/Desktop/Demo_20180123_Ecomm/eComm_Results/App/AndroidRun_1/ExecutionReport_Android.html");
    		File input = new File(Dir + "/" + HtmlFile);
        	Document doc = Jsoup.parse(input, "UTF-8");
        	
        	Elements spans = doc.select("span");
        	for (Element span:spans)
        	{
        		String strSpanClass = span.attr("class");
        		if (strSpanClass.contains("panel-lead suite-started-time"))
        		{
        			strStartTime = span.text();
        		}
        		if (strSpanClass.contains("panel-lead suite-ended-time"))
        		{
        			strEndTime = span.text();
        		}
        		if (strSpanClass.contains("suite-total-time-current-value panel-lead"))
        		{
        			strDuration = span.text().split("\\+")[0];
        		}
        	}
        	
        	Elements lis = doc.select("li");
        	for (Element li:lis)
        	{
        	  	String strLiClass = li.attr("class");
        	   	if (strLiClass.contains("collection-item test displayed active"))
        	   	{
        	   		countTotalTc ++;
        	   		if (strLiClass.contains("pass"))
        	   			countPassTc++;
        	   		else if (strLiClass.contains("fail"))
        	   			countFailTc++;
        	   		else
        	   			countOtherTc++;
        	   	}
        	}
        	    
        	Elements tds = doc.select("td");
        	for(Element td:tds)
        	{
        		String strTdClass = td.attr("class");
        	   	if (strTdClass.contains("status"))
        	   	{
        	   		countTotalStep ++;
        	   		if (strTdClass.contains("pass"))
        	   			countPassStep++;
        	   		else if (strTdClass.contains("fail"))
        	   			countFailStep++;
        	   		else
        	   			countOtherStep++;
        	   	}
        	}
        	

        	Elements divs = doc.select("div");
        	for(Element div:divs)
        	{
        	   	if (div.attr("class").contains("system-view"))
        	   	{
        	   		Elements trs = div.select("tr");
        	   		for (Element tr:trs)
        	   		{
        	   			String strTemp = tr.select("td").text();
        	   			//System.out.println(strTemp);
        	   			if (strTemp.contains("User Name"))
        	   				strUserName = strTemp.replace("User Name ", "");
        	   			else if (strTemp.contains("OS"))
        	   				strOS = strTemp.replace("OS ", "");
        	   			else if (strTemp.contains("Java Version"))
        	   				strJavaVersion = strTemp.replace("Java Version ", "");
        	   			else if (strTemp.contains("Host Name"))
        	   				strHostName = strTemp.replace("Host Name ", "");
        	   			else if (strTemp.contains("Environment"))
        	   				strEnv = strTemp.replace("Environment ", "");
        	   			else if (strTemp.contains("Platform"))
        	   				strPlatform = strTemp.replace("Platform ", "");
        	   		}
        	   	}
        	}    	
        	
        	
        	System.out.println("Exe Start time: " + strStartTime);
        	System.out.println("Exe End time: " + strEndTime);
        	System.out.println("Total time: " + strDuration);
        	
        	System.out.println("Total TC Count: " + countTotalTc);
        	System.out.println("Pass TC Count: " + countPassTc);
        	System.out.println("Fail TC Count: " + countFailTc);
        	System.out.println("Others TC Count: " + countOtherTc);
        	
        	System.out.println("Total Step Count: " + countTotalStep);
        	System.out.println("Pass Step Count: " + countPassStep);
        	System.out.println("Fail Step Count: " + countFailStep);
        	System.out.println("Others Step Count: " + countOtherStep);
        	
        	System.out.println("User Name: " + strUserName);
        	System.out.println("OS: " + strOS);
        	System.out.println("Java Version: " + strJavaVersion);
        	System.out.println("Host Name: " + strHostName);
        	System.out.println("Environment: " + strEnv);
        	System.out.println("Platform: " + strPlatform);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
		try
		{
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	
			// root elements
			org.w3c.dom.Document doc = (org.w3c.dom.Document) docBuilder.newDocument();
			org.w3c.dom.Element rootElement = doc.createElement("exeTree");
			doc.appendChild(rootElement);
			

			org.w3c.dom.Element staff = doc.createElement("exeDetails");
			rootElement.appendChild(staff);
	
			// set attribute to exeDetails element
			/*Attr attr = ((org.w3c.dom.Document) doc).createAttribute("timestamp");
			staff.setAttribute("timestamp", "1");*/
	
			//adding date element
			org.w3c.dom.Element dateValue = doc.createElement("dateValue");
			dateValue.appendChild(doc.createTextNode(strEndTime.split(" ")[0]));
			staff.appendChild(dateValue);
			
			//Adding tower element with Hardcoded value for ecom
			org.w3c.dom.Element tower = doc.createElement("tower");
			tower.appendChild(doc.createTextNode("eCommerce"));
			staff.appendChild(tower);
			
			org.w3c.dom.Element htmlFile = doc.createElement("htmlFile");
			htmlFile.appendChild(doc.createTextNode(Dir + "/" + HtmlFile));
			staff.appendChild(htmlFile);
			
			org.w3c.dom.Element hostName = doc.createElement("hostName");
			hostName.appendChild(doc.createTextNode(strHostName));
			staff.appendChild(hostName);			
			
			org.w3c.dom.Element osVersion = doc.createElement("osVersion");
			osVersion.appendChild(doc.createTextNode(strOS));
			staff.appendChild(osVersion);
			
			org.w3c.dom.Element platform = doc.createElement("platform");
			platform.appendChild(doc.createTextNode(strPlatform));
			staff.appendChild(platform);
			
			org.w3c.dom.Element appType = doc.createElement("appType");
			appType.appendChild(doc.createTextNode(HtmlFile.split("_")[0]));
			staff.appendChild(appType);
			
			org.w3c.dom.Element startTime = doc.createElement("startTime");
			startTime.appendChild(doc.createTextNode(strStartTime));
			staff.appendChild(startTime);
			
			org.w3c.dom.Element endTime = doc.createElement("endTime");
			endTime.appendChild(doc.createTextNode(strEndTime));
			staff.appendChild(endTime);
			
			org.w3c.dom.Element duration = doc.createElement("duration");
			duration.appendChild(doc.createTextNode(strDuration));
			staff.appendChild(duration);
			
			org.w3c.dom.Element testTotal = doc.createElement("testTotal");
			testTotal.appendChild(doc.createTextNode(countTotalTc.toString()));
			staff.appendChild(testTotal);
			
			org.w3c.dom.Element testPass = doc.createElement("testPass");
			testPass.appendChild(doc.createTextNode(countPassTc.toString()));
			staff.appendChild(testPass);
			
			org.w3c.dom.Element testFail = doc.createElement("testFail");
			testFail.appendChild(doc.createTextNode(countFailTc.toString()));
			staff.appendChild(testFail);
			
			org.w3c.dom.Element testOthers = doc.createElement("testOthers");
			testOthers.appendChild(doc.createTextNode(countOtherTc.toString()));
			staff.appendChild(testOthers);
			
			org.w3c.dom.Element stepTotal = doc.createElement("stepTotal");
			stepTotal.appendChild(doc.createTextNode(countTotalStep.toString()));
			staff.appendChild(stepTotal);
			
			org.w3c.dom.Element stepPass = doc.createElement("stepPass");
			stepPass.appendChild(doc.createTextNode(countPassStep.toString()));
			staff.appendChild(stepPass);
			
			org.w3c.dom.Element stepFail = doc.createElement("stepFail");
			stepFail.appendChild(doc.createTextNode(countFailStep.toString()));
			staff.appendChild(stepFail);
			
			org.w3c.dom.Element stepOthers = doc.createElement("stepOthers");
			stepOthers.appendChild(doc.createTextNode(countOtherStep.toString()));
			staff.appendChild(stepOthers);
			
			org.w3c.dom.Element env = doc.createElement("env");
			env.appendChild(doc.createTextNode(strEnv));
			staff.appendChild(env);
			
			org.w3c.dom.Element javaVersion = doc.createElement("javaVersion");
			javaVersion.appendChild(doc.createTextNode(strJavaVersion));
			staff.appendChild(javaVersion);
			
	
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(Dir + "/file.xml"));
	
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	
			transformer.transform(source, result);
	
			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
    }
    
    public static void updateLog(String Filename, String content)
    {
    	try {
        	File fLog = new File(Filename);
        	FileWriter fw = new FileWriter(fLog, true);
            BufferedWriter bw = new BufferedWriter(fw);
            if (content.contains("Start"))
            {
            	bw.newLine();
            }
            else
            {
            	bw.write("::::");
            }
            bw.write(content);
            bw.flush();
            bw.close();
    	}catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public static void connect() {

        try {
        	Class.forName("org.sqlite.JDBC");
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
            
            
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    } 

    
    
}