import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class MassStorage {
    private long capacity;  
    private long usedSpace; 
    private List<File> files = new ArrayList<>();
    private HashMap<Integer, List<MemoryPage>> pages = new HashMap<>();

    public MassStorage(long capacity) {
        this.capacity = capacity;
        this.usedSpace = 0;
    }

    public long getCapacity() {
        return capacity;
    }

    public long getUsedSpace() {
        return usedSpace;
    }

    public long getAvailableSpace() {
        return capacity - usedSpace;
    }

    public void  storeFile(File file) {
            usedSpace += file.getSize();
            files.add(file);
    }

    public void storePage(MemoryPage page){
        int pid = page.getProcessId();
        if(pages.containsKey(pid)){
            pages.get(pid).add(page);
        } else {
            List<MemoryPage> processPages = new ArrayList<>();
            processPages.add(page);
            pages.put(pid, processPages);
        }

        usedSpace += 2000;
    }

    public List<MemoryPage> getProcessPages(int pid){
        List<MemoryPage> processPages = pages.get(pid);
        if(processPages != null){
            usedSpace -= processPages.size() * 2000;
        }
        return processPages;
    }

    public List<File> getStoredFiles() {
        return files;
    }

    public int numFiles(){
        return files.size();
    }
}
