package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class Tracer {

    public void run(MyModel model) throws Exception {
        int i;
        String fileData="";
        for(i=0; i<model.getVector().getSize();++i){
            VFVectorEntry entry = model.getVector().getVec().elementAt(i);
            if(entry.getIsUpdateNeeded()){
                //System.out.println(entry.getTracerEntry());
                fileData += entry.getTracerEntry()+"\n\n\n";

            }
        }

        saveData(fileData);


    }


    private void saveData(String data) throws  Exception{

        String month = LocalDateTime.now().getMonth().toString();
        String year  = Integer.toString(LocalDateTime.now().getYear());
        String day  = Integer.toString(LocalDateTime.now().getDayOfMonth());
        String fileName = "results\\"+day+"_"+month+"_"+year+".txt";


        File f = new File(fileName);

        PrintWriter out = null;
        if (f.exists() && !f.isDirectory()) {
            out = new PrintWriter(new FileOutputStream(new File(fileName), true));
        } else {

            out = new PrintWriter(fileName);
        }
        out.append(data);
        out.close();

    }


}
