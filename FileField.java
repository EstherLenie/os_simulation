import javax.swing.*;
import java.awt.*;

public class FileField {
    JPanel panel = new JPanel(new GridLayout(1, 2));
    public FileField(File file){
        Integer size = file.getSize();

        panel.add(new JTextField(file.getName()));
        panel.add(new JTextField(size.toString()));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
    }
    
    public JPanel getPanel() {
        return panel;
    }
}

