package Main;

import Model.*;

public class Main {
    public static  void main(String[] args) {

        ExcelReader reader = new ExcelReader();
        VFHashMap map    = new VFHashMap();
        VFVector data = new VFVector();
        reader.readExcelFile("C:\\Users\\Alexis\\Desktop\\misc\\laxanika\\a2.xls", data);
        data.update(map);

        VFKef5DataBase dataBase = new VFKef5DataBase();
        dataBase.testDatabase();


    }

}

