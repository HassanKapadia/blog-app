package com.hancy.app.service;

import com.hancy.app.dto.article.CreateArticleDTO;
import com.hancy.app.dto.user.CreateUserDTO;
import com.hancy.app.model.Article;
import com.hancy.app.model.User;
import com.hancy.app.service.article.ArticleService;
import com.hancy.app.service.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ArticleServiceTest {

  @Autowired private ArticleService articleService;

  @Autowired private UserService userService;

  @Test
  public void canCreateArticle() {
    CreateUserDTO createUser = createUser();
    User author = userService.createUser(createUser);
    Assertions.assertEquals("hassank", author.getUsername());

    CreateArticleDTO createArticle = createArticle();
    String slug = createArticle.getTitle().toLowerCase().replaceAll("\\s+", "-");
    Article article = articleService.createArticle(author.getId(), createArticle);
    Assertions.assertEquals(slug, article.getSlug());
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
