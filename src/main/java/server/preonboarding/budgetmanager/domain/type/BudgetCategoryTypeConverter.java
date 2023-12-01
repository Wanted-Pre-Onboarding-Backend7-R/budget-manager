package server.preonboarding.budgetmanager.domain.type;

import jakarta.persistence.AttributeConverter;

public class BudgetCategoryTypeConverter implements AttributeConverter<BudgetCategoryType, String> {

    @Override
    public String convertToDatabaseColumn(BudgetCategoryType attribute) {
        return attribute.getDesc();
    }

    @Override
    public BudgetCategoryType convertToEntityAttribute(String dbData) {
        return BudgetCategoryType.ofDesc(dbData);
    }

}
