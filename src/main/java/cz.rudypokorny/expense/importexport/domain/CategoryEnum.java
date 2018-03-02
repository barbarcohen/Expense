package cz.rudypokorny.expense.importexport.domain;

import cz.rudypokorny.expense.model.Category;

import java.util.EnumSet;
import java.util.Set;

public enum CategoryEnum {
    //technical domain
    NOT_DEFINED("Not defined", "Unknown"),
    TBD("TBD", "TBD sub"),

    //real domain
    HOUSEHOLD_GROCERIES("Household", "Groceries"),
    HOUSEHOLD_MAINTENANCE_REPAIRS("Household", "Maintenance & Repairs"),
    HOUSEHOLD_IMPROVEMENT("Household", "Furniture & Equipments"),
    HOUSEHOLD_SUPPLIES("Household", "Supplies"),//drogerie
    HOUSEHOLD_MORTGAGE("Household", "Mortgage"),
    HOUSEHOLD_SERVICES_FEES("Household", "Services & Other fees"),
    HOUSEHOLD_INSURANCE("Household", "Insurance"),
    HOUSEHOLD_ELECTRICITY("Household", "Electricity"),
    HOUSEHOLD_OTHER("Household", "Other"),
    HOUSEHOLD_MEDICINE("Household", "Medicine"),
    HOUSEHOLD_HEALTHCARE("Household", "Healthcare"),
    HOUSEHOLD_GARDEN("Household", "Garden"),

    FOOD_SWEETS("Meals", "Sweet & Salty"),
    FOOD_SNACK("Meals", "Snack"),
    FOOD_WORK_LUNCH("Meals", "Work lunch"),
    FOOD_RESTAURANT("Meals", "Restaurant"),

    ENTERTAINMENT_BOOKS("Entertainment", "Books & Magazines"),
    ENTERTAINMENT_CULTURE("Entertainment", "Culture"),
    ENTERTAINMENT_PARTY_PUB("Entertainment", "Party & Pub"),
    ENTERTAINMENT_APPS_GAMES("Entertainment", "Apps & Games"),
    ENTERTAINMENT_OTHER("Entertainment", "Other"),

    WELLNESS_WELLNESS("Wellness", "Wellness"),
    SPORT_EQUIPMENT("Sport", "Equipment"),
    SPORT_FEES("Sport", "Fees"),

    ELECTRONICS_CAMERA("Electronics", "Camera & photo stuff"),
    ELECTRONICS_PHONE("Electronics", "Phone & phone stuff"),
    ELECTRONICS_COMPUTER("Electronics", "Computer & computer stuff"),
    ELECTRONICS_OTHER("Electronics", "Other"),

    INVESTMENTS_INSURANCE("Investments", "Insurance"),
    INVESTMENTS_INVESTMENTS("Investments", "Investments"),
    INVESTMENTS_PENSION("Investments", "Pension"),

    CAR_FUEL("Car", "Fuel"),
    CAR_LOAN("Car", "Loan"),
    CAR_INSURANCE("Car", "Insurance"),
    CAR_FEES("Car", "Fees"),
    CAR_REPAIRS_MAINTENANCE("Car", "Repairs & Maintenance"),
    CAR_OTHER("Car", "Other"),

    VACATION_FEES("Vacation", "Fees"),
    VACATION_ACCOMMODATION("Vacation", "Accommodation"),
    VACATION_TRANSPORTATION("Vacation", "Transportation"),
    VACATION_FOOD("Vacation", "Food"),
    VACATION_OTHER("Vacation", "Other"),

    PERSONAL_GIFTS_CHRISTMAS("Personal", "Christmas gifts"),
    PERSONAL_GIFTS("Personal", "Gifts"),
    PERSONAL_OTHER("Personal", "Other"),
    PERSONAL_TRANSPORTATION("Personal", "Transportation"),
    PERSONAL_FEES("Personal", "Fees"),
    PERSONAL_EDUCATION("Personal","Education"),


    FASHION_BOOTS("Fashion", "Boots"),
    FASHION_ACCESSORIES("Fashion", "Accessories"),
    FASHION_CLOTHES("Fashion", "Clothes"),

    BABY_FASHION("Baby", "Fashion"),
    BABY_MEDICINE("Baby", "Care & Medicine"),
    BABY_FOOD("Baby", "Food"),
    BABY_STUFF("Baby", "Stuff"),
    BABY_OTHER("Baby", "Other"),

    INCOME_SALARY("Income", "Salary"),
    INCOME_OTHER("Income", "Other")
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