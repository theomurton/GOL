import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class IntroGUI {
    private int[] parameters = new int[] { 4, 4, 1, 1, 1 };
    private Game game;

    public IntroGUI(Game game) {
        this.game = game;

        JFrame setup1Frame = new JFrame("GameOfLife");
        setup1Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setup1Frame.setSize(300, 300);

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        game.setState("new");
                        NewGameGUI newGUI = new NewGameGUI(game);
                        setup1Frame.dispose();
                    }
                });
        setup1Frame.getContentPane().add(newGameButton);
        JButton loadGameButton = new JButton("Load Game");

        loadGameButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        game.setState("load");
                        LoadGameGUI loadGUI = new LoadGameGUI(game);
                        setup1Frame.dispose();
                    }
                });
        setup1Frame.getContentPane().add(loadGameButton);
        setup1Frame.setLayout(new GridLayout(2, 1));
        // Adds Button to content pane of frame
        setup1Frame.getContentPane().add(loadGameButton);
        setup1Frame.setVisible(true);
    }

    public int[] getParameters() {
        return this.parameters;
    }
}
