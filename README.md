# alog 소개 - 사이드 프로젝트

실제 서비스하는 사이트는 여러 사람들이 보기 때문에 신경써서 만들어야 합니다. 그에 비해 저의 이전의 사이드 프로젝트는 저만 보면 됐기 때문에 대충(?) 만든 것이 티가 났습니다.  
이번에는 저도 그러한 사이트를 만들고 싶었고, 실제 서비스하는 사이트 하나를 결정해서 똑같이 만들기로 결정했습니다. (~~절대 아이디어가 없어서가 아닙니다..~~)

velog(벨로그)는 velopert님이 만든 개발자를 위한 블로그 서비스입니다.  
실제로 velog를 자주 보는 이유는 velog에 올라오는 글들의 퀄리티가 너무 괜찮고 보다보면 얻어가는 것이 더 많기 때문에 주로 눈팅(?)을 많이 했습니다.

그래서 평소 즐겨보는 velog 짝퉁(클론 코딩)을 만들어보면 좋겠다 생각했습니다. 

> 프로젝트 이름인 alog 에서 a 는 Azure(푸른 하늘)를 뜻합니다.

## 🕒 프로젝트 개발 기간

- 개인 프로젝트
- 프로젝트 기간: 2022-10-08 ~ 2022-12-08 (두 달간 진행)

## 🎨 기술스택

- Java 11
- Intellij Community, VS Code
- Spring 5.x MVC
- Spring Boot 2.7.x
- Spring Security
- Spring Data JPA
- QueryDSL 5.0.0
- H2 Database 2.1.214
- MariaDB 10.6
- Gradle 7.5
- Mustache
- Git
- Junit 5
  - MockMvc
- AWS
  - EC2
  - RDS
  - S3

## 🎻 API

- http://azurelog.link/docs/member.html
- http://azurelog.link/docs/posts.html
- http://azurelog.link/docs/tempsave.html
- http://azurelog.link/docs/comment.html
- http://azurelog.link/docs/hashtag.html
- http://azurelog.link/docs/postsLike.html

## 🍀 기능 구현

- 로그인/회원가입
  - 이메일 인증을 통한 로그인/회원가입
  - OAuth2 Client를 이용한 소셜로그인
    - Google, Naver, Kakao
- 회원 관리 (수정, 삭제)
  - 회원 썸네일 (추가, 삭제)
- 게시판 CMS 기능
  - 게시글 관리 (추가,조회,수정,삭제)
  - 댓글 관리 (추가,조회,수정,삭제)
  - 게시글 썸네일 관리 (추가,삭제,조회)
- 기본 게시판 기능
  - 등록, 수정, 삭제 기능 및 권한 처리 (내가 쓴 글만 수정, 삭제 가능)
  - 포스트 짧은 소개 (150자 제한)
  - 이미지(jpg, jpeg, jpe, png) 업로드 기능
  - 임시 저장 기능
  - 좋아요
  - 태그
  - 계층형 댓글 기능 (대댓글)
  - 검색, 정렬 기능 (무한스크롤 사용)
  - 페이징
  - `toast ui editor` 사용

## 🚀 프로젝트 구조

![alog_structure](https://user-images.githubusercontent.com/55525868/205878592-62bf750e-0d8e-4e1e-bf9c-b01596a6a7a5.png)

- 프론트
  - html, css, js (es6+)
  - jquery, ajax
- 백
  - Spring Security로 사용자 인증
    - 로그인 폼, 비밀번호없이 강제 인증
  - JPA를 이용한 객체지향적 테이블 관리 (ORM)
  - DTO를 통한 도메인 순수성 보장

## 🎫 데이터베이스 구조

### 요구사항

- 사용자는 소셜 로그인, 이메일 로그인을 할 수 있다.
- 로그인한 사용자는 게시글, 댓글을 작성할 수 있다.
- 로그인한 사용자는 댓글에 댓글을 작성할 수 있다.
- 로그인한 사용자는 회원과 게시글에 이미지를 업로드할 수 있다.
- 로그인한 사용자는 게시글을 작성하다 `임시저장`을 할 수 있다.
  - 임시저장은 10초마다 자동으로 저장될 수 있다.
  - 글 작성을 완료하거나 임시저장한 글을 수정해서 글 작성이 완료되면 해당 임시저장은 사라진다.
  - 임시저장은 제목과 내용만 저장이 된다.
- 로그인한 사용자는 게시글에 좋아요와 태그를 추가할 수 있다.
  - 태그 저장시 `#`은 모두 제거되고 DB에 저장된다.
- 태그를 클릭하면 태그 목록을 확인할 수 있다.
- 메인 화면에는 최신순과 좋아요순으로 볼 수 있도록 정렬 기능을 추가한다.
- 검색 페이징은 무한스크롤을 사용한다.

![erd2](https://user-images.githubusercontent.com/55525868/212695935-1f065110-807f-40eb-b173-12583e140708.PNG)

## 🌊 패키지 구조 (계층형 구조)

- 계층형 구조: 각 계층을 대표하는 디렉터리를 기준으로 구성
  - 장점: 전체적인 구조를 빠르게 파악할 수 있다. (프로젝트가 작은 경우에 사용하면 좋음)
  - 대신 한 패키지 안에 다양한 도메인이 섞여 있어 응집도가 낮다.
- 도메인형 구조: 각 도메인을 대표하는 디렉터리를 기준으로 구성
  - 장점: 관련된 코드들이 응집해 있는 장점이 있어 나중에 도메인을 분리할 때 유용하다. (프로젝트가 큰 경우에 사용하면 좋음)
  - 대신 전체적인 구조를 파악하는데 좀 어려울 수 있다.

```groovy
└── src
  ├── main
  │   ├── java
  │   │   └── com
  │   │       └── azurealstn
  │   │           └── alog
  │   │               ├── AlogApplication.java
  │   │               ├── domain
  │   │               │   ├── comment
  │   │               │   ├── email
  │   │               │   ├── hashtag
  │   │               │   ├── image
  │   │               │   ├── like
  │   │               │   ├── member
  │   │               │   ├── posts
  │   │               │   └── tempsave
  │   │               ├── controller
  │   │               │   ├── comment
  │   │               │   ├── email
  │   │               │   ├── hashtag
  │   │               │   ├── image
  │   │               │   ├── like
  │   │               │   ├── member
  │   │               │   ├── posts
  │   │               │   └── tempsave
  │   │               ├── dto
  │   │               │   ├── auth
  │   │               │   ├── comment
  │   │               │   ├── email
  │   │               │   ├── hashtag
  │   │               │   ├── image
  │   │               │   ├── like
  │   │               │   ├── login
  │   │               │   ├── member
  │   │               │   ├── posts
  │   │               │   └── tempsave
  │   │               ├── repository
  │   │               │   ├── comment
  │   │               │   ├── email
  │   │               │   ├── hashtag
  │   │               │   ├── image
  │   │               │   ├── like
  │   │               │   ├── member
  │   │               │   ├── posts
  │   │               │   └── tempsave
  │   │               ├── service
  │   │               │   ├── comment
  │   │               │   ├── email
  │   │               │   ├── hashtag
  │   │               │   ├── image
  │   │               │   ├── like
  │   │               │   ├── login
  │   │               │   ├── member
  │   │               │   ├── posts
  │   │               │   └── tempsave
  │   │               ├── infra
  │   │               │   ├── convert
  │   │               │   ├── exception
  │   │               │   ├── utils
  │   │               ├── config
  │   │               │   ├── auth
  │   └── resources
  │       ├── application.yml
  │       ├── application-mail.yml
  │       ├── application-oauth.yml
  │       └── application-aws.yml
```

## 🗻 git 협업

프로젝트를 혼자서 구현했기 때문에 git 협업 경험이 없습니다. 그래서 혼자서 브랜치를 따고 아래와 같은 패턴으로 소스 형상 관리를 했습니다.

1. 브랜치 생성 -> 브랜치 이동
2. 소스 수정 -> `git add .` -> `git commit`
3. main 브랜치 이동 -> `git merge --no-ff --log 브랜치명`
4. 브랜치 삭제 -> 새 브랜치 생성 (1번으로 이동, 반복...)

커밋 메시지는 `git commit message convention`을 참고해서 커밋을 진행했습니다.

## 🎆 배포 진행

1. `git clone` 혹은 `git pull`을 통해 새 버전의 프로젝트 받는다.
2. Gradle이나 Maven을 통해 프로젝트 테스트와 빌드
3. EC2 서버에서 해당 프로젝트 실행 및 재실행

위 과정에서 테스트 실패해서 소스 수정하고 `git push`를 했다면 `git pull`을 받아서 다시 테스트를 실행한다.

- gradlew 권한 : chmod +x ./gradlew

위 과정을 자동화하기 위해 `Shell Script`를 만들었습니다.

```shell
경로: /home/ec2-user/app/project/deploy.sh
실행순서:
1. git pull -> git 최신 업데이트
2. ./gradlew build -> gradle build 진행
3. jar 파일 복사
5. kill -pid -> 현재 구동중이면 애플리케이션 죽이고 새로 배포
6. nohup java -jar -Dspring.config.location=~ -Dspring.profiles.active=real alog-0.0.1-SNAPSHOT.jar
-> nohup을 사용하여 서버를 꺼도 계속 실행할 수 있도록 설정
-> -Dspring.config.location 설정으로 yml 경로 지정
-> -Dspring.profiles.active 설정으로 운영(real) profile 지정
```

## 👓 API 문서

클라이언트 입장에서는 어떤 API가 있는지 모르기 때문에 백엔드에서 API를 잘 정리해서 전달할 필요가 있습니다. 백엔드에서 개발한 실제 코드를 토대로 자동으로 API 문서화를 만들어주는 툴들이 있습니다.

- Swagger
- Postman
- GitBook
- I/O Docs
- Spring REST Docs

### Spring REST Docs의 장점

위 툴들 중에 Spring REST Docs를 사용하는 이유는 다음과 같습니다.

1. 배포된 운영코드를 수정하지 않아도 됩니다. (운영코드에 영향 X)
2. 코드가 변경이 되어도 API 문서도 같이 최신화 됩니다.
  - Test Case 기반으로 문서를 생성
  - API가 개발되면 Test Case 작성과 통과하는 것이 굉장히 중요

![apiDocs](https://user-images.githubusercontent.com/55525868/194799370-bbbf6b5f-7980-4043-8664-6c6f2fa0a51c.PNG)

### asciidoctor 실행

![asciidoctor](https://user-images.githubusercontent.com/55525868/194816059-8f7487d2-db03-4532-8d23-1334eea06b33.PNG)

1. asciidoctor 실행

![build](https://user-images.githubusercontent.com/55525868/194810511-9e5d75ad-898f-45ce-863e-4554a1046624.PNG)

2. build 실행

- asciidoctor는 test를 실행 후 성공하면 build 폴더에 snippets, html을 생성
- gradle build시 bootJar가 실행되면서 build 폴더에 있던 html을 /resources/static/docs 로 복사

## 🧱 어려웠던 점 & 해결

### 1. 강제 로그인 처리

- 기본적으로 Spring Security를 이용하면 인증/인가/Principal(접근주체)/Credential(비밀번호) 이 네 가지를 확인하여 사용자가 우리 사이트에 접근할 수 있는지 확인해줍니다.
- 하지만 이 프로젝트는 로그인이나 회원가입시에 **이메일 인증**을 통해서 진행하기 때문에 Credential(비밀번호) 부분이 필요가 없었습니다.
- 따라서 저는 비밀번호 없이도 로그인폼 없이도 강제로 로그인 처리할 수 있도록 개발하는데 어려움이 있었습니다.

<details>
<summary><b>기존 로그인 코드</b></summary>
<div markdown="1">

```java
@RequiredArgsConstructor
@Service
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User principal =  userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. " + username));
        return new PrincipalDetail(principal);
    }
}
```

</div>
</details>

- `UserDetailsService` 인터페이스를 상속받고 `loadUserByUsername` 메서드를 구현하여 `UserDetails`를 반환하는 Principal 사용자 객체를 넘겨주면 인증처리가 완료가 됩니다.
- 하지만 이는 강제 로그인에서는 처리할 수가 없었습니다.

<details>
<summary><b>강제 로그인 코드</b></summary>
<div markdown="1">

```java
private void forceLoginAuth(Member member) {
    List<GrantedAuthority> roles = new ArrayList<>();
    roles.add(new SimpleGrantedAuthority(member.getRoleKey()));
    Authentication authentication = new UsernamePasswordAuthenticationToken(member.getEmail(), null, roles);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    httpSession.setAttribute("member", new SessionMemberDto(member));
}
```

- 강제 로그인을 처리할 때는 `UsernamePasswordAuthenticationToken` 객체를 구현해서 `SecurityContextHolder` 클래스에 넘겨주면 됩니다.

</div>
</details>

### 2. 페이징 처리 (QueryDSL)

- Spring Data Jpa를 사용하면 기본적으로 페이징 처리에 많은 기능을 제공해주는 `Pageable` 인터페이스의 구현체인 `PageRequest` 클래스가 있습니다.
- 하지만 `Pageable`을 사용하면 좀 더 복잡한 검색이나 정렬이 들어가는 경우 기능이 다소 부족하기 때문에 직접 페이징을 구현하였습니다.
- 페이징 구현은 QueryDSL 라이브러리를 사용하였습니다.

<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">

```java
@Override
public List<Posts> findAllBySearch(PostsSearchDto searchDto) {
    return jpaQueryFactory
      .selectFrom(posts)
      .limit(searchDto.getSize())
      .offset(searchDto.getOffset())
      .where(eqTitleOrEqContent(searchDto.getSearchValue(), searchDto.getSearchValue()),
            posts.secret.eq(false))
      .orderBy(posts.likes.desc(), posts.id.desc())
      .fetch();
}

private BooleanExpression eqTitleOrEqContent(String title, String content) {
    if (StringUtils.hasLength(title) || StringUtils.hasLength(content)) {
        return posts.title.contains(title).or(posts.content.contains(content));
    }
    return null;
}
```

- 위 코드는 검색 페이지에서 사용하는 쿼리문입니다.
- `offset`과 `limit`으로 페이징 처리를 하였고, `orderBy`를 이용하여 좋아요순, 최신순으로 나오도록 정렬을 하였습니다.
- 그리고 `BooleanExpression`를 사용하여 검색시 복잡한 동적쿼리를 처리하는 메서드를 만들어서 사용했습니다.
- 또한 페이징에서 `totalRowCount`나 `prevPage` 같은 공통 컬럼들은 다른데서도 쓰이므로 공통 클래스인 `BasePageDto` 클래스를 하나 만들어서 사용하였습니다. (아래 코드)

```java
@ToString
@Getter
public class PostsSearchDto {

  private static final int MAX_SIZE = 200;

  private Integer page; //현재 페이지 번호
  private Integer size; //한 페이지당 데이터 수
  private BasePageDto basePageDto;
}

//Service단
int totalRowCount = postsRepository.findAllBySearchCount(searchDto);
BasePageDto basePageDto = new BasePageDto(searchDto.getPage(), searchDto.getSize(), totalRowCount);
searchDto.setBasePageDto(basePageDto);
```

</div>
</details>

### 3. 데이터 검증 & 예외처리

- 블로그 서비스를 개발하다보면 제목을 입력 안했다던지 내용을 입력을 안했다면 사용자에게 친절하게 알려줄 필요성을 느꼈습니다.
- 이러한 데이터 검증은 `BindingResult` 인터페이스를 컨트롤러마다 사용하여 처리할 수 있었습니다.
- 하지만 매번 컨트롤러마다 이러한 반복 코드를 적는 것은 너무나 조잡하고 복잡했습니다.

<details>
<summary><b>기존 코드</b></summary>
<div markdown="1">

```java
@PostMapping("/api/v1/posts")
public Map<String, String> posts(@Valid @RequestBody PostsCreateRequestDto requestDto, BindingResult result) {
    if (result.hasErrors()) {
        List<FieldError> fieldErrors = result.getFieldErrors();
        FieldError fieldError = fieldErrors.get(0);
        String fieldName = fieldError.getField();
        String defaultMessage = fieldError.getDefaultMessage();

        Map<String, String> error = new HashMap<>();
        error.put(fieldName, defaultMessage);
        return error;
    }
    return Map.of();
}
```

</div>
</details>

- 반복적인 코드를 줄이기 위해 `@RestControllerAdvice`를 사용하여 데이터 검증 예외가 발생하면 `@ExceptionHandler`가 붙은 메서드에서 처리할 수 있도록 전역에서 처리할 수 있도록 개선하였습니다.

<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">

```java
@Slf4j
@RestControllerAdvice
public class ExceptionApiController {

  /**
   * 에러가 발생했을 때 이 클래스에서 정의한 예외가 아닌 다른 예외가 터지면 PostsNotFound 예외가 터진다.
   * 하지만 PostsNotFound 예외는 RuntimeException을 상속받았기 때문에 무조건 서버에러(500)를 발생시킨다.
   * 따라서 발생한 에러에 대한 정확한 HTTP 상태코드를 발생시켜줘야 한다.
   * @ResponseStatus 대신에 ResponseEntity 클래스를 응답받는다.
   */
  @ExceptionHandler(GlobalException.class)
  public ResponseEntity<ErrorResponseDto> globalException(GlobalException e) {
    int statusCode = e.getStatusCode();
    ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
            .code(String.valueOf(statusCode))
            .message(e.getMessage())
            .build();

    ResponseEntity<ErrorResponseDto> responseEntity = ResponseEntity.status(statusCode).body(errorResponseDto);

    return responseEntity;
  }
}
```

- `PostsNotFound`나 `MemberNotFound` 같은 예외처리를 매번 `@ExceptionHandler` 메서드를 만들기에는 코드가 반복되기 때문에 `GlobalException`이라는 예외 클래스를 상속받게 만들어서 메서드 하나만 사용할 수 있게 하였습니다.
- `GlobalException` 클래스를 추상클래스로 만든 이유는 상태코드(status code)를 필수로 구현할 수 있도록 하기 위해서입니다.
- status code와 에러 Json 데이터를 세팅해주기 위해 `ResponseEntity`를 사용하였습니다.

</div>
</details>

## 💥 트러블 슈팅

### @ModelAttribute가 바인딩 되지 않는 문제

첫번째. DTO 클래스에 `@NoArgsConstructor`와 `@AllArgsConstructor` 둘 다 있는 경우 `NoArgsConstructor`를 호출하고, setter를 호출한 다음에 param을 필드에 각각 초기화를 한다.

두번째. 하지만 `@AllArgsConstructor`만 있는 경우 `@AllArgsConstructor`를 호출하고 param을 각각 초기화한 뒤에야 setter를 호출하여 다시 param 초기화를 덮어씌운다.

여기서 핵심은 첫번째는 setter를 먼저 호출하기 때문에 `@setter`가 있어야 한다. 따라서 이를 해결하기 위해 `@NoArgsConstructor`를 제거하면 된다.

- https://steady-coding.tistory.com/489

###  @Email은 null을 유효하다고 판단

Spring Boot의 validation 라이브러리의 `@Email`을 사용하면 빈 값(null)을 사용하면 유효성 체크에서 걸러지는줄 알았지만 `@Email`은 `null`을 허용한다.

- https://bellog.tistory.com/134

### Cannot construct instance of XXXDto - InvalidDefinitionException

DTO 클래스에 `@NoArgsConstructor`를 추가하면 해결 (생성자가 없어서 그런 것 같다.)

- https://yoo11052.tistory.com/158

### 세션에 저장할 때 Member 클래스를 사용하지 않은 이유

세션에 객체를 저장할 때는 직렬화를 구현해주어야 하는데 Member 클래스는 엔티티이기 때문에 도메인의 핵심이다. 그래서 따로 DTO 클래스를 만들어서 그 DTO에 직렬화를 구현해주면 된다. 

### could not initialize proxy - no Session

JPA에서 1대N 매핑 관계에서 Lazy 로딩이면 해당 객체가 필요로하면 그 때 가져온다. 이 에러가 나는 이유는 영속성 컨텍스트에서 관리되지 않는데 필요한 값을 가져오려 할 때 프록시 객체를 사용못한다는 의미이다.

그래서 전략을 Eager 로딩으로 변경하거나(비추천), 서비스단에 `@Transactional`을 추가한다. 

- https://cantcoding.tistory.com/78

### Failed to convert String to LocalDateTime

`String`에서 `LocalDateTime` 타입으로 변환이 잘 안된다면 `@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)`를 사용해보자.

- https://stackoverflow.com/questions/40274353/how-to-use-localdatetime-requestparam-in-spring-i-get-failed-to-convert-string

### MockMvc와 SpringBootTest 같이 사용하기

- https://backtony.github.io/spring/2021-07-14-spring-test-1/

### REST Docs configuration not found

`REST Docs configuration not found`와 같은 에러가 난다면 아래 사이트를 참고

- [https://sejoung.github.io](https://sejoung.github.io/2021/08/2021-08-11-spring_restdocs_REST_Docs_configuration_not_found/#junit-mockmvc-%ED%95%9C%EA%B8%80%EA%B9%A8%EC%A7%90-%EC%B2%98%EB%A6%AC%EC%8B%9C-restdocs-%EC%97%90%EB%9F%AC)

### cannot deserialize from Object value (no delegate- or property-based Creator)

`@Transactional` 붙여서 해결

- [https://velog.io/@sileeee](https://velog.io/@sileeee/JUnit%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%8B%A4%ED%96%89%EC%A4%91-%EB%A7%88%EC%A3%BC%EC%B9%9C-%EC%98%A4%EB%A5%98)

### No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor

`@JsonIgnore`로 해결

- https://csy7792.tistory.com/26

### Spring Security 강제 로그인

Spring Security에서 로그인할 때 로그인 폼을 거치지 않고 비밀번호 없이 로그인하는 방법이다.

- http://yoonbumtae.com/?p=1841
  
### ajax parseerror

위와 같은 ajax 에러가 발생한다면 `dataType`을 확인하자.

### No key, method or field with name

mustache에서 사용하려는 DTO 클래스의 필드 값이 `null`을 경우에 나는 에러이다. 이런 에러가 나면 분기문 처리로 `null`일 경우와 아닐 경우를 나누어서 출력하면 된다.

- `null`일 경우: `{{^DTO}}NO{{/DTO}}`
- [https://velog.io/@wijoonwu](https://velog.io/@wijoonwu/Spring-Boot-No-key-method-or-field-with-name-%EC%98%A4%EB%A5%98-%ED%95%B4%EA%B2%B0-%EB%B0%A9%EB%B2%95)

### fetchCount() deprecated

'전체 로우 수'를 구할 때 `fetchCount()`를 사용했는데 `deprecated` 되어서 더 이상 사용하면 안된다. 이럴 때는 `fetch().size()`를 사용하면 된다.

### mustache의 불편한 점

mustache의 장점은 쉽고 간단하게 사용할 수 있는 템플릿이다. 하지만 복잡한 연산이나 로직이 들어갈 때는 적합하지 않다.

```java
List<PostsResponseDto> postsList = postsService.findAll(searchDto);

//==페이징 처리 start==//
List<Integer> pagination = new ArrayList<>();
int startPage = searchDto.getBasePageDto().getStartPage();
int endPage = searchDto.getBasePageDto().getEndPage();
for (int i = startPage; i <= endPage; i++) {
pagination.add(i);
}

boolean hasDoublePrevPage = (searchDto.getPage() / 10) > 0;
boolean hasDoubleNextPage = (searchDto.getPage() / 10) < (searchDto.getBasePageDto().getTotalPageCount() / 10);
int doublePrevPage = startPage - 10;
int doubleNextPage = startPage + 10;

//==페이징 처리 end==//

model.addAttribute("postsList", postsList);
model.addAttribute("movePrevPage", searchDto.getPage() - 1);
model.addAttribute("moveNextPage", searchDto.getPage() + 1);
model.addAttribute("pagination", pagination);
model.addAttribute("hasDoubleNextPage", hasDoubleNextPage);
model.addAttribute("doubleNextPage", doubleNextPage);
model.addAttribute("hasDoublePrevPage", hasDoublePrevPage);
model.addAttribute("doublePrevPage", doublePrevPage);
```

예를 들어, 페이징을 구현하는데 보통 View단에서 반복문 처리를 하거나 연산을 직접 처리할 수 있는데 mustache는 그게 안되서 java단에서 미리 구해서 모델로 값을 일일이 넘겨야 한다. (사실 간단한 경우에 사용한다고 했는데 이정도도 불편하면 그냥 잘 사용을 안할 것 같다.)

### local class incompatible

자바에서는 `serializable` 인터페이스를 통해 쉽게 '직렬화', '역직렬화' 할 수 있는데 주의해야 할게 있다.

Serializable 인터페이스를 `implements`한 클래스에 아래와 같은 변수 값이 지정되어 있지 않으면 JVM이 클래스 구조 정보를 토대로 해싱값을 만들어 낸다. (따라서 사용하는 JVM에 따라 값이 다를 수 있다.)

```java
private static final long serialVersionUID = ?L
```

그리고 직렬화 -역직렬화 시에 이 값을 키 값으로 사용해서 객체의 호환을 따진다. 즉, 이 값이 같아야 데이터가 다시 역직렬화되는 것이다.

`local class incompatible` 이 에러는 `serialVersionUID`의 값이 매칭이 되지 않아서 생기는 에러이다.

이를 해결하기 위해서는 위 코드처럼 `static`으로 `serialVersionUID` 의 값을 지정해주면 서로 같은 값을 사용하게 되어 매칭이 되지 않은 일은 발생하지 않는다.

- https://stackoverflow.com/questions/10378855/java-io-invalidclassexception-local-class-incompatible
- https://hevton.tistory.com/164

### Cannot call sendError() after the response has been committed

`JSON` 타입 변환 과정 중 일어난 에러.  
원인은 JPA에서 테이블과 테이블간의 연관 관계(양방향)에 있으며 이를 **무한 순환 참조**라 한다.

해결법은 `@JsonIgnore` 를 사용한다. - 하지만 이게 정말 확실한 방법인지는 의문이다..

- https://to-moneyking.tistory.com/m/67

### No validator could be found for constraint 'javax.validation.constraints.NotBlank'

`@NotEmpty`, `@NotBlank` 는 String 타입에 사용하는 어노테이션이다. 따라서 `@NotNull` 을 사용하여 해결할 수 있다.

- https://www.inflearn.com/questions/16953

### Ambiguous handler methods mapped for

컨트롤러에서 중복된 URL이 있어서 발생하는 에러

### mustache 한글 깨짐 이슈

- Spring Boot 2.7.0에서 위 이슈 발생
- 2.6.x대로 다운 그레이드하거나 application.properties에 `server.servlet.encoding.force-response: true` 추가
- https://www.inflearn.com/questions/545116

### Table "SPRING_SESSION" not found; SQL statement 에러

`application.yml`에 아래 두 줄 추가

```groovy
spring.session.store-type=jdbc
spring.session.jdbc.initialize-schema=always
```

### 세션 저장소로 데이터베이스 사용하기

로그인할 때 사용되는 세션은 내장 톰캣의 메모리에 저장되기 때문에 애플리케이션을 재실행하면 로그인이 풀립니다. 따라서 설정이 간단한 MySQL과 같은 데이터베이스를 세션 저장소로 사용합니다.

### urlTemplate not found. If you are using MockMvc did you use RestDocumentationRequestBuilders to build the request?

`MockMvcRequestBuilders`가 아닌 `RestDocumentationRequestBuilders`를 사용해야 한다.

## google 소셜로그인시 프로필 사진 403 에러

이미지 속성에 `referrerpolicy="no-referrer"`를 추가한다.

## 🙁 아쉬운 점

### 1. React나 Vue를 사용하지 못한 점

React나 Vue 대신 SSR인 mustache template을 사용했는데 불편한 점이 한 가지 있었습니다.  
클라이언트에서 로직을 처리할 수 없는 부분입니다.

그나마 Thymeleaf의 경우 다양한 유틸성 메서드를 제공해서 괜찮았지만 Mustache는 그런 것이 없어서 로직을 Controller단에서 처리하여 그 처리된 결과값을 Model에 넘겨서 사용해야 했습니다. 그러다 보니 Controller단에 Model에 넘기는 부분이 많아졌습니다.

### 2. 나만의 아이디어로 프로젝트를 만들지 못한 점

이번 프로젝트의 목적은 실제로 운영하는 웹서비스를 똑같이 만들어보는 것이었습니다. 그 동안은 간단하게 CRUD 동작만 구현했다면 자주 방문하는 웹서비스를 똑같이 만들어보고 싶었습니다.

그러다보니 프론트쪽은 개발자 도구를 통해 거의(완전) 동일하게 구현하였고, 서버쪽도 아쉬운 점이 캐시나 검색엔진(Elastic Search 등), 인프라 등등 신경쓰지 못한 부분들이 굉장히 많았습니다.

그래서 다음 사이드 프로젝트는 뭔가 사람들에게 도움이 될만한 웹서비스를 만들고 싶어서 공공데이터 API를 이용하여 만들 예정이고, 이전 프로젝트에서 신경쓰지 못했던 부분들도 다음 프로젝트에 녹여낼 예정입니다.
