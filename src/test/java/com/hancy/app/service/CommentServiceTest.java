package com.hancy.app.service;

import com.hancy.app.dto.article.CreateArticleDTO;
import com.hancy.app.dto.comment.CommentDTO;
import com.hancy.app.dto.user.CreateUserDTO;
import com.hancy.app.model.Article;
import com.hancy.app.model.Comment;
import com.hancy.app.model.User;
import com.hancy.app.service.article.ArticleService;
import com.hancy.app.service.comment.CommentService;
import com.hancy.app.service.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CommentServiceTest {

  @Autowired private CommentService commentService;

  @Autowired private ArticleService articleService;

  @Autowired private UserService userService;

  private User user;
  private Article article;
  private Comment comment;

  @BeforeEach
  public void setup() {
    CreateUserDTO createUser = createUser();
    user = userService.createUser(createUser);

    CreateArticleDTO createArticle = createArticle();
    article = articleService.createArticle(user.getId(), createArticle);

    CommentDTO commentDTO = new CommentDTO();
    commentDTO.setComment("Great article");
    comment = commentService.createComment(user.getId(), article.getId(), commentDTO.getComment());
  }

  @Test
  public void canCreateComment() {
    String expectedSlug = article.getTitle().toLowerCase().replace("\\s+", "-");

    Assertions.assertEquals("hassank", comment.getCommentor().getUsername());
    Assertions.assertEquals(expectedSlug, comment.getArticle().getSlug());
    Assertions.assertEquals("Great article", comment.getComment());
  }

  @Test
  public void testRestrictUpdateComment() {
    CommentDTO updateComment = new CommentDTO();
    updateComment.setComment("Fantastic article");

    CreateUserDTO createUser = new CreateUserDTO();
    createUser.setName("Hassan Kapadia");
    createUser.setUsername("hassank1");
    createUser.setPassword("HassanK@7864");
    createUser.setEmail("hsn.kapadia1@blog.com");
    User user = userService.createUser(createUser);

    Assertions.assertThrows(
        IllegalAccessException.class,
        () ->
            commentService.updateComment(
                user.getId(), article.getId(), comment.getId(), updateComment.getComment()));
  }

  protected CreateUserDTO createUser() {
    CreateUserDTO createUser = new CreateUserDTO();
    createUser.setName("Hassan Kapadia");
    createUser.setUsername("hassank");
    createUser.setPassword("HassanK@7864");
    createUser.setEmail("hsn.kapadia@blog.com");
    return createUser;
  }

  protected CreateArticleDTO createArticle() {
    CreateArticleDTO createArticleDTO = new CreateArticleDTO();
    String title = "How to create a spring blog app";
    createArticleDTO.setTitle(title);
    createArticleDTO.setContent("Learn spring and create projects");
    return createArticleDTO;
  }
}
