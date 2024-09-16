import javax.swing.*;
import java.awt.*;

class ProcessField {
    JPanel processInfosPanel = new JPanel(new GridLayout(1, 3));
    private JTextField pidField, memoryField, statusField;

    public ProcessField(Integer PID, Integer memory, String status){
        pidField = new JTextField(PID.toString());
        memoryField = new JTextField(memory.toString());
        statusField = new JTextField(status);

        processInfosPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
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
