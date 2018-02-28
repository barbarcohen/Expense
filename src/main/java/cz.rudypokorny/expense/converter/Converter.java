package cz.rudypokorny.expense.converter;

import java.util.List;

/**
 * Created by Rudolf on 28/02/2018.
 */
public interface Converter<T> {


    public List<?> convert();
}
