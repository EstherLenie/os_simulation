import javax.swing.*;
import javax.swing.border.Border;
import java.util.HashMap;

import java.awt.*;

public class GUI {
    private JFrame frame;
    private LogsPanel logsPanel;
    private MainPanel memoryPanel;
    private ProcessPanel processPanel;
    private MainPanel filesPanel;

    public void createAndShowGUI() {

        frame = new JFrame("Grid Window Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        logsPanel = new LogsPanel();
        memoryPanel = new MainPanel("Memory");
        processPanel = new ProcessPanel();
        filesPanel = new MainPanel("Files");


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

    public void addLog(String log) {
        logsPanel.printLog(log);
    }

    public void newProcess(int PID, int memory, String status){
        processPanel.newProcess(PID, memory, status);
    }
}

class MainPanel{
        public JPanel panel = new JPanel();
        public JPanel contentPanel = new JPanel();
        public JScrollPane scrollPane = new JScrollPane();
        public Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        public JLabel titleLabel;

        public MainPanel(String title){
            titleLabel = new JLabel(title);
            titleLabel.setPreferredSize(new Dimension(panel.getPreferredSize().width, 20));
            titleLabel.setHorizontalAlignment(JLabel.CENTER); 

            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); 
            contentPanel.setPreferredSize(new Dimension(panel.getPreferredSize().width, 10));
            contentPanel.setBackground(new Color(123, 123, 0));
       
            scrollPane.setPreferredSize(new Dimension(40, 10));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);    
            scrollPane.setViewportView(contentPanel);
       
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));        
            panel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            panel.add(titleLabel);
            panel.add(scrollPane);
        }

        public JPanel getPanel (){
            return panel;
        }
}

class LogsPanel extends MainPanel{
    public JTextArea logsTextArea = new JTextArea(5, 1);
    public LogsPanel(){
        super("Logs");
        logsTextArea.setEditable(false);
        this.contentPanel.add(logsTextArea);
    }

    public void printLog(String log){
    logsTextArea.append(log + "\n");
    logsTextArea.setCaretPosition(logsTextArea.getDocument().getLength());
    }
}

class ProcessPanel extends MainPanel{
    private HashMap<Integer, ProcessField> panels = new HashMap<>();
    JPanel titlePanel = new JPanel(new GridLayout(1, 3));
    public ProcessPanel(){
        super("Process");
        TextField pidField = new TextField("PID");
        TextField memoryField = new TextField("Memory");
        TextField statusField = new TextField("Status");

        pidField.setEditable(false);
        memoryField.setEditable(false);
        statusField.setEditable(false);
        
        titlePanel.add(pidField);
        titlePanel.add(statusField);
        titlePanel.add(memoryField);
        titlePanel.setPreferredSize(new Dimension(titlePanel.getPreferredSize().width, 20));

        contentPanel.add(titlePanel);
    }

    public void newProcess(int PID, int memory, String status){
       ProcessField process = new ProcessField(PID, memory, status);
       panels.put(PID, process);
       this.contentPanel.add(process.getPanel());
    }

    public void updateProcessInfos(int PID, String Status){
        ProcessField process = panels.get(PID);
        if (process == null){
            return;
        }
        process.updateProcessInfos(Status);
    }

}

class ProcessField {
    JPanel processInfosPanel = new JPanel(new GridLayout(1, 3));
    private TextField pidField, memoryField, statusField;

    public ProcessField(Integer PID, Integer memory, String status){
        pidField = new TextField(PID.toString());
        memoryField = new TextField(memory.toString());
        statusField = new TextField(status);


        processInfosPanel.add(pidField);
        processInfosPanel.add(statusField);
        processInfosPanel.add(memoryField);
    }

    public JPanel getPanel(){
        return processInfosPanel;
    }

    public void updateProcessInfos(String status){
        statusField.setText(status);
    }
    
}