package Test.stackoverflow;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ModifyXMLFile {

    public static void main(String argv[]) {

        /*try {
            String filepath = "C:\\Users\\Alexis\\IdeaProjects\\Laxanika\\src\\Test\\stackoverflow\\file.xml";
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

            // Get the root element
            Node company = doc.getFirstChild();

            // Get the staff element , it may not working if tag has spaces, or
            // whatever weird characters in front...it's better to use
            // getElementsByTagName() to get it directly.
            // Node staff = company.getFirstChild();

            // Get the staff element by tag name directly
            Node staff = doc.getElementsByTagName("staff").item(0);

            // update staff attribute
            NamedNodeMap attr = staff.getAttributes();
            Node nodeAttr = attr.getNamedItem("id");
            nodeAttr.setTextContent("2");

            // append a new node to staff
            Element age = doc.createElement("age");
            age.appendChild(doc.createTextNode("28"));
            staff.appendChild(age);

            // loop the staff child node
            NodeList list = staff.getChildNodes();

            for (int i = 0; i < list.getLength(); i++) {

                Node node = list.item(i);

                // get the salary element, and update the value
                if ("salary".equals(node.getNodeName())) {
                    node.setTextContent("2000000");
                }

                //remove firstname
                if ("firstname".equals(node.getNodeName())) {
                    staff.removeChild(node);
                }

            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);

            System.out.println("Done");

        } catch (Exception e) {
            System.err.println("Method modify at class XMLModifier failed ");
            e.printStackTrace();
        }*/
        modifyXMLFile("ΣΥΚΑ1","ΣΥΚΑ");
    }
    public static void modifyXMLFile(String  vf_name,String newVfName){
        try {
            String filepath = "C:\\Users\\Alexis\\IdeaProjects\\Laxanika\\src\\Test\\stackoverflow\\server.xml";
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

            NodeList nodes = (NodeList) doc.getElementsByTagName("name");//.item(0);
            Node targetNode=null;

            for(int i=0; i < nodes.getLength();++i){

                targetNode =nodes.item(i);
                if(targetNode.getTextContent().equals(vf_name)){
                    targetNode.setTextContent(newVfName);
                    break;
                }
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);



        } catch (Exception e) {
            System.err.println("Method modifyXML at class XMLModifier has failed ");
            e.printStackTrace();
        }
    }
}