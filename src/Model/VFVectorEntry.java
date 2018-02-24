package Model;

import java.sql.SQLException;

public class VFVectorEntry {
    String vf_name ;
    String vf_origin;
    String vf_number;
    String vf_mm;
    String vf_packing;
    double vf_quantity;
    String vf_value;
    double vf_price;
    double vf_discount;
    String vf_net_value;
    double vf_tax;
    double vf_final_price;

    boolean isUpdateNeeded;
    String  kef5_code;
    double  kef5_price;

    double actual_profit;

    public String toString(){

        return vf_name+" "+vf_origin+" "+vf_mm+" "+" "+vf_number+" "+vf_packing+" "+vf_quantity+" "+vf_price
                +" "+vf_value+" "+vf_discount+" "+vf_net_value+" "+vf_tax+" "+vf_final_price+" "
                +isUpdateNeeded+" "+kef5_code+" "+kef5_price+"\n";
    }

    public boolean getIsUpdateNeeded(){
        return isUpdateNeeded;
    }

    public double getKef5_price(){return  kef5_price;}

    public String getKef5_code() {
        return kef5_code;
    }

    public String getVf_origin() {
        return vf_origin;
    }

    public String getVf_mm() {
        return vf_mm;
    }

    public String getVf_number() {
        return vf_number;
    }

    public String getVfName(){
        return vf_name;
    }

    public void updateActualProfit(double actProf){
        //TODO round up final price
        double price_with_taxes = ((vf_price*vf_tax*0.01)+vf_price);

        vf_final_price          = Math.round((price_with_taxes+actProf)*100);
        vf_final_price          = vf_final_price/100;
        actual_profit           = actProf;

    }

    public double getActualProfit(){
        return actual_profit;
    }

    public double getVfFinalPrice(){
        return vf_final_price;
    }

    public String getTracerEntry() {
        VFKef5DataBase dataBase = new VFKef5DataBase();
        String name             = null;

                String query = "select sName  from smast where sCode='"+kef5_code+"';";
        try {
            name  =  dataBase.getFromDatabase(query);
        }catch (SQLException e){
            System.out.println("ERROR:Sql failed to retrieve name for tracer");
        }

        return "Oνομα προϊόντος στο Kefalaio 5: "+name+"\n"
                +"Oνομα προϊόντος στο ΛΑΧΑΝΙΚΑ-ΦΡΟΥΤΑ: "+vf_name+"\n"
                +"Κωδικός προϊόντος: "+kef5_code+"\n"
                +"Παλιά τιμή στο Kefalaio 5: "+kef5_price+"\n"
                +"Νέα τιμή στο Kefalaio 5: "+vf_final_price;

    }
}
