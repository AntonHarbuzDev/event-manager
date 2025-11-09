package school.sorokin.event_manager.exception;

public abstract class EventBusinessException extends RuntimeException {
    EventBusinessException(String message) {
        super(message);
    }
}
