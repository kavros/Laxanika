package Main;


import Model.*;

import java.util.ArrayList;

public class Main {
    public static  void main(String[] args) {
/*
        XMLModifier xml = new XMLModifier();
        //xml.editXMLNode("ΑΓΓΟΥΡΙΑ","ΑΓΓΟΥΡΙΑ1","name");
        //xml.editXMLNode("2100","2100..","kef5Code");
        xml.editXMLNode("ΑΓΓΟΥΡΙΑ","0.251","profit");
        ExcelReader reader = new ExcelReader();
        VFHashMap map    = new VFHashMap();
        VFVector data = new VFVector();
        reader.readExcelFile("C:\\Users\\Alexis\\Desktop\\misc\\laxanika\\a2.xls", data);
        data.update(map);
        try{

           // VFVector vector = new VFVector();
            //PdfParser reader = new PdfParser();
            //reader.parsePdfFile("C:\\Users\\Alexis\\IdeaProjects\\Laxanika\\pdf\\Crystal Reports - Παραστατικό πωλήσεων (Laser) - FRUITLINE,0338AEDB-7AF2-470C-A692-DB26B157DA2B.pdf",vector);
            //System.out.println(vector);
            //VFKef5DataBase dataBase = new VFKef5DataBase();
            //dataBase.getFromDatabase("select count(sFileId) from dbo.SMAST;");
            //double a=dataBase.getFromDatabase("select sRetailPr  from dbo.smast where sCode='251';");
            //dataBase.updateKef5Price("UPDATE dbo.smast SET sRetailPr = 40 WHERE sCode='2100';");
            //VFHashMap hashmap =  new VFHashMap();
           // VFHashMapValues a =hashmap.get("ΜΗΛΑ");
            //System.out.println(vector);
           //System.out.println(hashmap);
            //System.out.println(a.getProfit());

        }catch (Exception e){

            e.getStackTrace();
        }

*/

 /*     try {


        PriceHistory a = new PriceHistory();
        a.setDate("15-Μαρ-18");
        a.addPrice("500", 3.60);
        a.addPrice("257", 3.60);
        a.addPrice("259", 3.60);
      a.setDate("16-Μαρ-18");
        a.addPrice("500", 3.70);
        a.addPrice("500", 3.60);


        a.setDate("17-Μαρ-18");
        a.addPrice("500", 3.40);*/
        /*a.addPrice("500", 3.70);
        a.addPrice("500", 3.80);
        a.addPrice("500", 3.90);
        a.addPrice("500", 3.91);

        a.addPrice("320", 3.90);
        a.addPrice("320", 3.92);
        a.addPrice("320", 3.82);


        a.addPrice("420", 3.91);
        a.addPrice("420", 3.92);
        System.out.println(a.getVector());
        PriceHistory.Prices prices = a.getPrices("500");
        System.out.println(prices.getTotalPrices());
        System.out.println(prices.getPrice1());
        System.out.println(prices.getPrice2());
        System.out.println(prices.getPrice3());
        prices = a.getPrices("400");
        System.out.println(prices.getTotalPrices());

        a.writeToFile();
    }catch (Exception e){
        e.printStackTrace();
    }
*/

        String a = new String("Τ∆Π00457493 ∆ευ  5/3/18 07:28");
        String b =a.replace("  "," ");
        System.out.println(b);
    }

}

