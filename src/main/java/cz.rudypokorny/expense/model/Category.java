package cz.rudypokorny.expense.model;

import cz.rudypokorny.expense.entity.Validable;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Rudolf on 10/07/2017.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Category implements Serializable, Validable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @ManyToOne
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children;

    public static Category named(String name) {
        Category category = new Category();
        category.name = name;
        return category;
    }

    public static Category namedWithParent(String name, Category parent) {
        Category category = new Category();
        category.name = name;
        category.parent = parent;
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

    public Category getParent() {
        return parent;
    }

    void setParent(Category parent) {
        this.parent = parent;
    }


    public List<Category> getChildren() {
        return children;
    }

    @Transient
    public boolean isValid() {
        return !StringUtils.isEmpty(name);
    }


}
