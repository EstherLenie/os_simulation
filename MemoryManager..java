import java.util.ArrayList;
import java.util.HashMap;
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
    private int totalMemorySize;
    private int pageSize;
    private int osMemorySize;
    private List<MemoryPage> pages;
    private Map<Integer, List<MemoryPage>> frameTable;
    private int freeFrames;

    public MemoryManager(int totalMemorySize, int pageSize, int osMemorySize) {
        this.totalMemorySize = totalMemorySize;
        this.pageSize = pageSize;
        this.osMemorySize = osMemorySize;
        this.pages = new ArrayList<>();
        this.frameTable = new HashMap<>();
        this.freeFrames = (totalMemorySize - osMemorySize) / pageSize;

        int numPages = (totalMemorySize - osMemorySize) / pageSize;
        for (int i = 0; i < numPages; i++) {
            pages.add(new MemoryPage(i));
        }
    }

    public int getFreeFrames() {
        return freeFrames;
    }

    public boolean canAllocate(int numFrames) {
        return freeFrames >= numFrames;
    }

    public void allocateMemory(int processId, int programSize) {
        int numFrames = (int) Math.ceil((double) programSize / pageSize);

        if (canAllocate(numFrames)) {
            List<MemoryPage> allocatedFrames = new ArrayList<>();

            for (MemoryPage page : pages) {
                if (!page.isAllocated() && allocatedFrames.size() < numFrames) {
                    page.allocate(processId);
                    allocatedFrames.add(page);
                    freeFrames--;
                }
            }

            if (allocatedFrames.size() == numFrames) {
                frameTable.put(processId, allocatedFrames);
                System.out.println("Allocated " + numFrames + " frames for Process " + processId);
            } else {
                System.out.println("Insufficient memory for Process " + processId);
            }
        } else {
            System.out.println("Not enough free frames for Process " + processId);
        }
    }

    public void deallocateMemory(int processId) {
        if (frameTable.containsKey(processId)) {
            List<MemoryPage> allocatedFrames = frameTable.get(processId);
            for (MemoryPage page : allocatedFrames) {
                page.deallocate();
                freeFrames++;
            }
            frameTable.remove(processId);
            System.out.println("Deallocated memory for Process " + processId);
        } else {
            System.out.println("No memory allocated for Process " + processId);
        }
    }
}

public class MemorySimulation {
    public static void main(String[] args) {
        int totalMemorySize = 4096; // Total memory size in bytes
        int pageSize = 256; // Page size in bytes
        int osMemorySize = 512; // OS memory size in bytes

        MemoryManager memoryManager = new MemoryManager(totalMemorySize, pageSize, osMemorySize);

        // Simulate memory allocation and deallocation for processes
        System.out.println("Free Frames: " + memoryManager.getFreeFrames()); // Total free frames before allocation

        int programSize = 1024;
        int numFrames = (int) Math.ceil((double) programSize / pageSize);

        if (memoryManager.canAllocate(numFrames)) { // Check if there are enough free frames
            memoryManager.allocateMemory(1, programSize); // Allocate memory for Process 1 with program size
            System.out.println("Free Frames: " + memoryManager.getFreeFrames()); // Free frames after allocation
        }

        memoryManager.deallocateMemory(1); // Deallocate memory for Process 1
        System.out.println("Free Frames: " + memoryManager.getFreeFrames()); // Free frames after deallocation
    }
}