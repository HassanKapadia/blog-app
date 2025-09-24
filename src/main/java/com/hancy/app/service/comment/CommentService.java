package com.hancy.app.service.comment;

import com.hancy.app.dto.comment.CommentDTO;
import com.hancy.app.model.Comment;
import java.util.List;

public interface CommentService {

  public Comment createComment(Long commentorId, Long articleId, CommentDTO createComment);

  public Comment updateComment(
      Long commentorId, Long articleId, Long commentId, CommentDTO updateComment)
      throws IllegalAccessException;

  public List<Comment> getCommentsByArticle(Long articleId);

  public List<Comment> getCommentsByCommentor(Long commentorId);
}
