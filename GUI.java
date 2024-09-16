import javax.swing.*;
import java.time.Instant;
import java.awt.*;

public class GUI {
    private JFrame frame;
    private LogsPanel logsPanel;
    private MemoryPanel memoryPanel;
    private ProcessPanel processPanel;
    private FilePanel filesPanel;

    public void createAndShowGUI() {

        frame = new JFrame("Grid Window Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        logsPanel = new LogsPanel();
        memoryPanel = new MemoryPanel(OS.getMemorySize(), OS.getStorageSize());
        processPanel = new ProcessPanel();
        filesPanel = new FilePanel();


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize);

        GridLayout gridLayout = new GridLayout(2, 2);
        frame.setLayout(gridLayout);

        frame.add(logsPanel.getPanel());
        frame.add(memoryPanel.getPanel());
        frame.add(processPanel.getPanel());
        frame.add(filesPanel.getPanel());
        frame.setVisible(true);
    }

    public void addLog(Log log) {
        logsPanel.printLog(log);
    }

    synchronized public void newProcess(int PID, int memory, ProcessState status){
        processPanel.newProcess(PID, memory, status);
        memoryPanel.mainMemoryUpdate(OS.getUsedMemorySpace());
    }

    public void processUpdate(int PID, ProcessState status){
        this.processPanel.processUpdate(PID, status);
        memoryPanel.mainMemoryUpdate(OS.getUsedMemorySpace());
    }

    public void newFile(File file){
        this.filesPanel.newFile(file);
        this.memoryPanel.secondaryMemoryUpdate(OS.getUsedStorage());
    }
}



