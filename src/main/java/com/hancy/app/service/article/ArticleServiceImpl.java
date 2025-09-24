package com.hancy.app.service.article;

import com.hancy.app.dto.article.CreateArticleDTO;
import com.hancy.app.dto.article.UpdateArticleDTO;
import com.hancy.app.model.Article;
import com.hancy.app.model.User;
import com.hancy.app.repo.ArticleRepository;
import com.hancy.app.service.user.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {

  private ArticleRepository articleRepo;
  private UserService userSerivce;

  @Autowired
  public ArticleServiceImpl(ArticleRepository articleRepo, UserService userSerivce) {
    this.articleRepo = articleRepo;
    this.userSerivce = userSerivce;
  }

  @Override
  public List<Article> getAllArticles() {
    return articleRepo.findAll();
  }

  @Override
  public Article getArticleBySlug(String slug) {
    Optional<Article> article = articleRepo.findBySlug(slug);
    if (!article.isPresent()) {
      throw new ArticleNotFoundException(slug);
    }
    return article.get();
  }

  @Override
  public Article createArticle(Long authorId, CreateArticleDTO createArticle) {
    Article article = new Article();

    User author = userSerivce.getUserById(authorId);
    article.setAuthor(author);

    article.setTitle(createArticle.getTitle());
    article.setSubTitle(createArticle.getSubTitle());
    article.setContent(createArticle.getContent());

    String slug = article.getTitle().toLowerCase().replace("\\s+", "-");
    article.setSlug(slug);

    return articleRepo.save(article);
  }

  @Override
  public Article updateArticle(Long authorId, Long articleId, UpdateArticleDTO updateArticle)
      throws IllegalAccessException {
    Optional<Article> article = articleRepo.findById(articleId);
    if (!article.isPresent()) {
      throw new ArticleNotFoundException(articleId);
    }

    User user = userSerivce.getUserById(authorId);

    Article savedArticle = article.get();

    if (!user.equals(savedArticle.getAuthor())) {
      throw new IllegalAccessException("User: " + user.getName() + " cannot edit the article.");
    }

    if (updateArticle.getTitle() != null) {
      savedArticle.setTitle(updateArticle.getTitle());
      String slug = savedArticle.getTitle().toLowerCase().replace("\\s+", "-");
      savedArticle.setSlug(slug);
    }

    if (updateArticle.getSubTitle() != null) {
      savedArticle.setSubTitle(updateArticle.getSubTitle());
    }

    if (updateArticle.getContent() != null) {
      savedArticle.setContent(updateArticle.getContent());
    }

    return articleRepo.save(savedArticle);
  }

  @Override
  public Article getArticleById(Long articleId) {
    Optional<Article> article = articleRepo.findById(articleId);
    if (!article.isPresent()) {
      throw new ArticleNotFoundException(articleId);
    }
    return article.get();
  }

  public static class ArticleNotFoundException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public ArticleNotFoundException(String slug) {
      super("Article with slug: " + slug + " not found");
    }

    public ArticleNotFoundException(Long articleId) {
      super("Article with id: " + articleId + " not found");
    }
  }
}
