package cz.rudypokorny.expense.converter.impl;


import cz.rudypokorny.expense.converter.Converter;
import cz.rudypokorny.expense.converter.RecordMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Rudolf on 28/02/2018.
 */
public class CSVConverter implements Converter {

    private static final Logger logger = LoggerFactory.getLogger(CSVConverter.class);

    private final CSVFormat csvFormat;
    private final RecordMapper<?> mapper;

    public CSVConverter(final RecordMapper mapper, CSVFormat csvFormat) {

        this.mapper = Objects.requireNonNull(mapper, "'mapper' cannot be null");
        this.csvFormat = Objects.requireNonNull(csvFormat, "'csvFormat' cannot be null");
    }

    @Override
    public List<?> convert() {
        try (FileReader fileReader = new FileReader(readFile(mapper.getFileName()));
             CSVParser csvFileParser = new CSVParser(fileReader, csvFormat);
        ) {
            List<?> results = csvFileParser.getRecords().stream()
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

    private File readFile(final String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        logger.info("Reading file: " + fileName);
        return new File(classLoader.getResource(fileName).getFile());
    }

}
