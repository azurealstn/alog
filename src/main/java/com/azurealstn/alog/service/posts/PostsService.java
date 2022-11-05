package com.azurealstn.alog.service.posts;

import com.azurealstn.alog.Infra.exception.member.MemberNotFound;
import com.azurealstn.alog.Infra.exception.posts.PostsNotFound;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsResponseDto;
import com.azurealstn.alog.dto.posts.PostsModifyRequestDto;
import com.azurealstn.alog.dto.posts.PostsSearchDto;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.azurealstn.alog.repository.posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final HttpSession httpSession;
    private final MemberRepository memberRepository;

    /**
     * 게시글 등록 API
     */
    @Transactional
    public Long create(PostsCreateRequestDto requestDto) {
        //Case 1. 저장한 데이터 Entity → response 응답
        //Case 2. 저장한 데이터의 primary_id → response 응답
        //  - Client에서는 수신한 id를 posts 조회 API를 통해서 글 데이터를 수신받음
        //Case 3. 응답 필요 없음 -> 클라이언트에서 모든 POST(글) 데이터 context를 잘 관리함
        SessionMemberDto sessionMemberDto = (SessionMemberDto) httpSession.getAttribute("member");
        Member member = memberRepository.findByEmail(sessionMemberDto.getEmail())
                .orElseThrow(() -> new MemberNotFound());
        requestDto.updateMember(member);
        Posts posts = requestDto.toEntity();
        return postsRepository.save(posts).getId();
    }

    /**
     * 게시글 단건 조회 API
     * - 클라이언트 요구사항 -> json 응답에서 title 길이를 최대 10글자로 서버에서 처리해주세요. (이런건 클라에서 처리하는것이 맞다)
     * PostsResponseDto 응답 클래스를 분리한 이유
     * 1. Posts 도메인에서 직접 제한할 경우 다른 리소스에서 예기치못한 버그를 발생시킨다.
     * 2. 다른 개발자는 매번 확인해야하는 유지보수의 어려움이 있다.
     * 3. 다른 리소스에서는 title 길이를 최대 100글자로 처리해달라는 요구사항이 오면 해결할 수가 없다.
     * 도메인 클래스에는 절대 서비스의 정책을 넣지 말아야 한다!
     */
    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long postsId) {
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new PostsNotFound());
        return new PostsResponseDto(posts);
    }

    /**
     * 글이 너무 많은 경우 -> 비용이 너무 많이 든다.
     * 글이 1억개 있을 때 -> DB 글을 모두 조회하는 경우 -> DB가 뻗을 수 있다.
     * DB -> 애플리케이션 서버로 전달하는 시간, 트래픽 비용 등이 많이 발생할 수 있다.
     * 이를 해결하기 위해 페이징 처리를 한다.
     * - limit: 출력할 행의 수
     * - offset: 몇번째 row부터 출력할지
     */
    @Transactional(readOnly = true)
    public List<PostsResponseDto> findAll(PostsSearchDto searchDto) {
        return postsRepository.findAll(searchDto).stream()
                .map(posts -> new PostsResponseDto(posts))
                .collect(Collectors.toList());
    }

    /**
     * 게시글 수정 API
     */
    @Transactional
    public Long modify(Long postsId, PostsModifyRequestDto requestDto) {
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new PostsNotFound());

        posts.modify(requestDto.getTitle(), requestDto.getContent(), requestDto.getDescription());

        return postsId;
    }

    /**
     * 게시글 삭제 API
     */
    @Transactional
    public void delete(Long postsId) {
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new PostsNotFound());
        postsRepository.delete(posts);
    }

    /**
     * 이전 페이지 URL -> 세션 저장
     */
    @Transactional
    public void savePrevPage(HttpServletRequest request) {
        String url = request.getHeader("Referer");
        if (url != null) {
            httpSession.setAttribute("prevUrl", url);
        } else {
            httpSession.setAttribute("prevUrl", "/");
        }
    }
}
