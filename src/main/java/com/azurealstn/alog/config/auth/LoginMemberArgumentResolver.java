package com.azurealstn.alog.config.auth;

import com.azurealstn.alog.dto.auth.SessionMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    /**
     * 파라미터에 @LoginMember 애노테이션이 붙어있고,
     * 파라미터 클래스 타입이 SessionMemberDto.class 이면
     * @return true
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginMemberAnnotation = parameter.getParameterAnnotation(LoginMember.class) != null;
        boolean isMemberClass = SessionMemberDto.class.equals(parameter.getParameterType());
        return isLoginMemberAnnotation && isMemberClass;
    }

    /**
     * 파라미터에 전달할 객체 생성
     * @return 세션에서 가져온 객체
     * */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return httpSession.getAttribute("member");
    }
}
