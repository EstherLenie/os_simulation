import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class MemoryPanel extends MainPanel {
        JProgressBar mainMemoryProgressBar;
        JProgressBar secondaryMemoryProgressBar;
        JTextField mainMemoryUsage = new JTextField("");
        JTextField secondaryMemoryUsage = new JTextField("");
        JPanel contentPanel = new JPanel(new GridLayout(2, 1));

        private final long  mainMemory;
        private final long  secondaryMemory;

        public MemoryPanel(long mainMemory, long secondaryMemory){
            super("Memoire");

            JPanel mainMemoryPannel = new JPanel(new GridLayout(3, 1)); 
            JPanel secondaryMemoryPannel = new JPanel(new GridLayout(3, 1)); 
            
            this.mainMemory = mainMemory;
            this.secondaryMemory = secondaryMemory;

            mainMemoryUsage.setText("Used:   "+ 0 + "/" + mainMemory);
            secondaryMemoryUsage.setText("Used:   " + 0 + "/" + secondaryMemory);

            mainMemoryProgressBar = new JProgressBar(0, (int)this.mainMemory/1000);
            mainMemoryProgressBar.setValue(0);
            mainMemoryProgressBar.setStringPainted(true);

            secondaryMemoryProgressBar = new JProgressBar(0, (int)this.secondaryMemory/1000);
            secondaryMemoryProgressBar.setValue(0);
            secondaryMemoryProgressBar.setStringPainted(true);

            mainMemoryPannel.add(new JLabel("Main Memory"));
            mainMemoryPannel.add(mainMemoryProgressBar);
            mainMemoryPannel.add(mainMemoryUsage);

            secondaryMemoryPannel.add(new JLabel("Secondary Memory"));
            secondaryMemoryPannel.add(secondaryMemoryProgressBar);
            secondaryMemoryPannel.add(secondaryMemoryUsage);

            contentPanel.add(mainMemoryPannel);
            contentPanel.add(secondaryMemoryPannel);
            this.scrollPane.setViewportView(contentPanel);
        }

        public void mainMemoryUpdate(long used){
            mainMemoryProgressBar.setValue((int)used/1000);
            mainMemoryUsage.setText("Used:   "+ used + "/" + mainMemory);
            this.contentPanel.revalidate();
        }

        public void secondaryMemoryUpdate(long used){
            secondaryMemoryProgressBar.setValue((int)used/1000);
            secondaryMemoryUsage.setText("Used:   "+ used + "/" + secondaryMemory);
            this.contentPanel.revalidate();
        }
}
