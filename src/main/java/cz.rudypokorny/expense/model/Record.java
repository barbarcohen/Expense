package cz.rudypokorny.expense.model;

import cz.rudypokorny.expense.entity.AccountAware;
import cz.rudypokorny.expense.entity.Validable;
import cz.rudypokorny.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Created by Rudolf on 10/07/2017.
 */
@Entity
@Table
public class Record extends Auditable implements Serializable, Validable, AccountAware {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Double amount;

    @Column
    @NotNull
    private ZonedDateTime when;

    @Column
    private String note;

    @ManyToOne
    private Category category;

    @ManyToOne
    @NotNull
    private Account account;

    @Transient
    private String vendor;

    @Transient
    private String currency;

    @Transient
    private String extId;

    @Transient
    private String payment;

    public static Record newExpense(Double amount) {
        Record record = new Record();
        record.setAmount(Objects.requireNonNull(amount));
        record.setWhen(DateUtil.getCurrentDateTime());
        return record;
    }

    public Record noted(String note) {
        this.setNote(note);
        return this;
    }


    public Record by(Account account) {
        this.setAccount(Objects.requireNonNull(account));
        return this;
    }

    public Record at(ZonedDateTime when) {
        if (when != null) {
            this.setWhen(when);
        }
        return this;
    }

    public Record on(Category category) {
        this.category = category;
        return this;
    }

    public Record EXT(String extId) {
        this.extId = extId;
        return this;
    }

    public Record vendor(String vendor) {
        this.vendor = vendor;
        return this;
    }

    public Record currency(String currency) {
        this.currency = currency;
        return this;
    }

    public Record payment(String payment) {
        this.payment = payment;
        return this;
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    void setAmount(Double amount) {
        this.amount = amount;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public ZonedDateTime getWhen() {
        return when;
    }

    void setWhen(ZonedDateTime when) {
        this.when = when;
    }

    public String getNote() {
        return note;
    }

    void setNote(String note) {
        this.note = note;
    }

    public Category getCategory() {
        return category;
    }

    void setCategory(Category category) {
        this.category = category;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public void setAccount(Account account) {
        this.account = account;
    }

    public String getVendor() {
        return vendor;
    }

    public String getCurrency() {
        return currency;
    }

    public String getExtId() {
        return extId;
    }

    public String getPayment() {
        return payment;
    }

    @Transient
    public boolean hasCategory() {
        return category != null && category.isValid();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(id, record.id) &&
                Objects.equals(amount, record.amount) &&
                Objects.equals(when, record.when) &&
                Objects.equals(note, record.note) &&
                Objects.equals(category, record.category) &&
                Objects.equals(account, record.account) &&
                Objects.equals(vendor, record.vendor) &&
                Objects.equals(currency, record.currency) &&
                Objects.equals(extId, record.extId) &&
                Objects.equals(payment, record.payment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, when, note, category, account, vendor, currency, extId, payment);
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", amount=" + amount +
                ", when=" + when +
                ", note='" + note + '\'' +
                ", category=" + category +
                ", account=" + account +
                ", vendor='" + vendor + '\'' +
                ", currency='" + currency + '\'' +
                ", extId='" + extId + '\'' +
                ", payment='" + payment + '\'' +
                '}';
    }
}
