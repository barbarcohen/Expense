package cz.rudypokorny.expense.model;

import java.util.Random;
import java.util.StringJoiner;

/**
 * Created by Rudolf on 11/07/2017.
 */
public class CategoryCreator {

    private static Random random = new Random();

    public static Category create(){
        Category category = new Category();
        category.setId(random.nextLong());
        category.setName(String.valueOf(random.nextInt()));
        return category;
    }
}
