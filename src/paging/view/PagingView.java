package paging.view;

import paging.model.Frame;
import paging.model.PCB;
import paging.model.PagingModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

public class PagingView extends JPanel {
  private static final long serialVersionUID = 1L;
  private static final int GUI_SIZE = 900;
  private static final int PROCESS_NAME = 0;
  private static final int TEXT_SEG_SIZE = 1;
  private static final int TEXT_SEG_NUM_PAGES = 2;
  private static final int DATA_SEG_SIZE = 3;
  private static final int DATA_SEG_NUM_PAGES = 4;
  private JFrame gui;
  private JPanel eventPanel, containerPanel, nextActionPanel;
  private JPanel memoryContainerPanel;
  private JPanel memoryFramePanel, memoryPagePanel;
  private JLabel memoryFrameLabel, memoryPageLabel;
  private JPanel[] memoryFrameContentsPanel;
  private JLabel[][] memoryFrameContentsLabel;
  private JPanel[] memoryFrameLabelPanel;
  private JPanel pcbContainerPanel;
  private JScrollPane pcbScrollPane;
  private JPanel[] pcbPanels;
  private JLabel[][] pcbLabels;
  private JButton nextEventButton;
  private ActionListener buttonListener;
  private JLabel eventPanelLabel;
  
  PagingModel model;
  private Map<Integer, Color> processColorsMapping;
  private Queue<Color> freeProcessColors;
  

  public PagingView() {
    // Create the frame
    gui = new JFrame("PagingSim");
    gui.setVisible(true);
    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Create the panel to display the current event
    eventPanel = new JPanel();
    eventPanelLabel = new JLabel();
    eventPanel.add(eventPanelLabel);

    // Create the panel to display the current state of memory
    memoryContainerPanel = new JPanel(new GridLayout(PagingModel.NUMBER_FRAMES + 1, 2));
    memoryFramePanel = new JPanel();
    memoryFrameLabel = new JLabel("Frame");
    memoryFramePanel.add(memoryFrameLabel);
    memoryPagePanel = new JPanel();
    memoryPageLabel = new JLabel("Frame Contents");
    memoryPagePanel.add(memoryPageLabel);
    memoryContainerPanel.add(memoryFramePanel);
    memoryContainerPanel.add(memoryPagePanel);

    memoryFrameLabelPanel = new JPanel[PagingModel.NUMBER_FRAMES];
    memoryFrameContentsPanel = new JPanel[PagingModel.NUMBER_FRAMES];
    memoryFrameContentsLabel = new JLabel[PagingModel.NUMBER_FRAMES][];

    for (int i = 0; i < PagingModel.NUMBER_FRAMES; i++) {
      memoryFrameLabelPanel[i] = new JPanel();
      memoryFrameLabelPanel[i].add(new JLabel("Frame " + i + ": "));
      memoryContainerPanel.add(memoryFrameLabelPanel[i]);

      memoryFrameContentsPanel[i] = new JPanel(new GridLayout(3,1));
      memoryFrameContentsPanel[i].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
      memoryFrameContentsLabel[i] = new JLabel[3];
      memoryFrameContentsLabel[i][0] = new JLabel("Process ");
      memoryFrameContentsLabel[i][0].setHorizontalAlignment(JLabel.LEFT);
      memoryFrameContentsLabel[i][1] = new JLabel("Segment Type: ");
      memoryFrameContentsLabel[i][1].setHorizontalAlignment(JLabel.LEFT);
      memoryFrameContentsLabel[i][2] = new JLabel("Page Number: ");
      memoryFrameContentsLabel[i][2].setHorizontalAlignment(JLabel.LEFT);

      memoryFrameContentsPanel[i].add(memoryFrameContentsLabel[i][0]);
      memoryFrameContentsPanel[i].add(memoryFrameContentsLabel[i][1]);
      memoryFrameContentsPanel[i].add(memoryFrameContentsLabel[i][2]);
      memoryContainerPanel.add(memoryFrameContentsPanel[i]);
    }

    // Create the panel that displays the processes
    pcbContainerPanel = new JPanel(new GridLayout(PagingModel.MAX_NUMBER_PROCESSES + 1, 1));
    pcbContainerPanel.setPreferredSize(new Dimension(200, 1000));
    pcbContainerPanel.add(new JLabel("Current Procceses"));
    pcbPanels = new JPanel[PagingModel.MAX_NUMBER_PROCESSES];
    pcbLabels = new JLabel[PagingModel.MAX_NUMBER_PROCESSES][];
    for (int i = 0; i < PagingModel.MAX_NUMBER_PROCESSES; i++) {
      pcbPanels[i] = new JPanel(new GridLayout(5, 1));
      pcbPanels[i].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
      pcbLabels[i] = new JLabel[5];
      for (int x = 0; x < 5; x++) {
        pcbLabels[i][x] = new JLabel("Blank");
        pcbLabels[i][x].setHorizontalAlignment(JLabel.LEFT);
        pcbPanels[i].add(pcbLabels[i][x]);
      }
      pcbContainerPanel.add(pcbPanels[i]);
    }
    pcbScrollPane = new JScrollPane(pcbContainerPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    // Create the panel that holds the next button
    nextEventButton = new JButton();
    nextEventButton.setOpaque(true);
    nextEventButton.setText("Next Event");
    nextEventButton.addActionListener(buttonListener);
    nextActionPanel = new JPanel();
    nextActionPanel.add(nextEventButton);

    // Create the layout panel
    containerPanel = new JPanel(new BorderLayout());
    containerPanel.add(eventPanel, BorderLayout.NORTH);
    containerPanel.add(nextActionPanel, BorderLayout.SOUTH);
    containerPanel.add(memoryContainerPanel, BorderLayout.CENTER);
    containerPanel.add(pcbScrollPane, BorderLayout.WEST);

    // Display the GUI
    gui.add(containerPanel);
    gui.pack();
    gui.setSize(GUI_SIZE, GUI_SIZE);
    gui.setVisible(true);

    // Create the paging model
    model = new PagingModel("input3a.data");
    
    // Create the color mapping
    freeProcessColors = new LinkedList<Color>();

    freeProcessColors.add(new Color(255, 96, 96)); // Red
    freeProcessColors.add(new Color(255, 157, 96)); // Orange
    freeProcessColors.add(new Color(255, 241, 96)); // Yellow
    freeProcessColors.add(new Color(133, 255, 96)); // Green
    freeProcessColors.add(new Color(96, 157, 255)); // Blue
    freeProcessColors.add(new Color(255, 96, 236)); // Pink
    freeProcessColors.add(new Color(96, 249, 255)); // light Blue
    freeProcessColors.add(new Color(183, 183, 183)); // Gray
    freeProcessColors.add(new Color(217, 94, 255)); // Purple
    freeProcessColors.add(Color.WHITE);
    
    processColorsMapping = new HashMap<Integer, Color>();
  }

  /**
   * A method that updates the display.
   */
  public final void display() {
    // Zero out the processes displayed
    for (int i = 0; i < pcbPanels.length; i++) {
      pcbPanels[i].setVisible(false);
    }
    int index = 0;
    for (int x : model.getPidSet()) {
      PCB temp = model.getPCB(x);
      pcbPanels[index].setVisible(true);
      pcbLabels[index][PROCESS_NAME].setText("Process: " + x);
      pcbLabels[index][TEXT_SEG_SIZE]
          .setText("Text Segment Size: " + temp.getTextSegmentSize() + " bytes");
      pcbLabels[index][TEXT_SEG_NUM_PAGES]
          .setText("Text Segment Number of Pages: " + temp.getNumPagesTextSegment());
      pcbLabels[index][DATA_SEG_SIZE].setText("Data Segment Size: " + temp.getDataSegmentSize());
      pcbLabels[index][DATA_SEG_NUM_PAGES]
          .setText("Data Segment Number of Pages: " + temp.getNumPagesDataSegment());
      index++;
    }
    for (int i = 0; i < PagingModel.NUMBER_FRAMES; i++) {
        model.getFrame(i).getPid();
        //TODO: fin
        if( model.getFrame(i).getSegmentType() == PCB.TEXT_SEGMENT) {
          
        }
        model.getFrame(i).getSegmentType();
        model.getFrame(i).getPageNumber();
    }
  }
  
  public void processNextEvent() {
    String event = model.processNextLine();
    String[] splitLine = event.split(" ");
    int pid = Integer.parseInt(splitLine[0]);
    if("Halt".equals(splitLine[1])) {
      freeProcessColors.add((Color)processColorsMapping.remove(pid));
      setEventMessage("Process " + pid + " has finished executing and has been removed from memory");
    } else {
      processColorsMapping.put(pid, freeProcessColors.remove());
      setEventMessage("Created Process " + pid +".");
    }
    display();
  }

  /**
   * Set the message on the top of the GUI.
   * 
   * @param playerName The player whose turn it is
   * @param diceRoll The value of the dice roll
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
     * @param e The action event
     */
    public void actionPerformed(final ActionEvent e) {

    }
  }

}
