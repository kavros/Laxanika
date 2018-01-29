package Main;

import Model.*;

public class Main {
    public static  void main(String[] args) {

        /*ExcelReader reader = new ExcelReader();
        VFHashMap map    = new VFHashMap();
        VFVector data = new VFVector();
        reader.readExcelFile("C:\\Users\\Alexis\\Desktop\\misc\\laxanika\\a2.xls", data);
        data.update(map);
*/
        try{

            VFVector vector = new VFVector();
            PdfParser reader = new PdfParser();
            reader.parsePdfFile("C:\\Users\\Alexis\\IdeaProjects\\Laxanika\\pdf\\Crystal Reports - Παραστατικό πωλήσεων (Laser) - FRUITLINE,0338AEDB-7AF2-470C-A692-DB26B157DA2B.pdf",vector);
            //System.out.println(vector);
            //VFKef5DataBase dataBase = new VFKef5DataBase();
            //dataBase.getKef5Price("select count(sFileId) from dbo.SMAST;");
            //double a=dataBase.getKef5Price("select sRetailPr  from dbo.smast where sCode='251';");
            //dataBase.updateKef5Price("UPDATE dbo.smast SET sRetailPr = 40 WHERE sCode='2100';");
            VFHashMap hashmap =  new VFHashMap();
            VFHashMapValues a =hashmap.get("ΜΗΛΑ");
            //System.out.println(vector);
           //System.out.println(hashmap);
            //System.out.println(a.getProfit());

        }catch (Exception e){

            e.getStackTrace();
        }


    }

}

