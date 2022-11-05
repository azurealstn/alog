package com.azurealstn.alog.Infra.exception.posts;

import com.azurealstn.alog.Infra.exception.GlobalException;

/**
 * 메서드마다 new IllegalArgumentException("") 리턴하면 메시지가 수정됐을 때 모두 수정해야 한다.
 * 테스트코드 또한 모두 변경해주어야 하기 때문에 번거롭다.
 * 내부 서비스에서 게시글 전용 Exception 클래스를 만들면
 * 명칭도 명확해지고, 메시지도 한 곳에서 한 번만 수정하면 된다.
 */
public class PostsNotFound extends GlobalException {

    private static final String MESSAGE = "해당 글이 없습니다.";

    public PostsNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
