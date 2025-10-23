package com.hancy.app.service.comment;

import com.hancy.app.model.Article;
import com.hancy.app.model.Comment;
import com.hancy.app.model.User;
import com.hancy.app.repo.CommentRepository;
import com.hancy.app.service.article.ArticleService;
import com.hancy.app.service.user.UserService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

  private CommentRepository commentRepo;
  private ArticleService articleService;
  private UserService userService;

  @Autowired
  public CommentServiceImpl(
      CommentRepository commentRepo, ArticleService articleService, UserService userService) {
    this.commentRepo = commentRepo;
    this.articleService = articleService;
    this.userService = userService;
  }

  @Override
  public Comment createComment(Long commentorId, Long articleId, String commentText) {
    User commentor = userService.getUserById(commentorId);
    Article article = articleService.getArticleById(articleId);

    Comment comment = new Comment();
    comment.setComment(commentText);
    comment.setArticle(article);
    comment.setCreatedOn(new Date());
    comment.setCommentor(commentor);

    return commentRepo.save(comment);
  }

  @Override
  public Comment updateComment(
      Long commentorId, Long articleId, Long commentId, String updateCommentText)
      throws IllegalAccessException {
    User commentor = userService.getUserById(commentorId);
    Article article = articleService.getArticleById(articleId);
    Optional<Comment> comment = commentRepo.findById(commentId);
    if (!comment.isPresent()) {
      throw new CommentNotFoundException(commentId);
    }

    Comment savedComment = comment.get();
    if (!commentor.equals(savedComment.getCommentor())) {
      throw new IllegalAccessException(
          "Commentor: " + commentor.getName() + " cannot edit the comment");
    }

    if (!savedComment.getArticle().equals(article)) {
      throw new IllegalAccessException(
          "Comment does not belong to the article: " + article.getTitle());
    }
    savedComment.setComment(updateCommentText);

    return commentRepo.save(savedComment);
  }

  @Override
  public List<Comment> getCommentsByArticle(Long articleId) {
    Article article = articleService.getArticleById(articleId);
    Optional<List<Comment>> commentList = commentRepo.findByArticle(article);
    return commentList.orElse(new ArrayList<>());
  }

  @Override
  public List<Comment> getCommentsByCommentor(Long commentorId) {
    User commentor = userService.getUserById(commentorId);
    Optional<List<Comment>> commentList = commentRepo.findByCommentor(commentor);
    return commentList.orElse(new ArrayList<>());
  }

  public static class CommentNotFoundException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public CommentNotFoundException(Long commentId) {
      super("Comment with id: " + commentId + " does not exist");
    }
  }
}
