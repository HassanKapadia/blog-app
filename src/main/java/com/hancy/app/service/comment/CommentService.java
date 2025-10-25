package com.hancy.app.service.comment;

import com.hancy.app.model.Comment;
import java.util.List;

public interface CommentService {

  public Comment createComment(Long commentorId, Long articleId, String commentText);

  public Comment updateComment(
      Long commentorId, Long articleId, Long commentId, String updateCommentText)
      throws IllegalAccessException;

  public List<Comment> getCommentsByArticle(Long articleId);

  public List<Comment> getCommentsByCommentor(Long commentorId);

  public void deleteComment(Long commentorId, Long articleId, Long commentId)
      throws IllegalAccessException;
}
