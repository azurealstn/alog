package com.azurealstn.alog.service.posts;

import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsResponseDto;
import com.azurealstn.alog.repository.posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    /**
     * 게시글 등록 API
     */
    @Transactional
    public Long create(PostsCreateRequestDto requestDto) {
        //Case 1. 저장한 데이터 Entity → response 응답
        //Case 2. 저장한 데이터의 primary_id → response 응답
        //  - Client에서는 수신한 id를 posts 조회 API를 통해서 글 데이터를 수신받음
        //Case 3. 응답 필요 없음 -> 클라이언트에서 모든 POST(글) 데이터 context를 잘 관리함
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
    public PostsResponseDto findById(Long postsId) {
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id=" + postsId));
        return new PostsResponseDto(posts);
    }

    public List<PostsResponseDto> findAll() {
        return postsRepository.findAll().stream()
                .map(posts -> new PostsResponseDto(posts))
                .collect(Collectors.toList());
    }
}
