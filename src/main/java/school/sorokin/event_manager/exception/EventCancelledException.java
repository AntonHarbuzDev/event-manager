package school.sorokin.event_manager.exception;

public class EventCancelledException extends EventBusinessException {
    public EventCancelledException(String message) {
        super(message);
    }
}
