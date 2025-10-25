package com.hancy.app.repo;

import com.hancy.app.model.Article;
import com.hancy.app.model.Comment;
import com.hancy.app.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CommentRepositoryTests {

  @Autowired private CommentRepository commentRepo;

  @Autowired private ArticleRepository articleRepo;

  @Autowired private UserRepository userRepo;

  @Test
  public void canCreateComment() {
    Comment comment = createComment();
    commentRepo.save(comment);
    commentRepo.flush();
  }

  public Comment createComment() {
    Comment comment = new Comment();
    comment.setComment("Awesome article");

    createCommentor();
    User commentor = userRepo.findByUsername("hassank").get();
    comment.setCommentor(commentor);

    createArticle();
    String title = "Article 1";
    String slug = title.toLowerCase().replaceAll("\\s+", "-");
    Article article = articleRepo.findBySlug(slug).get();
    comment.setArticle(article);

    return comment;
  }

  protected void createCommentor() {
    User commentor = new User();
    commentor.setName("Hassan Kapadia");
    commentor.setUsername("hassank");
    commentor.setEmail("hsn.kapadia@gmail.com");
    commentor.setPassword("StrongPassword");

    userRepo.save(commentor);
    userRepo.flush();
  }

  protected void createArticle() {
    User author = userRepo.findByUsername("hassank").get();
    String title = "Article 1";
    String slug = title.toLowerCase().replaceAll("\\s+", "-");
    Article article = new Article();
    article.setTitle(title);
    article.setSlug(slug);
    article.setContent("This is an article for tests");
    article.setAuthor(author);

    articleRepo.save(article);
    articleRepo.flush();
  }
}
