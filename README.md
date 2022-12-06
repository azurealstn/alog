# alog 소개 - 사이드 프로젝트

그 전의 사이드 프로젝트는 혼자만 볼거라고 신경을 안썼지만 실제 서비스하는 사이트를 여러가지 신경을 써서 만는 것이 티가 났습니다. 그래서 저도 그러한 사이트를 만들기 위해 실제 서비스하는 사이트 하나를 결정해서 똑같이 만들기로 결정했습니다. (~~절대 아이디어가 없어서가 아닙니다..~~)

velog(벨로그)는 velopert님이 만든 개발자를 위한 블로그 서비스입니다.  
실제로 velog를 자주 보는 이유는 velog에 올라오는 글들의 퀄리티가 너무 괜찮고 보다보면 얻어가는 것이 더 많기 때문에 주로 눈팅(?)을 많이 했습니다.

그래서 평소 즐겨보는 velog 짝퉁(클론 코딩)을 만들어보면 좋겠다 생각했습니다. 

> 프로젝트 이름인 alog 에서 a 는 Azure(푸른 하늘)를 뜻합니다.

## 🕒 프로젝트 개발 기간

- 프로젝트 참여: 본인
- 프로젝트 기간: 2022-10-08 ~ 2022-12-08 (두 달간 진행)

## 기술 스택

- Java 11
- Intellij
- Spring 5.x MVC
- Spring Boot 2.7.x
- Spring Security
- Spring Data JPA
- QueryDSL 5.0.0
- H2 Database 2.1.214
- MariaDB 10.6
- Gradle
- Mustache
- Git
- Junit 5
  - MockMvc
- AWS
  - EC2
  - RDS
  - S3

## API

- http://azurelog.link/docs/member.html
- http://azurelog.link/docs/posts.html
- http://azurelog.link/docs/tempsave.html
- http://azurelog.link/docs/comment.html
- http://azurelog.link/docs/hashtag.html
- http://azurelog.link/docs/postsLike.html

## 기능 구현

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
  - 예외처리

## 프로젝트 구조

![alog_structure](https://user-images.githubusercontent.com/55525868/205878592-62bf750e-0d8e-4e1e-bf9c-b01596a6a7a5.png)

- 프론트
  - html, css, js (es6+)
  - jquery, ajax
- 백
  - Spring Security로 사용자 인증
    - 로그인 폼, 비밀번호없이 강제 인증
  - JPA를 이용한 객체지향적 테이블 관리 (ORM)
  - DTO를 통한 도메인 순수성 보장

### 로그인

![login](https://user-images.githubusercontent.com/55525868/197791392-a1b3880f-c14f-427b-afa9-0415b4583235.png)

### 회원가입

![create-member](https://user-images.githubusercontent.com/55525868/197791383-5dbaff23-8d67-4389-be9f-b9fc7d38017e.png)

### 소셜로그인

![sns-login](https://user-images.githubusercontent.com/55525868/197791402-8f0ab835-1d4c-4b36-98b9-19592a20cc06.png)

## 데이터베이스 구조

### 요구사항

- 사용자는 소셜 로그인, 이메일 로그인을 할 수 있다.
- 로그인한 사용자는 게시글, 댓글을 작성할 수 있다.
- 로그인한 사용자는 댓글에 댓글을 작성할 수 있다.
- 로그인한 사용자는 회원과 게시글에 이미지를 업로드할 수 있다.
- 로그인한 사용자는 게시글을 작성하다 `임시저장`을 할 수 있다.
  - 임시저장은 10초마다 자동으로 저장될 수 있다.
- 로그인한 사용자는 게시글에 좋아요와 태그를 추가할 수 있다.
- 태그를 클릭하면 태그 목록을 확인할 수 있다.
- 메인 화면에는 최신순과 좋아요순으로 볼 수 있도록 정렬 기능을 추가한다.

![erd](https://user-images.githubusercontent.com/55525868/205479698-ce5d78a7-b5c4-4e70-9da2-a3cdcc75f15d.png)

## 패키지 구조 (도메인형 구조)

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

## git 협업

프로젝트를 혼자서 구현했기 때문에 git 협업 경험이 없습니다. 그래서 혼자서 브랜치를 따고 아래와 같은 패턴으로 소스 형상 관리를 했습니다.

1. 브랜치 생성 -> 브랜치 이동
2. 소스 수정 -> `git add .` -> `git commit`
3. main 브랜치 이동 -> `git merge --no-ff --log 브랜치명`
4. 브랜치 삭제 -> 새 브랜치 생성 (1번으로 이동, 반복...)

커밋 메시지는 `git commit message convention`을 참고해서 커밋을 진행했습니다.

## 배포 진행

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

## API 문서

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

