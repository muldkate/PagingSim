package paging.model;

public class Frame {
  public static final int FREE_MEMORY = -1;
  // The constant for the text segment.
  public static final int TEXT_SEGMENT = 0;
  // The constant for the data segment.
  public static final int DATA_SEGMENT = 1;

  int pid;
  int segmentType;
  int pageNumber;

  public Frame() {
    freeFrame();
  }

  public void freeFrame() {
    pid = FREE_MEMORY;
    segmentType = FREE_MEMORY;
    pageNumber = FREE_MEMORY;
  }

  public void setFrame(int pPid, int pSegmentType, int pPageNumber) {
    pid = pPid;
    segmentType = pSegmentType;
    pageNumber = pPageNumber;
  }

  public int getPid() {
    return pid;
  }

  public void setPid(int pid) {
    this.pid = pid;
  }

  public int getSegmentType() {
    return segmentType;
  }

  public void setSegmentType(int segmentType) {
    this.segmentType = segmentType;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }
  
  public boolean isFree() {
    return pid == FREE_MEMORY && segmentType == FREE_MEMORY && pageNumber == FREE_MEMORY;
  }
}
