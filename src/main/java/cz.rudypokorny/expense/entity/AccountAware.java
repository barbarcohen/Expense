package cz.rudypokorny.expense.entity;

import cz.rudypokorny.expense.model.Account;

/**
 * Created by Rudolf on 03/08/2017.
 */
public interface AccountAware {

    Account getAccount();

    void setAccount(Account account);
}
