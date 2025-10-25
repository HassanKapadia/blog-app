package com.hancy.app.web;

import com.hancy.app.common.constants.BlogAppConstants;
import com.hancy.app.dto.comment.CommentDTO;
import com.hancy.app.dto.comment.CommentResponseDTO;
import com.hancy.app.model.Comment;
import com.hancy.app.service.comment.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles/{articleId}/comments")
public class CommentRestController {

  private final CommentService commentService;

  @Autowired
  public CommentRestController(CommentService commentService) {
    this.commentService = commentService;
  }

  @PostMapping
  public ResponseEntity<CommentResponseDTO> createComment(
      @PathVariable("articleId") Long articleId,
      @RequestBody CommentDTO commentDTO,
      HttpServletRequest request) {
    Long userId =
        (Long)
            request.getAttribute(
                BlogAppConstants.AUTH_USER_ID); // Attribute set by JwtAuthenticationFilter
    Comment createdComment =
        commentService.createComment(userId, articleId, commentDTO.getComment());
    return ResponseEntity.ok().body(CommentResponseDTO.createResponse(createdComment));
  }

  @PutMapping("/{commentId}")
  public ResponseEntity<CommentResponseDTO> updateComment(
      @PathVariable("articleId") Long articleId,
      @PathVariable("commentId") Long commentId,
      @RequestBody CommentDTO commentDTO,
      HttpServletRequest request)
      throws IllegalAccessException {
    Long userId =
        (Long)
            request.getAttribute(
                BlogAppConstants.AUTH_USER_ID); // Attribute set by JwtAuthenticationFilter

    Comment updatedComment =
        commentService.updateComment(userId, articleId, commentId, commentDTO.getComment());
    return ResponseEntity.ok().body(CommentResponseDTO.createResponse(updatedComment));
  }

  @DeleteMapping("/{commentId}")
  public ResponseEntity<Void> deleteComment(
      @PathVariable("articleId") Long articleId,
      @PathVariable("commentId") Long commentId,
      HttpServletRequest request)
      throws IllegalAccessException {
    Long userId =
        (Long)
            request.getAttribute(
                BlogAppConstants.AUTH_USER_ID); // Attribute set by JwtAuthenticationFilter

    commentService.deleteComment(userId, articleId, commentId);
    return ResponseEntity.noContent().build();
  }
}
