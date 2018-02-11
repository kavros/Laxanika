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
            document = documentBuilder.parse("cfg\\products.xml");


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

    /**
     * editXMLNode("TOMATES" ,0.35,"profit") ->change profit for "TOMATES"  to 0.35
     * editXMLNode("TOMATES" ,"TOMATES1" ,"profit") ->change name for "TOMATES"  to "TOMATES" (done)
     * editXMLNode("2100" ,"2108" ,"kef5Code") ->change kef5Code from "2100"  to "2108" Problima: yparxoun proionta me ton idio kwdiko
     * @param targetVal
     * @param newVal
     * @param tag
     * @return
     */
    public  boolean editXMLNode(String  targetVal,String newVal,String tag){
        //tag can have three possible values : name,kef5Code,profit.
        if( (!tag.equals("name") ) && (!tag.equals("kef5Code")) && (!tag.equals("profit")) ){
            return false;
        }
        boolean isNewValAssigned = false;
        try {
            String filepath = "cfg\\products.xml";
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

            NodeList nodes = doc.getElementsByTagName("element");
            Node nNode;

            for(int temp=0; temp<nodes.getLength(); ++temp){
                nNode=nodes.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement       = (Element) nNode;
                    String name = eElement.getElementsByTagName("name").item(0).getTextContent();
                    String profit = eElement.getElementsByTagName("profit").item(0).getTextContent();
                    String kef5Code = eElement.getElementsByTagName("kef5Code").item(0).getTextContent();
                    //System.out.println(name);
                    if(tag.equals("name") && name.equals(targetVal )){
                        eElement.getElementsByTagName("name").item(0).setTextContent(newVal);
                    }else if(tag.equals("kef5Code") && kef5Code.equals(targetVal) ){
                        eElement.getElementsByTagName("kef5Code").item(0).setTextContent(newVal);
                    }else if(tag.equals("profit") && name.equals(targetVal) ){
                        eElement.getElementsByTagName("profit").item(0).setTextContent(newVal);
                    }
                }
            }

            /*for(int i=0; i < nodes.getLength();++i){

                targetNode =nodes.item(i);
                if(targetNode.getTextContent().equals(targetVal)){
                    System.out.println(targetNode.getTextContent()+"=="+targetVal);

                    targetNode.setTextContent(newVal);
                    isNewValAssigned =true;
                    break;
                }
            }*/

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

        return isNewValAssigned;
    }




    public void deleteXMLNode(String vf_name){
        editXMLNode(vf_name,"DELETED","name");
    }



}
