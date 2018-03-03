package Model;

import javax.swing.*;
import java.io.*;
import java.util.Vector;

public class PriceHistory {
    public class Prices{
        private double price1;
        private double price2;
        private double price3;
        private int totalPrices;

        public int getTotalPrices(){
            return totalPrices;
        }

        public double getPrice1(){
            return  price1;
        }

        public double getPrice2(){
            return  price2;
        }


        public double getPrice3(){
            return  price3;
        }

        public Prices(){
            totalPrices=0;
        }
    }

    private final String filePath = "history/history.txt";
    private Vector<String> history = null;

    public PriceHistory(){
        history = new Vector<>();
    }

    private void readFromFile(){
        MessageDialog msg = new MessageDialog();
        BufferedReader br = null;
        FileReader fr = null;

        try {
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                history.add(sCurrentLine);
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }

    }

    public Vector<String> getVector(){
        return  history;
    }

    //add the price to the vector.
    //before the call of this function the vector must be initialized using data from file.
    //if there is an entry with the same kef5Code then we keep last 3 prices.
    public void addPrice(String kef5Code,double price){
        if(history.isEmpty()){
            readFromFile(); //init vector using data from file.
        }

        boolean hasEntry = false;
        if(history.isEmpty()){
            history.add(kef5Code+"-"+price);
        }else{
            for(int i =0; i < history.size(); i++){
                String item = history.elementAt(i);
                if(item.contains(kef5Code)){
                    String[] array = item.split("-");
                    if(array.length == 4){
                        String str = array[0]+"-"
                                +array[2]+"-"+array[3]+"-"+String.valueOf(price);
                        history.setElementAt(str,i);
                    }else if(array.length < 4){
                        history.setElementAt(item+"-"+price,i);
                    }else{
                        assert (false);
                    }
                    hasEntry=true;
                    break;
                }
            }
            if(!hasEntry){
                history.add(kef5Code+"-"+price);
            }
        }
    }

    // if there are not values returns null
    //else returns the last 3 prices
    public  Prices getPrices(String kef5Code){
        MessageDialog msg = new MessageDialog();
        if(history == null){
            assert(false);
        }
        if(history.isEmpty()){
            readFromFile();
        }
        Prices prices = new Prices();
        for(int i=0; i < history.size();i++){
            String entry = history.get(i);
            if(entry.contains(kef5Code)){
                String values[] = entry.split("-");
                if(values.length == 2){
                    prices.price1 = Double.valueOf(values[1]);
                    prices.totalPrices =1;
                }if(values.length == 3){
                    prices.price1 = Double.valueOf(values[1]);
                    prices.price2 = Double.valueOf(values[2]);
                    prices.totalPrices =2;
                }if(values.length == 4){
                    prices.price1 = Double.valueOf(values[1]);
                    prices.price2 = Double.valueOf(values[2]);
                    prices.price3 = Double.valueOf(values[3]);
                    prices.totalPrices =3;
                }

            }
        }
        return  prices;

    }

    //erase file and write vector data to fille.
    public void writeToFile(){
        File f = new File(filePath);
        MessageDialog msg = new MessageDialog();

        if(history == null || history.isEmpty()){
            msg.showMessageDialog("Δεν υπαρχουν δεδομένα για εγγραφή στο ιστορικό.",
                                    "Αποτυχία εγγραφής ιστορικού",
                                    JOptionPane.INFORMATION_MESSAGE
            );
        }
        PrintWriter out = null;
        try {
            if (f.exists() && !f.isDirectory()) {
                out = new PrintWriter(new FileOutputStream(new File(filePath), false));
            } else {

                out = new PrintWriter(filePath);
            }


            for(String item:history){
                out.println(item);
            }

            out.close();
        }catch (Exception e) {
            e.printStackTrace();

            msg.showMessageDialog("Απέτυχε η ενημερώση του ιστορικού",
                    "Αποτυχία εγγραφής ιστορικού",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

}
