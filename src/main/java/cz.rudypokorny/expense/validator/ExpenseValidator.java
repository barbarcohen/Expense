package cz.rudypokorny.expense.validator;

import cz.rudypokorny.expense.entity.Rules;
import cz.rudypokorny.expense.model.Expense;
import org.springframework.stereotype.Component;

@Component(value = "expenseValidator")
public class ExpenseValidator implements IValidator<Expense> {

    @Override
    public Rules validate(Expense entity) {
        Rules rules = new Rules();
        //TODO only BS rules
        if (entity == null) {
            return rules.broken("Expense is null");
        }
        //FIXME these validation could be icked by hibernate manually
        if (entity.getAmount() == null) {
            rules.broken("amount is null");
        }
        if (entity.getAccount() == null || entity.getAccount().getId() == null) {
            rules.broken("account is null");
        }

        return rules;
    }

}
