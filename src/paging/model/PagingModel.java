package paging.model;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class PagingModel {
  // There are eight frames in 4K of memory.
  public static final int NUMBER_FRAMES = 8;
  // The frame size in bytes.
  public static final int FRAME_SIZE = 512;
  // Assuming that the maximum number of processes in 10.
  public static final int MAX_NUMBER_PROCESSES = 10;
  String lastProcessedEvent;
  Scanner scanner;
  File eventFile;
  Queue<Integer> freeMemory;
  Queue<Color> freeProcessColors;
  Map<Integer, PCB> processes;
  Frame[] memory;

  public PagingModel(String filename) {
    memory = new Frame[8];
    freeMemory = new LinkedList<Integer>();
    processes = new HashMap<Integer, PCB>();

    for (int i = 0; i < NUMBER_FRAMES; i++) {
      memory[i] = new Frame();
    }

    for (int i = 0; i < 8; i++) {
      freeMemory.add(i);
    }

    eventFile = new File(filename);
    try {
      scanner = new Scanner(eventFile);
    } catch (FileNotFoundException e) {
      System.out.println("There was a problem opening the file.");
    }
    lastProcessedEvent = "";
  }

  public boolean hasNextLine() {
    return scanner.hasNextLine();
  }

  public String processNextLine() {
    if (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      System.out.println("Read line: " + line);
      String[] splitLine = line.split(" ");
      if ("Halt".equals(splitLine[1])) {
        terminateProcess(Integer.parseInt(splitLine[0]));
      } else {
        initializeProcess(Integer.parseInt(splitLine[0]), Integer.parseInt(splitLine[1]),
            Integer.parseInt(splitLine[2]));
      }
      display();
      return line;
    } else {
      return null;
    }
  }

  public void initializeProcess(int pid, int textSegmentSize, int dataSegmentSize) {
    System.out
        .println("Initializing: " + pid + " tss: " + textSegmentSize + " dss: " + dataSegmentSize);
    PCB tempPCB = new PCB(pid, textSegmentSize, dataSegmentSize);
    processes.put(pid, tempPCB);

    // Load the text segment pages into memory
    for (int i = 0; i < tempPCB.getNumPagesTextSegment(); i++) {
      int frameNumber = freeMemory.remove();
      tempPCB.setPageTableMapping(PCB.TEXT_SEGMENT, i, frameNumber);
      memory[frameNumber].setFrame(pid, PCB.TEXT_SEGMENT, i);
    }
    // Load the data segment pages into memory
    for (int i = 0; i < tempPCB.getNumPagesDataSegment(); i++) {
      int frameNumber = freeMemory.remove();
      tempPCB.setPageTableMapping(PCB.DATA_SEGMENT, i, frameNumber);
      memory[frameNumber].setFrame(pid, PCB.DATA_SEGMENT, i);
    }
  }

  public void terminateProcess(int pid) {
    System.out.println("Terminating: " + pid);
    PCB tempPCB = processes.remove(pid);
    // Free the frames that the text segment occupies
    for (int i = 0; i < tempPCB.getNumPagesTextSegment(); i++) {
      int frameNumber = tempPCB.getPageTableMapping(PCB.TEXT_SEGMENT, i);
      memory[frameNumber].freeFrame();
      freeMemory.add(frameNumber);
    }
    // Free the frames that the data segment occupies
    for (int i = 0; i < tempPCB.getNumPagesDataSegment(); i++) {
      int frameNumber = tempPCB.getPageTableMapping(PCB.DATA_SEGMENT, i);
      memory[frameNumber].freeFrame();
      freeMemory.add(frameNumber);
    }
  }

  public void display() {
    for (int i = 0; i < NUMBER_FRAMES; i++) {
      System.out.println("Frame " + i + " Process " + memory[i].getPid() + " Type "
          + memory[i].getSegmentType() + " Page Number " + memory[i].getPageNumber());
    }
  }

  public Set<Integer> getPidSet() {
    return processes.keySet();
  }

  public PCB getPCB(int pid) {
    return processes.get(pid);
  }

  public Frame getFrame(int index) {
    return memory[index];
  }
}
