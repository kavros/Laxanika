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

    public  void put(String name,double profit,String num){
        vf_map.put(name,new VFHashMapValues(profit,num));
    }

    public  VFHashMapValues remove(String vf_name){
        return vf_map.remove(vf_name);
    }

    public int getSize(){

        return vf_map.size();
    }


    public boolean isNameValid(String name){
        return vf_map.containsKey(name);
    }

    public void updateModelWithHash(MyModel model){
        for (Map.Entry<String, VFHashMapValues> e : vf_map.entrySet()) {
            model.addRow( new Object[]{e.getKey(),e.getValue().getProfit(), e.getValue().getKef5Code()});
        }
    }

    public void calculateFinalPrice(Vector<VFVectorEntry> vec, int i)  {

        double price = vec.get(i).vf_price;
        double tax   = vec.get(i).vf_tax*0.01;
        double profit = vf_map.get(vec.get(i).vf_name).getProfit();


        double price_with_taxes = ((price*tax)+price);
        vec.get(i).vf_final_price =(price_with_taxes*profit)+price_with_taxes;

        //round  number using 2 decimals
        vec.get(i).vf_final_price = Math.round((vec.get(i).vf_final_price) *100);
        vec.get(i).vf_final_price = vec.get(i).vf_final_price /100;
        double actual_profit      = Math.round((vec.get(i).vf_final_price-price_with_taxes)*100);
        vec.get(i).actual_profit  = actual_profit/100;
    }
    /*
    * TODO:test this method
    * */
    public void updateKef5Codes(Vector<VFVectorEntry> vec,int i) {


        String vf_name       = vec.get(i).vf_name;
        vec.get(i).kef5_code =vf_map.get(vf_name).getKef5Code();

    }
    public VFHashMapValues get(String name){
        return vf_map.get(name);
    }
    @Override
    public String toString() {
        return vf_map.toString();
    }
}
