package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import Model.VFHashMapValues;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;



import Model.*;

import static com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolver.iterator;

/**
 * @author ashraf
 *
 */
public class Controller implements ActionListener,TableModelListener {

	int  BOOLEAN_COLUMN  = 5;
	private JTextField searchTermTextField;// = new JTextField(26);
	private JMenuItem openMi;
	JMenuItem editListMi;
	private MyModel model;

	JButton _filterButton;
	JButton _updateButton;
	JTable _table ;

    VFVector data;
    String fileName = null;

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
        data                     = model.getVector();
		JMenuActionListener();
	}


	public void setComponentsVisible(){

		searchTermTextField.setVisible(true);
		_updateButton.setVisible(true);
		_filterButton.setVisible(true);
        ReadExcel reader = new ReadExcel();
        reader.readXLSFile(fileName, model.getVector());
        data.update(model.getVFHashMap());

        model.setColumnIdentifiers(Constants.VF_TABLE_HEADER);
		for(int i=0; i < data.getSize(); ++i){
		    model.addRow(model.getRow(i));
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
                    //if table is not empty,remove all rows of table.
                    if(model.getRowCount()>0){
                    	model.setRowCount(0);
                    }
                    if( (fileName!=null) && (fileName.equals(selectedFile.getAbsolutePath())) ){

                        model.setColumnIdentifiers(Constants.VF_TABLE_HEADER);
                        for(int i=0; i < data.getSize();++i){
                            model.addRow(model.getRow(i));
						}

                    }else {
                        fileName = selectedFile.getAbsolutePath();
                        setComponentsVisible();
                    }
                }
            }});
        editListMi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Object[][] newData= new Object[][]
				model.setRowCount(0);
                model.setColumnIdentifiers(Constants.PRODUCTS_TABLE_HEADER);
				model.updateModelWithHash();
            }
        });
    }

	@Override
	public void actionPerformed(ActionEvent e) {

        String searchTerm = searchTermTextField.getText();
		if (searchTerm != null && !"".equals(searchTerm)) {
			Object[][] newData = new Object[data.getSize()][];
			int idx = 0;
			for(int i=0; i < data.getSize();++i){
			    Object[] o= model.getRow(i);
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

			boolean isUpdateDone =data.updateVectorValueUpdateNeeded(vf_name,checked);
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


