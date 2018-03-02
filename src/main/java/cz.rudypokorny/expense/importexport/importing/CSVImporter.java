package cz.rudypokorny.expense.importexport.importing;


import cz.rudypokorny.expense.importexport.RecordMapper;
import cz.rudypokorny.util.FileUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CSVImporter<T> {

    private static final Logger logger = LoggerFactory.getLogger(CSVImporter.class);

    private final RecordMapper<CSVRecord, T, CSVFormat> mapper;

    public CSVImporter(final RecordMapper<CSVRecord, T, CSVFormat> mapper) {
        this.mapper = Objects.requireNonNull(mapper, "'mapper' cannot be null");
    }

    public List<T> load() {
        try (FileReader fileReader = new FileReader(FileUtil.readFile(mapper.getFileName()));
             CSVParser csvFileParser = new CSVParser(fileReader, mapper.getConfig())
        ) {
            List<T> results = csvFileParser.getRecords().stream()
                    .map(mapper::map)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            logger.debug("Converting ended. Converted {} records.", results.size());
            return results;
        } catch (Exception e) {
            logger.error("Exception has occured while parsing CSV: " + e.getMessage());
        }
        return Collections.emptyList();
    }


}
