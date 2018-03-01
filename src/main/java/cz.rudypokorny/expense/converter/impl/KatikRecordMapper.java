package cz.rudypokorny.expense.converter.impl;

import cz.rudypokorny.expense.converter.RecordMapper;
import cz.rudypokorny.expense.converter.categories.CategoryMapping;
import cz.rudypokorny.expense.model.Category;
import cz.rudypokorny.expense.model.Expense;
import cz.rudypokorny.util.DateUtil;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import static cz.rudypokorny.expense.converter.categories.CategoryEnum.*;

/**
 * //7/26/17;Miminko;477;;Vana,prebalovak +povlak
 */
public class KatikRecordMapper implements RecordMapper<Expense> {

    private static final String FILENAME = "source_katik.csv";
    private static final String DATEFORMAT = "MM/dd/yy";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATEFORMAT);
    private static final String CURRENCY = "CZK";
    private static final Logger logger = LoggerFactory.getLogger(KatikRecordMapper.class);

    private final ZoneId timezone;

    public KatikRecordMapper() {
        timezone = DateUtil.getCurrentTimeZone();
    }

    public Expense map(final CSVRecord csvRecord) {
        Objects.requireNonNull(csvRecord, "csvRecord cannot be null");
        Expense expense = null;
        try {
            Double amount = Double.valueOf(cleanAmountValue(csvRecord.get(2)));
            String category = csvRecord.get(1);
            String note = StringUtils.trimToNull(csvRecord.get(4));

            ZonedDateTime date = DATE_FORMAT.parse(csvRecord.get(0)).toInstant().atZone(timezone);

            Category convertedCategory = additionalMapping(CategoryMapping.getMappingFor(category).full(), category, amount, note);

            expense = Expense.newExpense(amount).
                    on(convertedCategory).
                    at(date).
                    noted(note);
            logger.trace("Converted entity: " + expense.toString());
        } catch (Exception e) {
            logger.error("Importing failed for Reason: {}", e);
        }

        return expense;
    }

    @Override
    public String getFileName() {
        return FILENAME;
    }

    private String cleanAmountValue(String value) {
        return value.replaceAll("[^\\d.-]", "");
    }

    /**
     * if the amout is greater than 1000 or notes are not empty try to determine using additional data
     *
     * @param originalCategory
     * @param amount
     * @param notes
     * @return
     */
    private boolean involveAdditionalData(final Category category, final String originalCategory, final Double amount, final String notes) {
        return StringUtils.isNotBlank(notes) && isControversial(category);
    }

    private Category additionalMapping(final Category originalCategory, final String categorySource, final Double amount, final String note) {
        Category resultedCategory = null;
        if (involveAdditionalData(originalCategory, categorySource, amount, note)) {
            resultedCategory = determineCategory(originalCategory, categorySource, amount, new Note(note));
        }
        return resultedCategory != null ? resultedCategory : originalCategory;
    }

    private boolean isControversial(final Category category) {
        return TBD.full().equals(category);
    }

    private Category determineCategory(final Category foundCategory, final String originalCategory, final Double amount, final Note note) {
        //logger.debug("Determining KATIK's category: {}, amount: {}, notes: '{}'", originalCategory, amount, note);
        Category category = null;

        if (note.is("Spoření, pojištění atd za mě a Viky")) {
            category = INVESTMENTS_INSURANCE.full();
        } else if (note.is("Spoření", "Spoření, zivotko atd", "Trvalé platby")) {
            category = INVESTMENTS_INVESTMENTS.full();
        } else if (note.is("Mléko nutrilon")) {
            category = BABY_FOOD.full();
        } else if (note.is("Fusak", "Láhve", "Oleje", "Otiskovačka", "Vana,prebalovak +povlak", "Oblečky, gel",
                "Odsavač hlenů", "Teploměr", "Pomůcky do porodnice", "Pleny")) {
            category = BABY_STUFF.full();
        } else if (note.is("Náušnice", "Babycalm", "Probiotika")) {
            category = BABY_MEDICINE.full();
        } else if (note.is("Rukavice, ponožky, čepice", "Bryndák, podložka,kruh..", "Obleček")) {
            category = BABY_FASHION.full();
        } else if (note.is("Výměna plášťů, duší 2x", "Termoska", "Látky", "Sádra", "Záplata", "Ručník",
                "Fotky na řidičák", "Rám na náušnice")) {
            category = HOUSEHOLD_OTHER.full();
        } else if (note.is("Auto potřeby", "USB do auta", "Utěrky do auta", "voda do ostřikovače", "Parkování")) {
            category = CAR_OTHER.full();
        } else if (note.is("Značky")) {
            category = CAR_FEES.full();
        } else if (note.is("Pneumatiky")) {
            category = CAR_REPAIRS_MAINTENANCE.full();
        } else if (note.is("Pangamin", "Fe,kyselina listová", "Lymfatické doplňky")) {
            category = HOUSEHOLD_MEDICINE.full();
        } else if (note.is("Deodorant", "Znaménka", "Zuby", "Kika zuby", "Čistič pleti", "Lešticka zubů")) {
            category = HOUSEHOLD_HEALTHCARE.full();
        } else if (note.is("Impregnace", "Vložky do bot")) {
            category = FASHION_BOOTS.full();
        } else if (note.is("Kabelka", "Šperk", "Vložky do bot", "Svatební prsteny", "Deštník")) {
            category = FASHION_ACCESSORIES.full();

        } else if (note.is("Spodního prádla", "Sekáč", "Šátky", "Tílko", "Bunda", "Ponožky", "Podprsenky", "Kalhotky", "Primark", "Svatba šaty", "Svatební šaty", "Sport výstaviště")) {
            category = FASHION_CLOTHES.full();
        } else if (note.is("Kadernice", "Kadeřnice", "Svatba kadeřnictví, kosmetika", "Svatba nehty, nákup", "Svatba účes", "Svatba stuhy",
                "Stuhy", "Svatba ubrousky", "Bijou")) {
            category = PERSONAL_OTHER.full();
        } else if (note.is("Poplatek čsk", "Tisk", "Pošta", "Pas", "Řidičák", "Popelnice", "Doklady fotky na řidičák",
                "Doklady trvalé bydliště", "Nájem cernotin", "Absolventská karta")) {
            category = PERSONAL_FEES.full();
        } else if (note.is("obědy")) {
            category = FOOD_WORK_LUNCH.full();
        } else if (note.is("Nájem prosinec")) {
            category = HOUSEHOLD_SERVICES_FEES.full();
        } else if (note.is("Benzín začátek prosince", "Benzín", "Plyn")) {
            category = CAR_FUEL.full();
        } else if (note.is("Alpy", "Pojištění", "Lyže", "Plavání permanentka", "Pobyt na horách", "Inline 24", "Pasohlávky wake", "Jóga", "Plavání", "Lyže", "Skipas", "Aply", "Permanentka bluegym", "Broušení lyží", "Skipasy 2x")) {
            category = SPORT_FEES.full();
        } else if (note.is("Vybavení", "Posilovací guma", "Brašna", "Karimatka", "Kolečka")) {
            category = SPORT_EQUIPMENT.full();
        } else if (note.is("Box")) {
            category = HOUSEHOLD_IMPROVEMENT.full();
        } else if (note.is("ČSK PŘÍSPĚVEK", "Předporodní kurz")) {
            category = PERSONAL_EDUCATION.full();
        } else if (note.is("Salinkarta", "Bus", "Autobus", "Vlaky do Brna R,k", "Vlaky hranice",
                "Vlaky podruhé")) {
            category = PERSONAL_TRANSPORTATION.full();
        } else if (note.is("Rudy nákrčník")) {
            category = PERSONAL_GIFTS.full();
        } else if (note.is("Laser")) {
            category = HOUSEHOLD_HEALTHCARE.full();
        } else if (note.is("Knihy", "Kniha")) {
            category = ENTERTAINMENT_BOOKS.full();
        } else if (note.is("Paintball", "Bowling")) {
            category = ENTERTAINMENT_OTHER.full();
        } else if (note.is("Výběr hotovosti", "Stromek", "Polsko", "Kapesné")) {
            category = VACATION_OTHER.full();
        } else if (note.is("Jídlo", "Chlast")) {
            category = VACATION_FOOD.full();
        } else if (note.is("Mallorca", "Ubytování Krumlov", "Oběd Krkonoše", "Silvestr", "Atc Merkur, stan")) {
            category = VACATION_ACCOMMODATION.full();
        } else if (note.is("Letenky", "Vlaky", "Nafta Jirka", "Buš Vídeň", "Hradec")) {
            category = VACATION_TRANSPORTATION.full();
        } else if (note.is("Svatba kafe")) {
            category = FOOD_SNACK.full();
        } else if (note.is("Svatba nákup pití", "Svatba vysluzky", "Jídlo na párty", "Jídlo sklípek", "U kiky")) {
            category = ENTERTAINMENT_PARTY_PUB.full();
        } else if (note.is("Pevný disk")) {
            category = ELECTRONICS_COMPUTER.full();
        } else if (note.is("Obal na mobil")) {
            category = ELECTRONICS_PHONE.full();
        } else if (note.is("Foťák")) {
            category = ELECTRONICS_CAMERA.full();
        }
        if (category == null) {
            logger.warn("No mapping found for {} with note:'{}', using PERSONAL_OTHER", originalCategory, note);
            category = PERSONAL_OTHER.full();
        }
        return category;
    }
}
