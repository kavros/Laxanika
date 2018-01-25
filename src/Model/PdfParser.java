package Model;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class PdfParser {



     public void parsePdfFile(String fileName,VFVector vector) throws Exception{

         // /Loading an existing document
        File file = new File(fileName);
        PDDocument document = PDDocument.load(file);
        int numberOfRows    = document.getNumberOfPages();


        //Instantiate PDFTextStripper class
        PDFTextStripper pdfStripper = new PDFTextStripper();
        pdfStripper.setSortByPosition(true);

        //Retrieving text from PDF document
        String text = pdfStripper.getText(document);

        String[] pdfLines = text.split("\n");
        boolean isReading = false;

        for(int i=0; i < pdfLines.length;++i){
            String line = pdfLines[i];
            if(line.contains("ΣΧΕΤΙΚΑ ΠΑΡΑΣΤΑΤΙΚΑ")
              ||line.contains("Εκ Μεταφοράς")) {

                isReading = true;
                continue;

            }else if(
                      (line.contains("ΥΠΟΛΟΙΠΑ"))
                    ||(line.contains("Σε µεταφορά"))
                    ){
                isReading=false;
            }
            if(isReading == true){
                //System.out.println(line);
                addProductToVector(line,vector);
            }
        }

        //Closing the document
        document.close();

    }

     void addProductToVector(String productLine,VFVector vector){

        if(productLine.contains("ΤΕΛΑΡΑ")
           || productLine.contains("ΚΕΝΑ")){
            return;
        }

        VFVectorEntry laxaniko = new VFVectorEntry();
        StringBuffer  nameAndOrigin = new StringBuffer();
        String        lineAfterName = new String();

        for(int i=0; i < productLine.length();++i){
            char character = productLine.charAt(i) ;
            if( Character.isLetter (character)
               ||character == '-'
               || character == ' ' || character== '∆'){
                if(character == '∆'){
                    nameAndOrigin.append('Δ');
                }else if(character=='Ω'){
                    nameAndOrigin.append("Ω");
                }else{
                    nameAndOrigin.append(character);
                }

            }else if(Character.isDigit(character)){
                lineAfterName=productLine.substring(i);
                break;
            }

        }

        laxaniko.vf_name   = nameAndOrigin.toString().split("-")[0];
         ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(laxaniko.vf_name );

         laxaniko.vf_origin = nameAndOrigin.toString().split("-",2)[1];

        String[] lax = lineAfterName.split(" ");
        for(int i=0;i<lax.length; ++i){
            lax[i]=lax[i].replace(",",".");
        }
        laxaniko.vf_mm   = lax[0];
        laxaniko.vf_packing  = lax[1];
        laxaniko.vf_quantity = Double.parseDouble(lax[3]);
        laxaniko.vf_value    = lax[5];
        laxaniko.vf_price    = Double.parseDouble(lax[4]);
        laxaniko.vf_discount = Double.parseDouble(lax[6]);
        laxaniko.vf_net_value= lax[7];
        laxaniko.vf_tax      = Double.parseDouble(lax[8]);
        vector.add(laxaniko);
        //System.out.println(vector);
    }

}