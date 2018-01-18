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
		JButton filterButton = new JButton("Εύρεση");
		JButton updateButton = new JButton("Ενημέρωση Κεφαλαίου");
		JTable table ;//= new JTable();
		JMenuBar menubar = new JMenuBar();


		//create icons for menu bar
		ImageIcon iconNew = new ImageIcon("icons\\new.png");
		ImageIcon iconOpen = new ImageIcon("icons\\open.png");
		ImageIcon iconSave = new ImageIcon("icons\\save.png");
		ImageIcon iconExit = new ImageIcon("icons\\exit.png");
		iconNew  = scaleIcon(iconNew,10,10);
		iconSave =scaleIcon(iconSave,10,10);
		iconExit =scaleIcon(iconExit,10,10);
		iconOpen =scaleIcon(iconOpen,10,10);

		//create and add JMenu items
		JMenu fileMenu = new JMenu("Αρχείο");
		JMenu editMenu = new JMenu("Επεξεργασία");

		JMenu impMenu = new JMenu("Εισαγωγή");
		JMenuItem newsfMi = new JMenuItem("Import newsfeed list...");
		JMenuItem bookmMi = new JMenuItem("Import bookmarks...");
		JMenuItem mailMi = new JMenuItem("Import mail...");

		impMenu.add(newsfMi);
		impMenu.add(bookmMi);
		impMenu.add(mailMi);


		//JMenuItem newMi = new JMenuItem("Νέο Αρχείο", iconNew);
		JMenuItem openMi = new JMenuItem("Άνοιγμα", iconOpen);
		//JMenuItem saveMi = new JMenuItem("Αποθήκευση", iconSave);
		JMenuItem exitMi = new JMenuItem("Έξοδος", iconExit);
		JMenuItem editListMi = new JMenuItem("Λίστα Λαχανικών-Φρούτων");
		exitMi.setToolTipText("Exit application");

		//fileMenu.add(newMi);
		fileMenu.add(openMi);
		//fileMenu.add(saveMi);
		fileMenu.addSeparator();
		fileMenu.add(impMenu);
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
					case 5:
						return Boolean.class;
					default:
						return String.class;
				}
			}
			@Override
			public boolean isCellEditable(int row, int column){
				 if(column<5){
				     return false;
                 }else{
				     return true;
                 }

			}
		};

		table.setModel(model);


		// Set the view layout
		JPanel ctrlPane = new JPanel();

		searchTermTextField.setVisible(false);
		updateButton.setVisible(false);
		filterButton.setVisible(false);

		ctrlPane.add(searchTermTextField);
		ctrlPane.add(filterButton);
		ctrlPane.add(updateButton);

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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(splitPane);
		frame.setJMenuBar(menubar);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

        // Create controller
        Controller controller = new Controller(searchTermTextField,openMi,editListMi,model,filterButton,updateButton,table);
        filterButton.addActionListener(controller);
		table.getModel().addTableModelListener(controller);

    }

	private ImageIcon scaleIcon(ImageIcon imageicon,int w,int h){
		Image image = imageicon.getImage(); // transform it
		Image newimg = image.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
		return new ImageIcon(newimg);  // transform it back
	}



}
