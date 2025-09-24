package com.hancy.app.service.article;

import com.hancy.app.dto.article.CreateArticleDTO;
import com.hancy.app.dto.article.UpdateArticleDTO;
import com.hancy.app.model.Article;
import java.util.List;

public interface ArticleService {

  public List<Article> getAllArticles();

  public Article getArticleBySlug(String slug);

  public Article createArticle(Long authorId, CreateArticleDTO createArticle);

  public Article updateArticle(Long authorId, Long articleId, UpdateArticleDTO updateArticle)
      throws IllegalAccessException;

  public Article getArticleById(Long articleId);
}
