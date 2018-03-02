package cz.rudypokorny.expense.importexport;

import org.apache.commons.csv.CSVRecord;

/**
 * Created by Rudolf on 28/02/2018.
 */
public interface RecordMapper<IN, OUT, CONFIG> {

    CONFIG getConfig();

    OUT map(IN record);

    String getFileName();

}
