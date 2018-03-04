package cz.rudypokorny.expense.tools.importexport.importing.mappers;

import com.google.common.collect.ImmutableMap;
import cz.rudypokorny.expense.model.Account;
import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.expense.tools.importexport.RecordMapper;
import cz.rudypokorny.expense.tools.importexport.domain.CategoryEnum;
import cz.rudypokorny.expense.tools.importexport.domain.CategoryMapping;
import cz.rudypokorny.util.DateUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static cz.rudypokorny.expense.tools.importexport.domain.CategoryEnum.*;

/**
 * //7/26/17;Miminko;477;;Vana,prebalovak +povlak;vendor
 */
public class KatikCSVRecordMapper implements RecordMapper<CSVRecord, Expense, CSVFormat> {

    private static final String FILENAME = "source_katik.csv";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yy");
    private static final String CURRENCY = "CZK";

    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.withDelimiter(';');
    private static final Logger logger = LoggerFactory.getLogger(KatikCSVRecordMapper.class);
    private static final Account ACCOUNT = Account.named("Kátik");
    private static final Set<CategoryEnum> CONTROVERSIAL_CATEGORIES = EnumSet.of(SHOPPING_GRUGSTORE, LIFE_HOLIDAYS,
            INVESTMENTS_FINANCIAL, LIFE_SPORT, FINANCIAL_FEES, SHOPPING_CLOTHES, SHOPPING_BABY, VEHICLE_FUEL, LIFE_HEALCARE);
    private final ZoneId timezone;

    //mapping katka
    final private static Map<String, CategoryEnum> KATIK_CATEGORY_MAPPING = ImmutableMap.<String, CategoryEnum>builder().
            put("Volný čas-kino,koncert..", LIFE_CULTURE).
            put("K+R", FOOD_GROCERIES).
            put("drogerie", SHOPPING_HEALTH_BEAUTY).
            put("lékárna", SHOPPING_GRUGSTORE).//jenom kata
            put("Kavárna", FOOD_SNACKS).
            put("jidlo", FOOD_SNACKS).
            put("Doprava", VEHICLE_FUEL).//auto (1000+) + all others MHD
            put("Na jedno", LIFE_PARTY).
            put("Miminko", SHOPPING_BABY).//leky vybaneni jidlo plinky
            put("společná domácnost", HOUSING_FURNITURE).//? jidlo nebo vybaveni?
            put("Oblečení boty", SHOPPING_CLOTHES).//boty taky
            put("Ostatní", FINANCIAL_FEES).//viz poznamky
            put("Sport", LIFE_SPORT).// fees & vybaveni - viz poznamky
            put("Mlsání", FOOD_SWEETS).
            put("Nutné výdaje", INVESTMENTS_FINANCIAL).//pojisteni a investice
            put("Zvaní na pokrm,pití", FOOD_RESTAURANT).
            put("dovolená", LIFE_HOLIDAYS).
            put("obědy", FOOD_WORK_LUNCH).
            put("dárky k narozeninám", SHOPPING_GIFTS).
            put("Zdraví,relax,sauna,lymf.", LIFE_WELLNESS_BEAUTY).
            put("Restaurace", FOOD_RESTAURANT).
            put("doktor", LIFE_HEALCARE).
            put("dárky vánoční", SHOPPING_CHRISTMAS_GIFTS).
            put("Polsko nákupy společné", LIFE_HOLIDAYS).
            put("Polsko nákupy mé", LIFE_HOLIDAYS).
            put("pokuty", FINANCIAL_FINES).
            put("Sámoška-Potraviny a nápoje", FOOD_GROCERIES).
            put("Income-Salary", INCOME_WAGE).
            put("Income-Other", INCOME_CHILD_SUPPORT).
            build();

    public KatikCSVRecordMapper() {
        timezone = DateUtil.getCurrentTimeZone();
    }

    @Override
    public String getFileName() {
        return FILENAME;
    }

    @Override
    public CSVFormat getConfig() {
        return CSV_FORMAT;
    }

    @Override
    public Expense map(final CSVRecord record) {
        Objects.requireNonNull(record, "csvRecord cannot be null");
        Expense expense = null;
        try {
            Double amount = Double.valueOf(cleanAmountValue(record.get(2)));
            String category = record.get(1);
            String note = StringUtils.trimToNull(record.get(4));
            String vendor = StringUtils.trimToNull(record.get(5));

            ZonedDateTime date = DATE_FORMAT.parse(record.get(0)).toInstant().atZone(timezone);

            CategoryEnum convertedCategory = additionalMapping(CategoryMapping.doTheMapping(KATIK_CATEGORY_MAPPING, category), category, note);

            expense = Expense.newExpense(convertAmount(amount, convertedCategory)).
                    on(convertedCategory).
                    by(ACCOUNT).
                    at(date).
                    currency(CURRENCY).
                    payment("Cash").
                    vendor(vendor).
                    noted(note);
            logger.trace("Converted entity: " + expense.toString());
        } catch (Exception e) {
            logger.error("Importing failed for Reason: {}", e);
        }

        return expense;
    }

    private Double convertAmount(Double amount, CategoryEnum category) {
        if (!CategoryEnum.incomes().contains(category)) {
            amount *= -1;
        }
        return amount;
    }

    private String cleanAmountValue(String value) {
        return value.replaceAll("[^\\d.-]", "");
    }

    /**
     * if the amout is greater than 1000 or notes are not empty try to determine using additional data
     */
    private boolean involveAdditionalData(final CategoryEnum category, final String notes) {
        return StringUtils.isNotBlank(notes) && isControversial(category);
    }

    private CategoryEnum additionalMapping(final CategoryEnum originalCategory, final String categorySource, final String note) {
        CategoryEnum resultedCategory = null;
        if (involveAdditionalData(originalCategory, note)) {
            resultedCategory = determineCategory(originalCategory, categorySource, new Note(note));
        }
        return resultedCategory != null ? resultedCategory : originalCategory;
    }

    private boolean isControversial(final CategoryEnum category) {
        return CONTROVERSIAL_CATEGORIES.contains(category);
    }

    private CategoryEnum determineCategory(final CategoryEnum foundCategory, final String originalCategory, final Note note) {
        CategoryEnum category = null;

        if (note.is("Spoření, pojištění atd za mě a Viky")) {
            category = FINANCIAL_INSURANCES;
        } else if (note.is("Spoření", "Spoření, zivotko atd", "Trvalé platby")) {
            category = INVESTMENTS_FINANCIAL;
        } else if (note.is("Mléko nutrilon")) {
            category = FOOD_BABY_FOOD;
        } else if (note.is("Fusak", "Láhve", "Oleje", "Otiskovačka", "Vana,prebalovak +povlak", "Oblečky, gel",
                "Odsavač hlenů", "Teploměr", "Pomůcky do porodnice", "Pleny")) {
            category = SHOPPING_BABY;
        } else if (note.is("Náušnice", "Babycalm", "Probiotika")) {
            category = LIFE_HEALCARE_BABY;
        } else if (note.is("Rukavice, ponožky, čepice", "Bryndák, podložka,kruh..", "Obleček")) {
            category = SHOPPING_BABY_CLOTHES;
        } else if (note.is("Výměna plášťů, duší 2x", "Termoska", "Látky", "Sádra", "Záplata", "Ručník",
                "Fotky na řidičák", "Rám na náušnice")) {
            category = HOUSING_GENERAL;
        } else if (note.is("Auto potřeby", "USB do auta", "Utěrky do auta", "voda do ostřikovače", "Parkování")) {
            category = VEHICLE_GENERAL;
        } else if (note.is("Značky")) {
            category = VEHICLE_GENERAL;
        } else if (note.is("Pneumatiky")) {
            category = VEHICLE_GENERAL;
        } else if (note.is("Pangamin", "Fe,kyselina listová", "Lymfatické doplňky", "Deodorant")) {
            category = SHOPPING_GRUGSTORE;
        } else if (note.is("Znaménka", "Zuby", "Kika zuby", "Čistič pleti", "Lešticka zubů")) {
            category = LIFE_HEALCARE;
        } else if (note.is("Impregnace", "Vložky do bot")) {
            category = SHOPPING_SHOES;
        } else if (note.is("Kabelka", "Šperk", "Vložky do bot", "Svatební prsteny", "Deštník")) {
            category = SHOPPING_JEWELS_ACCESSORIES;
        } else if (note.is("Spodního prádla", "Sekáč", "Šátky", "Tílko", "Bunda", "Ponožky", "Podprsenky", "Kalhotky", "Primark", "Svatba šaty", "Svatební šaty", "Sport výstaviště")) {
            category = SHOPPING_CLOTHES;
        } else if (note.is("Kadernice", "Kadeřnice", "Svatba kadeřnictví, kosmetika", "Svatba nehty, nákup", "Svatba účes", "Svatba stuhy",
                "Stuhy", "Svatba ubrousky", "Bijou", "Pomocník", "Dáno rudymu", "Kytka", "Klíče")) {
            category = LIFE_WELLNESS_BEAUTY;
        } else if (note.is("Půjčka", "Poplatek čsk", "Tisk", "Pošta", "Pas", "Řidičák", "Popelnice", "Doklady fotky na řidičák",
                "Doklady trvalé bydliště", "Nájem cernotin", "Absolventská karta")) {
            category = FINANCIAL_FEES;
        } else if (note.is("obědy")) {
            category = FOOD_WORK_LUNCH;
        } else if (note.is("Nájem prosinec")) {
            category = HOUSING_RENT;
        } else if (note.is("Benzín začátek prosince", "Benzín", "Plyn")) {
            category = VEHICLE_FUEL;
        } else if (note.is("Alpy", "Pojištění", "Lyže", "Plavání permanentka", "Pobyt na horách", "Inline 24", "Pasohlávky wake", "Jóga", "Plavání", "Lyže", "Skipas", "Aply", "Permanentka bluegym", "Broušení lyží", "Skipasy 2x")) {
            category = LIFE_SPORT;
        } else if (note.is("Vybavení", "Posilovací guma", "Brašna", "Karimatka", "Kolečka")) {
            category = LIFE_SPORT;
        } else if (note.is("Box")) {
            category = HOUSING_GENERAL;
        } else if (note.is("ČSK PŘÍSPĚVEK", "Předporodní kurz")) {
            category = LIFE_EDUCATION;
        } else if (note.is("Salinkarta", "Bus", "Autobus", "Vlaky do Brna R,k", "Vlaky hranice",
                "Vlaky podruhé")) {
            category = TRANSPORTATION_MHD;
        } else if (note.is("Rudy nákrčník")) {
            category = SHOPPING_GIFTS;
        } else if (note.is("Laser")) {
            category = LIFE_HEALCARE;
        } else if (note.is("Knihy", "Kniha")) {
            category = LIFE_BOOKS_AUDIO;
        } else if (note.is("Paintball", "Bowling")) {
            category = LIFE_SPORT;
        } else if (note.is("Výběr hotovosti", "Stromek", "Polsko", "Kapesné")) {
            category = LIFE_HOLIDAYS;
        } else if (note.is("Jídlo", "Chlast")) {
            category = LIFE_HOLIDAYS;
        } else if (note.is("Mallorca", "Ubytování Krumlov", "Oběd Krkonoše", "Silvestr", "Atc Merkur, stan")) {
            category = LIFE_HOLIDAYS;
        } else if (note.is("Letenky", "Vlaky", "Nafta Jirka", "Buš Vídeň", "Hradec")) {
            category = TRANSPORTATION_VACATION;
        } else if (note.is("Svatba kafe")) {
            category = FOOD_SNACKS;
        } else if (note.is("Svatba nákup pití", "Svatba vysluzky", "Jídlo na párty", "Jídlo sklípek", "U kiky")) {
            category = LIFE_PARTY;
        } else if (note.is("Pevný disk")) {
            category = PC;
        } else if (note.is("Obal na mobil")) {
            category = SHOPPING_PHONE_ACCESSORIES;
        } else if (note.is("Foťák")) {
            category = SHOPPING_CAMERA_ACCESSORIES;
        }
        if (category == null) {
            logger.warn("No mapping found for {} with note:'{}', using category {}", originalCategory, note, foundCategory);
            category = foundCategory;
        }
        return category;
    }

    private class Note {
        private String note;

        public Note(String note) {
            this.note = note;
        }

        public boolean is(final String... values) {
            return values != null && values.length > 0 && noteHasValue(values);
        }

        @Override
        public String toString() {
            return note;
        }

        private boolean noteHasValue(String... values) {
            for (String value : values) {
                if (value != null && value.trim().equals(this.note)) {
                    return true;
                }
            }
            return false;
        }
    }
}
