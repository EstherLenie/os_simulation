import javax.swing.*;
import java.awt.*;


public class FilePanel extends MainPanel{
    JPanel contentPanel = new JPanel();
    JTextField totalFiles = new JTextField("Total File Created: 0");
    public FilePanel(){
        super("Files");

        JPanel titles = new JPanel(new GridLayout(1, 2));
        JTextField separator = new JTextField("");

        JTextField fileIndex = new JTextField("File Name");
        JTextField fileSize = new JTextField("File Size");

        titles.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        totalFiles.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        titles.add(fileIndex);
        titles.add(fileSize);

        this.contentPanel.add(totalFiles);
        this.contentPanel.add(separator);
        this.contentPanel.add(titles);

        this.scrollPane.setViewportView(contentPanel);
    }

    public void newFile(File file){
        FileField f = new FileField(file);

        this.contentPanel.add(f.getPanel());
        this.contentPanel.revalidate();
        this.totalFiles.setText("Total Files Created: " +  OS.numFiles());
    }
}