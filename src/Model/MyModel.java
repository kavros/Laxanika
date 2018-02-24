package Model;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class MyModel extends DefaultTableModel {

	private VFHashMap vf_rates;
	private VFVector data;

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
	    ArrayList<String> notValidNames=new ArrayList<>();
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
		VFHashMapValues a = null;
		try {
			a = vf_rates.get(data.getVec().get(i).vf_name);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assert a != null;
		profit = a.getProfit();

		profit = profit *100;

		//
		return  new Object[]{
				data.getVec().get(i).vf_name,
				String.valueOf(profit)+"%",
				data.getVec().get(i).vf_price,
				data.getVec().get(i).kef5_price,
				data.getVec().get(i).vf_final_price,
				data.getVec().get(i).actual_profit,
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

	public  ArrayList<String > updateKef5Prices() throws SQLException{
		return data.updateKef5Prices();
	}
}
