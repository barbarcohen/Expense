package cz.rudypokorny.expense.converter;

import org.apache.commons.csv.CSVRecord;

/**
 * Created by Rudolf on 28/02/2018.
 */
public interface RecordMapper<T> {

    T map(CSVRecord csvRecord);

    String getFileName();
}
