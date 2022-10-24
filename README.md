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

### 로그인/회원가입

- 로그인은 이메일 인증 방식으로 진행
- 

## 트러블 슈팅

- @ModelAttribute가 바인딩 되지 않는 문제
  - https://velog.io/@tsi0521/ModelAttribute-%EB%B0%94%EC%9D%B8%EB%94%A9-%EB%90%98%EC%A7%80-%EC%95%8A%EB%8A%94-%EB%AC%B8%EC%A0%9C
- @Email은 null을 유효하다고 판단
- Cannot construct instance of XXXDto - InvalidDefinitionException

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

