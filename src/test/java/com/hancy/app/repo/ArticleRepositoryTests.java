package com.hancy.app.repo;

import com.hancy.app.model.Article;
import com.hancy.app.model.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ArticleRepositoryTests {

  @Autowired private ArticleRepository articleRepo;

  @Autowired private UserRepository userRepo;

  @Test
  public void canCreateArticle() {
    Article article = createArticle();
    articleRepo.save(article);
    articleRepo.flush();
  }

  @Test
  public void canSearchArticle() {
    Article article = createArticle();
    articleRepo.save(article);
    articleRepo.flush();

    User author = userRepo.findByUsername("hassank").get();
    Optional<List<Article>> articleList = articleRepo.findByAuthor(author);

    Assertions.assertEquals(1, articleList.get().size());
  }

  protected void createAuthor() {
    User user = new User();
    user.setName("Hassan Kapadia");
    user.setUsername("hassank");
    user.setEmail("hsn.kapadia@gmail.com");
    user.setPassword("StrongPassword");
    userRepo.save(user);
    userRepo.flush();
  }

  protected Article createArticle() {
    createAuthor();
    User author = userRepo.findByUsername("hassank").get();
    String title = "Article 1";
    String slug = "article-1";
    Article article = new Article();
    article.setTitle(title);
    article.setSlug(slug);
    article.setContent("This is an article for tests");
    article.setAuthor(author);

    return article;
  }
}
