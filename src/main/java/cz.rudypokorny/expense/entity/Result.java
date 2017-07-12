package cz.rudypokorny.expense.entity;

/**
 * Created by Rudolf on 12/07/2017.
 */
public class Result<T> {
    private final T object;
    private final Rules rules;

    private Result(T object, Rules rules) {
        this.object = object;
        this.rules = rules;
    }

    public static <T> Result<T> create(T object) {
        return new Result<T>(object, new Rules());
    }

    public static <T> Result<T> fail(Rules rules) {
        return new Result<T>(null, rules);
    }

    public static <T> Result<T> create(T object, Rules rules) {
        return new Result<T>(object, rules);
    }

    public T get() {
        return object;
    }

    public Rules rules() {
        return rules;
    }

    public boolean isCompromised() {
        return rules().areBroken();
    }

    public boolean isOk() {
        return !isCompromised();
    }

}
