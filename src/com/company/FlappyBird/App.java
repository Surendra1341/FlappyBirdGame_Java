package com.company.FlappyBird;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        int boardWidth = 360;
        int boardHeight = 640;

        JFrame frame = new JFrame("Flappy bird");
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);

        FlappyGame game = new FlappyGame();
        frame.add(game);
        frame.pack();
        game.requestFocus();
        frame.setVisible(true);
    }
}
