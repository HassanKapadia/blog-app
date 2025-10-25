package com.hancy.app.web;

import com.hancy.app.common.constants.BlogAppConstants;
import com.hancy.app.common.dto.ErrorResponseDTO;
import com.hancy.app.dto.article.ArticleResponseDTO;
import com.hancy.app.dto.article.CreateArticleDTO;
import com.hancy.app.dto.article.UpdateArticleDTO;
import com.hancy.app.model.Article;
import com.hancy.app.service.article.ArticleService;
import com.hancy.app.service.article.ArticleServiceImpl.ArticleNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
public class ArticleRestController {

  private final ArticleService articleService;

  @Autowired
  public ArticleRestController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @GetMapping
  public ResponseEntity<List<ArticleResponseDTO>> getAllArticles() {
    List<Article> articleList = articleService.getAllArticles();
    return ResponseEntity.ok().body(ArticleResponseDTO.createResponse(articleList));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ArticleResponseDTO> getArticleById(@PathVariable("id") Long id) {
    Article article = articleService.getArticleById(id);
    return ResponseEntity.ok().body(ArticleResponseDTO.createResponse(article));
  }

  @PostMapping
  public ResponseEntity<ArticleResponseDTO> createArticle(
      @RequestBody CreateArticleDTO createArticle, HttpServletRequest request) {

    Long userId =
        (Long)
            request.getAttribute(
                BlogAppConstants.AUTH_USER_ID); // Attribute set by JwtAuthenticationFilter

    if (userId == null || !userId.equals(createArticle.getAuthorId())) {
      throw new UnauthorizedArticleAccessException(
          "You cannot create an article for another user!");
    }
    Article createdArticle = articleService.createArticle(userId, createArticle);
    URI createdArticleURI = URI.create(BlogAppConstants.API_ARTICLE + "/" + createdArticle.getId());
    return ResponseEntity.created(createdArticleURI)
        .body(ArticleResponseDTO.createResponse(createdArticle));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ArticleResponseDTO> updateArticle(
      @PathVariable("id") Long articleId,
      @RequestBody UpdateArticleDTO updateArticle,
      HttpServletRequest request)
      throws IllegalAccessException {
    Long userId =
        (Long)
            request.getAttribute(
                BlogAppConstants.AUTH_USER_ID); // Attribute set by JwtAuthenticationFilter

    Article updatedArticle = articleService.updateArticle(userId, articleId, updateArticle);
    return ResponseEntity.ok().body(ArticleResponseDTO.createResponse(updatedArticle));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteArticle(
      @PathVariable("id") Long articleId, HttpServletRequest request)
      throws IllegalAccessException {
    Long userId =
        (Long)
            request.getAttribute(
                BlogAppConstants.AUTH_USER_ID); // Attribute set by JwtAuthenticationFilter

    articleService.deleteArticle(userId, articleId);
    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponseDTO> manageErrorResponse(Exception ex) {
    HttpStatus status;
    String message;

    if (ex instanceof ArticleNotFoundException) {
      status = HttpStatus.NOT_FOUND;
    } else {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    message = ex.getLocalizedMessage();
    ErrorResponseDTO errorResponse = new ErrorResponseDTO(message);
    return ResponseEntity.status(status).body(errorResponse);
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  public class UnauthorizedArticleAccessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnauthorizedArticleAccessException(String message) {
      super(message);
    }
  }
}
