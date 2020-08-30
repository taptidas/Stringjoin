package JdbcConn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.lang.Object;
import org.codehaus.jackson.map.ObjectMapper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UpdatedClass {


	private Connection con = null;
	public UpdatedClass() throws ClassNotFoundException, SQLException
	{
		Class.forName("oracle.jdbc.driver.OracleDriver");

		con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "oracle123");


	}

	List<TrainDetailsVO> trainDetailsVOList = new ArrayList<TrainDetailsVO>();
	List<TrainDetailsVO> trainDetailsVOListj = new ArrayList<TrainDetailsVO>();
	List<TrainDetailsVO> trainDetailsVOListx = new ArrayList<TrainDetailsVO>();

	public void deleteRecordDb() throws SQLException
	{
		Statement stmt = con.createStatement();
		String sql="delete from traindetails";
		stmt.executeUpdate(sql);  
		System.out.println(" records affected");  


	}


	public void readDataFromFiles(char ch) throws FileNotFoundException, IOException, ParseException, ParserConfigurationException, SAXException, ClassNotFoundException, SQLException
	{

		TrainDetailsVO trainDetailsVO = null;
		switch(ch) {

		case'y':
		{	// text file
			String currLine;
			String[] tempArray;
			String filePath = "C:\\Users\\HP\\Downloads\\tapticts\\TrainRoutesAndFares.dat";

			FileReader fileReader = new FileReader(filePath);
			BufferedReader reader = new BufferedReader(fileReader);

			while ((currLine = (reader.readLine())) != null) {
				tempArray = currLine.split(",");
				int tempSource = Integer.parseInt(tempArray[2]);
				int tempDestination = Integer.parseInt(tempArray[3]);

				trainDetailsVO = new TrainDetailsVO();
				trainDetailsVO.setTrainNumber(tempArray[0]);
				trainDetailsVO.setRoute(tempArray[1]);
				trainDetailsVO.setSource(tempSource);
				trainDetailsVO.setDestination(tempDestination);

				trainDetailsVOList.add(trainDetailsVO);
			}
			reader.close();
			insertToDb(trainDetailsVOList);
			break;
		}

		case'n':
		{// json file reading
			JSONParser jsonParser = new JSONParser();
			Object obj = (Object) jsonParser.parse(new FileReader("train.json"));

			JSONArray jsonObject = (JSONArray) obj;
			for (int j = 0; j < jsonObject.size(); j++) {
				TrainDetailsVO trainDetailsVOj = new TrainDetailsVO();

				JSONObject object = (JSONObject) jsonObject.get(j);
				trainDetailsVOj.setTrainNumber((String) object.get("trainNumber"));
				trainDetailsVOj.setSource(((Long) object.get("source")).intValue());
				trainDetailsVOj.setRoute((String) object.get("route"));
				trainDetailsVOj.setDestination(((Long) object.get("destination")).intValue());
				trainDetailsVOListj.add(trainDetailsVOj);

			}
			insertToDb(trainDetailsVOListj);
			break;
		}
		default:
		{
			// xml file reading
			File file = new File("E:\\tapti\\Jdbc\\src\\JdbcConn\\traindetail.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			// System.out.println("Root element: " +
			// doc.getDocumentElement().getNodeName());
			NodeList nodeList = doc.getElementsByTagName("TrainDetailsVO");

			for (int itr = 0; itr < nodeList.getLength(); itr++) {
				Node node = nodeList.item(itr);
				// System.out.println("\nNode Name :" + node.getNodeName());
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					TrainDetailsVO trainDetailsVOx = new TrainDetailsVO();

					Element eElement = (Element) node;
					trainDetailsVOx.setTrainNumber((eElement.getElementsByTagName("trainNumber").item(0).getTextContent()));
					trainDetailsVOx.setRoute((eElement.getElementsByTagName("route").item(0).getTextContent()));
					trainDetailsVOx.setSource((Integer.parseInt(eElement.getElementsByTagName("source").item(0).getTextContent())));
					trainDetailsVOx.setDestination((Integer.parseInt(eElement.getElementsByTagName("destination").item(0).getTextContent())));
					trainDetailsVOListx.add(trainDetailsVOx);

				}
			}
			insertToDb(trainDetailsVOListx);
			break;
		}
		}
	}

	public  void insertToDb(List<TrainDetailsVO> listName)throws SQLException, ClassNotFoundException {
		/**Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "oracle123");
		Class.forName("oracle.jdbc.driver.OracleDriver");**/

		String queryxml = "insert into traindetails(train_number,route,source,destination) values(?,?,?,?)";
		PreparedStatement pstmtx = con.prepareStatement(queryxml);
		int res = 0;

		for (int i = 0; i < listName.size(); i++) {
			System.out.println(listName.get(i));
			pstmtx.setString(1, listName.get(i).getTrainNumber());
			pstmtx.setString(2, listName.get(i).getRoute());
			pstmtx.setInt(3, listName.get(i).getSource());
			pstmtx.setInt(4, listName.get(i).getDestination());

			res = pstmtx.executeUpdate();
			System.out.println(res+"records inserted");
		}

	}

	public  List<TrainDetailsVO> retrieveDb() throws SQLException, ClassNotFoundException {
		/**	Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "oracle123");
		Class.forName("oracle.jdbc.driver.OracleDriver");**/

		List<TrainDetailsVO> trainDetailsVOList = new ArrayList<TrainDetailsVO>();
		PreparedStatement pstmt1 = con.prepareStatement("select * from traindetails ");
		ResultSet rs = pstmt1.executeQuery();
		// ResultSet rs=pstmt.executeQuery("select * from traindetails");
		while (rs.next()) {
			TrainDetailsVO trainDetailsVO = new TrainDetailsVO();
			trainDetailsVO.setTrainNumber(rs.getString(1));
			trainDetailsVO.setRoute(rs.getString(2));
			trainDetailsVO.setSource(rs.getInt(3));
			trainDetailsVO.setDestination(rs.getInt(4));
			trainDetailsVOList.add(trainDetailsVO);
			System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getInt(3) + " " + rs.getInt(4));
			System.out.println("*************************");

		}

		return trainDetailsVOList;
	}

	//assertEquals("String.valueOf(src)-String.valueOf(dest)",stringWithHyphen);


	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, ParserConfigurationException, SAXException, ClassNotFoundException, SQLException {
		Scanner sc=new Scanner(System.in);
		UpdatedClass uc=new UpdatedClass();


		System.out.println("do you want values from text file(y/n)?");
		char ch=sc.next().charAt(0);
		//uc.deleteRecordDb();
		uc.readDataFromFiles(ch);


		List<TrainDetailsVO> trainDetailsVOListc=uc.retrieveDb();
		System.out.println("list:"+trainDetailsVOListc);
		Map<String,List<TrainDetailsVO>> map=new HashMap<>();
		String	stringWithHyphen = null;

		for(int i=0;i<trainDetailsVOListc.size();i++)
		{
			stringWithHyphen=String.join("-",String.valueOf(trainDetailsVOListc.get(i).getSource()),String.valueOf(trainDetailsVOListc.get(i).getDestination() ));
			if(map.containsKey(stringWithHyphen))
			{
				List<TrainDetailsVO> temp=map.get(stringWithHyphen);
				temp.add(trainDetailsVOListc.get(i));
				map.put(stringWithHyphen,temp);	

			}
			else
			{
				List<TrainDetailsVO> temp=new ArrayList<>();
				temp.add(trainDetailsVOListc.get(i));
				map.put(stringWithHyphen,temp);	


			}
		}
		System.out.println("enter a source and a destination:");
		int src=sc.nextInt();
		int dest=sc.nextInt();
		String input=String.join("-",String.valueOf(src),String.valueOf(dest));
		if(map.containsKey(input))
		{
			ObjectMapper Obj = new ObjectMapper();
			String jsonStr = Obj.defaultPrettyPrintingWriter().writeValueAsString(map.get(input));
			System.out.println(jsonStr); 

			//System.out.println(map.get(input));
		}
	}


}
