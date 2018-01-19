package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;



import Model.*;

/**
 * @author ashraf
 *
 */
public class Controller implements ActionListener,TableModelListener {

	int  BOOLEAN_COLUMN  = 5;
	private JTextField searchTermTextField;// = new JTextField(26);
	private JMenuItem openMi;
	private JMenuItem editListMi;
	private MyModel model;

	JButton _filterButton;
	JButton _updateButton;
	JTable  _table ;


    String   fileName = null;

	public Controller(JTextField searchTermTextField,
					  JMenuItem openM,
					  JMenuItem editListMi,
					  MyModel model,
                      JButton filterButton,
                      JButton updateButton,
                      JTable table) {
		super();
		this.searchTermTextField = searchTermTextField;
		openMi                   = openM;
		this.model 				 = model;
		_table					 = table;
		_updateButton            = updateButton;
		_filterButton			 = filterButton;
		this.editListMi 		 = editListMi;
		JMenuActionListener();
	}


	public void setComponentsVisible(){

	    //make necessary components visible
		searchTermTextField.setVisible(true);
		_updateButton.setVisible(true);
		_filterButton.setVisible(true);

        //if table is not empty,remove all rows of table.
        //and recreate vector.
        if(model.getRowCount()>0){
            model.reCreateVector();
            model.setRowCount(0);
        }

        //update vector with excel data.
        ExcelReader reader = new ExcelReader();
        reader.readExcelFile(fileName, model.getVector());

        //if product is not in hash Map show error
        model.getVector().transformVector();
        ArrayList<String> unknownProductsList = model.getUnknownNames();
        if(unknownProductsList.isEmpty() == false){
            //System.out.println(fileName);
            //System.out.println(unknownProductsList);
            JOptionPane.showMessageDialog(null,
                    "Παρακαλώ καταχώρησε στην λίστα τα παρακάτω: "+""+unknownProductsList.toString(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            model.reCreateVector();
            model.setRowCount(0);
            return;
        }

        //updates every entry on vector
        //with the right value for kef5code and final price.
        model.getVector().update(model.getVFHashMap());


        //fix columns names
        model.setColumnIdentifiers(Constants.VF_TABLE_HEADER);

        //add rows to the model
		for(int i = 0; i < model.getVector().getSize(); ++i){
		    model.addRow(model.getVectorRow(i));
        }
	}


	private void JMenuActionListener(){
        openMi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    fileName = selectedFile.getAbsolutePath();
                    setComponentsVisible();
                }
            }});

        editListMi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

				model.setRowCount(0);

                //make necessary components invisible
                searchTermTextField.setVisible(false);
                _updateButton.setVisible(false);
                _filterButton.setVisible(false);

                model.setColumnIdentifiers(Constants.PRODUCTS_TABLE_HEADER);
				model.updateModelWithHash();
            }
        });
    }

	@Override
	public void actionPerformed(ActionEvent e) {

        String searchTerm = searchTermTextField.getText();
		if (searchTerm != null && !"".equals(searchTerm)) {
			Object[][] newData = new Object[model.getVector().getSize()][];
			int idx = 0;
			for(int i = 0; i < model.getVector().getSize(); ++i){
			    Object[] o= model.getVectorRow(i);
			    if("*".equals(searchTerm.trim())) {
                    newData[idx++] = o;
                }else{

                    if(String.valueOf( o[0]).startsWith(searchTerm.toUpperCase().trim())){
                        newData[idx++] = o;
                    }
                }
            }
			model.setDataVector(newData, Constants.VF_TABLE_HEADER);
		} else {
			JOptionPane.showMessageDialog(null,
					"Search term is empty", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}


	public void tableChanged(TableModelEvent e) {
		int row = e.getFirstRow();
		int column = e.getColumn();
		if (column == BOOLEAN_COLUMN) {
			//TableModel model    = (TableModel) e.getSource();
			String columnName   = model.getColumnName(column);
			String vf_name      = (String) model.getValueAt(row,0);
			Boolean checked     = (Boolean) model.getValueAt(row, column);

			boolean isUpdateDone = model.getVector().updateVectorValueUpdateNeeded(vf_name,checked);
			if(isUpdateDone == false){
			    System.err.println("Error: Vegetable or fruit name does not in vector can not updated !");
            }
			if (checked) {
				System.out.println(columnName + ": "+vf_name+","+ true);
			} else {
				System.out.println(columnName + ": "+vf_name+"," + false);
			}
		}
	}
}


