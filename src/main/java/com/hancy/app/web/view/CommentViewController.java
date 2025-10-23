package com.hancy.app.web.view;

import com.hancy.app.common.constants.BlogAppConstants;
import com.hancy.app.dto.comment.CommentDTO;
import com.hancy.app.dto.comment.CommentResponseDTO;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/articles/{articleId}/comments")
public class CommentViewController {

  private final RestTemplate restTemplate;

  @Autowired
  public CommentViewController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @PostMapping
  public String postComment(
      @PathVariable("articleId") Long articleId,
      @RequestParam("comment") String commentText,
      HttpSession session) {
    Map<String, Object> authUser =
        (Map<String, Object>) session.getAttribute(BlogAppConstants.AUTH_USER);
    Long userId = ((Number) authUser.get("id")).longValue();

    CommentDTO commentDTO = new CommentDTO();
    commentDTO.setComment(commentText);

    HttpHeaders headers = getDefaultHttpHeader(session);
    HttpEntity<CommentDTO> entity = new HttpEntity<>(commentDTO, headers);

    String createCommentEndpoint = String.format(BlogAppConstants.COMMENT_BASE_URL, articleId);
    restTemplate.exchange(createCommentEndpoint, HttpMethod.POST, entity, CommentResponseDTO.class);

    return BlogAppConstants.REDIRECT_ARTICLES
        + "/"
        + articleId; // This will redirect to view article endpoint
  }

  private HttpHeaders getDefaultHttpHeader(HttpSession session) {
    HttpHeaders headers = new HttpHeaders();
    String jwtToken = (String) session.getAttribute(BlogAppConstants.AUTH_TOKEN_JWT);
    headers.setBearerAuth(jwtToken); // attach JWT
    return headers;
  }
}
