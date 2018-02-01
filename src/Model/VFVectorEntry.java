package Model;

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
    String     kef5_code;
    double  kef5_price;

    public String toString(){

        return vf_name+" "+vf_origin+" "+vf_mm+" "+" "+vf_number+" "+vf_packing+" "+vf_quantity+" "+vf_price
                +" "+vf_value+" "+vf_discount+" "+vf_net_value+" "+vf_tax+" "+vf_final_price+" "
                +isUpdateNeeded+" "+kef5_code+" "+kef5_price+"\n";
    }




}
