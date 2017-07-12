package cz.rudypokorny.expense.validator;

import cz.rudypokorny.expense.entity.Rules;
import cz.rudypokorny.expense.model.Account;
import org.springframework.stereotype.Component;

@Component("accountValidator")
public class AccountValidator implements IValidator<Account> {

    @Override
    public Rules validate(Account entity) {
        //TODO only BS rules
        Rules rules = new Rules();
        if(entity == null){
            return rules.broken("account is null");
        }

        //TODO implement
        return rules;
    }
}
