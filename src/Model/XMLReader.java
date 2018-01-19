package Model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;

public class XMLReader {
    public class DatabaseEntry{
        String username;
        String password;
        String dbURL;
    }

    public DatabaseEntry getDatabaseCredentials(){
        DatabaseEntry dbEntry = new DatabaseEntry();
        try {

            File fXmlFile = new File("cfg\\database.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            doc.getDocumentElement().normalize();

            String rootName=doc.getDocumentElement().getNodeName();
            NodeList nList = doc.getElementsByTagName(rootName);

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    //System.out.println("Staff id : " + eElement.getAttribute("id"));
                    dbEntry.username = eElement.getElementsByTagName("username").item(0).getTextContent();
                    dbEntry.password= eElement.getElementsByTagName("password").item(0).getTextContent();
                    dbEntry.dbURL= eElement.getElementsByTagName("dbURL").item(0).getTextContent();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbEntry;
    }

    public HashMap<String, VFHashMapValues> getProducts(){

        HashMap<String, VFHashMapValues>   map    = new  HashMap<String, VFHashMapValues> ();

        String                           vfName = new String();

        try {

            File fXmlFile = new File("cfg\\products.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            doc.getDocumentElement().normalize();

            String rootName=doc.getDocumentElement().getNodeName();
            NodeList nList = doc.getElementsByTagName("element");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    VFHashMapValues values = new VFHashMapValues();
                    Element eElement       = (Element) nNode;

                    //System.out.println("Staff id : " + eElement.getAttribute("id"));
                    vfName = eElement.getElementsByTagName("name").item(0).getTextContent();
                    values.profit   = Double.parseDouble(eElement.getElementsByTagName("profit").item(0).getTextContent());
                    values.kef5Code = Integer.parseInt(eElement.getElementsByTagName("kef5Code").item(0).getTextContent());

                    map.put(vfName,values);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  map;
    }

}
