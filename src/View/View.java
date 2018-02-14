package View;


import Controller.Controller;

import Model.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * @author ashraf
 *
 */
public class View {

	public View() {
		// Create views swing UI components 
		JTextField searchTermTextField = new JTextField(10);

		/*JTextField productName     = new JTextField(10);
		JTextField productProfit   = new JTextField(10);
		JTextField productKef5Code = new JTextField(10);*/

		JButton printButton = new JButton("Εκτύπωση");
		JButton filterButton = new JButton("Εύρεση");
		JButton updateButton = new JButton("Ενημέρωση Κεφαλαίου");
		JTable table ;//= new JTable();


		JMenuBar menubar = new JMenuBar();


		//create icons for menu bar
		ImageIcon iconOpen = new ImageIcon("icons\\open.png");
		ImageIcon iconExit = new ImageIcon("icons\\exit.png");
		ImageIcon iconAdd  = new ImageIcon("icons\\add2.png");
		ImageIcon iconDelete  = new ImageIcon("icons\\delete.png");
		ImageIcon iconEdit  = new ImageIcon("icons\\edit.png");

		iconExit = scaleIcon(iconExit,15,15);
		iconOpen = scaleIcon(iconOpen,15,15);

		iconAdd  	= scaleIcon(iconAdd,40,40);
		iconDelete  = scaleIcon(iconDelete,40,40);
		iconEdit    = scaleIcon(iconEdit,40,40);
		JButton  addXmlButton      	= new JButton("Καταχώρηση",iconAdd);
		JButton  deleteXmlButton	= new JButton("Διαγραφή",iconDelete);
		JButton  editXmlButton 		= new JButton("Επεξεργασία",iconEdit);

		//create and add JMenu items
		JMenu fileMenu = new JMenu("Αρχείο");
		JMenu editMenu = new JMenu("Επεξεργασία");

		JMenuItem openMi = new JMenuItem("Άνοιγμα", iconOpen);
		JMenuItem exitMi = new JMenuItem("Έξοδος", iconExit);
		JMenuItem editListMi = new JMenuItem("Λίστας");
		exitMi.setToolTipText("Έξοδος απο την εφαρμογή");
		editListMi.setToolTipText("Επεξεργασία κωδικών και ποσοστών κέρδους για τα φρούτα και τα λαχανικά");


		fileMenu.add(openMi);
		fileMenu.addSeparator();
		fileMenu.add(exitMi);
		menubar.add(fileMenu);

		editMenu.add(editListMi);
		menubar.add(editMenu);

		// Create table model
		MyModel model = new MyModel();


		table = new JTable(model){
			@Override
			public Class getColumnClass(int column) {
				switch (column) {
					case 6:
						return Boolean.class;
					default:
						return String.class;
				}
			}
			@Override
			public boolean isCellEditable(int row, int column) {
                return column == 6;//editXmlButton.isVisible() || column >= 5;

            }
		};
		table.setFont(new Font("Serif", Font.PLAIN, 17));
		table.setModel(model);

		// Set the view layout
		JPanel ctrlPane = new JPanel();

		//hide buttons and search field.
		searchTermTextField.setVisible(false);
		updateButton.setVisible(false);
		filterButton.setVisible(false);
		printButton.setVisible(false);

		ctrlPane.add(searchTermTextField);
		ctrlPane.add(filterButton);
		ctrlPane.add(updateButton);
		ctrlPane.add(printButton);

		ctrlPane.add(addXmlButton);
		ctrlPane.add(editXmlButton);
		ctrlPane.add(deleteXmlButton);
		addXmlButton.setVisible(false);
		editXmlButton.setVisible(false);
		deleteXmlButton.setVisible(false);


		JScrollPane tableScrollPane = new JScrollPane(table);
		tableScrollPane.setPreferredSize(new Dimension(1000, 600));
		tableScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Λαχανικά-Φρούτα",
				TitledBorder.CENTER, TitledBorder.TOP));
        //tableScrollPane.setVisible(false);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,tableScrollPane, ctrlPane );
		splitPane.setDividerLocation(700);
		splitPane.setEnabled(false);


		// Display it all in a scrolling window and make the window appear
		JFrame frame = new JFrame("ΛΑΧΑΝΙΚΑ-ΦΡΟΥΤΑ");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(splitPane);
		frame.setJMenuBar(menubar);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		//JTextFields to insert product to products.xml
		JTextField vfNameField = new JTextField(15);
		JTextField profitField = new JTextField(15);
		JTextField kef5CodeField = new JTextField(15);




        // Create controller
        Controller controller = new Controller(searchTermTextField,
											openMi,editListMi,model,
											filterButton,updateButton,
											vfNameField,profitField,
											kef5CodeField,
											addXmlButton,
											deleteXmlButton,
				  							table,splitPane,printButton,exitMi);

        filterButton.addActionListener(controller);
		table.getModel().addTableModelListener(controller);

    }

	private ImageIcon scaleIcon(ImageIcon imageicon,int w,int h){
		Image image = imageicon.getImage(); // transform it
		Image newimg = image.getScaledInstance(w, h,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
		return new ImageIcon(newimg);  // transform it back
	}



}
