package school.sorokin.event_manager.exception;

public class EventCapacityExceededException extends EventBusinessException {
    public EventCapacityExceededException(String message) {
        super(message);
    }
}
