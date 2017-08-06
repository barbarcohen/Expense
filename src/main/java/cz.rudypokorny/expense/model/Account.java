package cz.rudypokorny.expense.model;

import cz.rudypokorny.expense.entity.Validable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "activeAccount")
    private List<User> activeUsers = new ArrayList<>();

    public Account(Long id) {
        this.id = id;
    }

    public Account() {
    }

    public static Account named(String name) {
        Objects.requireNonNull(name, "Account name cannot be null.");
        Account account = new Account();
        account.setName(name);
        return account;
    }

    public Account activeFor(User user){
        Objects.requireNonNull(user, "User cannot be null.");
        this.activeUsers.add(user);
        return this;
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

    public List<User> getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(List<User> activeUsers) {
        this.activeUsers = activeUsers;
    }

    @Transient
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name +
                '}';
    }
}
