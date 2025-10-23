package com.hancy.app.web;

import com.hancy.app.common.constants.BlogAppConstants;
import com.hancy.app.dto.comment.CommentDTO;
import com.hancy.app.dto.comment.CommentResponseDTO;
import com.hancy.app.model.Comment;
import com.hancy.app.service.comment.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
}
