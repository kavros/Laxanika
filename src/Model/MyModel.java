package Model;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Vector;

/**
 * @author ashraf
 *
 */
@SuppressWarnings("serial")
public class MyModel extends DefaultTableModel {

	VFHashMap vf_rates;
	VFVector data;


	public MyModel() {
		vf_rates = new VFHashMap();
		data = new VFVector();
	}


	public VFVector getVector(){
		return data;
	}

	/*
	* Returns all products from vector that can not match with hashMap
	* */
	public ArrayList<String> getUnknownNames(){
	    ArrayList<String> notValidNames=new ArrayList<String>();
        Vector<VFVectorEntry> vec = data.getVec();
        for(int i=0; i < data.getSize(); ++i){
            boolean isNameValid =vf_rates.isNameValid(vec.get(i).vf_name);
            if(!isNameValid){
                notValidNames.add(vec.get(i).vf_name);
            }
        }
        return  notValidNames;

    }

	public VFHashMap getVFHashMap() {
		return vf_rates;
	}

	public Object[] getVectorRow(int i){

		//find profit value from hashmap
		double profit;
		if(vf_rates.getHashMapValues(data.getVec().get(i).vf_name) == null){
			String[] a = (data.getVec().get(i).vf_name).split(" ",2);
			profit = vf_rates.getHashMapValues(a[0]).profit;
		}else{
			profit = vf_rates.getHashMapValues(data.getVec().get(i).vf_name).profit;
		}
		profit = profit *100;

		//
		return  new Object[]{
				data.getVec().get(i).vf_name,
				String.valueOf(profit)+"%",
				data.getVec().get(i).vf_price,
				data.getVec().get(i).kef5_price,
				data.getVec().get(i).vf_final_price,
				false
		};

	}

	public void updateModelWithHash(){
		vf_rates.updateModelWithHash(this);
	}

	public void reCreateVector(){
		data = null;
		data = new VFVector();

	}

	public  ArrayList<String > updateKef5Prices(){
		return data.updateKef5Prices();
	}
}
