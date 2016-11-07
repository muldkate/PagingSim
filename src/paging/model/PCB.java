package paging.model;

public class PCB {
  public static final int TEXT_SEGMENT = 0;
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
    for(int i = 0; i<numPagesTextSegment; i++){
      pageTable[TEXT_SEGMENT][i] = -1;
    }
    for(int i = 0; i<numPagesDataSegment; i++){
      pageTable[DATA_SEGMENT][i] = -1;
    }
  }

  // Calculate the number of pages needed to store a segment of the given size.
  private int calculateNumberPages(int size) {
    return (size / PagingModel.FRAME_SIZE
        + ((size % PagingModel.FRAME_SIZE == 0) ? 0 : 1));
  }

  // A Setter for the page table mapping
  public void setPageTableMapping(int segment, int page, int frameMapping) {
    pageTable[segment][page] = frameMapping;
  }

  // A Setter for the page table mapping
  public int getPageTableMapping(int segment, int page) {
    return pageTable[segment][page];
  }

  public int getPid() {
    return pid;
  }

  public void setPid(int pid) {
    this.pid = pid;
  }

  public int getTextSegmentSize() {
    return textSegmentSize;
  }

  public void setTextSegmentSize(int textSegmentSize) {
    this.textSegmentSize = textSegmentSize;
  }

  public int getDataSegmentSize() {
    return dataSegmentSize;
  }

  public void setDataSegmentSize(int dataSegmentSize) {
    this.dataSegmentSize = dataSegmentSize;
  }

  public int getNumPagesTextSegment() {
    return numPagesTextSegment;
  }

  public void setNumPagesTextSegment(int numPagesTextSegment) {
    this.numPagesTextSegment = numPagesTextSegment;
  }

  public int getNumPagesDataSegment() {
    return numPagesDataSegment;
  }

  public void setNumPagesDataSegment(int numPagesDataSegment) {
    this.numPagesDataSegment = numPagesDataSegment;
  }
}
