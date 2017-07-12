package cz.rudypokorny.expense.model;

import cz.rudypokorny.expense.entity.Validable;
import cz.rudypokorny.expense.entity.Rules;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Rudolf on 10/07/2017.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Category implements Serializable, Validable{

    @Transient
    private Rules brokenRules = new Rules();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    public static Category named(String name){
        Category category = new Category();
        category.name = name;
        return category;
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

    @Transient
    public boolean isValid(){
        return !StringUtils.isEmpty(name);
    }


}
