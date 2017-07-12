package cz.rudypokorny.expense.model;

import cz.rudypokorny.expense.entity.Validable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rudolf on 10/07/2017.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Account extends Auditable implements Validable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private List<Expense> expenses = new ArrayList<>();

    public static Account named(String name) {
        Account account = new Account();
        account.setName(name);
        return account;
    }

    public Long getId() {
        return id;
    }

    void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }
}
