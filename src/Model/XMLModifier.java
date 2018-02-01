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
    private boolean isInputsValid(String vf_name,String profit,String kef5Code){
        if(vf_name.isEmpty() || profit.isEmpty()|| kef5Code.isEmpty()){
            return false;
        }

        if(Double.parseDouble(profit)> 1 ){
            System.out.println(profit);
            return  false;
        }
        char[] pro  = profit.toCharArray();
        int i;
        for( i=0; i < pro.length; ++i){
            if( !Character.isDigit(pro[i]) &&
                     pro[i] != '.'){
                return false;
            }
        }


        return  true;

    }

    public boolean addXMLNode(String vf_name,String profit,String kef5Code){

        //fix number format for profit
        char[] pr = profit.toCharArray();
        for(int i=0; i < pr.length; ++i){
            if(pr[i] == ','){
                pr[i] ='.';
            }
        }
        profit = new String(pr);

        double profit_num = Double.parseDouble(profit);
        if(profit_num > 1){
            profit_num=profit_num/100;
        }
        profit = String.valueOf(profit_num);

        //validate user inputs
        if(! isInputsValid(vf_name,profit,kef5Code) ){
            return false;
        }

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document;
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

            Transformer transformer;
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

            StreamResult result = new StreamResult("C:\\Users\\Alexis\\IdeaProjects\\Laxanika\\cfg\\products.xml");
            transformer.transform(source, result);

        }catch (Exception e){
            System.err.println("function addToXML at class XMLModifier has failed");
            e.printStackTrace();
        }
        return  true;
    }



    public  void modifyXMLNode(String  vf_name,String newVfName){
        try {
            String filepath = "C:\\Users\\Alexis\\IdeaProjects\\Laxanika\\cfg\\products.xml";
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

            NodeList nodes = doc.getElementsByTagName("name");
            Node targetNode;

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
