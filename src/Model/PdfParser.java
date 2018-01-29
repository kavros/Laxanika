package Model;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;

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

     void addProductToVector(String productLine,VFVector vector) throws  Exception{

         //replace strange characters
         productLine=productLine.replace('∆','Δ');
         productLine=productLine.replace('Ω','Ω');

         //ignore ΤΕΛΑΡΑ and ΚΕΝΑ
        if(productLine.contains("ΤΕΛΑΡΑ")
           || productLine.contains("ΚΕΝΑ")){
            return;
        }

        VFVectorEntry laxaniko = new VFVectorEntry();

         //line[0]  -> proion,proeleusi,noumero
         //line[1]  -> ypoloipa dedomena gramis xwris Monada Metrisis
        String[] line=null;
        if(productLine.contains("ΚΙΛ")){
            line = productLine.split("ΚΙΛ");
        }else if(productLine.contains("ΤΕΜ")){
            line =  productLine.split("ΤΕΜ");
        }else{
            throw new Exception("Error: function addProductToVector on PdfParser failed");
        }


        String[] subLine2 = line[0].split(" ");
        String[] subLine3 = line[1].trim().split(" ");


        laxaniko.vf_number = subLine2[subLine2.length-1];

        laxaniko.vf_name    = line[0].split("-")[0];
        laxaniko.vf_origin  = line[0].split("-")[1];

        laxaniko.vf_mm      = "ΚΙΛ";
        laxaniko.vf_packing = subLine3[0].replace(",",".");
        laxaniko.vf_quantity= Double.parseDouble(subLine3[1].replace(",","."));
        laxaniko.vf_price   = Double.parseDouble(subLine3[2].replace(",","."));
        laxaniko.vf_value   = subLine3[3].replace(",",".");
        laxaniko.vf_discount= Double.parseDouble(subLine3[4].replace(",","."));
        laxaniko.vf_net_value=subLine3[5].replace(",",".");
        laxaniko.vf_tax      =Double.parseDouble(subLine3[6].replace(",","."));
         vector.add(laxaniko);
       /*System.out.println("name: "+laxaniko.vf_name );
        System.out.println("origin: "+laxaniko.vf_origin );
        System.out.println("number: "+laxaniko.vf_number );
        System.out.println("mm: "+laxaniko.vf_mm );
        System.out.println("packing: "+laxaniko.vf_packing );
        System.out.println("quantity: "+laxaniko.vf_quantity );
        System.out.println("price: "+laxaniko.vf_price );
        System.out.println("value: "+laxaniko.vf_value );
        System.out.println("discount: "+laxaniko.vf_discount );
        System.out.println("net value: "+laxaniko.vf_net_value );
        System.out.println("tax: "+laxaniko.vf_tax );*/
    }

}