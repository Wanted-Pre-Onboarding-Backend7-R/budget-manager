package server.preonboarding.budgetmanager.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.preonboarding.budgetmanager.domain.type.BudgetCategoryType;
import server.preonboarding.budgetmanager.domain.type.BudgetCategoryTypeConverter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class BudgetCategory extends BaseIdEntity {

    @Convert(converter = BudgetCategoryTypeConverter.class)
    @Column(length = 8, nullable = false, insertable = false, updatable = false, unique = true)
    private BudgetCategoryType type;

}
