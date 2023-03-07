import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class NewGameGUI {
    private int[] parameters;
    private Game game;

    public NewGameGUI(Game game) {
        this.parameters = new int[5];
        this.game = game;
        this.game.setNewGameGUI(this);
        JFrame setup2Frame = new JFrame("Game of Life - Setup");
        setup2Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SpringLayout layout = new SpringLayout();

        setup2Frame.setSize(300, 300);
        JPanel contentPane = new JPanel();

        contentPane.setLayout(layout);

        JLabel heightJLabel = new JLabel("Select game height:");
        JLabel widthJLabel = new JLabel("Select game width:");
        JLabel xLabel = new JLabel("Set x");
        JLabel yLabel = new JLabel("Set y");
        JLabel zLabel = new JLabel("Set z");
        String[] intChoices = new String[8];
        for (int i = 1; i <= 8; i++) {
            intChoices[i - 1] = Integer.toString(i);
        }
        String[] sizeChoices = new String[47];
        for (int i = 4; i <= 50; i++) {
            sizeChoices[i - 4] = Integer.toString(i);
        }
        JComboBox<String> heightBox = new JComboBox<String>(sizeChoices);
        JComboBox<String> widthBox = new JComboBox<String>(sizeChoices);
        heightBox.setSelectedIndex(46);
        widthBox.setSelectedIndex(46);
        JComboBox<String> xBox = new JComboBox<String>(intChoices);
        JComboBox<String> yBox = new JComboBox<String>(intChoices);
        JComboBox<String> zBox = new JComboBox<String>(intChoices);
        heightBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                parameters[0] = Integer.parseInt((String) heightBox.getSelectedItem());
            }
        });

        widthBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                parameters[1] = Integer.parseInt((String) widthBox.getSelectedItem());
            }
        });
        xBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                parameters[2] = Integer.parseInt((String) xBox.getSelectedItem());
            }
        });
        xBox.setSelectedIndex(1);
        yBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                parameters[3] = Integer.parseInt((String) yBox.getSelectedItem());
            }
        });
        yBox.setSelectedIndex(2);
        zBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                parameters[4] = Integer.parseInt((String) zBox.getSelectedItem());
            }
        });
        zBox.setSelectedIndex(2);

        contentPane.add(heightJLabel);
        contentPane.add(heightBox);
        contentPane.add(widthJLabel);
        contentPane.add(widthBox);
        contentPane.add(xLabel);
        contentPane.add(xBox);
        contentPane.add(yLabel);
        contentPane.add(yBox);
        contentPane.add(zLabel);
        contentPane.add(zBox);

        layout.putConstraint(SpringLayout.WEST, heightJLabel, 5, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, heightJLabel, 10, SpringLayout.NORTH, contentPane);

        layout.putConstraint(SpringLayout.WEST, heightBox, 6, SpringLayout.EAST, heightJLabel);
        layout.putConstraint(SpringLayout.NORTH, heightBox, 6, SpringLayout.NORTH, contentPane);

        layout.putConstraint(SpringLayout.WEST, widthJLabel, 5, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, widthJLabel, 20, SpringLayout.SOUTH, heightJLabel);

        layout.putConstraint(SpringLayout.WEST, widthBox, 6, SpringLayout.EAST, widthJLabel);
        layout.putConstraint(SpringLayout.NORTH, widthBox, -4, SpringLayout.NORTH, widthJLabel);

        layout.putConstraint(SpringLayout.WEST, xLabel, 6, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, xLabel, 20, SpringLayout.SOUTH, widthJLabel);

        layout.putConstraint(SpringLayout.WEST, xBox, 6, SpringLayout.EAST, xLabel);
        layout.putConstraint(SpringLayout.NORTH, xBox, -4, SpringLayout.NORTH, xLabel);

        layout.putConstraint(SpringLayout.WEST, yLabel, 6, SpringLayout.EAST, xBox);
        layout.putConstraint(SpringLayout.NORTH, yLabel, 20, SpringLayout.SOUTH, widthJLabel);

        layout.putConstraint(SpringLayout.WEST, yBox, 6, SpringLayout.EAST, yLabel);
        layout.putConstraint(SpringLayout.NORTH, yBox, -4, SpringLayout.NORTH, yLabel);

        layout.putConstraint(SpringLayout.WEST, zLabel, 6, SpringLayout.EAST, yBox);
        layout.putConstraint(SpringLayout.NORTH, zLabel, 20, SpringLayout.SOUTH, widthJLabel);

        layout.putConstraint(SpringLayout.WEST, zBox, 6, SpringLayout.EAST, zLabel);
        layout.putConstraint(SpringLayout.NORTH, zBox, -4, SpringLayout.NORTH, zLabel);

        ConfirmButton confirmButton = new ConfirmButton("Confirm", this.game);
        confirmButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            parameters[0] = Integer.parseInt((String) heightBox.getSelectedItem());
                            parameters[1] = Integer.parseInt((String) widthBox.getSelectedItem());
                            parameters[2] = Integer.parseInt((String) xBox.getSelectedItem());
                            parameters[3] = Integer.parseInt((String) yBox.getSelectedItem());
                            parameters[4] = Integer.parseInt((String) zBox.getSelectedItem());
                            setup2Frame.dispose();
                            game.setParameters(parameters);
                            //System.out.println("paramaters" +parameters[0] + " "+parameters[1] + " "+parameters[2] + " "+parameters[3] + " "+parameters[4]);
                            game.newGame();
                        }

                catch (Exception exce) {

                        }
                    }
                });
        contentPane.add(confirmButton);

        layout.putConstraint(SpringLayout.NORTH, confirmButton, 20, SpringLayout.SOUTH, xBox);
        layout.putConstraint(SpringLayout.WEST, confirmButton, 40, SpringLayout.SOUTH, xBox);
        setup2Frame.add(contentPane);
        setup2Frame.setVisible(true);

    }

    public int[] getParameters() {
        return this.parameters;
    }
}

class ConfirmButton extends JButton implements Runnable {
    Game game;

    public ConfirmButton(String text, Game game) {
        this.setText(text);
        this.game = game;
    }

    public void run() {
        try {
            this.game.newGame();
        } catch (Exception e) {
            // System.exit(0);
        }
    }
}