package cz.rudypokorny.expense.model;

import cz.rudypokorny.expense.model.Account;

import java.util.Random;

/**
 * Created by Rudolf on 11/07/2017.
 */
public class AccountCreator {

    private static Random random = new Random();

    public static Account create(){
        Account account = new Account();
        account.setName(String.valueOf(random.nextInt()));
        account.setId(random.nextLong());
        return account;
    }
}
