package cz.rudypokorny.expense;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.format.datetime.joda.LocalDateParser;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rudolf on 31/07/2017.
 */
@Component
public class FileImporter {


    public List<Record> readCsvFile(String fileName) {

        //Create a new list of student to be filled by CSV file data
        List<Record> records = new ArrayList<Record>();


        FileReader fileReader = null;

        CSVParser csvFileParser = null;

        //Create the CSVFormat object with the header mapping
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withDelimiter(',').withFirstRecordAsHeader();

        try {


            //initialize FileReader object

            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(fileName).getFile());
            fileReader = new FileReader(file);

            //initialize CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);

            //Get a list of CSV file records
            List csvRecords = csvFileParser.getRecords();

            //Read the CSV file records starting from the second record to skip the header
            for (int i = 1; i < csvRecords.size(); i++) {
                CSVRecord record = (CSVRecord) csvRecords.get(i);
                //Create a new student object and fill his data
                records.add(new Record(record.get(0), record.get(2)+" -> "+record.get(3), record.get(7), record.get(8)));
            }


        }
        catch (Exception e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                csvFileParser.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader/csvFileParser !!!");
                e.printStackTrace();
            }
        }
        return records;

    }

    public class Record {
        LocalDate date;
        String category;
        String amount;
        String note;

        public Record(String stringdate, String category, String amount, String note) {
            this.date = LocalDate.parse(stringdate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            this.category = category;
            this.amount = amount;
            this.note = note;
        }

        public LocalDate getDate() {
            return date;
        }

        public String getCategory() {
            return category;
        }

        public String getAmount() {
            return amount;
        }

        public String getNote() {
            return note;
        }
    }

}
