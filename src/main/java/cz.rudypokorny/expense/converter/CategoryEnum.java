package cz.rudypokorny.expense.converter;

import cz.rudypokorny.expense.model.Category;

import java.util.EnumSet;
import java.util.Set;

public enum CategoryEnum {
    //technical categories
    NOT_DEFINED("Not defined", "Unknown"),
    TBD("TBD", "TBD sub"),

    //real categories
    HOUSEHOLD_GLOCERIES("Household", "Gloceries"),
    HOUSEHOLD_MAINTENANCE("Household", "Maintanence"),
    HOUSEHOLD_STUFF("Household", "?"),
    HOUSEHOLD_IMPROVEMENT("Household", "Furniture & Improvements"),

    FOOD_SNACK("Food", "Snack"),
    FOOD_LUNCH("Food", "Work lunch"),
    FOOD_RESTAURANT("Food", "Restaturant"),

    ENTERTAINMENT_BOOZ("Zábava", "Alkohol"),
    ENTERTAINMENT_MOVIES("Zábava", "Vstupenky"),
    ENTERTAINMENT_PARTY("Zábava", "Party"),
    ;

    public static Set<CategoryEnum> categories = EnumSet.complementOf(EnumSet.of(NOT_DEFINED, TBD));

    private String category;
    private String subCategory;


    CategoryEnum(String category, String subCategory) {
        this.category = category;
        this.subCategory = subCategory;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public Category full(){
        return Category.namedWithParent(subCategory, Category.named(category));
    }
}