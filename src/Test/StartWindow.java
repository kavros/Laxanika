package Test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class StartWindow extends JFrame {

    public StartWindow() {

        initUI();
    }

    private void initUI() {

        createMenuBar();
        createJTable();
        setTitle("Laxanika");
        setSize(360, 250);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private ImageIcon scaleIcon(ImageIcon imageicon,int w,int h){
        Image image = imageicon.getImage(); // transform it
        Image newimg = image.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        return new ImageIcon(newimg);  // transform it back
    }

    private void createMenuBar() {

        JMenuBar menubar = new JMenuBar();

        ImageIcon iconNew = new ImageIcon("icons\\new.png");
        ImageIcon iconOpen = new ImageIcon("icons\\open.png");
        ImageIcon iconSave = new ImageIcon("icons\\save.png");
        ImageIcon iconExit = new ImageIcon("icons\\exit.png");

        iconNew  = scaleIcon(iconNew,10,10);
        iconSave =scaleIcon(iconSave,10,10);
        iconExit =scaleIcon(iconExit,10,10);
        iconOpen =scaleIcon(iconOpen,10,10);
        JMenu fileMenu = new JMenu("File");

        JMenu impMenu = new JMenu("Import");
        JMenuItem newsfMi = new JMenuItem("Import newsfeed list...");
        JMenuItem bookmMi = new JMenuItem("Import bookmarks...");
        JMenuItem mailMi = new JMenuItem("Import mail...");

        impMenu.add(newsfMi);
        impMenu.add(bookmMi);
        impMenu.add(mailMi);

        JMenuItem newMi = new JMenuItem("New", iconNew);
        JMenuItem openMi = new JMenuItem("Open", iconOpen);
        JMenuItem saveMi = new JMenuItem("Save", iconSave);

        JMenuItem exitMi = new JMenuItem("Exit", iconExit);
        exitMi.setToolTipText("Exit application");

        openMi.addActionListener((ActionEvent event)->{
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            }
        });

        exitMi.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });

        fileMenu.add(newMi);
        fileMenu.add(openMi);
        fileMenu.add(saveMi);
        fileMenu.addSeparator();
        fileMenu.add(impMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitMi);

        menubar.add(fileMenu);
        add(createJTable());

        setJMenuBar(menubar);
    }

    private JTable createJTable(){
        JFrame frame = new JFrame();
        final JTable table;


        String[] columnTitles= {"Προϊόντα","Τιμή Χονδρικής",
                "Τιμή Πώλησης","Νέα Τιμή Πώλησης"
                ,"Ενημέρωση Κεφαλαίου"};

        Object[][] rowData = { { "ΤΟΜΑΤΕΣ", "1,2", "1,3", "14",false } };
        table = new JTable(rowData, columnTitles);
        table.setLocation(0,200);
        table.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel = table.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String selectedData = null;

                int[] selectedRow = table.getSelectedRows();
                int[] selectedColumns = table.getSelectedColumns();

                for (int i = 0; i < selectedRow.length; i++) {
                    for (int j = 0; j < selectedColumns.length; j++) {
                        selectedData = (String) table.getValueAt(selectedRow[i], selectedColumns[j]);
                    }
                }
                System.out.println("Selected: " + selectedData);
            }

        });



        return table;

    }


    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            StartWindow ex = new StartWindow();
            ex.setVisible(true);
        });
    }
}