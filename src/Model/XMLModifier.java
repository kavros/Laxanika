package Model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XMLModifier {

    public void addXMLNode(String vf_name,String profit,String kef5Code){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = null;
            document = documentBuilder.parse("C:\\Users\\Alexis\\IdeaProjects\\Laxanika\\cfg\\products.xml");


            Element root = document.getDocumentElement();


            // product elements
            Element newProduct = document.createElement("element");

            Element name = document.createElement("name");
            name.appendChild(document.createTextNode(vf_name));
            newProduct.appendChild(name);

            Element profitEL = document.createElement("profit");
            profitEL.appendChild(document.createTextNode(profit));
            newProduct.appendChild(profitEL);

            Element kef5CodeEl = document.createElement("kef5Code");
            kef5CodeEl.appendChild(document.createTextNode(kef5Code));
            newProduct.appendChild(kef5CodeEl);


            root.appendChild(newProduct);


            DOMSource source = new DOMSource(document);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = null;
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

            StreamResult result = new StreamResult("C:\\Users\\Alexis\\IdeaProjects\\Laxanika\\cfg\\products.xml");
            transformer.transform(source, result);

        }catch (Exception e){
            System.err.println("function addToXML at class XMLModifier has failed");
            e.printStackTrace();
        }
    }



    public  void modifyXMLNode(String  vf_name,String newVfName){
        try {
            String filepath = "C:\\Users\\Alexis\\IdeaProjects\\Laxanika\\cfg\\products.xml";
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

    public void deleteXMLNode(String vf_name){
        modifyXMLNode(vf_name,"DELETED");
    }



}
