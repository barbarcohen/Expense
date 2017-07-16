package cz.rudypokorny.expense.validator;

import cz.rudypokorny.expense.entity.Rules;
import cz.rudypokorny.expense.model.Category;
import org.springframework.stereotype.Component;

@Component("categoryValidator")
public class CategoryValidator implements IValidator<Category> {

    @Override
    public Rules validateNew(Category entity) {
        Rules rules = new Rules();
        //TODO only BS rules
        if(entity == null){
            return rules.broken(Rules.Messages.ENTITY_IS_NULL);
        }
        if(entity.getName() == null){
            rules.broken("name is null");
        }
        return rules;
    }

    @Override
    public Rules validateUpdate(Category entity) {
        return null;
    }

    @Override
    public Rules validateDelete(Category entity) {
        return null;
    }
}
