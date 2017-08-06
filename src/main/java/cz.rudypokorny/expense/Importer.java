package cz.rudypokorny.expense;

import cz.rudypokorny.expense.model.Account;
import io.reactivex.Observable;

import java.util.List;

/**
 * Created by Rudolf on 01/08/2017.
 */
public class Importer {

    public static void main(String []args){


        FileImporter fileImporter = new FileImporter();
        List<FileImporter.Record> result =  fileImporter.readCsvFile("source_rudy.csv");


        //filter disticts categories
        Observable.fromIterable(result).distinct(FileImporter.Record::getCategory).forEach(e->{
            //        categoryDao.save(Category.create(e.getCategory()));
            System.out.println(e.getCategory());
        });


        System.out.println(result.stream().count());

    }
}
