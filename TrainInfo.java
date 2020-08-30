package JdbcConn;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

//import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.*;

public class TrainInfo {

	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException, ParseException, JSONException, SQLException, ClassNotFoundException {
		
		Scanner sc = new Scanner(System.in);
		
		TrainServiceManager t = new TrainServiceManager();
		List<TrainDetailsVO> trainDetailsVOListc = new ArrayList<TrainDetailsVO>();
		List<TrainDetailsVO> trainDetailsVOListj=new ArrayList<TrainDetailsVO>();
		boolean flag=false;
		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "oracle123");
		Class.forName("oracle.jdbc.driver.OracleDriver");
	
		Statement stmt = con.createStatement();
		System.out.println("do you want values from json(y/n)?");
		char ch=sc.next().charAt(0);
		
		try {

			trainDetailsVOListc = t.getTrainDetails("C:\\Users\\HP\\Downloads\\tapticts\\TrainRoutesAndFares.dat");
			System.out.println(trainDetailsVOListc);
			switch(ch){
			case 'y':
			{	
				ObjectMapper mapper = new ObjectMapper(); 
			 	mapper.writerWithDefaultPrettyPrinter().writeValue(new File("train.json"),trainDetailsVOListc);
			 	JSONParser jsonParser = new JSONParser();
			 	Object obj=(Object)jsonParser.parse(new FileReader("train.json"));
			 
			 	JSONArray jsonObject = (JSONArray) obj;
			
			 
			 	String query = "insert into traindetails(train_number,route,source,destination) values(?,?,?,?)";
				PreparedStatement pstmtj = con.prepareStatement(query);
				int res=0;
			for( int j=0;j<jsonObject.size();j++)
			{JSONObject object = (JSONObject) jsonObject.get(j);
				String tno=(String) object.get("trainNumber");
				Long src=(Long) object.get("source");
				String rt=(String) object.get("route");
				Long destn=(long) object.get("destination");
				//inserting to db
				pstmtj.setString(1, tno);
				pstmtj.setString(2, rt);
				pstmtj.setLong(3, src);
				pstmtj.setLong(4, destn);
				res = pstmtj.executeUpdate();
				System.out.println(res + " record(s) inserted for json file");
	
			}
			

			}
			break;
			
		//text file
			case'n': {	
			int i=0;
			String sql = "insert into traindetails(train_number,route,source,destination) values(?,?,?,?)";
			PreparedStatement pstmt = con.prepareStatement(sql);
			for (int itr = 0; itr < 3; itr++) {
				//System.out.println("hi");
				pstmt.setString(1, trainDetailsVOListc.get(itr).getTrainNumber());
				pstmt.setString(2, trainDetailsVOListc.get(itr).getRoute());
				pstmt.setInt(3, trainDetailsVOListc.get(itr).getSource());
				pstmt.setInt(4, trainDetailsVOListc.get(itr).getDestination());
				
				i = pstmt.executeUpdate();
				System.out.println(i+ " record(s) inserted for text file");
			}
			
			pstmt.close();

			}
			break;
			
			//xml file
		default:
		{	try   
			{   
			File file = new File("E:\\tapti\\Jdbc\\src\\JdbcConn\\traindetail.xml");    
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
			
			DocumentBuilder db = dbf.newDocumentBuilder();  
			Document doc = db.parse(file);  
			doc.getDocumentElement().normalize();  
			//System.out.println("Root element: " + doc.getDocumentElement().getNodeName());  
			NodeList nodeList = doc.getElementsByTagName("TrainDetailsVO"); 
			
			String queryxml = "insert into traindetails(train_number,route,source,destination) values(?,?,?,?)";
			PreparedStatement pstmtx = con.prepareStatement(queryxml);
			int res=0;
  
			for (int itr = 0; itr < nodeList.getLength(); itr++)   
			{  
			Node node = nodeList.item(itr);  
			//System.out.println("\nNode Name :" + node.getNodeName());  
			if (node.getNodeType() == Node.ELEMENT_NODE)   
			{  
			Element eElement = (Element) node;  
			pstmtx.setString(1, eElement.getElementsByTagName("trainNumber").item(0).getTextContent());
			pstmtx.setString(2,eElement.getElementsByTagName("route").item(0).getTextContent());
			pstmtx.setInt(3, Integer.parseInt(eElement.getElementsByTagName("source").item(0).getTextContent()));
			pstmtx.setInt(4, Integer.parseInt(eElement.getElementsByTagName("destination").item(0).getTextContent()));
			res=pstmtx.executeUpdate();
			System.out.println(res+" record(s) inserted in xml file");
			
			
			}  
			}  
			}   
			catch (Exception e)   
			{  
			e.printStackTrace();  
			}  
		}
			}

			
		//user input
			System.out.println("enter a source and destination:");
			int src = sc.nextInt();
			int dest = sc.nextInt();
			PreparedStatement pstmt1 = con.prepareStatement("select * from traindetails where source=? and destination=? ");
			pstmt1.setInt(1, src);
			pstmt1.setInt(2, dest);
			ResultSet rs = pstmt1.executeQuery();
			// ResultSet rs=pstmt.executeQuery("select * from traindetails");
			while (rs.next()) {
				System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getInt(3) + " " + rs.getInt(4));
				System.out.println("*************************");
			}
			
			rs.close();
			stmt.close();
						con.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	

}


