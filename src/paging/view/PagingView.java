package paging.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import paging.model.PCB;
import paging.model.PagingModel;

// This class creates the GUI to display the paging simulation
public class PagingView extends JPanel {
  private static final long serialVersionUID = 1L;
  // The file to open
  private static final String FILENAME = "input3a.data";
  // The size of the GUI
  private static final int GUI_SIZE = 800;
  // The indexes of the labels in the process list and memory
  private static final int PROCESS_NAME = 0;
  private static final int TEXT_SEG_SIZE = 1;
  private static final int TEXT_SEG_NUM_PAGES = 2;
  private static final int DATA_SEG_SIZE = 3;
  private static final int DATA_SEG_NUM_PAGES = 4;
  private static final int SEG_TYPE = 1;
  private static final int PAGE_NUM = 2;
  // The indexes for the memory panels
  private static final int LABEL_PANEL = 0;
  private static final int FRAME_PANEL = 1;
  // The components to create the GUI
  private JFrame gui;
  private JPanel eventPanel, containerPanel, nextActionPanel;
  private JPanel memoryContainerPanel;
  JPanel[] memoryFramePanel;
  private JPanel[] frameContentsPanel;
  private JLabel[][] frameLabels;
  private JPanel pcbContainerPanel;
  private JPanel[] pcbPanels;
  private JLabel[][] pcbLabels;
  private JButton nextEventButton;
  private JLabel eventPanelLabel;
  private JScrollPane pcbScrollPane;

  // The paging model instance to display
  PagingModel model;
  // The mapping of process id to display color
  private Map<Integer, Color> processColorsMapping;
  // The queue of colors not mapped to a process
  private Queue<Color> freeProcessColors;
  // The color to display free frames as
  private Color freeFrameColor;

  // The constructor for the GUI
  public PagingView() {
    // Create the panel to display the current event
    eventPanel = new JPanel();
    eventPanelLabel = new JLabel();
    eventPanel.add(eventPanelLabel);

    // Create the panel to display the current state of memory
    memoryContainerPanel = new JPanel(new BorderLayout(40, 10));
    memoryFramePanel = new JPanel[2];
    memoryFramePanel[LABEL_PANEL] = new JPanel(
        new GridLayout(PagingModel.MAX_NUMBER_PROCESSES + 1, 1, 5, 5));
    memoryFramePanel[FRAME_PANEL] = new JPanel(
        new GridLayout(PagingModel.MAX_NUMBER_PROCESSES + 1, 1, 5, 5));

    memoryFramePanel[LABEL_PANEL].add(new JLabel("Frame Number"));
    memoryFramePanel[FRAME_PANEL].add(new JLabel("Frame Contents"));

    frameContentsPanel = new JPanel[PagingModel.NUMBER_FRAMES];
    frameLabels = new JLabel[PagingModel.NUMBER_FRAMES][];

    for (int i = 0; i < PagingModel.NUMBER_FRAMES; i++) {
      JPanel temp = new JPanel();
      temp.add(new JLabel("Frame " + i + ":"));
      memoryFramePanel[LABEL_PANEL].add(temp);

      frameContentsPanel[i] = new JPanel(new GridLayout(3, 1));
      frameContentsPanel[i]
          .setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
      frameLabels[i] = new JLabel[3];
      frameLabels[i][PROCESS_NAME] = new JLabel("Process ");
      frameLabels[i][PROCESS_NAME].setHorizontalAlignment(JLabel.LEFT);
      frameLabels[i][SEG_TYPE] = new JLabel("Segment Type: ");
      frameLabels[i][SEG_TYPE].setHorizontalAlignment(JLabel.LEFT);
      frameLabels[i][PAGE_NUM] = new JLabel("Page Number: ");
      frameLabels[i][PAGE_NUM].setHorizontalAlignment(JLabel.LEFT);

      frameContentsPanel[i].add(frameLabels[i][PROCESS_NAME]);
      frameContentsPanel[i].add(frameLabels[i][SEG_TYPE]);
      frameContentsPanel[i].add(frameLabels[i][PAGE_NUM]);
      memoryFramePanel[FRAME_PANEL].add(frameContentsPanel[i]);
    }

    memoryContainerPanel.add(memoryFramePanel[LABEL_PANEL], BorderLayout.WEST);
    memoryContainerPanel.add(memoryFramePanel[FRAME_PANEL],
        BorderLayout.CENTER);

    // Create the panel that displays the processes
    pcbContainerPanel =
        new JPanel(new GridLayout(PagingModel.MAX_NUMBER_PROCESSES + 1, 1, 5, 5));
    pcbContainerPanel
        .setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    pcbContainerPanel.add(new JLabel("Current Procceses"));
    pcbPanels = new JPanel[PagingModel.MAX_NUMBER_PROCESSES];
    pcbLabels = new JLabel[PagingModel.MAX_NUMBER_PROCESSES][];
    pcbScrollPane = new JScrollPane(pcbContainerPanel,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    pcbScrollPane.setPreferredSize(new Dimension(300, 900));

    // Create the panel that holds the next button
    nextEventButton = new JButton();
    nextEventButton.setOpaque(true);
    nextEventButton.setText("Next Event");
    nextEventButton.addActionListener((ActionListener) EventHandler
        .create(ActionListener.class, this, "processNextEvent"));
    nextActionPanel = new JPanel();
    nextActionPanel.add(nextEventButton);
    memoryContainerPanel.add(nextActionPanel, BorderLayout.SOUTH);

    // Create the container panel
    containerPanel = new JPanel(new BorderLayout(30, 30));
    containerPanel.add(eventPanel, BorderLayout.NORTH);
    containerPanel.add(memoryContainerPanel, BorderLayout.CENTER);
    containerPanel.add(pcbScrollPane, BorderLayout.WEST);
    containerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Create the frame and display the GUI
    gui = new JFrame("Paging Sim");
    gui.setVisible(true);
    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gui.add(containerPanel);
    gui.pack();
    gui.setSize(GUI_SIZE, GUI_SIZE + 50);
    gui.setResizable(false);
    gui.setVisible(true);

    // Create the paging model
    model = new PagingModel(FILENAME);

    if ("ERROR".equals(model.getLastProcessedEvent())) {
      eventPanelLabel.setText("There was a problem opening the file.");
      nextEventButton.setEnabled(false);
    }

    // Create the list of free colors
    freeProcessColors = new LinkedList<Color>();

    // Ten possible process colors
    freeProcessColors.add(new Color(255, 96, 96)); // Red
    freeProcessColors.add(new Color(255, 157, 96)); // Orange
    freeProcessColors.add(new Color(255, 241, 96)); // Yellow
    freeProcessColors.add(new Color(96, 157, 255)); // Blue
    freeProcessColors.add(new Color(255, 96, 236)); // Pink
    freeProcessColors.add(new Color(96, 249, 255)); // light Blue
    freeProcessColors.add(new Color(183, 183, 183)); // Gray
    freeProcessColors.add(new Color(217, 94, 255)); // Purple
    freeProcessColors.add(new Color(0, 255, 149)); // Light teal
    freeProcessColors.add(Color.WHITE);

    // Create the mapping for processes to colors
    processColorsMapping = new HashMap<Integer, Color>();

    // Set the color used for a free frame
    freeFrameColor = new Color(89, 255, 86);
    display();
  }

  /**
   * A method that updates the display.
   */
  public final void display() {
    // Zero out the processes displayed
    for (int i = 0; i < pcbPanels.length; i++) {
      if (pcbPanels[i] != null) {
        pcbContainerPanel.remove(pcbPanels[i]);
        pcbPanels[i] = null;
      }
    }

    // Set the labels for the current processes
    int index = 0;
    for (int x : model.getPidSet()) {
      PCB temp = model.getPCB(x);
      pcbPanels[index] = new JPanel(new GridLayout(5, 1));
      pcbPanels[index]
          .setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
      pcbLabels[index] = new JLabel[5];
      pcbPanels[index]
          .setBackground((Color) processColorsMapping.get(temp.getPid()));
      for (int i = 0; i < 5; i++) {
        pcbLabels[index][i] = new JLabel("");
        pcbLabels[index][i].setHorizontalAlignment(JLabel.LEFT);
        pcbPanels[index].add(pcbLabels[index][i]);
      }
      pcbLabels[index][PROCESS_NAME].setText("Process: " + x);
      pcbLabels[index][TEXT_SEG_SIZE].setText(
          "Text Segment Size: " + temp.getTextSegmentSize() + " bytes");
      pcbLabels[index][TEXT_SEG_NUM_PAGES].setText(
          "Text Segment Number of Pages: " + temp.getNumPagesTextSegment());
      pcbLabels[index][DATA_SEG_SIZE]
          .setText("Data Segment Size: " + temp.getDataSegmentSize());
      pcbLabels[index][DATA_SEG_NUM_PAGES].setText(
          "Data Segment Number of Pages: " + temp.getNumPagesDataSegment());
      pcbContainerPanel.add(pcbPanels[index]);
      index++;
    }

    // Repaint the panel
    pcbContainerPanel.repaint();

    // Create a boolean array to track free frames
    boolean freeFrames[] = new boolean[PagingModel.NUMBER_FRAMES];
    for (int i = 0; i < PagingModel.NUMBER_FRAMES; i++) {
      freeFrames[i] = true;
    }

    // Set the labels and colors for the frames with pages in them
    for (int x : model.getPidSet()) {
      PCB temp = model.getPCB(x);
      // Set the text segment frames
      for (int i = 0; i < temp.getNumPagesTextSegment(); i++) {
        int frame = temp.getPageTableMapping(PCB.TEXT_SEGMENT, i);
        // Set the background color for the frame
        frameContentsPanel[frame]
            .setBackground(processColorsMapping.get(temp.getPid()));
        // Set the process number
        frameLabels[frame][0].setText(" Process " + temp.getPid());
        // Set the segment type
        frameLabels[frame][1].setText(" Segment Type: Text");
        // Set the page number
        frameLabels[frame][2].setText(" Page Number: " + i);
        // Mark the frame as taken
        freeFrames[frame] = false;
      }

      // Set the data segment frames
      for (int i = 0; i < temp.getNumPagesDataSegment(); i++) {
        int frame = temp.getPageTableMapping(PCB.DATA_SEGMENT, i);
        // Set the background color for the frame
        frameContentsPanel[frame]
            .setBackground(processColorsMapping.get(temp.getPid()));
        // Set the process number
        frameLabels[frame][0].setText(" Process " + temp.getPid());
        // Set the segment type
        frameLabels[frame][1].setText(" Segment Type: Data");
        // Set the page number
        frameLabels[frame][2].setText(" Page Number: " + i);
        // Mark the frame as taken
        freeFrames[frame] = false;
      }
    }
    // Set the contents of the free frames
    for (int i = 0; i < PagingModel.NUMBER_FRAMES; i++) {
      if (freeFrames[i]) {
        frameContentsPanel[i].setBackground(freeFrameColor);
        frameLabels[i][0].setText(" Free Frame");
        frameLabels[i][1].setText("");
        frameLabels[i][2].setText("");
      }
    }
  }

  // Method processes the next event and sets the message displayed to the user.
  public void processNextEvent() {
    // Check if there are more events in the file
    if (model.hasNextLine() == true) {
      String event = model.processNextLine();
      // Set the message displayed to the user
      String[] splitLine = event.split(" ");
      int pid = Integer.parseInt(splitLine[0]);
      if ("Halt".equals(splitLine[1])) {
        freeProcessColors.add((Color) processColorsMapping.remove(pid));
        eventPanelLabel.setText("Process " + pid
            + " has finished executing and has been removed from memory.");
      } else {
        processColorsMapping.put(pid, freeProcessColors.remove());
        eventPanelLabel.setText("Created Process " + pid + ".");
      }
      // Update the display
      display();
    } else {
      eventPanelLabel.setText("There are no more events to display.");
      nextEventButton.setEnabled(false);
    }
  }
}
