package com.github.themadscientist.periodicguesser;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

public class Game extends JFrame implements ActionListener {
    public static final long serialVersionUID = 2367829;

    private JLabel atomicNumberLabel;
    private JLabel resultLabel;
    private JLabel strikesLabel;
    private JLabel hintLabel;
    private JLabel gameOverLabel;

    private JTextField guessField;
    private int guessesLeft = 5;

    private JButton submitButton;
    private JButton getNewElementButton;
    private JButton restartButton;

    private Timer hideTimer;
    private boolean canSubmit = true;
    private boolean gameOver = false;

    private Elements elements;
    private Random random;

    private void getNewElement() {
        atomicNumberLabel.setText(String.valueOf(random.nextInt(118) + 1));
    }

    private void generateHint(String guess) {
        if(elements.elementMap.containsValue(guess)) {
            for(Map.Entry<Integer, String> entry : elements.elementMap.entrySet()) {
                if(entry.getValue().equals(guess)) {
                    if(Integer.valueOf(atomicNumberLabel.getText()) < entry.getKey()) {
                        hintLabel.setText("Hint: The correct element is to the left of the entered one.");
                    } else {
                        hintLabel.setText("Hint: The correct element is to the right of the entered one.");
                    }
                }
            }
        } else {
            hintLabel.setText(guess + " is not on the periodic table!");
        }
        hintLabel.setVisible(true);
    }

    public Game() {
        elements = new Elements();
        try {
            elements.genElementMap();
        } catch(Exception e) {
            e.printStackTrace();
        }

        random = new Random();

        setTitle("Periodic Guesser");
        setSize(800, 640);
        setMinimumSize(new Dimension(512, 10));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel elementPanel = new JPanel();
        elementPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.LINE_AXIS));

        atomicNumberLabel = new JLabel("77");
        atomicNumberLabel.setFont(new Font("Serif", Font.PLAIN, 32));
        elementPanel.add(atomicNumberLabel);

        resultLabel = new JLabel("Correct!");
        resultLabel.setVisible(false);
        resultLabel.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        elementPanel.add(resultLabel);

        add(elementPanel, BorderLayout.NORTH);

        guessField = new JTextField("");
        guessField.setColumns(15);
        inputPanel.add(guessField);

        submitButton = new JButton("Make Guess");
        submitButton.addActionListener(this);
        inputPanel.add(submitButton);

        getNewElementButton = new JButton("Get New Element");
        getNewElementButton.addActionListener(this);
        inputPanel.add(getNewElementButton);

        restartButton = new JButton("Restart");
        restartButton.addActionListener(this);
        inputPanel.add(restartButton);

        add(inputPanel);

        JPanel strikesPanel = new JPanel();
        strikesPanel.setLayout(new BoxLayout(strikesPanel, BoxLayout.LINE_AXIS));

        strikesLabel = new JLabel("Guesses left: " + String.valueOf(guessesLeft));
        strikesLabel.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        strikesPanel.add(strikesLabel);

        hintLabel = new JLabel("Hint: The correct element is to the left of the one entered.");
        hintLabel.setVisible(false);
        strikesPanel.add(hintLabel);

        add(strikesPanel, BorderLayout.SOUTH);

        gameOverLabel = new JLabel("Game Over!");
        gameOverLabel.setVisible(false);
        elementPanel.add(gameOverLabel);

        hideTimer = new Timer(1500, this);

        getNewElement();

        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(submitButton) && !gameOver) {
            if(!guessField.getText().equalsIgnoreCase("") && canSubmit) {
                if(guessField.getText().equals(elements.elementMap.get(Integer.valueOf(atomicNumberLabel.getText())))) {
                    resultLabel.setText("Correct!");
                } else {
                    resultLabel.setText("Wrong!");
                    guessesLeft--;
                    if(guessesLeft <= 0) {
                        gameOver = true;
                        gameOverLabel.setVisible(true);
                    }
                    strikesLabel.setText("Guesses left: " + String.valueOf(guessesLeft));
                    generateHint(guessField.getText());
                    hintLabel.setVisible(true);
                }
                resultLabel.setVisible(true);
                guessField.setText("");
                hideTimer.start();
                canSubmit = false;
            }
        }
        if(e.getSource().equals(getNewElementButton) && !gameOver) {
            getNewElement();
            hintLabel.setVisible(false);
        }
        if(e.getSource().equals(restartButton)) {
            gameOver = false;
            gameOverLabel.setVisible(false);

            hintLabel.setVisible(false);
            getNewElement();

            guessesLeft = 5;
            strikesLabel.setText("Guesses left: 5");
        }
        if(e.getSource().equals(hideTimer)) {
            if(resultLabel.getText().equals("Correct!")) {
                getNewElement();
            }
            hideTimer.stop();
            resultLabel.setVisible(false);
            canSubmit = true;
        }
    }

}
