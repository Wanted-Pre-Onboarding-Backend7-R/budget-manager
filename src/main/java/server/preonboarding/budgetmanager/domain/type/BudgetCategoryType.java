package server.preonboarding.budgetmanager.domain.type;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum BudgetCategoryType {

    FOOD("식비"),
    HOUSE("주거"),
    COMMUNICATION("통신"),
    TRANSPORTATION("교통"),
    HOUSEHOLD("생활용품"),
    CLOTHING("의류"),
    BEAUTY("미용"),
    HEALTH("건강"),
    CULTURE("문화"),
    EDUCATION("교육"),
    TRIBUTE("경조사"),
    DUES("회비"),
    TAX("세금"),
    INTEREST("이자"),
    AUTOMOBILE("차량"),
    INFANT_CARE("육아"),
    ETC("기타"),
    ;

    @JsonValue
    private final String desc;

    public static BudgetCategoryType ofDesc(String dbData) {
        return Arrays.stream(values())
                .filter(e -> e.getDesc().equals(dbData))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(getErrorMessage(dbData)));
    }

    private static String getErrorMessage(String dbData) {
        return "예산 카테고리에 desc=\"%s\"이 존재하지 않습니다.".formatted(dbData);
    }

}
