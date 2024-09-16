import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Hashtable;
import java.util.Iterator;

class MemoryManager {
    private int pageSize;
    private long memorySize;
    private Hashtable<Integer,MemoryPage> frames;
    private Hashtable<Integer, List<Integer>> frameTable;
    private LinkedList<Integer> freeFrames;

    public MemoryManager(long totalMemorySize, int pageSize, int osSize) {
        this.pageSize = pageSize;
        this.frames = new Hashtable<>();
        this.frameTable = new Hashtable<>();
        this.freeFrames = new LinkedList<>();
        this.memorySize = totalMemorySize;

        int numPages =(int) (totalMemorySize - (long)osSize) / pageSize;
        for (int i = 0; i < numPages; i++) {
            MemoryPage page = new MemoryPage(i);
            frames.put(i, page);
            freeFrames.add(i);
        }
    }

    public int getFreeMemory() {
        return freeFrames.size() * pageSize;
    }

    public long getMemorySize(){
        return this.memorySize;
    }

    public void loadProcessFrames(int pid){
        List<MemoryPage> processPages = OS.getProcessPages(pid);
        if(processPages==null){
            return;
        }
        List<Integer> availables = makeSpace(processPages.size());
        Iterator<Integer> pageIterator = availables.iterator();

        for (MemoryPage page : processPages){
            MemoryPage freeFrame = frames.get(pageIterator.next());
            freeFrame.allocate(pid);
        }
    }

    public boolean canAllocate(int numFrames) {
        return freeFrames.size() >= numFrames;
    }

    public int allocateMemory(int processId, int programSize) {
        int numFrames = (int) Math.ceil((double) programSize / pageSize);

        if (!canAllocate(numFrames)) {
            List<Integer> availables = makeSpace(numFrames);

            if (availables.size() != numFrames) {
                System.out.println("Insufficient memory for Process " + processId); 
                return 0;
            }

            reallocateFrames(availables, processId);
            return numFrames;
        }
        
        
        List<Integer> allocatedFrames = allocateFrames(numFrames, processId);
        if (allocatedFrames.size() != numFrames) {
            System.out.println("Insufficient memory for Process " + processId); 
            return 0;
        }
        frameTable.put(processId, allocatedFrames);
        System.out.println("Allocated " + numFrames + " frames for Process " + processId);
        return allocatedFrames.size();
    }

    private List<Integer> allocateFrames(int numFrames, int processId){
        List<Integer> allocatedFrames = new ArrayList<>();

        while (allocatedFrames.size() < numFrames) {
            int pageNumber = freeFrames.poll();
            MemoryPage page = frames.get(pageNumber);
            if (!page.isAllocated() ) {
                page.allocate(processId);
                allocatedFrames.add(page.getPageNumber());
            }
        }
        return allocatedFrames;
    }

    private void reallocateFrames(List<Integer> availables, int processId){
        for(Integer frameNumber : availables){
                MemoryPage frame = frames.get(frameNumber);

                OS.storePage(frame);
                if (frame.getProcessId() > 1){
                    frameTable.get(frame.getProcessId()).remove(frameNumber);
                }
                frame.deallocate();
                frame.allocate(processId);

                List<Integer> table = new ArrayList<Integer>();
                List<Integer> inMemoryFrames = frameTable.get(processId);
                if (inMemoryFrames != null){
                    table.addAll(inMemoryFrames);
                }
                table.addAll(availables);
                frameTable.put(processId, table);
            }
    }

    private List<Integer> makeSpace(int numFrames){
        int pid=0;
        List<Integer> availables = new ArrayList<Integer>(freeFrames);
        Iterator <Integer>keys = frameTable.keys().asIterator();

        while(keys.hasNext() && availables.size() < numFrames){
            pid = keys.next();
            if (OS.getProcessState(pid) != ProcessState.Ready)
                continue;

            Iterator<Integer> allocatedFrameNumbers = frameTable.get(pid).iterator();
            while(allocatedFrameNumbers.hasNext() && availables.size() < numFrames){
                int next = allocatedFrameNumbers.next();
                MemoryPage frame = frames.get(next);
                availables.add(frame.getPageNumber());
            }
        }
        return availables;
    }

    public void deallocateMemory(int processId) {
        if (!frameTable.containsKey(processId)) {
             System.out.println("No memory allocated for Process " + processId);
        }
        List<Integer> allocatedFrames = frameTable.get(processId);
        for (Integer pageNumber : allocatedFrames) {
            MemoryPage  frame = frames.get(pageNumber);
            frame.deallocate();
            freeFrames.add(frame.getPageNumber());
        }
        frameTable.remove(processId);
        System.out.println("Deallocated memory for Process " + processId);   
    }

    public long getUsedMemory(){
        return (frames.size() - freeFrames.size()) * pageSize;
    }
}