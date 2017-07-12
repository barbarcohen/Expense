package cz.rudypokorny.expense.validator;

import cz.rudypokorny.expense.entity.Rules;
import cz.rudypokorny.expense.model.Category;
import org.springframework.stereotype.Component;

@Component("categoryValidator")
public class CategoryValidator implements IValidator<Category> {

    @Override
    public Rules validate(Category entity) {
        Rules rules = new Rules();
        //TODO only BS rules
        if(entity == null){
            return rules.broken("Category is null");
        }
        if(entity.getName() == null){
            rules.broken("name is null");
        }
        return rules;
    }
}
