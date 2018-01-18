package Model;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Vector;


public class VFVector {
    Vector<VFVectorEntry> vec;

    public VFVector(){
        vec = new Vector<VFVectorEntry>();
    }
    public int getSize(){
        return  vec.size();
    }



    public Vector<VFVectorEntry> getVec() {
        return vec;
    }

    void add(VFVectorEntry entry){
        //ignore null names
        if(entry.vf_name==null) return;
        vec.add(entry);
    }

    public String toString(){
        return vec.toString();
    }

    /**
     * TODO:update kefalaio 5 codes
     * updates: kef5_code,vf_finalPrices,vf_name ,vf_origin,vf_kef5prices.
     */
    public void update(VFHashMap vf_rates ){

        //remove TELARA,ΚΕΝΑ from vec
        for(int i=0; i < vec.size(); ++i){
            if(vec.get(i).vf_name.contains("ΤΕΛΑΡΑ")){
                //System.out.println(vec.get(i).vf_name);
                vec.remove(i);
            }else if(vec.get(i).vf_name.contains("ΚΕΝΑ")){
                vec.remove(i);
            }
        }

        for(int i = 0 ;  i < vec.size() ; ++i){
            String str[] = vec.get(i).vf_name.split("-",2);


            vec.get(i).vf_name      = str[0];
            vec.get(i).vf_origin    = str[1];
            if(vf_rates.isNameValid(vec.get(i).vf_name) == true){

                try {
                    vf_rates.calculateFinalPrice(vec,i);
                    vf_rates.updateKef5Codes(vec,i);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //System.out.println(vec.get(i).vf_name +" "+vec.get(i).vf_final_price+" "+vec.get(i).kef5_code);
            }


        }

        //System.out.print(vec);

    }

    public  boolean updateVectorValueUpdateNeeded(String name, Boolean new_bool_val){
        boolean isUpdateDone = false;
        for(int i =0;i<vec.size(); ++i){
            if(vec.get(i).vf_name == name){
                vec.get(i).isUpdateNeeded = new_bool_val;
                isUpdateDone = true;
                //System.out.println(vec.get(i).vf_name+" "+vec.get(i).isUpdateNeeded);
            }
            if(isUpdateDone == true){
                break;
            }
        }
        return isUpdateDone;
    }

}
