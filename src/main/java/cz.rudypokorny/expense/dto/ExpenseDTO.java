package cz.rudypokorny.expense.dto;

import cz.rudypokorny.expense.entity.Validable;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Created by Rudolf on 03/08/2017.
 */
public class ExpenseDTO implements Serializable, Validable {

    private Long id;
    @NotNull
    private Double amount;

    private ZonedDateTime when;

    private String note;

    private String category;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getWhen() {
        return when;
    }

    public void setWhen(ZonedDateTime when) {
        this.when = when;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
