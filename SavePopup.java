import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.File;
public class SavePopup{
    Board board;
    public SavePopup(MainGUI mainGUI, Board board){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JTextField field = new JTextField(20);
        JLabel label = new JLabel("Add comments:");
        JLabel saveLabel = new JLabel("Enter Name:");
        JLabel warning = new JLabel("");
        JTextField saveField = new JTextField(20);
        frame.setSize(1000, 200);
        JButton save = new JButton("Save");
        save.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String name = saveField.getText();
                File f = new File(name);
                if (!f.exists()){
                    try{
                    String comment = field.getText();
                    if (comment.equals("")){
                        comment = "No comment";
                    }
                    mainGUI.save(name, comment, board);
                    frame.dispose();
                    } catch (Exception z){
                        
                    }
                } else {
                    warning.setText("File already exists!");
                }
            }
        }
        );
        panel.add(saveLabel);
        panel.add(saveField);
        panel.add(label);
        panel.add(field);
        panel.add(save);
        panel.add(warning);
        frame.add(panel);
        frame.setVisible(true);
    }
}