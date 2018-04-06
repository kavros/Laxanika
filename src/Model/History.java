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
import java.util.Vector;

//TODO: fix  xml indentation
public class History {

    public class HistoryNode{
        private String _name;
        private String _kef5Code;

        private String _date1;
        private float _price1;

        private String _date2;
        private float _price2;

        private String _date3;
        private float _price3;

        //constractor
        public HistoryNode(String _name,
                           String _kef5Code,
                           String _date1,
                           float _price1,
                           String _date2,
                           float _price2,
                           String _date3,
                           float _price3) {

            this._name = _name;
            this._kef5Code = _kef5Code;
            this._date1 = _date1;
            this._price1 = _price1;
            this._date2 = _date2;
            this._price2 = _price2;
            this._date3 = _date3;
            this._price3 = _price3;
        }


        //getters
        public String getDate1() {
            return _date1;
        }

        public float getPrice1() {
            return _price1;
        }

        public String getDate2() {
            return _date2;
        }

        public float getPrice2() {
            return _price2;
        }

        public String getDate3() {
            return _date3;
        }

        public float getPrice3() {
            return _price3;
        }

        public String getName() {
            return _name;
        }

        public String getKef5Code() {
            return _kef5Code;
        }


        //setters
        public void setName(String name) {
            this._name = name;
        }

        public void setDate1(String date1){
            this._date1 = date1;
        }

        public void setPrice1(float _price1) {
            this._price1 = _price1;
        }

        public void setDate2(String _date2) {
            this._date2 = _date2;
        }

        public void setPrice2(float _price2) {
            this._price2 = _price2;
        }

        public void setDate3(String _date3) {
            this._date3 = _date3;
        }

        public void setPrice3(float _price3) {
            this._price3 = _price3;
        }

        public void setKef5Code(String _kef5Code) {
            this._kef5Code = _kef5Code;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "_name='" + _name + '\'' +
                    ", _date1='" + _date1 + '\'' +
                    ", _price1=" + _price1 +
                    ", _date2='" + _date2 + '\'' +
                    ", _price2=" + _price2 +
                    ", _date3='" + _date3 + '\'' +
                    ", _price3=" + _price3 +
                    '}';
        }

        public String toString2() {
            return
                     _price1 +" "+
                     _price2 +" "+
                     + _price3
                    ;
        }

    }

    private Vector<HistoryNode>   _history;
    private Vector<String> _dates;

    public History() {
        _history = new Vector<HistoryNode>();
        _dates   = new Vector<String>();
    }

    public Vector<HistoryNode> getHistoryVector() {
        return _history;
    }

    private boolean isDateNew(String date){
        for(int i =0; i < _dates.size();i++){
            if(_dates.get(i).trim().equals(date.trim())){
                return  false;
            }
        }
        return  true;
    }

    public void addDateNodeToDatabase(String date){
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document;
            document = documentBuilder.parse(Constants.getHistoryDatabaseFilePath());

            Element root = document.getDocumentElement();
            NodeList nodeList = document.getElementsByTagName("date");

            Element newDate = document.createElement("date");
            newDate.appendChild(document.createTextNode(date));

            root.insertBefore(newDate,nodeList.item(0));

            DOMSource source = new DOMSource(document);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer;
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "1");

            StreamResult result = new StreamResult(Constants.getHistoryDatabaseFilePath());
            transformer.transform(source, result);


        }catch (Exception e ){
            System.err.println("function addDateToDatabase at class History has failed");
            e.printStackTrace();

        }
    }

    public void addHistoryNodeToDatabase(HistoryNode node){
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document= documentBuilder.parse(Constants.getHistoryDatabaseFilePath());;
            Element root = document.getDocumentElement();

            Element newProduct = document.createElement("product");
            Element name = document.createElement("name");
            name.appendChild(document.createTextNode(node.getName()));
            newProduct.appendChild(name);

            Element kef5Code = document.createElement("kef5Code");
            kef5Code.appendChild(document.createTextNode(node.getKef5Code()));
            newProduct.appendChild(kef5Code);


            Element price1 = document.createElement("price1");

            price1.setAttribute("date",node.getDate1());
            price1.appendChild(document.createTextNode( String.valueOf (node.getPrice1())));
            newProduct.appendChild(price1);

            Element price2 = document.createElement("price2");
            price2.setAttribute("date",node.getDate2());
            price2.appendChild(document.createTextNode( String.valueOf (node.getPrice2())));
            newProduct.appendChild(price2);

            Element price3 = document.createElement("price3");
            price3.setAttribute("date",node.getDate3());
            price3.appendChild(document.createTextNode( String.valueOf (node.getPrice3())));
            newProduct.appendChild(price3);

            root.appendChild(newProduct);


            DOMSource source = new DOMSource(document);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer;
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "5");

            StreamResult result = new StreamResult(Constants.getHistoryDatabaseFilePath());
            transformer.transform(source, result);

        }catch (Exception e ){
            System.err.println("function addHistoryNodeToDatabase at class History has failed");
            e.printStackTrace();

        }

    }

    private void editHistoryNode(double price,String date,String kef5Code){

        try{
            String filePath  = Constants.getHistoryDatabaseFilePath();
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filePath);
            NodeList nodes = doc.getElementsByTagName("product");


            Node nNode;

            for(int i =0; i < nodes.getLength();i++){
                nNode = nodes.item(i);
                Element elem = ((Element)nNode);
                String code =elem.getElementsByTagName("kef5Code").item(0).getTextContent();
                if(kef5Code.equals(code)){
                    String price2 = elem.getElementsByTagName("price2").item(0).getTextContent();
                    String price3 =elem.getElementsByTagName("price3").item(0).getTextContent();
                    if(price2.equals("0.0") && price3.equals("0.0")){
                        elem.getElementsByTagName("price2").item(0).setTextContent(Double.toString(price));
                        Element ee = (Element) elem.getElementsByTagName("price2").item(0);
                        ee.setAttribute("date",date);
                    }else if( (!price2.equals("0.0")) && (price3.equals("0.0")) ){
                        elem.getElementsByTagName("price3").item(0).setTextContent(Double.toString(price));
                        Element ee = (Element) elem.getElementsByTagName("price3").item(0);
                        ee.setAttribute("date",date);
                    }else if(!price2.isEmpty() & (!price3.isEmpty()) ){
                        //move pair (price 2,date2) to pair (price 1,date 1)
                        String prevPrice2 = elem.getElementsByTagName("price2").item(0).getTextContent();
                        Element ee = (Element) elem.getElementsByTagName("price2").item(0);
                        String prevDate2 = ee.getAttribute("date");

                        elem.getElementsByTagName("price1").item(0).setTextContent(prevPrice2);
                        ee = (Element) elem.getElementsByTagName("price1").item(0);
                        ee.setAttribute("date",prevDate2);

                        //move pair (price 3,date 3) to pair (price 2,date 2)
                        String prevPrice3 = elem.getElementsByTagName("price3").item(0).getTextContent();
                        ee = (Element) elem.getElementsByTagName("price3").item(0);
                        String prevDate3 = ee.getAttribute("date");

                        elem.getElementsByTagName("price2").item(0).setTextContent(prevPrice3);
                        ee = (Element) elem.getElementsByTagName("price2").item(0);
                        ee.setAttribute("date",prevDate3);

                        //add price and date to 3d position.
                        elem.getElementsByTagName("price3").item(0).setTextContent(Double.toString(price));
                        ee = (Element) elem.getElementsByTagName("price3").item(0);
                        ee.setAttribute("date",date);

                    }
                }
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(filePath));
                transformer.transform(source, result);

            }

        }catch (Exception e){
            System.err.println("editHistoryNode failed");
            e.printStackTrace();
        }
    }

    public void updateHistoryDatabase(VFVector products,String timestamp){
        if(!isDateNew(timestamp)){
            return;//if date is already known stop update process.
        }
        //add date node to the history.
        addDateNodeToDatabase(timestamp);
        if(_history.isEmpty()){
            readDatabase();
        }
        //update history
        boolean isProductInHistory = false;
        for(int i=0; i < products.getSize(); i++){
            VFVectorEntry entry = products.getVec().get(i);
            String kef5Code = entry.getKef5Code();
            for(int j = 0; j < _history.size(); j++){
                String code = _history.get(j)._kef5Code;
                if(code.equals(kef5Code)){
                    //modify xml node
                    editHistoryNode(entry.getVfFinalPrice(),timestamp,kef5Code);
                    isProductInHistory = true;
                }
            }
            if(isProductInHistory == false){
                float a =(float) entry.vf_final_price;
                addHistoryNodeToDatabase(
                        new HistoryNode(
                                entry.vf_name,entry.kef5_code,timestamp,a,
                                "",0.0f,"",0.0f)
                                        );
            }
            isProductInHistory =false;
        }
        clearHistory();
        readDatabase();
    }

    public void readDatabase(){
        try {

            File fXmlFile = new File(Constants.getHistoryDatabaseFilePath());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            doc.getDocumentElement().normalize();

            String rootName=doc.getDocumentElement().getNodeName();
            NodeList nList = doc.getElementsByTagName(rootName);


            //read Dates from xml database.
            NodeList datesList = doc.getElementsByTagName("date");

            for(int i=0; i < datesList.getLength(); i++){
                String currDate =datesList.item(i).getTextContent();
                _dates.add(currDate);
            }


            NodeList productsList = doc.getElementsByTagName("product");
            for(int i = 0; i < productsList.getLength(); i++){
                Node nNode = productsList.item(i);

                Element eElement = (Element) nNode;
                String name = eElement.getElementsByTagName("name").item(0).getTextContent();
                String kef5Code = eElement.getElementsByTagName("kef5Code").item(0).getTextContent();
                String price1 =eElement.getElementsByTagName("price1").item(0).getTextContent();
                String date1 =((Element)eElement.getElementsByTagName("price1").item(0)).getAttribute("date");
                String price2 =eElement.getElementsByTagName("price2").item(0).getTextContent();
                String date2 =((Element)eElement.getElementsByTagName("price2").item(0)).getAttribute("date");
                String price3 =eElement.getElementsByTagName("price3").item(0).getTextContent();
                String date3 =((Element)eElement.getElementsByTagName("price3").item(0)).getAttribute("date");
                HistoryNode histNode = new HistoryNode(
                                    name,kef5Code,date1,Float.parseFloat(price1),
                                    date2,Float.parseFloat(price2),
                                    date3,Float.parseFloat(price3)
                );

                _history.add(histNode);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearHistory(){
        _history.clear();
        _dates.clear();
    }
    @Override
    public String toString() {
        return "History{" +
                "_history=" + _history +
                ", _dates=" + _dates +
                '}';
    }
}
