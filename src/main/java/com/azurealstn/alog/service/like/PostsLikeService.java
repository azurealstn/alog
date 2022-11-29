package com.azurealstn.alog.service.like;

import com.azurealstn.alog.Infra.exception.member.MemberNotFound;
import com.azurealstn.alog.Infra.exception.posts.PostsNotFound;
import com.azurealstn.alog.domain.like.PostsLike;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.like.PostsLikeRequestDto;
import com.azurealstn.alog.dto.like.PostsLikeResponseDto;
import com.azurealstn.alog.repository.like.PostsLikeRepository;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.azurealstn.alog.repository.posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsLikeService {

    private final PostsLikeRepository postsLikeRepository;
    private final PostsRepository postsRepository;
    private final MemberRepository memberRepository;

    /**
     * 좋아요 및 취소
     */
    public Boolean toggleLikeButton(PostsLikeRequestDto requestDto) {
        postsLikeRepository.exist(requestDto.getMemberId(), requestDto.getPostsId())
                .ifPresentOrElse(
                        postsLike -> {
                            postsLikeRepository.deleteById(postsLike.getId());
                            Posts posts = findPosts(requestDto);
                            int likes = postsLikeRepository.findPostsLikeCount(posts.getId());
                            posts.updateLike(likes);
                        },
                        () -> {
                            Posts posts = findPosts(requestDto);
                            Member member = memberRepository.findById(requestDto.getMemberId())
                                    .orElseThrow(() -> new MemberNotFound());
                            PostsLike postsLike = PostsLike.builder()
                                    .member(member)
                                    .posts(posts)
                                    .build();
                            postsLikeRepository.save(postsLike);
                            int likes = postsLikeRepository.findPostsLikeCount(posts.getId());
                            posts.updateLike(likes);
                        }
                );
        return true;
    }

    @Transactional(readOnly = true)
    public Posts findPosts(PostsLikeRequestDto requestDto) {
        return postsRepository.findById(requestDto.getPostsId())
                .orElseThrow(() -> new PostsNotFound());
    }

    /**
     * 좋아요 개수
     */
    @Transactional(readOnly = true)
    public PostsLikeResponseDto findPostsLikeInfo(PostsLikeRequestDto requestDto) {
        int postsLikeCount = getPostsLikeCount(requestDto);
        boolean exist = isExist(requestDto);

        return new PostsLikeResponseDto(postsLikeCount, exist);
    }

    @Transactional(readOnly = true)
    private boolean isExist(PostsLikeRequestDto requestDto) {
        boolean exist = postsLikeRepository.exist(requestDto.getMemberId(), requestDto.getPostsId()).isPresent();
        return exist;
    }

    @Transactional(readOnly = true)
    public int getPostsLikeCount(PostsLikeRequestDto requestDto) {
        int postsLikeCount = postsLikeRepository.findPostsLikeCount(requestDto.getPostsId());
        return postsLikeCount;
    }
}
