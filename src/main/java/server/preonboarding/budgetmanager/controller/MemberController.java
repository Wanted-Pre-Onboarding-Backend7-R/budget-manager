package server.preonboarding.budgetmanager.controller;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.preonboarding.budgetmanager.dto.MemberJoinRequest;
import server.preonboarding.budgetmanager.dto.ValidationSequence;
import server.preonboarding.budgetmanager.service.MemberService;

@RequestMapping("/api/members")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> join(@RequestBody @Validated(ValidationSequence.class) MemberJoinRequest dto) {
        memberService.join(dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
