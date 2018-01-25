package Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class VFHashMap {

    protected HashMap<String,VFHashMapValues> vf_map ;
    public VFHashMap(){

        XMLReader xmlProducts = new XMLReader();
        vf_map = xmlProducts.getProducts();

    }

    public int getSize(){

        return vf_map.size();
    }

    public VFHashMapValues getHashMapValues(String key){
        VFHashMapValues val = vf_map.get(key);

        return val;
    }

    public boolean isNameValid(String name){
        if(vf_map.containsKey(name)){
            return  true;
        }

        for (Map.Entry<String, VFHashMapValues> e : vf_map.entrySet()) {
            if (e.getKey().startsWith(name.split(" ")[0])) {
                return true;

            }
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


        if( ! vf_map.containsKey(vec.get(i).vf_name) ) {
            VFHashMapValues val;
             //patch for ΤΟΜΑΤΕΣ (ELPIDA),ΜΗΛΑ JONA,ΜΗΛΑ SMITH,TOMATINIA ΒΕΛΑΜΙΔΙΑ
            if ( (val=vf_map.get(vec.get(i).vf_name.split(" ")[0])) != null ) {
                profit = val.profit;
            }

        }else{
            profit = vf_map.get(vec.get(i).vf_name).profit;
        }

        if(profit == -1 ){
            throw new Exception("Error:profit is -1");
        }
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
        String kef5_code      = null;


        if( ! vf_map.containsKey(vec.get(i).vf_name) ) {
            //patch for ΤΟΜΑΤΕΣ (ELPIDA),ΜΗΛΑ JONA,ΜΗΛΑ SMITH,TOMATINIA ΒΕΛΑΜΙΔΙΑ
            VFHashMapValues val;
            if ( (val =vf_map.get(vec.get(i).vf_name.split(" ")[0])) != null ) {
                kef5_code =val.kef5Code;
            }

        }else{
            kef5_code = this.vf_map.get(vf_name).kef5Code;
        }

        if(kef5_code == null ){
            throw new Exception("Error:kef5Code is negaive,probably not found in hashmap");
        }

        vec.get(i).kef5_code =kef5_code;
    }



}
