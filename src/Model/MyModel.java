package Model;

import javax.swing.table.DefaultTableModel;

/**
 * @author ashraf
 *
 */
@SuppressWarnings("serial")
public class MyModel extends DefaultTableModel {

	VFHashMap vf_rates;
	VFVector data;


	public MyModel() {
		vf_rates=new VFHashMap();
		data                     = new VFVector();
	}

	public VFVector getVector(){
		return  data;
	}

	public VFHashMap getVFHashMap() {
		return vf_rates;
	}

	public Object[] getRow(int i){
		double profit;// = vf_rates.getHashMapValues(data.getVec().get(i).vf_name).profit;
		if(vf_rates.getHashMapValues(data.getVec().get(i).vf_name) == null){
			String[] a = (data.getVec().get(i).vf_name).split(" ",2);
			profit = vf_rates.getHashMapValues(a[0]).profit;
		}else{
			profit = vf_rates.getHashMapValues(data.getVec().get(i).vf_name).profit;
		}
		profit = profit *100;

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
}
