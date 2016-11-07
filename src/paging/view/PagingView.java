package paging.view;

import paging.model.Frame;
import paging.model.PCB;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PagingView extends JPanel {
  private static final long serialVersionUID = 1L;
  private static final int GUI_SIZE = 600;
  private JFrame gui;
  private JPanel eventPanel;
  private JButton nextEventButton;
  private ActionListener buttonListener;
  private JPanel containerPanel;
  private Container nextActionPanel;
  private JLabel eventPanelLabel;
  private JPanel framePanel;

  public PagingView(final Frame memory, final Map<Integer, PCB> processes) {
    gui = new JFrame("PagingSim");
    gui.setVisible(true);
    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    framePanel = new JPanel(new GridLayout(8, 2));
    for(int i = 0; i<8; i++){
      framePanel.add(new JLabel("Frame " + i + ":"));
      
    }
    

    eventPanel = new JPanel();
    eventPanelLabel = new JLabel();
    eventPanel.add(eventPanelLabel);

    nextEventButton = new JButton();
    nextEventButton.setOpaque(true);
    nextEventButton.setText("Next Event");
    nextEventButton.addActionListener(buttonListener);
    nextActionPanel.add(nextEventButton);

    containerPanel = new JPanel(new BorderLayout());
    containerPanel.add(eventPanel, BorderLayout.NORTH);
    containerPanel.add(nextActionPanel, BorderLayout.SOUTH);

    gui.add(containerPanel);
    gui.pack();
    gui.setSize(GUI_SIZE, GUI_SIZE);
    gui.setVisible(true);
  }

  /**
   * A method that updates the board.
   * 
   * @param lgame
   *          The ludo model to display
   */
  public final void display(final Frame memory,
      final Map<Integer, PCB> processes) {

  }

  /**
   * Set the message on the bottom of the GUI.
   * 
   * @param playerName
   *          The player whose turn it is
   * @param diceRoll
   *          The value of the dice roll
   */
  public final void setEventMessage(String message) {
    eventPanelLabel.setText(message);
  }

  /**
   * An inner class to handle events.
   * 
   * @author Katie Mulder
   *
   */
  private static class ButtonListener implements ActionListener {
    /**
     * The event handler.
     * 
     * @param e
     *          The action event
     */
    public void actionPerformed(final ActionEvent e) {

    }
  }

}
