package paging.model;

// This class represents a process control block
public class PCB {
  // The index of the text segment
  public static final int TEXT_SEGMENT = 0;
  // The index of the data segment
  public static final int DATA_SEGMENT = 1;
  // The process ID.
  private int pid;
  // The process' text segment size.
  private int textSegmentSize;
  // The process' data segment size.
  private int dataSegmentSize;
  // The number of pages in the text segment.
  private int numPagesTextSegment;
  // The number of pages in the data segment.
  private int numPagesDataSegment;
  // The process' page table. The first index is the segment, the second the
  // page number, and the value is the frame it maps to in memory.
  private int[][] pageTable;

  // A constructor for a PCB
  public PCB(int pPid, int pTextSegmentSize, int pDataSegmentSize) {
    pid = pPid;
    textSegmentSize = pTextSegmentSize;
    dataSegmentSize = pDataSegmentSize;
    numPagesTextSegment = calculateNumberPages(pTextSegmentSize);
    numPagesDataSegment = calculateNumberPages(pDataSegmentSize);
    pageTable = new int[2][];
    pageTable[TEXT_SEGMENT] = new int[numPagesTextSegment];
    pageTable[DATA_SEGMENT] = new int[numPagesDataSegment];
    // Initialize the mappings to -1
    for (int i = 0; i < numPagesTextSegment; i++) {
      pageTable[TEXT_SEGMENT][i] = -1;
    }
    for (int i = 0; i < numPagesDataSegment; i++) {
      pageTable[DATA_SEGMENT][i] = -1;
    }
  }

  // Calculate the number of pages needed to store a segment of the given size.
  private int calculateNumberPages(int size) {
    // If the size does not evenly divide by the frame size, add another page to
    // hold the extra bytes
    return (size / PagingModel.FRAME_SIZE
        + ((size % PagingModel.FRAME_SIZE == 0) ? 0 : 1));
  }

  // A setter for a page table mapping
  public void setPageTableMapping(int segment, int page, int frameMapping) {
    pageTable[segment][page] = frameMapping;
  }

  // A getter for a page table mapping
  public int getPageTableMapping(int segment, int page) {
    return pageTable[segment][page];
  }

  // A getter for the pid
  public int getPid() {
    return pid;
  }

  // A getter for the text segment size
  public int getTextSegmentSize() {
    return textSegmentSize;
  }

  // A getter for the data segment size
  public int getDataSegmentSize() {
    return dataSegmentSize;
  }

  // A getter for the number of pages in the text segment
  public int getNumPagesTextSegment() {
    return numPagesTextSegment;
  }

  // A getter for the pages in the data segment
  public int getNumPagesDataSegment() {
    return numPagesDataSegment;
  }
}
