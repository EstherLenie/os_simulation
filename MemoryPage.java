class MemoryPage {
    private int pageNumber;
    private int processId;

    public MemoryPage(int pageNumber) {
        this.pageNumber = pageNumber;
        this.processId = -1;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getProcessId() {
        return processId;
    }

    public void allocate(int processId) {
        this.processId = processId;
    }

    public void deallocate() {
        this.processId = -1;
    }

    public boolean isAllocated() {
        return processId != -1;
    }
}
