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
import paging.model.Frame;
import paging.model.PagingModel;

public class PagingView extends JPanel {
  private static final long serialVersionUID = 1L;
  private static final int GUI_SIZE = 800;
  private static final int PROCESS_NAME = 0;
  private static final int TEXT_SEG_SIZE = 1;
  private static final int TEXT_SEG_NUM_PAGES = 2;
  private static final int DATA_SEG_SIZE = 3;
  private static final int DATA_SEG_NUM_PAGES = 4;
  private static final int LABEL_PANEL = 0;
  private static final int FRAME_PANEL = 1;
  private JFrame gui;
  private JPanel eventPanel, containerPanel, nextActionPanel;
  private JPanel memoryContainerPanel;
  JPanel[] memoryFramePanel;
  private JPanel[] frameContentsPanel;
  private JLabel[][] frameLabels;
  private JPanel pcbContainerPanel;
  private JScrollPane pcbScrollPane;
  private JPanel[] pcbPanels;
  private JLabel[][] pcbLabels;
  private JButton nextEventButton;
  private JLabel eventPanelLabel;

  PagingModel model;
  private Map<Integer, Color> processColorsMapping;
  private Queue<Color> freeProcessColors;
  private Color freeFrameColor;

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
      frameLabels[i][0] = new JLabel("Process ");
      frameLabels[i][0].setHorizontalAlignment(JLabel.LEFT);
      frameLabels[i][1] = new JLabel("Segment Type: ");
      frameLabels[i][1].setHorizontalAlignment(JLabel.LEFT);
      frameLabels[i][2] = new JLabel("Page Number: ");
      frameLabels[i][2].setHorizontalAlignment(JLabel.LEFT);

      frameContentsPanel[i].add(frameLabels[i][0]);
      frameContentsPanel[i].add(frameLabels[i][1]);
      frameContentsPanel[i].add(frameLabels[i][2]);
      memoryFramePanel[FRAME_PANEL].add(frameContentsPanel[i]);
    }

    memoryContainerPanel.add(memoryFramePanel[LABEL_PANEL], BorderLayout.WEST);
    memoryContainerPanel.add(memoryFramePanel[FRAME_PANEL],
        BorderLayout.CENTER);

    // Create the panel that displays the processes
    pcbContainerPanel =
        new JPanel(new GridLayout(PagingModel.MAX_NUMBER_PROCESSES + 1, 1));
    pcbContainerPanel
        .setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    pcbContainerPanel.setPreferredSize(new Dimension(200, 1000));
    pcbContainerPanel.add(new JLabel("Current Procceses"));
    pcbPanels = new JPanel[PagingModel.MAX_NUMBER_PROCESSES];
    pcbLabels = new JLabel[PagingModel.MAX_NUMBER_PROCESSES][];
    for (int i = 0; i < PagingModel.MAX_NUMBER_PROCESSES; i++) {
      pcbPanels[i] = new JPanel(new GridLayout(5, 1));
      pcbPanels[i]
          .setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
      pcbLabels[i] = new JLabel[5];
      for (int x = 0; x < 5; x++) {
        pcbLabels[i][x] = new JLabel("Blank");
        pcbLabels[i][x].setHorizontalAlignment(JLabel.LEFT);
        pcbPanels[i].add(pcbLabels[i][x]);
      }
      pcbContainerPanel.add(pcbPanels[i]);
    }
    pcbScrollPane = new JScrollPane(pcbContainerPanel,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    pcbScrollPane.setPreferredSize(new Dimension(300, 900));

    // Create the panel that holds the next button
    nextEventButton = new JButton();
    nextEventButton.setOpaque(true);
    nextEventButton.setText("Next Event");
    // nextEventButton.addActionListener(buttonListener);
    nextEventButton.addActionListener((ActionListener) EventHandler
        .create(ActionListener.class, this, "processNextEvent"));
    nextActionPanel = new JPanel();
    nextActionPanel.add(nextEventButton);
    memoryContainerPanel.add(nextActionPanel, BorderLayout.SOUTH);

    // Create the layout panel
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
    model = new PagingModel("input3a.data");

    // Create the color mapping
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

    processColorsMapping = new HashMap<Integer, Color>();

    freeFrameColor = new Color(89, 255, 86);
    display();
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
      pcbPanels[index]
          .setBackground((Color) processColorsMapping.get(temp.getPid()));
      pcbLabels[index][PROCESS_NAME].setText("Process: " + x);
      pcbLabels[index][TEXT_SEG_SIZE].setText(
          "Text Segment Size: " + temp.getTextSegmentSize() + " bytes");
      pcbLabels[index][TEXT_SEG_NUM_PAGES].setText(
          "Text Segment Number of Pages: " + temp.getNumPagesTextSegment());
      pcbLabels[index][DATA_SEG_SIZE]
          .setText("Data Segment Size: " + temp.getDataSegmentSize());
      pcbLabels[index][DATA_SEG_NUM_PAGES].setText(
          "Data Segment Number of Pages: " + temp.getNumPagesDataSegment());
      index++;
    }

    // Zero out the memory frames
    for (int i = 0; i < PagingModel.NUMBER_FRAMES; i++) {
      if (model.getFrame(i).isFree()) {
        frameContentsPanel[i].setBackground(freeFrameColor);
        frameLabels[i][0].setText(" Free Frame");
        frameLabels[i][1].setText("");
        frameLabels[i][2].setText("");
      } else {
        Frame temp = model.getFrame(i);
        frameContentsPanel[i]
            .setBackground(processColorsMapping.get(temp.getPid()));
        frameLabels[i][0].setText(" Process " + temp.getPid());
        if (temp.getSegmentType() == Frame.TEXT_SEGMENT) {
          frameLabels[i][1].setText(" Segment Type: Text");
        } else {
          frameLabels[i][1].setText(" Segment Type: Data");
        }
        frameLabels[i][2].setText(" Page Number: " + temp.getPageNumber());
      }
    }
  }

  public void processNextEvent() {
    if (model.hasNextLine() == true) {
      String event = model.processNextLine();
      String[] splitLine = event.split(" ");
      int pid = Integer.parseInt(splitLine[0]);
      if ("Halt".equals(splitLine[1])) {
        freeProcessColors.add((Color) processColorsMapping.remove(pid));
        eventPanelLabel.setText("Process " + pid
            + " has finished executing and has been removed from memory");
      } else {
        processColorsMapping.put(pid, freeProcessColors.remove());
        eventPanelLabel.setText("Created Process " + pid + ".");
      }
      display();
    } else {
      eventPanelLabel.setText("There are no more events to process.");
      nextEventButton.setEnabled(false);
    }
  }
}
