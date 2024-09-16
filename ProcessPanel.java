import javax.swing.*;
import java.util.HashMap;
import java.awt.*;

class ProcessPanel extends MainPanel{
    private HashMap<Integer, ProcessField> panels = new HashMap<>();
    JPanel titlePanel = new JPanel(new GridLayout(1, 3));
    JPanel contentPanel;

    JTextField numCreatedProcesses = new JTextField("Total Process Created: 0");
    JTextField numLiveProcesses = new JTextField("Living Processes: 0");
    public ProcessPanel(){
        super("Process");

        JTextField pidField = new JTextField("PID");
        JTextField memoryField = new JTextField("Memory");
        JTextField statusField = new JTextField("Status");

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JTextField separator = new JTextField();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        pidField.setEditable(false);
        memoryField.setEditable(false);
        statusField.setEditable(false);
        
        titlePanel.add(pidField);
        titlePanel.add(statusField);
        titlePanel.add(memoryField);
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        numCreatedProcesses.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        numLiveProcesses.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
    
        this.contentPanel.add(numCreatedProcesses);
        this.contentPanel.add(numLiveProcesses);
        this.contentPanel.add(separator);
        this.contentPanel.add(titlePanel);
        this.scrollPane.setViewportView(contentPanel);
        
    }

    public void newProcess(int PID, int memory, ProcessState status){
        ProcessField process = new ProcessField(PID, memory, status.toString());
        panels.put(PID, process);
        this.contentPanel.add(process.getPanel());
        this.numCreatedProcesses.setText("Total Process Created :" + OS.getNumProcess());  
        this.numLiveProcesses.setText("Total live Processes :" + OS.numLiveProcesses());
        this.contentPanel.revalidate();

    }

    public void processUpdate(int PID, ProcessState status){
        ProcessField process = panels.get(PID);
        process.updateProcessInfos(status.toString());
        this.contentPanel.revalidate();
    }

    public void updateProcessInfos(int PID, String Status){
        ProcessField process = panels.get(PID);
        if (process == null){
            return;
        }
        process.updateProcessInfos(Status);
    }

}