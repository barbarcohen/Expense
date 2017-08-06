package cz.rudypokorny.exception;

/**
 * Created by Rudolf on 04/08/2017.
 */
public class AccountAwareException extends RuntimeException {

    public AccountAwareException(String message) {
        super(message);
    }
}
