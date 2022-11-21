package com.azurealstn.alog.controller.api.comment;

import com.azurealstn.alog.dto.comment.CommentCreateRequestDto;
import com.azurealstn.alog.dto.comment.CommentModifyRequestDto;
import com.azurealstn.alog.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping("/api/v1/comment")
    public Long create(@Valid @RequestBody CommentCreateRequestDto requestDto) {
        return commentService.create(requestDto);
    }

    @PatchMapping("/api/v1/comment/{commentId}")
    public Long modify(@Valid @RequestBody CommentModifyRequestDto requestDto, @PathVariable Long commentId) {
        return commentService.modify(commentId, requestDto);
    }

    @DeleteMapping("/api/v1/comment/{commentId}")
    public void delete(@PathVariable Long commentId) {
        commentService.delete(commentId);
    }
}
