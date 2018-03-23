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

        @Override
        public String toString() {
            if(totalPrices ==1){
                return  String.valueOf(price1)+"€";
            }else if(totalPrices== 2){
                return  String.valueOf(price1) +"€,"
                        + String.valueOf(price2)+"€";

            }else if(totalPrices == 3){
                return  String.valueOf(price1) +"€,"
                        +String.valueOf(price2)+"€,"
                        +String.valueOf(price3)+"€";

            }
            return null;
        }
    }

    private Vector<String> history = null;
    private String date;
    private String dates;

    public PriceHistory(){
        history = new Vector<>();
        dates = null;
        date = null;
        readFromFile();
    }

    public Vector<String> getVector(){
        return  history;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){
        return date;
    }

    public String getDates() {return dates;}

    /**
     * @return true if the given date on invoice is not in history or
     * false if the given date is already in history or if it is null.
     */
    public boolean isDateInHistory() {
        if(date == null || dates == null){
            return false;
        }

        if(dates.contains(date.trim())){
            return true;
        }

        return  false;
    }

    //add the price to the vector.
    //before the call of this function the vector must be initialized using data from file.
    //if there is an entry with the same kef5Code then we keep last 3 prices.
    public void addPrice(String kef5Code,double price) {

        if(history.isEmpty()){
            readFromFile(); //init vector using data from file.
        }

        //TODO : check if we have a valid date.
        if(isDateInHistory()){
            return;
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

    public void readFromFile(){
        MessageDialog msg = new MessageDialog();
        BufferedReader br = null;
        FileReader fr = null;

        try {
            fr = new FileReader(Constants.getHistoryFilePath());
            br = new BufferedReader(fr);

            String sCurrentLine;
            int count = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                //read the first line that contains the dates.
                if(count == 0){
                    dates = sCurrentLine;

                    count++;
                    continue;
                }
                //skip empty line after dates.
                if(sCurrentLine.isEmpty()){
                    continue;
                }

                //adds historical entries to the vector.
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

    //erase file and write vector data to file.
    public void writeToFile(){
        File f = new File(Constants.getHistoryFilePath());
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
                out = new PrintWriter(new FileOutputStream(new File(Constants.getHistoryFilePath()), false));
            } else {

                out = new PrintWriter(Constants.getHistoryFilePath());
            }

            //update dates and write them to file.
            if(dates == null || dates.isEmpty()){
                dates = date;
            }else if(!dates.contains(date.trim())){
                dates +=","+date;
            }
            out.println(dates);

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
