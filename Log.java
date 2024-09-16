public class Log {
    public String message;
    public EventType eventType;
    public EventOutcome eventOutcome;

    public Log(EventOutcome eventOutcome, EventType eventType, String message){
        this.message = message;
        this.eventOutcome = eventOutcome;
        this.eventType = eventType;
    }
}
