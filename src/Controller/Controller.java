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
import javax.swing.text.StyledEditorKit;

import Model.*;
import Model.Printer;

public class Controller implements ActionListener,TableModelListener {
	public enum CurrentMode{

		editListMode,
		priceMode,
		emptyWindow,
		historyMode,
		PrintCustomLabels
	}

	public static CurrentMode _viewMode = CurrentMode.emptyWindow;;
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
	private JMenuItem _viewHistMi;
	private JMenuItem _newLabelsMi;
	private JButton _printLabels;
	private JButton _addLabel;
	private JButton _deleteLabel;
	private Object[]  _labelFields;
    History hist ;
	JTextField _labelName;
	JTextField _labelPrice;
	JTextField _labelCode;
	JTextField _labelOrigin;
	JButton _printCustomLabels;
	JTextField _newPrice;
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
					  JMenuItem viewHistMi,
					  JButton printLabelsButton,
					  JMenuItem newLabelsMi,
					  JButton addLabel,
					  JButton deleteLabel,
					  JTextField labelName,
					  JTextField labelPrice,
					  JTextField labelCode,
					  JButton printCustomLabels,
					  JTextField labelOrigin) {
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
		_labelFields = new Object[]{"Όνομα",labelName,"Προέλευση",labelOrigin,"Τιμή",labelPrice,"Κωδικός Τιμολογίου",labelCode};
		_printCustomLabels = printCustomLabels;

		_exitMi = exitMi;
		_addXmlButton = addXmlButton;
		_deleteXmlButton = deleteXmlButton;

		_mainPane = mainPane;

		_newLabelsMi = newLabelsMi;

		_printButton = printButton;
		_viewHistMi      =viewHistMi;

		_searchTermTextField.setVisible(false);
		_filterButton.setVisible(false);
		_printLabels = printLabelsButton;
		_addLabel=addLabel;
		_deleteLabel=deleteLabel;
		_labelName = labelName;
		_labelPrice = labelPrice;
		_labelCode =labelCode;
		_labelOrigin = labelOrigin;
		_newPrice = new JTextField();
        hist =model.getHistory();
		//init actions listeners
		JMenuActionListener();
		updateButtonListener();
		addXmlButtonListener();
		deleteXmlButtonListener();
		addMouseListener();
		addDragAndDropListener();
		addPrintButtonListener();
		addPrintLabelsButtonListener();
		addLabelsListener();
	}

	private void deactivateButtons()
	{
		_searchTermTextField.setVisible(false);
		_filterButton.setVisible(false);
		_updateButton.setVisible(false);
		_printButton.setVisible(false);
		_printLabels.setVisible(false);
		_addXmlButton.setVisible(false);
		_deleteXmlButton.setVisible(false);
		_addLabel.setVisible(false);
		_deleteLabel.setVisible(false);
		_printCustomLabels.setVisible(false);
	}
	private void addLabelsListener()
	{
		_newLabelsMi.addActionListener(e -> {
			_viewMode  = CurrentMode.PrintCustomLabels;

			deactivateButtons();

			_addLabel.setVisible(true);
			_deleteLabel.setVisible(true);
			_printCustomLabels.setVisible(true);

			model.setRowCount(0);
			model.setColumnIdentifiers(new Object[]{""});

		});

		_addLabel.addActionListener(e-> {

			_labelCode.setText("");
			_labelName.setText("");
			_labelOrigin.setText("");
			_labelPrice.setText("");

			int result = JOptionPane.showConfirmDialog(null, _labelFields,
					"Εισαγωγή Προϊόντος", JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				model.setColumnIdentifiers(Constants.LABELS_TABLE_HEADER);
				model.addRow(new Object[]{_labelName.getText().toUpperCase(),_labelOrigin.getText().toUpperCase(),
										 _labelPrice.getText().toUpperCase(),_labelCode.getText().toUpperCase()});

			}

		});

		_deleteLabel.addActionListener(e -> {
			model.removeRow(_table.getSelectedRow());
		});

		_printCustomLabels.addActionListener(e->{
			String labelsContent = "";
			for(int i=0; i < model.getRowCount(); i++){
				String labelName = (String)model.getValueAt(i,0);
				String labelOrigin = (String)model.getValueAt(i,1);
				String labelPrice = (String)model.getValueAt(i,2);
				if(!labelPrice.isEmpty()){
					labelPrice+="€";
				}
				String labelCode = (String)model.getValueAt(i,3);

				labelsContent+=labelName+"%"+labelOrigin+"%"+labelPrice+"%"+labelCode+"\n";
			}
			labelsContent = labelsContent.toUpperCase();

			LabelPrinter labelPrinter = new LabelPrinter();

			labelPrinter.setDoc(labelsContent);
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintable(labelPrinter);
			boolean ok = job.printDialog();

			if (ok) {
				try {
					job.print();
				} catch (PrinterException ex) {
					/* The job did not successfully complete */
					ex.printStackTrace();
				}
			}

		});
	}

	private void addPrintLabelsButtonListener(){
		_printLabels.addActionListener(e -> {

				LabelPrinter labelPrinter = new LabelPrinter();
				String labels = "";
				VFKef5DataBase dataBase = new VFKef5DataBase();
				Vector<VFVectorEntry> vector = model.getVector().getVec();

				for (int i = 0; i < vector.size(); ++i) {
					VFVectorEntry entry = vector.get(i);

					if(entry.getIsPrintNeeded()== false){
						continue;
					}
					String query = "select sRetailPr  from dbo.smast where sCode=" + "'" + entry.getKef5Code() + "'" + ";";
					Double price = null;
					try{
						price = Double.parseDouble(dataBase.getFromDatabase(query));
					}catch (Exception ex){
						MessageDialog msg = new MessageDialog();
						msg.showMessageDialog(
								"Δεν βρέθηκαν τιμή για το προϊόν "+ entry.getVfName(), "Error",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}

					labels += entry.getVfName() + "%" +
							entry.getVfOrigin() + "%" +
							String.format(	"%.2f",price ) + "€" + "%" +
							entry.getVfNumber() + "\n";
				}



			labelPrinter.setDoc(labels);
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintable(labelPrinter);
			boolean ok = job.printDialog();

			if (ok) {
				try {
					job.print();
				} catch (PrinterException ex) {
					/* The job did not successfully complete */
					ex.printStackTrace();
				}
			}

		});
	}

	private void addPrintButtonListener() {
		_printButton.addActionListener(e -> {
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
		deactivateButtons();
		_updateButton.setVisible(true);
		_printButton.setVisible(true);
		_printLabels.setVisible(true);



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
		_table.getColumnModel().getColumn(1).setPreferredWidth(200);

		//initialize history variables
		String currInvoiceDate = model.getVector().getDate();
        for (int i = 0; i < model.getVector().getSize(); ++i) {
			//add rows to the model.
			model.addRow(model.getVectorRow(i));

        }

		//update history
		hist.updateHistoryDatabase(model.getVector(),currInvoiceDate.trim());

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
							if (o[1].equals(name)) {

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
			deactivateButtons();

			//make necessary components invisible
			_searchTermTextField.setVisible(true);
			_filterButton.setVisible(true);

			model.setColumnIdentifiers(Constants.PRODUCTS_TABLE_HEADER);
			model.updateModelWithHash();

			_addXmlButton.setVisible(true);
			_deleteXmlButton.setVisible(true);

		});

		_viewHistMi.addActionListener(e->{

			//clear array
			model.setRowCount(0);

			deactivateButtons();

			_searchTermTextField.setVisible(true);
			_filterButton.setVisible(true);

			//change mode
			_viewMode = CurrentMode.historyMode;

			model.setColumnIdentifiers(Constants.HISTORY_TABLE_HEADER);
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
					if (_table.getSelectedColumn() == 6) {
						editProfit();
					} else if ((_viewMode == CurrentMode.priceMode.editListMode) && _table.getSelectedColumn() == 1) {
						editProfitPercentage();
					} else if ((_viewMode == CurrentMode.priceMode.editListMode)  && _table.getSelectedColumn() == 2) {
						editKef5Code();
					} else if((_viewMode == CurrentMode.priceMode.priceMode)  && _table.getSelectedColumn() == 1){
						changePriceUsingHistory();
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

		String name = (String) model.getValueAt(_table.getSelectedRow(), 1);
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

						_table.setValueAt(vectorEntry.getActualProfit(), row, 6);
						_table.setValueAt(vectorEntry.getVfFinalPrice(), row, 5);

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
		int BOOLEAN_COLUMN_FOR_KEF5_UPDATES =7;
		int BOOLEAN_COLUMN_FOR_LABELS=0;

		if (column == BOOLEAN_COLUMN_FOR_KEF5_UPDATES) {
			//TableModel model    = (TableModel) e.getSource();
			String vf_name = (String) model.getValueAt(row, 1);
			Boolean checked = (Boolean) model.getValueAt(row, column);

			boolean isUpdateDone = model.getVector().updateVectorValueUpdateNeeded(vf_name, checked);
			if (!isUpdateDone) {
				System.err.println("Error: Vegetable or fruit name does not in vector can not updated !");
			}
		}else if(column == BOOLEAN_COLUMN_FOR_LABELS) {
			String vf_name = (String) model.getValueAt(row, 1);
			Boolean val = (Boolean) model.getValueAt(row, column);
			Vector<VFVectorEntry> vec = model.getVector().getVec();
			boolean found = false;
			for(int i =0; i < vec.size(); ++i) {
				if(vec.get(i).getVfName().equals(vf_name)){
					vec.get(i).setPrintNeeded(val);
					found = true;
					//System.out.println(vec.get(i).getIsPrintNeeded());
					//System.out.println(vec.get(i).getVfName());

				}
				if(found){
					break;
				}
			}



		}

	}

	public void changePriceUsingHistory() {


		String vf_name = (String) model.getValueAt(_table.getSelectedRow(), 1);
		Double vf_final_price = (Double) model.getValueAt(_table.getSelectedRow(), 5);
		String kef5Code = model.getVFHashMap().get(vf_name).getKef5Code();
		String pr = null;
		for (int i = 0; i < hist.getHistoryVector().size(); i++) {
			if (hist.getHistoryVector().elementAt(i).getKef5Code().equals(kef5Code)) {
				pr = hist.getHistoryVector().elementAt(i).toString2();
			}
		}

		_newPrice.setText(vf_final_price.toString());
		Object[] dialogContent = new Object[]{"Οι παλαιότερες τιμές για το προιoν ειναι: " + pr, "Νεα Τιμή:", _newPrice};
		int result = JOptionPane.showConfirmDialog(null, dialogContent, "Ιστορικό", JOptionPane.OK_CANCEL_OPTION);

		boolean priceChanged = false;
		Double newFinalPrice = null;
		if (result == JOptionPane.OK_OPTION) {
			String newPrice = _newPrice.getText().toString().trim().replace(",", ".");

			try {
				newFinalPrice = Double.parseDouble(newPrice);
			} catch (Exception e) {
				MessageDialog msg = new MessageDialog();
				msg.showMessageDialog("Η νεα τιμή πρέπει να είναι αριθμός.", "Λαθος Νεα Τιμή", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if (newFinalPrice.equals(vf_final_price)) {
				//System.out.println("Same price");
			} else {
				//System.out.println("Price Changed");
				priceChanged = true;
			}

		}

		//if price has changed then we need to update both table and vector with the new value.
		if (priceChanged) {
			Vector<VFVectorEntry> vec = model.getVector().getVec();
			for (int i = 0; i < vec.size(); i++) {
				if (vec.elementAt(i).getKef5Code().equals(kef5Code)) {

					vec.elementAt(i).setUpdateNeeded(true);
					vec.elementAt(i).setVf_final_price(newFinalPrice);
					double price_with_taxes = ((vec.elementAt(i).getVfPrice()*vec.elementAt(i).getVf_tax()*0.01)+vec.elementAt(i).getVfPrice());
					double profit = newFinalPrice-price_with_taxes;

					profit = Math.round((profit*100f))/100f;
					vec.elementAt(i).setActual_profit(profit);

					model.setValueAt(newFinalPrice,_table.getSelectedRow(),5);
					model.setValueAt(true,_table.getSelectedRow(),7);
					model.setValueAt( Math.round((profit*100f))/100f, _table.getSelectedRow(),6);

					break;
				}
			}

		}


		//if _newPrice changed set VFVectorEntry value isUpdateNeeded to true.

		dialogContent = null;

	}

}


