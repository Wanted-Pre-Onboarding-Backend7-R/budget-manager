package server.preonboarding.budgetmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.preonboarding.budgetmanager.domain.type.BudgetCategoryType;
import server.preonboarding.budgetmanager.service.BudgetService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/budgets")
@RestController
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping("/categories")
    public List<BudgetCategoryType> getCategories() {
        return budgetService.getCategories();
    }

}
