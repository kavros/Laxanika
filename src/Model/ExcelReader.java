
package Model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

public class ExcelReader {

    public void readExcelFile(String fileName, VFVector vec) {

        InputStream ExcelFileToRead = null;
        HSSFWorkbook workbook = null;
        try {
            ExcelFileToRead = new FileInputStream(fileName);
            //Getting the workbook instance for excel file
            workbook = new HSSFWorkbook(ExcelFileToRead);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //getting the first sheet from the workbook using sheet name.
        // We can also pass the index of the sheet which starts from '0'.
        HSSFSheet sheet = workbook.getSheet("Παραστατικό πωλήσεων (Laser) - ");
        HSSFRow row;
        HSSFCell cell;

        //Iterating all the rows in the sheet
        Iterator rows = sheet.rowIterator();
        int row_num                 = 1;
        int cell_num                = 1;

        boolean skip_last_lines     = false;
        while (rows.hasNext()) {
            row = (HSSFRow) rows.next();
            VFVectorEntry entry = new VFVectorEntry();
            //Iterating all the cells of the current row
            Iterator cells = row.cellIterator();

            //Skip first 17 lines
            if(row_num < 17){
                row_num++;
                continue;
            }

            while (cells.hasNext()) {
                if(skip_last_lines == true){
                    break;
                }
                cell = (HSSFCell) cells.next();
                if (cell.getCellTypeEnum() == CellType.STRING) {

                    //stop parsing  files data
                    if(cell.getStringCellValue().contains("ΥΠΟΛΟΙΠΑ")){
                            skip_last_lines =true;
                            break;
                    }

                    if(cell_num==1){
                        entry.vf_name=cell.getStringCellValue();
                    }else if(cell_num==2) {
                        entry.vf_mm = cell.getStringCellValue();
                    }else if(cell_num==3){
                        entry.vf_packing=cell.getStringCellValue();
                    }else if(cell_num==7){
                        entry.vf_value = cell.getStringCellValue();
                    }else if(cell_num == 9){
                        entry.vf_net_value =cell.getStringCellValue();
                    }

                } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                    if(cell_num==4){

                    }else if(cell_num==5){
                        entry.vf_quantity = cell.getNumericCellValue();
                    }else if(cell_num==6){
                        entry.vf_price = cell.getNumericCellValue();
                    }else if(cell_num==8) {
                        entry.vf_discount = cell.getNumericCellValue();
                    }else if(cell_num==10){
                        entry.vf_tax = cell.getNumericCellValue();
                    }

                } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {


                } else { // //Here if require, we can also add below methods to

                }
                cell_num++;
            }
            if(skip_last_lines == true){
                break;
            }
            vec.add(entry);
            row_num++;
            cell_num=1;

        }
        //
        try {
            ExcelFileToRead.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}