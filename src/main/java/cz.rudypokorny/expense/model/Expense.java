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
public class Expense extends Auditable implements Serializable, Validable, AccountAware {

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

    public static Expense newExpense(Double amount) {
        Expense expense = new Expense();
        expense.setAmount(Objects.requireNonNull(amount));
        expense.setWhen(DateUtil.getCurrentDateTime());
        return expense;
    }

    public Expense noted(String note) {
        this.setNote(note);
        return this;
    }

    public Expense by(Account account) {
        this.setAccount(Objects.requireNonNull(account));
        return this;
    }

    public Expense at(ZonedDateTime when) {
        if (when != null) {
            this.setWhen(when);
        }
        return this;
    }

    public Expense on(Category category) {
        this.category = category;
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

    @Transient
    public boolean hasCategory() {
        return category != null && category.isValid();
    }

}
