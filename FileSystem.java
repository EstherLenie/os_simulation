import java.util.List;

class FileSystem {
    private MassStorage massStorage;

    public FileSystem(MassStorage massStorage) {
        this.massStorage = massStorage;
    }

    public void createFile(int[] content) {
        File newFile = new File(content);
        massStorage.storeFile(newFile);
    }

    public int getStorageSize(){
        return massStorage.getCapacity();
    }

    public int getUsedStorage(){
        return massStorage.getUsedSpace();
    }

    public int getStorageAvailable(){
        return massStorage.getAvailableSpace();
    }

    public boolean storeFile(File file) {
        int fileSize = file.getSize();
        if (fileSize <= massStorage.getAvailableSpace() ) {
            massStorage.storeFile(file);
            return true;
        } 
        return false; 
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
