package cz.rudypokorny.util;

import java.util.EnumSet;
import java.util.Set;

public enum CategoryEnum {
    //technical categories
    NOT_DEFINED("Not defined", "Unknown"),
    TBD("TBD", "TBD sub"),

    //real categories
    DOMACNOST_POTRAVINY("Domácnost", "Potraviny");

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
}