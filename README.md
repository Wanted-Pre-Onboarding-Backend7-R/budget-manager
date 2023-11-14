# BudgetManager - 예산 관리 웹 백엔드 서버

본 서비스는 사용자들이 **개인 예산을 설정하고 지출을 추적/모니터링하여 개인 재무 목표를 달성**하는 데 도움을 주는 웹 애플리케이션입니다. 

<br/>

## 0. 목차
- [1. 프로젝트 소개](#1-프로젝트-소개)
  - [1-A. 주요 기능](#1-A-유저스토리)
  - [1-B. 기술 스택](#1-B-기술-스택)
- [2. API 설계](#2-api-설계)
- [3. ERD 설계](#3-erd-설계)
- [4. 구현과정(설계 및 의도)](#4-구현과정--설계-및-의도-)
- [5. 트러블슈팅 및 회고](#5-트러블슈팅-및-회고)
- [6. 규칙](#6-규칙)
- [7. 작성자](#7-작성자)


<br/>


## 1. 프로젝트 소개

### 1-A. 유저스토리

[요구사항 링크](https://bow-hair-db3.notion.site/90cba97a58a843e4a2563a226db3d5b5#8bf25fd04a5045edbdde0ed0a507949a)

- **A. 유저**는 본 사이트에 들어와 회원가입을 통해 서비스를 이용합니다.
- **B. 예산 설정 및 설계 서비스**
    - `월별` 총 예산을 설정합니다.
    - 본 서비스는 `카테고리` 별 예산을 설계(=추천)하여 사용자의 과다 지출을 방지합니다.
- **C. 지출 기록**
    - 사용자는 `지출` 을  `금액`, `카테고리` 등을 지정하여 등록 합니다. 언제든지 수정 및 삭제 할 수 있습니다.
- **D. 지출 컨설팅**
    - `월별` 설정한 예산을 기준으로 오늘 소비 가능한 `지출` 을 알려줍니다.
    - 매일 발생한 `지출` 을 `카테고리` 별로 안내받습니다.
- **E. 지출 통계**
    - `지난 달 대비` , `지난 요일 대비`,  `다른 유저 대비` 등 여러 기준 `카테고리 별` 지출 통계를 확인 할 수 있습니다.


### 1-B. 기술 스택

- 언어 및 프레임워크: 
![Java](https://img.shields.io/badge/Java-%2017%20-lightcoral.svg?&style=flat&logo=Java&logoColor=white&labelColor=red)
![Spring Boot](https://img.shields.io/badge/SpringBoot-%203.1.5%20-lightgreen.svg?&style=flat&logo=SpringBoot&logoColor=white&labelColor=6DB33F)

- DB 및 데이터 접근 기술: 
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=flat&logo=springdatajpa&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-blue?style=flat&logo=&logoColor=white")
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white)
![H2](https://img.shields.io/badge/H2-4479A1?style=flat&logo=H2&logoColor=white)

- 인증/인가: 
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=flat&logo=JSON%20web%20tokens)

- 기타:
![Swagger](https://img.shields.io/badge/Swagger-%ffffff.svg?style=flat&logo=swagger&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=flat&logo=gradle&logoColor=white)

<br/>


## 2. API 설계

<br/>


## 3. ERD 설계

<br/>


## 4. 구현과정(설계 및 의도)

<br/>


## 5. 트러블슈팅 및 회고

<br/>


## 6. 규칙

<details>
<summary><strong>Code 컨벤션</strong> - click</summary>

- 변수명: boolean인 경우 형용사, 그 외 명사
- 함수명: 동사 현재형으로 시작
- 클래스명: 명사
- if, for 중괄호 한 줄이라도 항상 치기
- 커밋하기 전에
    - import 정리: `ctrl + alt(option) + o`
    - line formatting: `ctrl(command) + alt(option) + l`
    - 마지막 빈 줄 추가
  ```java
  /** 예시 **/
  public class Clazz {

      public int addCountIfValid(int count, boolean isValid) {
          if (isValid) {
              return count + 1;
          }
          return count;
      }
  }
  // 마지막 빈 줄
  ```

- Optional 줄바꿈
   ```java
   Member member = memberRepository.findByEmail(dto.getEmail())
         .orElseThrow(NotFoundMemberByEmailException::new);`
   ```
- 객체 생성 규칙
    - 외부에서 직접적인 new 지양하고 내부적으로 활용 `@Builder` 및 정적 팩토리 메서드 활용
    - 정적 팩토리 메서드 이름은 단일 인자일 경우 `from`, 다중 인자일 경우는 `of`로 명명
    - Bean 제외 DTO, Entity들은 `@All-/@Required-ArgsContructor` 활용 제한, 직접 코드로 생성자 작성 및 private/protected 등으로 잠그기
    - 목적: 같은 타입의 필드 연속될 때 1) 잘못된 값 입력하는 human error 최소화, 2) 필드 순서를 바꿀 경우 IDE에 의한 리팩토링이 적용되지 않는 Lombok 에러 방지, 3) 가독성을 위한 작성법 통일을 위하여
  ```java
   @Getter
   @NoArgsConstructor(access = AccessLevel.PROTECTED)
   @EqualsAndHashCode(of = "accountName", callSuper = false)
   @Entity
   public class Member extends BaseEntity {
 
       @Column(nullable = false, unique = true)
       private String accountName;
 
       @Column(nullable = false)
       private String email;
   
       @Column(length = 60, nullable = false)
       private String password;
 
       @Column(length = 6, nullable = false)
       private String approvalCode;
   
       @Column(nullable = false)
       private Boolean isApproved;
   
       @Enumerated(EnumType.STRING)
       private Authority authority;
   
       @Builder
       private Member(String accountName, String email, String password, String approvalCode, Boolean isApproved) {
           this.accountName = accountName;
           this.email = email;
           this.password = password;
           this.approvalCode = approvalCode;
           this.isApproved = isApproved;
           authority = Authority.ROLE_USER;
       }
 
       public static Member of(MemberJoinRequest dto, String encodedPassword, String approvalCode) {
           return builder()
                   .accountName(dto.getAccountName())
                   .email(dto.getEmail())
                   .password(encodedPassword)
                   .approvalCode(approvalCode)
                   .isApproved(false)
                   .build();
       }
   }

  ```

</details>

<br/>

<details>
<summary><strong>Git 컨벤션</strong> - click</summary>

- **commit message rules**

  | type     | description |
        |----------|-------------|
  | feat     | 새로운 기능 추가 |
  | fix      | 버그 및 로직 수정 |
  | refactor | 기능 변경 없는 코드 구조, 변수/메소드/클래스 이름 등 수정 |
  | style    | 코드 위치 변경 및 포맷팅, 빈 줄 추가/제거, 불필요한 import 제거 |
  | test     | 테스트 코드 작성 및 리팩토링 |
  | setup    | build.gradle, application.yml 등 환경 설정 |
  | docs     | 문서 작업 |

    ```bash
    # commit title format
    git commit -m "{커밋 유형} #{이슈번호}: #{내용}"
  
    # example of git conventions
    git commit -m "refactor #125: `ChatService` 중복 로직 추출
  
    예외 압축
    메소드 위치 변경
    메소드 이름 변경
    "
    ```

- **branch naming rules**
    ```bash
    # branch name format
    git checkout -b "feat/#{이슈번호}-{내용}"
    ```

</details>

<br/>


## 7. 작성자

| name | github | email |
|------|--------|-------|
| 정준희 | [@JoonheeJeong](https://github.com/JoonheeJeong) | jeonggoo75@gmail.com |

<br/>
