package paging.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

// This class simulates paging based on an input file
public class PagingModel {
  // There are eight frames in 4K of memory.
  public static final int NUMBER_FRAMES = 8;
  // The frame size in bytes.
  public static final int FRAME_SIZE = 512;
  // Assumption: the maximum number of processes in memory at a time is 10.
  public static final int MAX_NUMBER_PROCESSES = 10;
  // The last string read from the file and processed
  String lastProcessedEvent;
  Scanner scanner;
  // The file holding the events
  File eventFile;
  // The queue holds a list of the free slots in memory
  Queue<Integer> freeMemory;
  // The map of process id's to the process's control block
  Map<Integer, PCB> processes;

  // A constructor for a paging model
  // filename: the name of the file containing the events to process
  public PagingModel(String filename) {
    freeMemory = new LinkedList<Integer>();
    processes = new HashMap<Integer, PCB>();

    // Initialize memory as having all free frames
    for (int i = 0; i < 8; i++) {
      freeMemory.add(i);
    }

    // Open the file
    eventFile = new File(filename);
    try {
      scanner = new Scanner(eventFile);
      lastProcessedEvent = "";
    } catch (FileNotFoundException e) {
      System.out.println("There was a problem opening the file.");
      lastProcessedEvent = "ERROR";
    }
  }

  // Method returns true if there is another line to process
  public boolean hasNextLine() {
    return scanner.hasNextLine();
  }

  // Read the next line of the file and process the event
  public String processNextLine() {
    if (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      System.out.println("Read line: " + line);
      String[] splitLine = line.split(" ");
      if ("Halt".equals(splitLine[1])) {
        terminateProcess(Integer.parseInt(splitLine[0]));
      } else {
        initializeProcess(Integer.parseInt(splitLine[0]),
            Integer.parseInt(splitLine[1]), Integer.parseInt(splitLine[2]));
      }
      return line;
    } else {
      return null;
    }
  }

  // Create a PCB with the parameter values and map the segments into memory
  public void initializeProcess(int pid, int textSegmentSize,
      int dataSegmentSize) {
    System.out.println("Initializing: " + pid + " tss: " + textSegmentSize
        + " dss: " + dataSegmentSize);
    PCB tempPCB = new PCB(pid, textSegmentSize, dataSegmentSize);
    
    // Place the PCB in the processes map
    processes.put(pid, tempPCB);

    // Load the text segment pages into memory
    for (int i = 0; i < tempPCB.getNumPagesTextSegment(); i++) {
      int frameNumber = freeMemory.remove();
      tempPCB.setPageTableMapping(PCB.TEXT_SEGMENT, i, frameNumber);
    }
    // Load the data segment pages into memory
    for (int i = 0; i < tempPCB.getNumPagesDataSegment(); i++) {
      int frameNumber = freeMemory.remove();
      tempPCB.setPageTableMapping(PCB.DATA_SEGMENT, i, frameNumber);
    }
  }

  // Remove a process and free the frames that it occupies
  public void terminateProcess(int pid) {
    System.out.println("Terminating: " + pid);
    PCB tempPCB = processes.remove(pid);
    // Free the frames that the text segment occupies
    for (int i = 0; i < tempPCB.getNumPagesTextSegment(); i++) {
      int frameNumber = tempPCB.getPageTableMapping(PCB.TEXT_SEGMENT, i);
      freeMemory.add(frameNumber);
    }
    // Free the frames that the data segment occupies
    for (int i = 0; i < tempPCB.getNumPagesDataSegment(); i++) {
      int frameNumber = tempPCB.getPageTableMapping(PCB.DATA_SEGMENT, i);
      freeMemory.add(frameNumber);
    }
  }

  // Return a set of the process ids
  public Set<Integer> getPidSet() {
    return processes.keySet();
  }

  // Return the PCB of a process
  public PCB getPCB(int pid) {
    return processes.get(pid);
  }

  // Return the last line of the file that was processed
  public String getLastProcessedEvent() {
    return lastProcessedEvent;
  }
}
