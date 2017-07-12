package cz.rudypokorny.expense.entity;

import java.util.List;


public interface ImportSource<T> {

    List<T> loadDate();
}
