import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

class MemoryManager {
    private int pageSize;
    private LinkedList<MemoryPage> pages;
    private Map<Integer, List<MemoryPage>> frameTable;

    private int freeFrames = 0;

    public MemoryManager(int totalMemorySize, int pageSize, int osSize) {
        this.pageSize = pageSize;
        this.pages = new LinkedList<>();
        this.frameTable = new HashMap<>();



        int numPages = (totalMemorySize - osSize) / pageSize;
        for (int i = 0; i < numPages; i++) {
            pages.add(new MemoryPage(i));
            freeFrames++;
        }
    }

    public int getFreeFrames() {
        return freeFrames;
    }

    public boolean canAllocate(int numFrames) {
        return freeFrames >= numFrames;
    }

    public int allocateMemory(int processId, int programSize) {
        int numFrames = (int) Math.ceil((double) programSize / pageSize);

        if (!canAllocate(numFrames)) {
             System.out.println("Not enough free frames for Process " + processId);
             return 0;
        }
        
        List<MemoryPage> allocatedFrames = new ArrayList<>();
        while (freeFrames > 0 && allocatedFrames.size() < numFrames) {
            MemoryPage page = pages.poll();
            if (!page.isAllocated() ) {
                page.allocate(processId);
                allocatedFrames.add(page);
                freeFrames--;
            }
        }

        if (allocatedFrames.size() != numFrames) {
            System.out.println("Insufficient memory for Process " + processId); 
            return 0;
        }
        
        frameTable.put(processId, allocatedFrames);
        System.out.println("Allocated " + numFrames + " frames for Process " + processId);
        return allocatedFrames.size();
    }

    public void deallocateMemory(int processId) {
        if (!frameTable.containsKey(processId)) {
             System.out.println("No memory allocated for Process " + processId);
        }
            List<MemoryPage> allocatedFrames = frameTable.get(processId);
            for (MemoryPage page : allocatedFrames) {
                page.deallocate();
                freeFrames++;
            }
            frameTable.remove(processId);
            System.out.println("Deallocated memory for Process " + processId);
       
    }
}