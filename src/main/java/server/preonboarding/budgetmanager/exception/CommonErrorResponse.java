package server.preonboarding.budgetmanager.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Getter
public class CommonErrorResponse {

    private final String errorCode;
    private final String message;

    @Builder
    private CommonErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    static ResponseEntity<CommonErrorResponse> makeErrorResponse(ErrorCode errorCode) {
        CommonErrorResponse response = CommonErrorResponse.from(errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    public static CommonErrorResponse from(ErrorCode errorCode) {
        return CommonErrorResponse.builder()
                .errorCode(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

}
