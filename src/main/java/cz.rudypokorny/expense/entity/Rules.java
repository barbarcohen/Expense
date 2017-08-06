package cz.rudypokorny.expense.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rudolf on 11/07/2017.
 */
public class Rules {
    public interface Messages {
        String ENTITY_IS_NULL = "entity.is.null";
    }


    private List<String> errors = new ArrayList<>();
    private List<Throwable> exceptions = new ArrayList<>();

    public Rules broken(String what) {
        if (what != null) {
            errors.add(what);
        }
        return this;
    }

    public Rules exception(Throwable e) {
        if (e != null) {
            exceptions.add(e);
        }
        return this;
    }

    public boolean areBroken() {
        return !errors.isEmpty() || !exceptions.isEmpty();
    }


    @Override
    public String toString() {
        return "Rules{" +
                "errors=" + errors +
                ", exceptions=" + exceptions +
                '}';
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public List<Throwable> getExceptions() {
        return Collections.unmodifiableList(exceptions);
    }
}
