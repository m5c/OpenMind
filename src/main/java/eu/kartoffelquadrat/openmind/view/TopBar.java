package eu.kartoffelquadrat.openmind.view;

import eu.kartoffelquadrat.openmind.controller.CombinationTool.CombinationException;
import eu.kartoffelquadrat.openmind.controller.OpenMind;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import eu.kartoffelquadrat.openmind.model.GameCharacteristic;

/**
 * Populates the top bar.
 *
 * @author Maximilian Schiedermeier, 2012
 */
public class TopBar extends JPanel {

    private JComboBox colourCounter;
    private JComboBox pebbleCounter;
    private JCheckBox repetitions;

    /**
     * This constructor is to called only on the first initiation of the GUI.
     *
     * @param defaultCharacteristic as the Characteristics defines as default by the main method
     */
    public TopBar(GameCharacteristic defaultCharacteristic) {
        setBackground(Color.gray);
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        //add button
        JButton resetButton = new JButton("Start over...");
        resetButton.addActionListener(new RestartListener());
        add(resetButton);

        //glue
        add(Box.createGlue());

        //add checkbox for doubles
        repetitions = new JCheckBox("Duplicates", defaultCharacteristic.isWithRepetitions());
        repetitions.addActionListener(new SettingListener());
        add(repetitions);

        //add dropdown for colours
        colourCounter = new JComboBox(new String[]
                {
                        "4 Colours", "6 Colours", "8 Colours"
                });
        colourCounter.setPreferredSize(new Dimension(120, 0));
        colourCounter.setSelectedIndex(colourCountToIndex(defaultCharacteristic.getNumberOfColours()));
        colourCounter.addActionListener(new SettingListener());
        add(colourCounter);

        //add dropdown for number of pebbles
        pebbleCounter = new JComboBox(new String[]
                {
                        "3 Böppels", "4 Böppels", "5 Böppels"
                });
        pebbleCounter.setPreferredSize(new Dimension(120, 0));
        pebbleCounter.setSelectedIndex(pebbleCountToIndex(defaultCharacteristic.getNumberOfPebbles()));
        pebbleCounter.addActionListener(new SettingListener());
        add(pebbleCounter);

        //glue
        add(Box.createGlue());

        //add about button
        JButton aboutButton = new JButton("?");
        aboutButton.addActionListener(new HelpListener());
        add(aboutButton);

        //glue
        add(Box.createGlue());

        //add quit button
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(new QuitListener());
        add(quitButton);
    }

    private GameCharacteristic getCurrentSettings() {
        int numberOfColours = 4 + 2 * colourCounter.getSelectedIndex();
        int numberOfPebbles = 3 + pebbleCounter.getSelectedIndex();
        boolean reps = repetitions.isSelected();
        GameCharacteristic nextSettings = new GameCharacteristic(numberOfColours, numberOfPebbles, reps, OpenMind.limit);

        return nextSettings;
    }

    private int colourCountToIndex(int numberOfColours) {
        if (numberOfColours == 4) {
            return 0;
        }
        if (numberOfColours == 6) {
            return 1;
        }
        if (numberOfColours == 8) {
            return 2;
        }

        throw new RuntimeException("Someone messed up my GUI! - Maex");
    }

    private int pebbleCountToIndex(int numberOfPebbles) {
        if (numberOfPebbles == 3) {
            return 0;
        }
        if (numberOfPebbles == 4) {
            return 1;
        }
        if (numberOfPebbles == 5) {
            return 2;
        }

        throw new RuntimeException("Someone messed up my GUI! - Maex");
    }

    /**
     * generates a game-characteristic object and sends it to the controller gets parameters for next game directly from
     * gui.
     */
    private class RestartListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            try {
                OpenMind.startGame();
            } catch (CombinationException ex) {
                throw new RuntimeException("Invalid settings occurred althoug test did detect them.");
            }
        }

    }

    private class SettingListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            OpenMind.getGameState().modifyNextGameCharacteristic(getCurrentSettings());
        }
    }

    private class HelpListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            JOptionPane.showMessageDialog(null, OpenMind.aboutMessage, "About", JOptionPane.INFORMATION_MESSAGE, null);
        }

    }

    private class QuitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            OpenMind.quitOpenMind();
        }

    }
}
