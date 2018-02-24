package Model;

import javax.swing.*;

public class MessageDialog {
    public void showMessageDialog(String msg,String title,int type){
        JOptionPane.showMessageDialog(null,msg,title,type);
    }
}
