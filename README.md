# alog (velog 짝퉁)

velog(벨로그)는 velopert님이 만든 개발자를 위한 블로그 서비스입니다.  
실제로 velog를 자주 보는 이유는 velog에 올라오는 글들의 퀄리티가 너무 괜찮고 보다보면 얻어가는 것이 더 많기 때문에 주로 눈팅(?)을 많이 합니다.

그래서 평소 즐겨보는 velog 짝퉁(클론 코딩)을 만들어보면 좋겠다 생각했습니다. 

> 프로젝트 이름인 alog 에서 a 는 Azure(푸른 하늘)를 뜻합니다.

## 패키지 구조

## git 협업

## 데이터 검증

## 기능 구현

- 로그인/회원가입
  - 이메일 인증을 통한 로그인/회원가입
  - OAuth2 Client를 이용한 소셜로그인

### 로그인

![login](https://user-images.githubusercontent.com/55525868/197791392-a1b3880f-c14f-427b-afa9-0415b4583235.png)

### 회원가입

![create-member](https://user-images.githubusercontent.com/55525868/197791383-5dbaff23-8d67-4389-be9f-b9fc7d38017e.png)

### 소셜로그인

![sns-login](https://user-images.githubusercontent.com/55525868/197791402-8f0ab835-1d4c-4b36-98b9-19592a20cc06.png)
  
## 트러블 슈팅

### @ModelAttribute가 바인딩 되지 않는 문제

첫번째. DTO 클래스에 `@NoArgsConstructor`와 `@AllArgsConstructor ` 둘 다 있는 경우 `NoArgsConstructor`를 호출하고, setter를 호출한 다음에 param을 필드에 각각 초기화를 한다.

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

## API 문서

클라이언트 입장에서는 어떤 API가 있는지 모르기 때문에 백엔드에서 API를 잘 정리해서 전달할 필요가 있습니다. 백엔드에서 개발한 실제 코드를 토대로 자동으로 API 문서화를 만들어주는 툴들이 있습니다.

- Swagger
- Postman
- GitBook
- I/O Docs
- Spring REST Docs

## 만난 에러들

- mustache 한글 깨짐 이슈
  - Spring Boot 2.7.0에서 위 이슈 발생
  - 2.6.x대로 다운 그레이드하거나 application.properties에 `server.servlet.encoding.force-response: true` 추가
  - https://www.inflearn.com/questions/545116
- Table "SPRING_SESSION" not found; SQL statement 에러
  - https://velog.io/@ojs0073/Table-SPRINGSESSION-not-found-SQL-statement-%EC%97%90%EB%9F%AC

## 세션 저장소로 데이터베이스 사용하기

로그인할 때 사용되는 세션은 내장 톰캣의 메모리에 저장되기 때문에 애플리케이션을 재실행하면 로그인이 풀립니다. 따라서 설정이 간단한 MySQL과 같은 데이터베이스를 세션 저장소로 사용합니다.

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

