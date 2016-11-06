package paging.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PagingModel {
  // There are eight frames in 4K of memory.
  public static final int NUMBER_FRAMES = 8;
  // The frame size in bytes.
  public static final int FRAME_SIZE = 512;
  // Since memory can hold only eight frames, and a process will have at minimum
  // 1 page, the maximum number of processes in memory at once will be eight.
  public static final int MAX_NUMBER_PROCESSES = 8;
  Scanner scanner;
  File eventFile;

  Frame[] memory;

  public static void main(String[] args) {
    PagingModel model = new PagingModel();
    while (model.processNextLine()) {

    }
  }

  public PagingModel() {
    memory = new Frame[8];
    for (int i = 0; i < NUMBER_FRAMES; i++) {
      memory[i] = new Frame();
    }

    eventFile = new File("input3a.data");
    try {
      scanner = new Scanner(eventFile);
    } catch (FileNotFoundException e) {
      System.out.println("There was a problem opening the file.");
    }
  }

  public boolean processNextLine() {
    if (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      System.out.println(line);
      return true;
    } else {
      return false;
    }
  }

  public void display() {

  }
}
