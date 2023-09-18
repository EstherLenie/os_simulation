import java.util.List;

class FileSystem {
    private MassStorage massStorage;

    public FileSystem(MassStorage massStorage) {
        this.massStorage = massStorage;
    }

    public int numFiles(){
        return massStorage.numFiles();
    }

    public long getStorageSize(){
        return massStorage.getCapacity();
    }

    public long getUsedStorage(){
        return massStorage.getUsedSpace();
    }

    public long getStorageAvailable(){
        return massStorage.getAvailableSpace();
    }

    public Log storeFile(File file) {
        int fileSize = file.getSize();
        if (fileSize >= massStorage.getAvailableSpace() ) {
            return new Log(EventOutcome.ERROR, EventType.CREATE_FILE, "not enough space to create file");
        } 
        massStorage.storeFile(file);
        return new Log(EventOutcome.SUCCESS, EventType.CREATE_FILE, "new file created"); 
    }

    public boolean storePage(MemoryPage page){
        if(massStorage.getAvailableSpace() < 2000){
            return false;
        }
        massStorage.storePage(page);
        return true;
    }

    public List<MemoryPage> getProcessPages(int pid){
        return massStorage.getProcessPages(pid);
    }
}
