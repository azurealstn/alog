package com.azurealstn.alog.config.auth;

import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.dto.auth.OAuthAttributesDto;
import com.azurealstn.alog.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

/**
 * CustomOAuth2UserService 클래스 역할
 * 구글 로그인 이후 가져온 사용자의 정보(email, name, picture..)들을 기반으로
 * 회원가입 및 정보수정, 세션 저장 등의 기능을 지원한다.
 */
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    OAuthAttributesDto attributes = null;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        //현재 로그인 진행중인 서비스를 구분하는 코드 (구글인지, 네이버인지, 카카오인지..)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //OAuth2 로그인 진행시 키가 되는 필드값 (Primary Key와 같은 의미)
        //구글은 기본적으로 코드를 지원하지만, 네어버 카카오 등은 기본 지원을 하지 않는다. (구글의 기본코드: sub)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        attributes = OAuthAttributesDto.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_MEMBER")),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    //oauth2을 통한 사용자 정보를 반환
    public Member getSnsMemberInfo() {
        return Member.builder()
                .name(attributes.getName())
                .email(attributes.getEmail())
                .picture(attributes.getPicture())
                .build();
    }
}
