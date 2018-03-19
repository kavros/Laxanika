package Model;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class MyModel extends DefaultTableModel {

	private VFHashMap _vf_rates;
	private VFVector _data;
	PriceHistory _priceHistory ;

	//search data
	private Object[][] _historyTable;
	private Object[][] _productsTable;
	private Object[][] _newPricesTable;


	public MyModel() {
		_priceHistory=new PriceHistory();
		_vf_rates = new VFHashMap();
		_data = new VFVector();
	}


	public VFVector getVector(){
		return _data;
	}

	public PriceHistory getPriceHistory(){
		return _priceHistory;
	}

	/*
	* Returns all products from vector that can not match with hashMap
	* */
	public ArrayList<String> getUnknownNames(){
	    ArrayList<String> notValidNames=new ArrayList<>();
        Vector<VFVectorEntry> vec = _data.getVec();
        for(int i = 0; i < _data.getSize(); ++i){
            boolean isNameValid = _vf_rates.isNameValid(vec.get(i).vf_name);
            if(!isNameValid){
                notValidNames.add(vec.get(i).vf_name);
            }
        }
        return  notValidNames;

    }

	public VFHashMap getVFHashMap() {
		return _vf_rates;
	}

	public Object[] getVectorRow(int i){

		//find profit value from hashMap
		double profit;
		VFHashMapValues a = null;
		try {
			a = _vf_rates.get(_data.getVec().get(i).vf_name);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assert a != null;
		profit = a.getProfit();

		profit = profit *100;

		//
		return  new Object[]{
				_data.getVec().get(i).vf_name,
				String.valueOf(profit)+"%",
				_data.getVec().get(i).vf_price,
				_data.getVec().get(i).kef5_price,
				_data.getVec().get(i).vf_final_price,
				_data.getVec().get(i).actual_profit,
				false
		};

	}

	public void updateModelWithHash(){
		_vf_rates.updateModelWithHash(this);
	}

	public void updateModelWithHistory(){
		_historyTable =new Object[_priceHistory.getVector().size()][];
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
				productName = greekChars(productName);
				Object[] entry;
				if( totalPrices == 1){
					entry = new Object[]{productName, prices.getPrice1(),"-","-"};
					addRow( entry);
					_historyTable[i] =entry;
				}else if( totalPrices == 2){
					entry = new Object[]{productName, prices.getPrice1(),prices.getPrice2(),"-"};
					addRow( entry);
					_historyTable[i] =entry;
				}else if( totalPrices == 3){
					entry =new Object[]{productName, prices.getPrice1(),prices.getPrice2(),prices.getPrice3()};
					addRow(entry);
					_historyTable[i] =entry;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	public Object[][] getHistoryTable(){
		return  _historyTable;
	}
	public void reCreateVector(){
		_data = null;
		_data = new VFVector();

	}

	public  ArrayList<String > updateKef5Prices() throws SQLException{
		for (int i = 0; i < _data.getVec().size(); ++i) {
			VFVectorEntry entry = _data.getVec().get(i);
			if(entry.isUpdateNeeded == true){

				entry.kef5_price = entry.vf_final_price;
				setValueAt(entry.getVfFinalPrice(),i,3);
			}
		}
		return _data.updateKef5Prices();
	}

	private String greekChars(String str) {

		StringBuilder str2 = new StringBuilder(str);

		for (int i = 0; i < str2.length(); i++) {
			switch (str2.charAt(i)) {
				case 'A':
					str2.setCharAt(i, 'Α');
					break;
				case 'B':
					str2.setCharAt(i, 'Β');
					break;
				case 'E':
					str2.setCharAt(i, 'Ε');
					break;
				case 'H':
					str2.setCharAt(i, 'Η');
					break;
				case 'I':
					str2.setCharAt(i, 'Ι');
					break;
				case 'K':
					str2.setCharAt(i, 'Κ');
					break;
				case 'M':
					str2.setCharAt(i, 'Μ');
					break;
				case 'N':
					str2.setCharAt(i, 'Ν');
					break;
				case 'O':
					str2.setCharAt(i, 'Ο');
					break;
				case 'P':
					str2.setCharAt(i, 'Ρ');
					break;
				case 'T':
					str2.setCharAt(i, 'Τ');
					break;
				case 'X':
					str2.setCharAt(i, 'Χ');
					break;
				case 'Y':
					str2.setCharAt(i, 'Υ');
					break;
				case 'Z':
					str2.setCharAt(i, 'Ζ');
					break;

			}

		}
		str = str2.toString();
		return str;
	}
}
