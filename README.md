# alog (velog 짝퉁)

velog(벨로그)는 velopert님이 만든 개발자를 위한 블로그 서비스입니다.  
실제로 velog를 자주 보는 이유는 velog에 올라오는 글들의 퀄리티가 너무 괜찮고 보다보면 얻어가는 것이 더 많기 때문에 주로 눈팅(?)을 많이 합니다.

그래서 평소 즐겨보는 velog 짝퉁(클론 코딩)을 만들어보면 좋겠다 생각했습니다. 

> 프로젝트 이름인 alog 에서 a 는 Azure(푸른 하늘)를 뜻합니다.

## 패키지 구조

## git 협업

## 데이터 검증

## 트러블 슈팅

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