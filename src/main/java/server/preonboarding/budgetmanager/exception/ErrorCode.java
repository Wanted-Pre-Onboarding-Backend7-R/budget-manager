package server.preonboarding.budgetmanager.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    EMAIL_BLANK("이메일의 입력 값이 비어있습니다.", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID_FORMAT("이메일 형식이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_BLANK("비밀번호의 입력 값이 비어있습니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID_FORMAT("비밀번호는 8자 이상 24자 이하의 영문, 숫자, 특수문자의 조합만 가능합니다.", HttpStatus.BAD_REQUEST),
    EMAIL_DUPLICATE("같은 이름의 이메일이 존재합니다.", HttpStatus.BAD_REQUEST),

    MEMBER_INFO_WRONG("이메일이나 비밀번호가 틀렸습니다.", HttpStatus.BAD_REQUEST),

    AUTH_HEADER_NOT_FOUND("인가 헤더가 존재하지 않습니다.", HttpStatus.UNAUTHORIZED),
    AUTH_TYPE_UNMATCHED("인가 타입이 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    AUTH_TOKEN_EXPIRED("토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    AUTH_TOKEN_INVALID("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
    AUTH_ACCESS_DENIED("접근이 거부되었습니다.", HttpStatus.FORBIDDEN),
    AUTH_FAILED("인증을 실패했습니다.", HttpStatus.UNAUTHORIZED),

    MEMBER_NOT_FOUND_BY_ID("주어진 토큰에 해당하는 회원이 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR),


    ERROR_CODE_NOT_FOUND("주어진 이름과 일치하는 에러코드가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ERROR_CODE_NOT_ASSIGNED("해당 입력 값의 검증 오류에 대한 에러코드가 지정되지 않았습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_SERVER_ERROR("서버 내부에서 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final String message;
    private final HttpStatus httpStatus;

}
