package Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class VFHashMap {

    private HashMap<String,VFHashMapValues> vf_map ;
    public VFHashMap(){

        XMLReader xmlProducts = new XMLReader();
        vf_map = xmlProducts.getProducts();

    }

    public int getSize(){

        return vf_map.size();
    }


    public boolean isNameValid(String name){
        if(vf_map.containsKey(name)){
            return  true;
        }

        return false;
    }

    public void updateModelWithHash(MyModel model){
        for (Map.Entry<String, VFHashMapValues> e : vf_map.entrySet()) {
            model.addRow( new Object[]{e.getKey(),e.getValue().profit, e.getValue().kef5Code});
        }
    }

    public void calculateFinalPrice(Vector<VFVectorEntry> vec, int i) throws Exception {

        double price = vec.get(i).vf_price;
        double tax   = vec.get(i).vf_tax*0.01;
        double profit= -1;

        profit = vf_map.get(vec.get(i).vf_name).profit;


        double price_with_taxes = ((price*tax)+price);
        vec.get(i).vf_final_price =(price_with_taxes*profit)+price_with_taxes;

        //round  number using 2 decimals
        vec.get(i).vf_final_price =Math.round((vec.get(i).vf_final_price) *100);
        vec.get(i).vf_final_price =vec.get(i).vf_final_price /100;
    }
    /*
    * TODO:test this method
    * */
    public void updateKef5Codes(Vector<VFVectorEntry> vec,int i) throws Exception {


        String vf_name       = vec.get(i).vf_name;
        String kef5_code      =vf_map.get(vf_name).kef5Code;

        vec.get(i).kef5_code =kef5_code;
    }
    public VFHashMapValues get(String name){
        return vf_map.get(name);
    }
    @Override
    public String toString() {
        return vf_map.toString();
    }
}
