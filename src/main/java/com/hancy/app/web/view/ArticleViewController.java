package com.hancy.app.web.view;

import com.hancy.app.common.constants.BlogAppConstants;
import com.hancy.app.dto.article.ArticleResponseDTO;
import com.hancy.app.dto.article.CreateArticleDTO;
import com.hancy.app.dto.article.UpdateArticleDTO;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/articles")
public class ArticleViewController {

  private final RestTemplate restTemplate;

  @Autowired
  public ArticleViewController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @GetMapping
  public String listArticles(Model model, HttpSession session) {
    Map<String, Object> authUser =
        (Map<String, Object>) session.getAttribute(BlogAppConstants.AUTH_USER);

    HttpHeaders headers = getDefaultHttpHeader(session);
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<List> response =
        restTemplate.exchange(
            BlogAppConstants.ARTICLE_BASE_URL, HttpMethod.GET, entity, List.class);
    model.addAttribute("articles", response.getBody());
    model.addAttribute(BlogAppConstants.AUTH_USER, authUser);
    return BlogAppConstants.ARTICLE_LIST_PAGE;
  }

  @GetMapping("/{id}")
  public String viewArticle(@PathVariable("id") Long articleId, Model model, HttpSession session) {
    String viewArticleEndpoint =
        String.format(BlogAppConstants.API_ARTICLE_DETAIL, String.valueOf(articleId));

    HttpHeaders headers = getDefaultHttpHeader(session);
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<ArticleResponseDTO> response =
        restTemplate.exchange(
            viewArticleEndpoint, HttpMethod.GET, entity, ArticleResponseDTO.class);
    model.addAttribute("article", response.getBody());
    model.addAttribute("comments", response.getBody().getCommentList());
    model.addAttribute(
        BlogAppConstants.AUTH_USER, session.getAttribute(BlogAppConstants.AUTH_USER));
    return BlogAppConstants.ARTICLE_DETAIL_PAGE;
  }

  @GetMapping("/create")
  public String showCreateArticleForm(Model model) {
    model.addAttribute("articleForm", new CreateArticleDTO());
    return BlogAppConstants.ARTICLE_CREATE_PAGE;
  }

  @PostMapping("/create")
  public String handleCreateArticle(
      @ModelAttribute("articleForm") CreateArticleDTO article, Model model, HttpSession session) {
    try {
      Map<String, Object> authUser =
          (Map<String, Object>) session.getAttribute(BlogAppConstants.AUTH_USER);
      article.setAuthorId(((Number) authUser.get("id")).longValue());

      HttpHeaders headers = getDefaultHttpHeader(session);

      HttpEntity<CreateArticleDTO> entity = new HttpEntity<>(article, headers);
      ResponseEntity<ArticleResponseDTO> response =
          restTemplate.exchange(
              BlogAppConstants.ARTICLE_BASE_URL, HttpMethod.POST, entity, ArticleResponseDTO.class);

      return BlogAppConstants.REDIRECT_ARTICLES;
    } catch (Exception e) {
      model.addAttribute("error", "Failed to create article: " + e.getMessage());
      return BlogAppConstants.ARTICLE_CREATE_PAGE;
    }
  }

  @GetMapping("/{id}/edit")
  public String showEditForm(@PathVariable("id") Long articleId, Model model, HttpSession session) {
    HttpHeaders headers = getDefaultHttpHeader(session);
    HttpEntity<Void> entity = new HttpEntity<Void>(headers);
    String viewArticleEndpoint =
        String.format(BlogAppConstants.API_ARTICLE_DETAIL, String.valueOf(articleId));

    ResponseEntity<ArticleResponseDTO> response =
        restTemplate.exchange(
            viewArticleEndpoint, HttpMethod.GET, entity, ArticleResponseDTO.class);
    model.addAttribute("articleForm", response.getBody());
    return BlogAppConstants.ARTICLE_EDIT_PAGE;
  }

  @PostMapping("/{id}/edit")
  public String handleUpdateArticle(
      @PathVariable("id") Long articleId,
      @ModelAttribute("articleForm") UpdateArticleDTO updateArticle,
      HttpSession session,
      RedirectAttributes redirectAttributes) {
    HttpHeaders headers = getDefaultHttpHeader(session);
    HttpEntity<UpdateArticleDTO> entity = new HttpEntity<>(updateArticle, headers);
    String viewArticleEndpoint =
        String.format(BlogAppConstants.API_ARTICLE_DETAIL, String.valueOf(articleId));

    restTemplate.exchange(viewArticleEndpoint, HttpMethod.PUT, entity, ArticleResponseDTO.class);
    redirectAttributes.addFlashAttribute("success", "Article updated successfully!");

    return BlogAppConstants.REDIRECT_ARTICLES + "/" + articleId;
  }

  @PostMapping("/{id}/delete")
  public String deleteArticle(@PathVariable("id") Long articleId, HttpSession session) {
    HttpHeaders headers = getDefaultHttpHeader(session);
    HttpEntity<Void> entity = new HttpEntity<>(headers);
    String viewArticleEndpoint =
        String.format(BlogAppConstants.API_ARTICLE_DETAIL, String.valueOf(articleId));

    restTemplate.exchange(viewArticleEndpoint, HttpMethod.DELETE, entity, Void.class);
    return BlogAppConstants.REDIRECT_ARTICLES;
  }

  private HttpHeaders getDefaultHttpHeader(HttpSession session) {
    HttpHeaders headers = new HttpHeaders();
    String jwtToken = (String) session.getAttribute(BlogAppConstants.AUTH_TOKEN_JWT);
    headers.setBearerAuth(jwtToken); // attach JWT
    return headers;
  }
}
