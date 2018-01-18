package Main;

import Model.ReadExcel;
import Model.VFHashMap;
import Model.VFKef5DataBase;
import Model.VFVector;

public class Main {
    public static  void main(String[] args) {

        ReadExcel reader = new ReadExcel();
        VFHashMap map    = new VFHashMap();
        VFVector data = new VFVector();
        reader.readXLSFile("C:\\Users\\Alexis\\Desktop\\misc\\laxanika\\a2.xls", data);
        data.update(map);

        VFKef5DataBase dataBase = new VFKef5DataBase();
        dataBase.testDatabase();


    }

}

