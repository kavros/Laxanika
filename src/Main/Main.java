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
            reader.parsePdfFile("C:\\Users\\Alexis\\IdeaProjects\\Laxanika\\pdf\\a.pdf",vector);
            System.out.println(vector);
            VFKef5DataBase dataBase = new VFKef5DataBase();
            dataBase.testDatabase();

        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }


    }

}

