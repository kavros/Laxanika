package Controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;


import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import Model.*;


/**
 * @author ashraf
 *
 */
public class Controller implements ActionListener,TableModelListener {

    private JTextField searchTermTextField;// = new JTextField(26);
	private JMenuItem openMi;
	private JMenuItem editListMi;
	private MyModel model;

	private JButton _filterButton;
	private JButton _updateButton;
	private JTable  _table ;

	private JTextField _vfNameField ;
	private JTextField _profitField ;
	private JTextField _kef5CodeField ;
	private Object[] _inputFields ;
	private JButton  _addXmlButton;
	private JButton  _deleteXmlButton;
	private JButton  _editXmlButton;

	private String   fileName = null;

	public Controller(JTextField searchTermTextField,
					  JMenuItem openM,
					  JMenuItem editListMi,
					  MyModel model,
                      JButton filterButton,
                      JButton updateButton,
					  JTextField vfNameField,
					  JTextField profitField,
					  JTextField kef5CodeField,
					  Object[] inputFields,
					  JButton  addXmlButton,
					  JButton  deleteXmlButton,
					  JButton  editXmlButton,
                      JTable table) {
		super();
		this.searchTermTextField = searchTermTextField;
		openMi                   = openM;
		this.model 				 = model;
		_table					 = table;
		_updateButton            = updateButton;
		_filterButton			 = filterButton;
		this.editListMi 		 = editListMi;

		_vfNameField 		     = vfNameField;
		_profitField             = profitField;
		_kef5CodeField           = kef5CodeField;
		_inputFields             = inputFields;

		_addXmlButton            = addXmlButton;
		_deleteXmlButton         = deleteXmlButton;
		_editXmlButton           = editXmlButton;

		//init actions listeners
		JMenuActionListener();
		updateButtonListener();
		addXmlButtonListener();
		deleteXmlButtonListener();
		editXmlButtonListener();
	}

	private void showMessageDialog(String msg,String title,int type){
		JOptionPane.showMessageDialog(null,msg,title,type);
	}

	private void setComponentsVisible(){


        //if table is not empty,remove all rows of table.
        //and recreate vector.
        if(model.getRowCount()>0){
            model.reCreateVector();
            model.setRowCount(0);
        }

        //update vector with pdf data.
		PdfParser reader = new PdfParser();
        try {
			reader.parsePdfFile(fileName, model.getVector());
		}catch (Exception e){
        	System.out.println(Arrays.toString(e.getStackTrace()));
		}


        //if product is not in hash Map show error
        model.getVector().transformVector();
        ArrayList<String> unknownProductsList = model.getUnknownNames();
        if( ! unknownProductsList.isEmpty() ){

			showMessageDialog(
					"Παρακαλώ καταχώρησε στην λίστα τα παρακάτω: "
							+""+unknownProductsList.toString(), "Error",
                    JOptionPane.ERROR_MESSAGE
			);

            model.reCreateVector();
            model.setRowCount(0);
            editListMi.doClick();
            return;
        }

        //make necessary components visible
        searchTermTextField.setVisible(true);
        _updateButton.setVisible(true);
        _filterButton.setVisible(true);

        _addXmlButton.setVisible(false);
        _deleteXmlButton.setVisible(false);
        _editXmlButton.setVisible(false);

        //updates every entry on vector
        //with the right value for kef5code and final price.
		try {
			model.getVector().update(model.getVFHashMap());
		}catch (SQLException e){
			showMessageDialog("Απέτυχε η εύρεση των τιμών απο το κεφάλαιο λογω προβλήματος με την SQL.",
                    "Αποτυχία SQL",JOptionPane.ERROR_MESSAGE);
		}



        //fix columns names
        model.setColumnIdentifiers(Constants.VF_TABLE_HEADER);

        //add rows to the model
		for(int i = 0; i < model.getVector().getSize(); ++i){
		    model.addRow(model.getVectorRow(i));
        }
	}

	private void JMenuActionListener(){
        openMi.addActionListener((ActionEvent e) -> {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setPreferredSize(new Dimension(600,700));
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                fileName = selectedFile.getAbsolutePath();
                setComponentsVisible();
            }
        });

        editListMi.addActionListener(e -> {

            model.setRowCount(0);

            //make necessary components invisible
            searchTermTextField.setVisible(false);
            _updateButton.setVisible(false);
            _filterButton.setVisible(false);

            model.setColumnIdentifiers(Constants.PRODUCTS_TABLE_HEADER);
            model.updateModelWithHash();

            _addXmlButton.setVisible(true);
            _editXmlButton.setVisible(true);
            _deleteXmlButton.setVisible(true);
        });

    }

    private void updateButtonListener(){
		_updateButton.addActionListener(e -> {
            ArrayList<String> products ;

            try{
                products = model.updateKef5Prices();
				showMessageDialog(
						"Ενημερώθηκαν επιτυχώς οι τιμές στο Κεφάλαιο 5 για τα παρακάτω: "
							+""+products.toString(), "Ενημέρωση Τιμών στο Κεφάλαιο 5",
							JOptionPane.INFORMATION_MESSAGE
				);
				Tracer tracer = new Tracer();
				try{
					tracer.run(model);
				}catch(Exception ex){
					showMessageDialog("Απέτυχε η ενημέρωση του txt αρχείου.","Αποτυχία ενημέρωσης txt",JOptionPane.ERROR_MESSAGE);
				}


            }catch (SQLException a){
                showMessageDialog("Απέτυχε η ενημέρωση τιμών λόγω πρόβληματος με την SQL.","Αποτυχία SQL",JOptionPane.ERROR_MESSAGE);

            }

        });
	}

	private void deleteXmlButtonListener(){
		_deleteXmlButton.addActionListener(e -> {
            if(_table.getSelectedRow() != -1 ){

                XMLModifier xml = new XMLModifier();

                String selected_vf_name =(String) model.getValueAt(_table.getSelectedRow(),0);
                VFHashMapValues val = model.getVFHashMap().remove(selected_vf_name);
                model.removeRow(_table.getSelectedRow());
                xml.deleteXMLNode(selected_vf_name);

                showMessageDialog(
                        "Το προιόν : "+selected_vf_name+" "+val.toString()+" διαγράφηκε επιτυχώς\n",
                        "Επιτυχής Διαγραφή",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }else{
                showMessageDialog(
                        "Παρακαλώ επιλέξετε την γραμμή που θέλετε να διαγράψετε!\n",
                        "Αποτυχής Διαγραφή",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        });
	}

	private void editXmlButtonListener(){
		_editXmlButton.addActionListener(e -> _editXmlButton.setVisible(false));
	}

	private void addXmlButtonListener(){
		_addXmlButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, _inputFields,
                    "Εισαγωγή Προϊόντος", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {

                XMLModifier xml= new XMLModifier();
                //adds info to products.xml
                boolean isSuccessfullyAdded = xml.addXMLNode( _vfNameField.getText(),
                                _profitField.getText(),
                                _kef5CodeField.getText()
                                );
                //adds info to hash table

                String a      = _profitField.getText();
                a= a.replace(",",".");
                double profit = Double.parseDouble(a);

                model.getVFHashMap().put(_vfNameField.getText(),profit,_kef5CodeField.getText());

                if(isSuccessfullyAdded ){
                    showMessageDialog(
                            "Η καταχώρηση του προιόντος έγινε επιτυχώς", "Επιτυχής Καταχώρηση",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }else{
                    showMessageDialog(
                            "Η καταχώρηση του προιόντος απέτυχε.\n" +
                                    "Τα πεδία κωδικός και κέρδος πρέπει να είναι αριθμός.\n",
                            "Αποτυχής Καταχώρηση",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
            //update gui table
            model.setRowCount(0);
            model.setColumnIdentifiers(Constants.PRODUCTS_TABLE_HEADER);
            model.updateModelWithHash();


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
			showMessageDialog(
					"Search term is empty", "Error",
					JOptionPane.ERROR_MESSAGE
			);
		}

	}

	public void tableChanged(TableModelEvent e) {
		int row = e.getFirstRow();
		int column = e.getColumn();
        int BOOLEAN_COLUMN = 5;
        if (column == BOOLEAN_COLUMN) {
			//TableModel model    = (TableModel) e.getSource();
			String vf_name      = (String) model.getValueAt(row,0);
			Boolean checked     = (Boolean) model.getValueAt(row, column);

			boolean isUpdateDone = model.getVector().updateVectorValueUpdateNeeded(vf_name,checked);
			if(!isUpdateDone ){
			    System.err.println("Error: Vegetable or fruit name does not in vector can not updated !");
            }

		}
	}


}


