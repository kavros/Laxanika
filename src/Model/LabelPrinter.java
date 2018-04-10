package Model;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;

import org.apache.poi.xwpf.usermodel.*;

import javax.swing.*;

public class LabelPrinter {

    public void printLabels(Vector<VFVectorEntry> vector){
        try {
            //Blank Document
            XWPFDocument document = new XWPFDocument();

            //Write the Document in file system
            JFileChooser f = new JFileChooser();
            f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            f.setCurrentDirectory(new File
                    (System.getProperty("user.home") + System.getProperty("file.separator")+ "Desktop"));
            f.showSaveDialog(null);
            FileOutputStream out = new FileOutputStream(new File(f.getSelectedFile()+"\\"+"labels.docx"));

            //create paragraph
            XWPFParagraph paragraph = document.createParagraph();
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            for (int i =0; i < vector.size(); i++){
                createLabel(vector.elementAt(i),paragraph,i+1);
            }

            document.write(out);
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void createLabel(VFVectorEntry entry,XWPFParagraph paragraph,int number) {

        //Set Bold an Italic
        XWPFRun paragraphOneRunOne = paragraph.createRun();
        //paragraphOneRunOne.setBold(true);
        //paragraphOneRunOne.setItalic(true);

        paragraphOneRunOne.setFontSize(36);

        paragraphOneRunOne.setText(entry.getVfName()+" "+entry.getVfOrigin());
        paragraphOneRunOne.addBreak();

        //Set text Position
        XWPFRun paragraphOneRunTwo = paragraph.createRun();
        paragraphOneRunTwo.setFontSize(36);
        paragraphOneRunTwo.setText(entry.getVfFinalPrice()+"€");
        //paragraphOneRunTwo.setTextPosition(100);
        paragraphOneRunTwo.addBreak();

        //Set Strike through and Font Size and Subscript
        XWPFRun paragraphOneRunThree = paragraph.createRun();
        paragraphOneRunThree.setFontSize(11);
        paragraphOneRunThree.setText(entry.getVfNumber());
        paragraphOneRunThree.addBreak();

        XWPFRun paragraphOneRunFour = paragraph.createRun();
        paragraphOneRunFour.setFontSize(36);
        paragraphOneRunFour.setText("");
        paragraphOneRunFour.addBreak();
        if(number%3==0){
            paragraphOneRunFour.addBreak(BreakType.PAGE);
        }



    }

}
