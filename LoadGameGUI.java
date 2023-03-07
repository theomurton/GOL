import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoadGameGUI {
    private int count;
    private int[] parameters = new int[] { 4, 4, 1, 1, 1 };
    private Game game;

    public LoadGameGUI(Game game) {
        JFrame loadGameFrame = new JFrame("Game of Life - Load save");
        loadGameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel topPane = new JPanel();
        JPanel bottomPane = new JPanel();
        BorderLayout layout = new BorderLayout();
        loadGameFrame.setSize(500, 300);
        this.game = game;
        this.game.setLoadGameGUI(this);
        File folder = new File(".");
        ArrayList<String> saves = new ArrayList<String>();
        Pattern pattern = Pattern.compile("(.*\\.class|.*\\.java|.*\\.git)");
        for (String file : folder.list()) {
            Matcher matcher = pattern.matcher(file);
            if (!matcher.matches()) {
                saves.add(file);
            }
        }
        String[] savesArray = new String[saves.size()];
        saves.toArray(savesArray);

        JComboBox<String> savesBox = new JComboBox<String>(savesArray);
        SavesButton savesButton = new SavesButton("Load Save", this.game);
        savesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String file = savesBox.getSelectedItem().toString();
                    loadGameFrame.dispose();
                    game.setFilename(file);
                    game.setParameters(parameters);
                    game.newGame();
                } catch (Exception exce) {

                }
            }
        });
        JLabel xLabel = new JLabel("Set x");
        JLabel yLabel = new JLabel("Set y");
        JLabel zLabel = new JLabel("Set z");
        String[] intChoices = new String[9];
        for (int i = 1; i <= 9; i++) {
            intChoices[i - 1] = Integer.toString(i);
        }
        JComboBox<String> xBox = new JComboBox<String>(intChoices);
        JComboBox<String> yBox = new JComboBox<String>(intChoices);
        JComboBox<String> zBox = new JComboBox<String>(intChoices);
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
        topPane.add(savesBox);
        topPane.add(xLabel);
        topPane.add(xBox);
        topPane.add(yLabel);
        topPane.add(yBox);
        topPane.add(zLabel);
        topPane.add(zBox);
        bottomPane.add(savesButton);
        loadGameFrame.add(BorderLayout.NORTH, topPane);
        loadGameFrame.add(BorderLayout.SOUTH, bottomPane);
        loadGameFrame.setVisible(true);

    }

    public void load(String file) throws Exception {
        this.game.loadGame(file);
    }
}

class SavesButton extends JButton implements Runnable {
    Game game;

    public SavesButton(String text, Game game) {
        this.setText(text);
        this.game = game;
    }

    public void run() {
        try {
            this.game.newGame();
        } catch (Exception e) {

        }
    }

}