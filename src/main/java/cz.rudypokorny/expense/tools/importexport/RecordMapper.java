package cz.rudypokorny.expense.tools.importexport;

/**
 * Created by Rudolf on 28/02/2018.
 */
public interface RecordMapper<IN, OUT, CONFIG> {

    CONFIG getConfig();

    OUT map(IN record);

    String getFileName();

}
