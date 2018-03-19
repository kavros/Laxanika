package Controller;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.HashMap;


import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import Model.*;
import Model.Printer;


/**
 * @author ashraf
 *
 */
public class Controller implements ActionListener,TableModelListener {
	enum CurrentMode{

		editListMode,
		priceMode,
		emptyWindow,
		historyMode
	}
	CurrentMode _viewMode;
	private JTextField _searchTermTextField;// = new JTextField(26);
	private JMenuItem openMi;
	private JMenuItem editListMi;
	private JMenuItem _exitMi;
	private MyModel model;

	private JButton _filterButton;
	private JButton _updateButton;
	private JTable _table;

	private JTextField _vfNameField;
	private JTextField _profitField;
	private JTextField _kef5CodeField;
	private Object[] _inputFields;
	private Object[] _editFields;
	private JTextField _desiredProfit;
	private JButton _addXmlButton;
	private JButton _deleteXmlButton;
	private JButton _printButton;
	JSplitPane _mainPane;
	private String fileName = null;
	PriceHistory _priceHistory ;
	private JMenuItem _viewHistMi;

	public Controller(JTextField searchTermTextField,
					  JMenuItem openM,
					  JMenuItem editListMi,
					  MyModel model,
					  JButton filterButton,
					  JButton updateButton,
					  JTextField vfNameField,
					  JTextField profitField,
					  JTextField kef5CodeField,
					  JButton addXmlButton,
					  JButton deleteXmlButton,
					  JTable table,
					  JSplitPane mainPane,
					  JButton printButton,
					  JMenuItem exitMi,
					  JMenuItem viewHistMi) {
		super();
		this._searchTermTextField = searchTermTextField;
		openMi = openM;
		this.model = model;
		_table = table;
		_updateButton = updateButton;
		_filterButton = filterButton;
		this.editListMi = editListMi;

		_vfNameField = vfNameField;
		_profitField = profitField;
		_kef5CodeField = kef5CodeField;
		_inputFields = new Object[]{"Όνομα Προϊόντος", vfNameField,
				"Κέρδος", profitField,
				"Κωδικός", kef5CodeField};
		_desiredProfit = new JTextField(5);


		_exitMi = exitMi;
		_addXmlButton = addXmlButton;
		_deleteXmlButton = deleteXmlButton;

		_mainPane = mainPane;
		_viewMode = CurrentMode.emptyWindow;


		_printButton = printButton;
		_viewHistMi      =viewHistMi;

		_priceHistory = model.getPriceHistory();


		_searchTermTextField.setVisible(true);
		_filterButton.setVisible(true);

		//init actions listeners
		JMenuActionListener();
		updateButtonListener();
		addXmlButtonListener();
		deleteXmlButtonListener();
		addMouseListener();
		addDragAndDropListener();
		addPrintButtonListener();

	}

	private void addPrintButtonListener() {
		_printButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Printer printer = new Printer();
				String text = "";
				Vector<VFVectorEntry> vector = model.getVector().getVec();
				for (int i = 0; i < vector.size(); ++i) {
					VFVectorEntry entry = vector.get(i);
					text += entry.getVfName() + " " +
							entry.getVfOrigin() + " " +
							entry.getVfNumber() + " " +
							entry.getVfFinalPrice() + "\n";
                   /*text += entry.getVfName() +" "+
                           entry.getVfOrigin()+" "+
                           entry.getVfNumber()+" "+
                           entry.getVfPrice() +"  "+
                           entry.getKef5Price()+" "+
                           entry.getVfFinalPrice()+ " "+
                           entry.getActualProfit()+"\n";*/
				}
				printer.setDoc(text);
				PrinterJob job = PrinterJob.getPrinterJob();
				job.setPrintable(printer);
				boolean ok = job.printDialog();
				if (ok) {
					try {
						job.print();
					} catch (PrinterException ex) {
						/* The job did not successfully complete */
					}
				}
			}
		});

	}


	private void setMainTableVisible() {


		//if table is not empty,remove all rows of table.
		//and recreate vector.
		if (model.getRowCount() > 0) {
			model.reCreateVector();
			model.setRowCount(0);
		}
		//editListMode = false;
		//priceMode = true;
		_viewMode= CurrentMode.priceMode;

		//update vector with pdf data.
		PdfParser reader = new PdfParser();
		try {
			reader.parsePdfFile(fileName, model.getVector());
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog msg = new MessageDialog();
			msg.showMessageDialog("Δεν είναι δυνατή η φόρτωση του αρχείου.\n"
					, "Αποτυχία φόρτωσης αρχείου", JOptionPane.ERROR_MESSAGE);
			return;
		}


		//if product is not in hash Map show error
		model.getVector().transformVector();
		ArrayList<String> unknownProductsList = model.getUnknownNames();
		if (!unknownProductsList.isEmpty()) {
			MessageDialog msg = new MessageDialog();
			msg.showMessageDialog(
					"Παρακαλώ καταχώρησε στην λίστα τα παρακάτω: "
							+ "" + unknownProductsList.toString(), "Error",
					JOptionPane.ERROR_MESSAGE
			);

			for(int i =0; i < unknownProductsList.size(); i++){
				_vfNameField.setText(unknownProductsList.get(i));
				_kef5CodeField.setText("");
				_profitField.setText("");
				_addXmlButton.doClick();
			}
		}

		//make necessary components visible
		//_searchTermTextField.setVisible(true);
		//_filterButton.setVisible(true);
		_updateButton.setVisible(true);
		_printButton.setVisible(true);

		_addXmlButton.setVisible(false);
		_deleteXmlButton.setVisible(false);

		//updates every entry on vector
		//with the right value for kef5code and final price.
		try {
			model.getVector().update(model.getVFHashMap());
		} catch (SQLException e) {
			MessageDialog msg = new MessageDialog();
			msg.showMessageDialog("Απέτυχε η εύρεση των τιμών απο το κεφάλαιο λογω προβλήματος με την SQL.",
					"Αποτυχία SQL", JOptionPane.ERROR_MESSAGE);
		}


		//add columns names
		model.setColumnIdentifiers(Constants.VF_TABLE_HEADER);
		_table.getColumnModel().getColumn(0).setPreferredWidth(200);

		//initialize history variables
		String currInvoiceDate = model.getVector().getDate();

		_priceHistory.setDate(currInvoiceDate);
		boolean isDateInHist =_priceHistory.isDateInHistory();

		for (int i = 0; i < model.getVector().getSize(); ++i) {
			//add rows to the model.
			model.addRow(model.getVectorRow(i));

			//update history.
			if(!isDateInHist) {
				VFVectorEntry entry = model.getVector().getVec().get(i);
				_priceHistory.addPrice(entry.getKef5Code(), entry.getVfFinalPrice());
			}
		}

		_priceHistory.writeToFile();

		addColorRenderer();
	}

	private void addColorRenderer() {
		_table.setDefaultRenderer(Object.class, new TableCellRenderer() {
			private DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				Vector<VFVectorEntry> vec = model.getVector().getVec();
				Vector<Integer> RowsWithHigherPrices = new Vector();

				for (int i = 0; i < vec.size(); ++i) {
					String name = vec.get(i).getVfName();
					if (vec.get(i).getVfFinalPrice() > vec.get(i).getKef5Price()) {
						for (int j = 0; j < model.getVector().getSize(); ++j) {
							Object[] o = model.getVectorRow(j);
							if (o[0].equals(name)) {
								RowsWithHigherPrices.add(j);
							}
						}

					}
				}
				c.setForeground(Color.black);
				if (RowsWithHigherPrices.contains(row) && _printButton.isVisible()) {
					c.setForeground(Color.red);
				}

				return c;
			}

		});
	}

	private void JMenuActionListener() {
		openMi.addActionListener((ActionEvent e) -> {

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setPreferredSize(new Dimension(600, 700));
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			int result = fileChooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				fileName = selectedFile.getAbsolutePath();
				setMainTableVisible();
			}
		});

		editListMi.addActionListener(e -> {
			//editListMode = true;
			//priceMode = false;
			_viewMode = CurrentMode.editListMode;

			model.setRowCount(0);

			//make necessary components invisible

			_updateButton.setVisible(false);

			_printButton.setVisible(false);

			model.setColumnIdentifiers(Constants.PRODUCTS_TABLE_HEADER);
			model.updateModelWithHash();

			_addXmlButton.setVisible(true);
			_deleteXmlButton.setVisible(true);

		});

		_viewHistMi.addActionListener(e->{

			//clear array
			model.setRowCount(0);

			//make necessary components invisible
			if(_viewMode == CurrentMode.priceMode) {

				_updateButton.setVisible(false);

				_printButton.setVisible(false);
			}else if(_viewMode == CurrentMode.editListMode){
				_addXmlButton.setVisible(false);
				_deleteXmlButton.setVisible(false);
			}

			//change mode
			_viewMode = CurrentMode.historyMode;

			//model.setColumnIdentifiers(Constants.HISTORY_TABLE_HEADER);
			model.updateModelWithHistory();

		});
		_exitMi.addActionListener(e -> {
			System.exit(1);
		});
	}

	private void addMouseListener() {
		_table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2) {
					if (_table.getSelectedColumn() == 5) {
						editProfit();
					} else if ((_viewMode == CurrentMode.priceMode.editListMode) && _table.getSelectedColumn() == 1) {
						editProfitPercentage();
					} else if ((_viewMode == CurrentMode.priceMode.editListMode)  && _table.getSelectedColumn() == 2) {
						editKef5Code();
					} else if((_viewMode == CurrentMode.priceMode.priceMode)  && _table.getSelectedColumn() == 0){
						showHistory();
					}

				}
			}
		});
	}

	private void editKef5Code() {
		XMLModifier xml = new XMLModifier();
		String kef5Code = (String) model.getValueAt(_table.getSelectedRow(), 2);
		String name = (String) model.getValueAt(_table.getSelectedRow(), 0);
		double profit = (double) model.getValueAt(_table.getSelectedRow(), 1);
		JTextField newKef5Code = new JTextField();

		_editFields = new Object[]{"Δώστε τον νεο κωδικό για το προϊόν: " + name, newKef5Code};
		int result = JOptionPane.showConfirmDialog(null, _editFields,
				"Εισαγωγή νέου κωδικού", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			//TODO check if code is valid.(on kefalaio,not empty).Update hashmap

			//edit hashMap.
			model.getVFHashMap().put(name, profit, newKef5Code.getText());
			//edit jTable row.
			int row = _table.getSelectedRow();
			_table.setValueAt(newKef5Code.getText(), row, 2);
			//edit xml file.
			xml.editXMLNode(kef5Code, newKef5Code.getText(), "kef5Code");

		}


	}

	private void editProfitPercentage() {
		XMLModifier xml = new XMLModifier();

		String name = (String) model.getValueAt(_table.getSelectedRow(), 0);
		String kef5Code = (String) model.getValueAt(_table.getSelectedRow(), 2);
		JTextField newProfitPercentage = new JTextField();

		_editFields = new Object[]{"Δώστε τον νεο ποσοστό κέρδους για το προϊόν: " + name, newProfitPercentage};
		int result = JOptionPane.showConfirmDialog(null, _editFields,
				"Εισαγωγή νέου Ποσοστού Κέρδους", JOptionPane.OK_CANCEL_OPTION);

		if (result == JOptionPane.OK_OPTION) {
			//TODO check if profit is valid and update hashMap
			String newProfit = newProfitPercentage.getText().replace(",", ".");
			//edit hashMap.
			model.getVFHashMap().put(name, Double.parseDouble(newProfit), kef5Code);

			//edit xml file.
			xml.editXMLNode(name, newProfit, "profit");
			//edit jTable row.
			int row = _table.getSelectedRow();
			_table.setValueAt(Double.parseDouble(newProfit), row, 1);
		}


	}

	private void editProfit() {
		if (_addXmlButton.isVisible()) {
			return;
		}
		_desiredProfit.setText("");

		String name = (String) model.getValueAt(_table.getSelectedRow(), 0);
		_editFields = new Object[]{"Δώστε το επιθυμιτό κέρδος σε χρήματα  για το προϊόν: " + name, _desiredProfit};
		int result = JOptionPane.showConfirmDialog(null, _editFields,
				"Κέρδος", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			String desiredProfitVal = _desiredProfit.getText().replace(",", ".");
			try {
				double desProf = Double.parseDouble(desiredProfitVal);
				int i;
				Vector<VFVectorEntry> vec = model.getVector().getVec();
				for (i = 0; i < model.getVector().getSize(); ++i) {
					VFVectorEntry vectorEntry = vec.elementAt(i);
					if (vectorEntry.getVfName().equals(name)) {
						//updates actual profit and final price on vector
						vectorEntry.updateActualProfit(desProf);
						int row = _table.getSelectedRow();

						_table.setValueAt(vectorEntry.getActualProfit(), row, 5);
						_table.setValueAt(vectorEntry.getVfFinalPrice(), row, 4);

						break;
					}
				}


			} catch (NumberFormatException e) {
				MessageDialog msg = new MessageDialog();
				msg.showMessageDialog("Το επιθυμυτό κερδος δεν είναι σωστό.\n Παρακαλώ προσπαθήστε ξανά.",
						"Λάθος είσοδος", JOptionPane.ERROR_MESSAGE);
			}


		}
	}

	private void addDragAndDropListener() {
		_mainPane.setDropTarget(new DropTarget() {
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>)
							evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					if (droppedFiles.size() > 1) {
						MessageDialog msg = new MessageDialog();
						msg.showMessageDialog("Το drag & drop λειτουργεί μονο εαν τραβήξουμε ενα αρχειο στο" +
								"παράθηρο ", "Λάθος ενέργεια", JOptionPane.ERROR_MESSAGE);
					}
					fileName = droppedFiles.get(0).getAbsolutePath();
					setMainTableVisible();
                    /*
                    for (File file : droppedFiles) {
                        fileName = file.getAbsolutePath();
                        setMainTableVisble();
                    }*/
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	private void updateButtonListener() {
		_updateButton.addActionListener(e -> {
			ArrayList<String> products;

			try {
				products = model.updateKef5Prices();
				MessageDialog msg = new MessageDialog();
				msg.showMessageDialog(
						"Ενημερώθηκαν επιτυχώς οι τιμές στο Κεφάλαιο 5 για τα παρακάτω: \n"
								+ "" + products.toString(), "Ενημέρωση Τιμών στο Κεφάλαιο 5",
						JOptionPane.INFORMATION_MESSAGE
				);
				Tracer tracer = new Tracer();
				try {
					tracer.run(model);
				} catch (Exception ex) {

					msg.showMessageDialog("Απέτυχε η ενημέρωση του txt αρχείου.", "Αποτυχία ενημέρωσης txt", JOptionPane.ERROR_MESSAGE);
				}


			} catch (SQLException a) {

				MessageDialog msg = new MessageDialog();
				msg.showMessageDialog("Απέτυχε η ενημέρωση τιμών λόγω πρόβληματος με την SQL.", "Αποτυχία SQL", JOptionPane.ERROR_MESSAGE);

			}

		});
	}

	private void deleteXmlButtonListener() {
		_deleteXmlButton.addActionListener(e -> {
			if (_table.getSelectedRow() != -1) {
				MessageDialog msg = new MessageDialog();
				String selected_vf_name = (String) model.getValueAt(_table.getSelectedRow(), 0);
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(
						null,
						"Θέλετε να διαγράψετε το προϊόν: " + selected_vf_name,
						"Διαγραφή", dialogButton);

				if (dialogResult == 0) {
					XMLModifier xml = new XMLModifier();
					VFHashMapValues val = model.getVFHashMap().remove(selected_vf_name);
					model.removeRow(_table.getSelectedRow());
					xml.deleteXMLNode(selected_vf_name);

					msg.showMessageDialog(
							"Το προιόν : " + selected_vf_name + " " + val.toString() + " διαγράφηκε επιτυχώς\n",
							"Επιτυχής Διαγραφή",
							JOptionPane.INFORMATION_MESSAGE
					);
				}

			} else {
				MessageDialog msg = new MessageDialog();
				msg.showMessageDialog(
						"Παρακαλώ επιλέξετε την γραμμή που θέλετε να διαγράψετε!\n",
						"Αποτυχής Διαγραφή",
						JOptionPane.INFORMATION_MESSAGE
				);
			}

		});
	}


	private void addXmlButtonListener() {
		_addXmlButton.addActionListener(e -> {
			int result = JOptionPane.showConfirmDialog(null, _inputFields,
					"Εισαγωγή Προϊόντος", JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {

				XMLModifier xml = new XMLModifier();
				//adds info to products.xml
				boolean isSuccessfullyAdded = xml.addXMLNode(_vfNameField.getText(),
						_profitField.getText(),
						_kef5CodeField.getText()
				);
				MessageDialog msg = new MessageDialog();
				if (isSuccessfullyAdded == false && (_viewMode == CurrentMode.priceMode.editListMode) ) {
					msg.showMessageDialog(
							"Η καταχώρηση του προιόντος απέτυχε.\n" +
									"Τα πεδία κωδικός και κέρδος πρέπει να είναι αριθμός.\n",
							"Αποτυχής Καταχώρηση",
							JOptionPane.INFORMATION_MESSAGE
					);
					return;
				}

				//adds info to hash table
				String a = _profitField.getText();
				a = a.replace(",", ".");
				double profit = Double.parseDouble(a);

				model.getVFHashMap().put(_vfNameField.getText(), profit, _kef5CodeField.getText());

				if((_viewMode == CurrentMode.priceMode.editListMode) ){
					msg.showMessageDialog(
							"Η καταχώρηση του προιόντος έγινε επιτυχώς", "Επιτυχής Καταχώρηση",
							JOptionPane.INFORMATION_MESSAGE
					);
				}

			}
			if((_viewMode == CurrentMode.priceMode.editListMode) ) {
				//update gui table
				model.setRowCount(0);
				model.setColumnIdentifiers(Constants.PRODUCTS_TABLE_HEADER);
				model.updateModelWithHash();
			}

		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String searchTerm = _searchTermTextField.getText();
		if (searchTerm != null && !"".equals(searchTerm) && (_table.getRowCount() != 0)) {

			Object[][] newData = new Object[model.getRowCount()][];
			Object[][] data = new Object[0][];
			if(_viewMode == CurrentMode.editListMode){
				//data is inside vf_rates
				HashMap<String,VFHashMapValues> vf_map = model.getVFHashMap().getVFMap();
				data = new Object[model.getRowCount()][];
				int cnt =0;
				for (Map.Entry<String, VFHashMapValues> a : vf_map.entrySet()) {
					data[cnt] = new Object[]{a.getKey(),a.getValue().getProfit(), a.getValue().getKef5Code()};
					cnt++;
				}
			}else if(_viewMode == CurrentMode.priceMode){
				data = new Object[model.getVector().getSize()][];
				for (int i = 0; i < model.getVector().getSize(); ++i) {
					data[i] = model.getVectorRow(i);
				}
			}else if(_viewMode == CurrentMode.historyMode){
				//data -> priceHistory
				data = model.getHistoryTable();
			}


			int idx = 0;
			for(int i = 0; i <data.length; i++){
				Object[] o =  data[i];
				if("*".equals(searchTerm.trim())){
					newData[idx++] = o;
				}else{
					String productName = (String)o[0];

					if(productName.trim().startsWith(searchTerm.trim())){

						newData[idx++] = o;

					}
				}
			}

			if(_viewMode == CurrentMode.editListMode){
				model.setDataVector(newData,Constants.PRODUCTS_TABLE_HEADER);
			}else if(_viewMode == CurrentMode.priceMode){
				model.setDataVector(newData,Constants.VF_TABLE_HEADER);
				_table.getColumnModel().getColumn(0).setPreferredWidth(200);
			}else if(_viewMode == CurrentMode.historyMode){
				model.setDataVector(newData,Constants.HISTORY_TABLE_HEADER);
			}


		} else {
			MessageDialog msg = new MessageDialog();
			msg.showMessageDialog(
					"Δεν βρέθηκαν αποτελέσματα", "Error",
					JOptionPane.ERROR_MESSAGE
			);
		}

	}

	public Object[][] getTableData (JTable table) {
		MyModel dtm = (MyModel) table.getModel();
		int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
		Object[][] tableData = new Object[nRow][nCol];
		for (int i = 0 ; i < nRow ; i++)
			for (int j = 0 ; j < nCol ; j++)
				tableData[i][j] = dtm.getValueAt(i,j);
		return tableData;
	}
	public void tableChanged(TableModelEvent e) {
		int row = e.getFirstRow();
		int column = e.getColumn();
		int BOOLEAN_COLUMN = 6;
		if (column == BOOLEAN_COLUMN) {
			//TableModel model    = (TableModel) e.getSource();
			String vf_name = (String) model.getValueAt(row, 0);
			Boolean checked = (Boolean) model.getValueAt(row, column);

			boolean isUpdateDone = model.getVector().updateVectorValueUpdateNeeded(vf_name, checked);
			if (!isUpdateDone) {
				System.err.println("Error: Vegetable or fruit name does not in vector can not updated !");
			}

		}
	}

	public void showHistory(){

		String vf_name = (String) model.getValueAt(_table.getSelectedRow(), 0);
		String kef5Code = model.getVFHashMap().get(vf_name).getKef5Code();
		PriceHistory.Prices prices = _priceHistory.getPrices(kef5Code);
		//System.out.println(prices);
		MessageDialog msg=new MessageDialog();
		msg.showMessageDialog("Οι παλαιότερες τιμές για το προιoν ειναι: "+prices,"Ιστορικό",
				JOptionPane.INFORMATION_MESSAGE);
	}

}


