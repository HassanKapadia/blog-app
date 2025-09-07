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
    User user = new User("hassank", "hsn.kapadia@gmail.com");
    userRepo.save(user);
    userRepo.flush();
  }

  protected Article createArticle() {
    createAuthor();
    User author = userRepo.findByUsername("hassank").get();
    String title = "Article 1";
    String slug = "article-1";
    Article article = new Article(title, "This is an article for tests", author);
    article.setSlug(slug);

    return article;
  }
}
