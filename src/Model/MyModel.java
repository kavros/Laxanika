package Model;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class MyModel extends DefaultTableModel {

	private VFHashMap vf_rates;
	private VFVector data;
	PriceHistory _priceHistory ;

	public MyModel() {
		_priceHistory=new PriceHistory();
		vf_rates = new VFHashMap();
		data = new VFVector();
	}


	public VFVector getVector(){
		return data;
	}

	public PriceHistory getPriceHistory(){
		return _priceHistory;
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

	public void updateModelWithHistory(){
		VFKef5DataBase dataBase = new VFKef5DataBase();
		Vector<String> historyVector = _priceHistory.getVector();
		if(historyVector == null || historyVector.isEmpty()){
			return;
		}
		int totalPrices;
		setColumnIdentifiers(Constants.HISTORY_TABLE_HEADER);
		for(int i =0; i < historyVector.size(); i++){
			try{
				String kef5Code =historyVector.get(i).split("-")[0];
				PriceHistory.Prices prices =_priceHistory.getPrices(kef5Code);
				totalPrices =prices.getTotalPrices();
				String productName = dataBase.getFromDatabase("select sName  from dbo.smast where sCode="+"'"+kef5Code+"'");

				if( totalPrices == 1){
					addRow( new Object[]{productName, prices.getPrice1(),"-","-"});
				}else if( totalPrices == 2){
					addRow( new Object[]{productName, prices.getPrice1(),prices.getPrice2(),"-"});
				}else if( totalPrices == 3){
					addRow( new Object[]{productName, prices.getPrice1(),prices.getPrice2(),prices.getPrice3()});
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}


	}

	public void reCreateVector(){
		data = null;
		data = new VFVector();

	}

	public  ArrayList<String > updateKef5Prices() throws SQLException{
		for ( int i = 0; i < data.getVec().size(); ++i) {
			VFVectorEntry entry =data.getVec().get(i);
			if(entry.isUpdateNeeded == true){

				entry.kef5_price = entry.vf_final_price;
				setValueAt(entry.getVfFinalPrice(),i,3);
			}
		}
		return data.updateKef5Prices();
	}
}
