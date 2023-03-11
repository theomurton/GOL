import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class SavePopup{
    Board board;
    public SavePopup(MainGUI mainGUI, Board board){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JTextField field = new JTextField(20);
        JLabel label = new JLabel("Add comments:");
        JLabel saveLabel = new JLabel("Enter Name:");
        JTextField saveField = new JTextField(20);
        frame.setSize(500, 100);
        JButton save = new JButton("Save");
        save.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                String name = saveField.getText();
                String comment = field.getText();
                mainGUI.save(name, comment, board);
                save.setText("Saved!");
		        Thread.sleep(500);
                frame.dispose();
                } catch (Exception z){

                }
            }
        }
        );
        panel.add(saveLabel);
        panel.add(saveField);
        panel.add(label);
        panel.add(field);
        panel.add(save);
        frame.add(panel);
        frame.setVisible(true);
    }
}