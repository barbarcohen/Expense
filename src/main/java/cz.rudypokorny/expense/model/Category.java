package cz.rudypokorny.expense.model;

import cz.rudypokorny.expense.entity.Validable;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

//TODO Categoreis per Account. Account can be used by many users. Users are using GROUP
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Category implements Serializable, Validable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private List<Expense> expenses;

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

    public Category withParent(String name) {
        if (name != null) {
            Category parent = Category.named(name);
            this.parent = parent;
        }
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

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", parent=" + parent +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (id != null ? !id.equals(category.id) : category.id != null) return false;
        if (!name.equals(category.name)) return false;
        if (parent != null ? !parent.equals(category.parent) : category.parent != null) return false;
        if (children != null ? !children.equals(category.children) : category.children != null) return false;
        return expenses != null ? expenses.equals(category.expenses) : category.expenses == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        result = 31 * result + (expenses != null ? expenses.hashCode() : 0);
        return result;
    }
}
