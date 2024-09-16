import java.time.Instant;

import javax.swing.*;

class LogsPanel extends MainPanel{
    public JTextArea logsTextArea = new JTextArea(5, 1);
    public LogsPanel(){
        super("Logs");
        logsTextArea.setEditable(false);
        this.scrollPane.setViewportView(logsTextArea);
    }

    public void printLog(Log log){
        String instant = Instant.now().toString();
        String logText = String.format("[%s] %s: %s. %s\n", log.eventOutcome, instant, log.eventType, log.message);
        logsTextArea.append(logText);
        logsTextArea.setCaretPosition(logsTextArea.getDocument().getLength());
    }
}
