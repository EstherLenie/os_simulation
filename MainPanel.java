import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

class MainPanel{
        public JPanel panel = new JPanel();
        public Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        public JLabel titleLabel;
        public JScrollPane scrollPane = new JScrollPane();

        public MainPanel(String title){
            titleLabel = new JLabel(title);
            titleLabel.setPreferredSize(new Dimension(panel.getPreferredSize().width, 20));
            titleLabel.setHorizontalAlignment(JLabel.CENTER); 
       
            scrollPane.setPreferredSize(new Dimension(40, 10));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);    
       
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));        
            panel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            panel.add(titleLabel);
            panel.add(scrollPane);
        }

        public JPanel getPanel (){
            return panel;
        }
}
