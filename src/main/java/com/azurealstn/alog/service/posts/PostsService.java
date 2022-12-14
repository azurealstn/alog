package com.azurealstn.alog.service.posts;

import com.azurealstn.alog.Infra.exception.member.MemberNotFound;
import com.azurealstn.alog.Infra.exception.posts.PostsNotFound;
import com.azurealstn.alog.Infra.utils.DateUtils;
import com.azurealstn.alog.domain.comment.Comment;
import com.azurealstn.alog.domain.hashtag.HashTag;
import com.azurealstn.alog.domain.image.PostsImage;
import com.azurealstn.alog.domain.member.Member;
import com.azurealstn.alog.domain.posts.Posts;
import com.azurealstn.alog.dto.BasePageDto;
import com.azurealstn.alog.dto.auth.SessionMemberDto;
import com.azurealstn.alog.dto.comment.CommentResponseDto;
import com.azurealstn.alog.dto.hashtag.HashTagResponseDto;
import com.azurealstn.alog.dto.hashtag.HashTagSearchDto;
import com.azurealstn.alog.dto.image.PostsImageResponseDto;
import com.azurealstn.alog.dto.like.PostsLikeRequestDto;
import com.azurealstn.alog.dto.like.PostsLikeResponseDto;
import com.azurealstn.alog.dto.posts.PostsCreateRequestDto;
import com.azurealstn.alog.dto.posts.PostsResponseDto;
import com.azurealstn.alog.dto.posts.PostsModifyRequestDto;
import com.azurealstn.alog.dto.posts.PostsSearchDto;
import com.azurealstn.alog.repository.image.PostsImageRepository;
import com.azurealstn.alog.repository.member.MemberRepository;
import com.azurealstn.alog.repository.posts.PostsRepository;
import com.azurealstn.alog.service.comment.CommentService;
import com.azurealstn.alog.service.hashtag.HashTagService;
import com.azurealstn.alog.service.image.PostsImageService;
import com.azurealstn.alog.service.like.PostsLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final MemberRepository memberRepository;
    private final HttpSession httpSession;
    private final PostsImageRepository postsImageRepository;
    private final PostsImageService postsImageService;
    private final PostsLikeService postsLikeService;
    private final HashTagService hashTagService;
    private final CommentService commentService;

    /**
     * ????????? ?????? API
     */
    @Transactional
    public Long create(PostsCreateRequestDto requestDto) throws IOException {
        //Case 1. ????????? ????????? Entity ??? response ??????
        //Case 2. ????????? ???????????? primary_id ??? response ??????
        //  - Client????????? ????????? id??? posts ?????? API??? ????????? ??? ???????????? ????????????
        //Case 3. ?????? ?????? ?????? -> ????????????????????? ?????? POST(???) ????????? context??? ??? ?????????
        SessionMemberDto sessionMemberDto = (SessionMemberDto) httpSession.getAttribute("member");
        Member member = memberRepository.findByEmail(sessionMemberDto.getEmail())
                .orElseThrow(() -> new MemberNotFound());
        requestDto.updateMember(member);
        Posts posts = requestDto.toEntity();
        return postsRepository.save(posts).getId();
    }

    /**
     * ????????? ?????? ?????? API
     * - ??????????????? ???????????? -> json ???????????? title ????????? ?????? 10????????? ???????????? ??????????????????. (????????? ???????????? ?????????????????? ??????)
     * PostsResponseDto ?????? ???????????? ????????? ??????
     * 1. Posts ??????????????? ?????? ????????? ?????? ?????? ??????????????? ??????????????? ????????? ???????????????.
     * 2. ?????? ???????????? ?????? ?????????????????? ??????????????? ???????????? ??????.
     * 3. ?????? ?????????????????? title ????????? ?????? 100????????? ?????????????????? ??????????????? ?????? ????????? ?????? ??????.
     * ????????? ??????????????? ?????? ???????????? ????????? ?????? ????????? ??????!
     */
    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long postsId) {
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new PostsNotFound());
        return new PostsResponseDto(posts);
    }

    /**
     * ?????? ?????? ?????? ?????? -> ????????? ?????? ?????? ??????.
     * ?????? 1?????? ?????? ??? -> DB ?????? ?????? ???????????? ?????? -> DB??? ?????? ??? ??????.
     * DB -> ?????????????????? ????????? ???????????? ??????, ????????? ?????? ?????? ?????? ????????? ??? ??????.
     * ?????? ???????????? ?????? ????????? ????????? ??????.
     * - limit: ????????? ?????? ???
     * - offset: ????????? row?????? ????????????
     */
    @Transactional(readOnly = true)
    public List<PostsResponseDto> findAll(PostsSearchDto searchDto) {
        int totalRowCount = postsRepository.findAllCount();
        BasePageDto basePageDto = new BasePageDto(searchDto.getPage(), searchDto.getSize(), totalRowCount);
        searchDto.setBasePageDto(basePageDto);
        return postsRepository.findAll(searchDto).stream()
                .map(posts -> new PostsResponseDto(posts))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostsResponseDto> findAllByIndexLiked(PostsSearchDto searchDto) {
        int totalRowCount = postsRepository.findAllCount();
        BasePageDto basePageDto = new BasePageDto(searchDto.getPage(), searchDto.getSize(), totalRowCount);
        searchDto.setBasePageDto(basePageDto);
        return postsRepository.findAllByIndexLiked(searchDto).stream()
                .map(posts -> new PostsResponseDto(posts))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostsResponseDto> findAllByLike(PostsSearchDto searchDto, Long memberId) {
        int totalRowCount = postsRepository.findAllByLikeCount(searchDto, memberId);
        BasePageDto basePageDto = new BasePageDto(searchDto.getPage(), searchDto.getSize(), totalRowCount);
        searchDto.setBasePageDto(basePageDto);
        return postsRepository.findAllByLike(searchDto, memberId).stream()
                .map(posts -> new PostsResponseDto(posts))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public int findAllBySearchCount(PostsSearchDto searchDto) {
        return postsRepository.findAllBySearchCount(searchDto);
    }

    @Transactional(readOnly = true)
    public List<PostsResponseDto> findAllBySearch(PostsSearchDto searchDto) {
        int totalRowCount = postsRepository.findAllBySearchCount(searchDto);
        BasePageDto basePageDto = new BasePageDto(searchDto.getPage(), searchDto.getSize(), totalRowCount);
        searchDto.setBasePageDto(basePageDto);
        List<PostsResponseDto> postsResponseDtoList = postsRepository.findAllBySearch(searchDto).stream()
                .map(posts -> new PostsResponseDto(posts))
                .collect(Collectors.toList());

        for (PostsResponseDto postsResponseDto : postsResponseDtoList) {
            log.info("postsResponseDto={}", postsResponseDto);
        }

        for (PostsResponseDto postsResponseDto : postsResponseDtoList) {
            postsResponseDto.addTotalRowCount(totalRowCount);
            PostsLikeRequestDto postsLikeRequestDto = new PostsLikeRequestDto(postsResponseDto.getMember().getId(), postsResponseDto.getId());
            PostsLikeResponseDto postsLikeInfo = postsLikeService.findPostsLikeInfo(postsLikeRequestDto);
            postsResponseDto.addLikeCount(postsLikeInfo.getPostsLikeCount());
            postsResponseDto.addCommentCount(commentService.commentCountByPosts(postsResponseDto.getId()));

            PostsImageResponseDto postsImageResponseDto = postsImageService.findThumbnailByPosts(postsResponseDto.getId());
            if (postsImageResponseDto != null) {
                postsResponseDto.addStoreFilename(postsImageResponseDto.getStoreFilename());
                postsResponseDto.addImageUrl(postsImageResponseDto.getImageUrl());
            }
            List<HashTagResponseDto> tags = hashTagService.findByTags(postsResponseDto.getId());
            List<HashTag> hashTags = tags.stream()
                    .map(tag -> HashTag.builder()
                            .name(tag.getName())
                            .build())
                    .collect(Collectors.toList());

            for (HashTag hashTag : hashTags) {
                postsResponseDto.addHashTag(hashTag);
            }
        }

        return postsResponseDtoList;
    }

    /**
     * ????????? ?????? API
     */
    @Transactional
    public Long modify(Long postsId, PostsModifyRequestDto requestDto) {
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new PostsNotFound());

        posts.modify(requestDto.getTitle(), requestDto.getContent(), requestDto.getDescription(), requestDto.getSecret());

        return postsId;
    }

    /**
     * ????????? ?????? API
     */
    @Transactional
    public void delete(Long postsId) {
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new PostsNotFound());
        postsRepository.delete(posts);
    }

    /**
     * ?????? ????????? URL -> ?????? ??????
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

    @Transactional
    public String previousPostsDate(Long postsId) {
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new PostsNotFound());

        return DateUtils.previousTimeCalc(posts.getCreatedDate());
    }

    @Transactional(readOnly = true)
    public Boolean isAuthenticated(SessionMemberDto member, PostsResponseDto posts) {
        Boolean isAuthenticated = null;
        if (member != null && posts != null) {
            if (member.getEmail().equals(posts.getMember().getEmail())) {
                isAuthenticated = true;
            } else {
                isAuthenticated = false;
            }
        }

        return isAuthenticated;
    }

    @Transactional(readOnly = true)
    public List<PostsResponseDto> findAllByMember(Long memberId) {
        return postsRepository.findAllByMember(memberId).stream()
                .map(posts -> new PostsResponseDto(posts))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostsResponseDto> findAllJoinWithHashTag(String name, HashTagSearchDto searchDto) {
        int totalRowCount = postsRepository.findAllJoinWithHashTagCount(name);
        BasePageDto basePageDto = new BasePageDto(searchDto.getPage(), searchDto.getSize(), totalRowCount);
        searchDto.setBasePageDto(basePageDto);
        return postsRepository.findAllJoinWithHashTag(name, searchDto).stream()
                .map(posts -> new PostsResponseDto(posts))
                .collect(Collectors.toList());
    }

    public void setPostsLikeResponseDto(List<PostsResponseDto> postsList) {
        for (PostsResponseDto postsResponseDto : postsList) {
            PostsLikeRequestDto postsLikeRequestDto = new PostsLikeRequestDto(postsResponseDto.getMember().getId(), postsResponseDto.getId());
            PostsLikeResponseDto postsLikeInfo = postsLikeService.findPostsLikeInfo(postsLikeRequestDto);
            postsResponseDto.addLikeCount(postsLikeInfo.getPostsLikeCount());
            postsResponseDto.addCommentCount(commentService.commentCountByPosts(postsResponseDto.getId()));

            PostsImageResponseDto postsImageResponseDto = postsImageService.findThumbnailByPosts(postsResponseDto.getId());
            if (postsImageResponseDto != null) {
                postsResponseDto.addStoreFilename(postsImageResponseDto.getStoreFilename());
                postsResponseDto.addImageUrl(postsImageResponseDto.getImageUrl());
            }
        }
    }

    public void setMemberPostsResponseDto(List<PostsResponseDto> postsListByMember) {
        for (PostsResponseDto postsResponseDto : postsListByMember) {
            List<HashTagResponseDto> tags = hashTagService.findByTags(postsResponseDto.getId());
            List<HashTag> hashTags = tags.stream()
                    .map(tag -> HashTag.builder()
                            .name(tag.getName())
                            .build())
                    .collect(Collectors.toList());

            for (HashTag hashTag : hashTags) {
                postsResponseDto.addHashTag(hashTag);
            }

            PostsLikeRequestDto postsLikeRequestDto = new PostsLikeRequestDto(postsResponseDto.getMember().getId(), postsResponseDto.getId());
            PostsLikeResponseDto postsLikeInfo = postsLikeService.findPostsLikeInfo(postsLikeRequestDto);
            postsResponseDto.addLikeCount(postsLikeInfo.getPostsLikeCount());
            postsResponseDto.addCommentCount(commentService.commentCountByPosts(postsResponseDto.getId()));
        }
    }

    public void setTagPostsResponseDto(List<PostsResponseDto> postsList) {
        for (PostsResponseDto postsResponseDto : postsList) {
            List<HashTagResponseDto> tags = hashTagService.findByTags(postsResponseDto.getId());
            List<HashTag> hashTags = tags.stream()
                    .map(tag -> HashTag.builder()
                            .name(tag.getName())
                            .build())
                    .collect(Collectors.toList());

            for (HashTag hashTag : hashTags) {
                postsResponseDto.addHashTag(hashTag);
            }

            PostsLikeRequestDto postsLikeRequestDto = new PostsLikeRequestDto(postsResponseDto.getMember().getId(), postsResponseDto.getId());
            PostsLikeResponseDto postsLikeInfo = postsLikeService.findPostsLikeInfo(postsLikeRequestDto);
            postsResponseDto.addLikeCount(postsLikeInfo.getPostsLikeCount());
            postsResponseDto.addCommentCount(commentService.commentCountByPosts(postsResponseDto.getId()));

            PostsImageResponseDto postsImageResponseDto = postsImageService.findThumbnailByPosts(postsResponseDto.getId());
            if (postsImageResponseDto != null) {
                postsResponseDto.addStoreFilename(postsImageResponseDto.getStoreFilename());
                postsResponseDto.addImageUrl(postsImageResponseDto.getImageUrl());
            }
        }
    }

    public void setIndexPostsResponseDto(List<PostsResponseDto> postsList) {
        for (PostsResponseDto postsResponseDto : postsList) {
            PostsLikeRequestDto postsLikeRequestDto = new PostsLikeRequestDto(postsResponseDto.getMember().getId(), postsResponseDto.getId());
            PostsLikeResponseDto postsLikeInfo = postsLikeService.findPostsLikeInfo(postsLikeRequestDto);
            postsResponseDto.addLikeCount(postsLikeInfo.getPostsLikeCount());
            postsResponseDto.addCommentCount(commentService.commentCountByPosts(postsResponseDto.getId()));

            PostsImageResponseDto postsImageResponseDto = postsImageService.findThumbnailByPosts(postsResponseDto.getId());
            if (postsImageResponseDto != null) {
                postsResponseDto.addStoreFilename(postsImageResponseDto.getStoreFilename());
                postsResponseDto.addImageUrl(postsImageResponseDto.getImageUrl());
            }
        }
    }

    public void setCommentResponseDto(List<CommentResponseDto> commentLevel0, SessionMemberDto member) {
        for (CommentResponseDto commentResponseDto : commentLevel0) {
            List<CommentResponseDto> commentResponseDtoList = commentService.findAllCommentLevel1(commentResponseDto.getId());
            List<Comment> commentList = commentResponseDtoList.stream()
                    .map(comment -> Comment.builder()
                            .id(comment.getId())
                            .content(comment.getContent())
                            .member(comment.getMember())
                            .posts(comment.getPosts())
                            .level(comment.getLevel())
                            .upCommentId(comment.getUpCommentId())
                            .isCommentMe(comment.getMember().getId().equals((member != null) ? member.getId() : 0))
                            .build())
                    .collect(Collectors.toList());

            commentResponseDto.addSubCommentList(commentList);
            commentResponseDto.addSubCommentListCount(commentList.size());

            if (commentList.size() > 0) {
                commentResponseDto.addHasSubCommentList(true);
            } else {
                commentResponseDto.addHasSubCommentList(false);
            }

            if (member != null) {
                if (commentResponseDto.getMember().getId().equals(member.getId())) {
                    commentResponseDto.addIsCommentMe(true);
                } else {
                    commentResponseDto.addIsCommentMe(false);
                }
            }
        }
    }
}
