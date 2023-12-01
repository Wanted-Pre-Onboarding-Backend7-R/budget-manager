package server.preonboarding.budgetmanager.service;

import org.springframework.stereotype.Service;
import server.preonboarding.budgetmanager.domain.type.BudgetCategoryType;

import java.util.List;

@Service
public class BudgetService {

    public List<BudgetCategoryType> getCategories() {
        return List.of(BudgetCategoryType.values());
    }

}
