package com.hancy.app.web.view;

import com.hancy.app.common.constants.BlogAppConstants;
import com.hancy.app.dto.user.CreateUserDTO;
import com.hancy.app.dto.user.LoginUserDTO;
import com.hancy.app.dto.user.UserResponseDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class UserViewController {

  private final RestTemplate restTemplate;
  private final HttpSession session;

  @Autowired
  public UserViewController(RestTemplate restTemplate, HttpSession session) {
    this.restTemplate = restTemplate;
    this.session = session;
  }

  @ModelAttribute("user")
  public CreateUserDTO createUserDTO() {
    return new CreateUserDTO();
  }

  @ModelAttribute("loginUser")
  public LoginUserDTO loginUserDTO() {
    return new LoginUserDTO();
  }

  @GetMapping("/")
  public String redirectRoot() {
    return BlogAppConstants.REDIRECT_APP_ROOT;
  }

  @GetMapping("/blog-app")
  public String showLoginPage() {
    return BlogAppConstants.USER_LOGIN_PAGE;
  }

  @GetMapping("/signup")
  public String showSignupForm(Model model) {
    return BlogAppConstants.USER_SIGNUP_PAGE;
  }

  @PostMapping("/login")
  public String handleLogin(
      @Valid @ModelAttribute("loginUser") LoginUserDTO user, BindingResult result, Model model) {
    if (result.hasErrors()) {
      return BlogAppConstants.USER_LOGIN_PAGE;
    }

    try {
      ResponseEntity<Map> response =
          restTemplate.postForEntity(BlogAppConstants.API_USER_LOGIN, user, Map.class);

      Map<String, Object> body = response.getBody();
      Map<String, Object> userData = (Map<String, Object>) body.get(BlogAppConstants.AUTH_USER);

      // Add user data and token to the session for client
      session.setAttribute(
          BlogAppConstants.AUTH_TOKEN_JWT, body.get(BlogAppConstants.AUTH_TOKEN_JWT));
      session.setAttribute(BlogAppConstants.AUTH_USER, userData);

      return BlogAppConstants.REDIRECT_ARTICLES;
    } catch (Exception e) {
      model.addAttribute("error", "Invalid username or password");
      return BlogAppConstants.USER_LOGIN_PAGE;
    }
  }

  @PostMapping("/signup")
  public String handleSignup(
      @Valid @ModelAttribute("user") CreateUserDTO user, BindingResult result, Model model) {

    if (result.hasErrors()) {
      return BlogAppConstants.USER_SIGNUP_PAGE;
    }

    try {
      ResponseEntity<UserResponseDTO> response =
          restTemplate.postForEntity(BlogAppConstants.API_USER_SIGNUP, user, UserResponseDTO.class);

      model.addAttribute(
          "message", "User registered successfully: " + response.getBody().getUsername());
      return BlogAppConstants.USER_LOGIN_PAGE;
    } catch (Exception e) {
      model.addAttribute("error", "Signup failed: " + e.getMessage());
      return BlogAppConstants.USER_SIGNUP_PAGE;
    }
  }

  @GetMapping("/my-account")
  public String myAccount(Model model) {
    HttpHeaders headers = getDefaultHttpHeader();
    HttpEntity<Void> entity = new HttpEntity<>(headers);
    ResponseEntity<UserResponseDTO> response =
        restTemplate.exchange(
            BlogAppConstants.API_USER_ACCOUNT, HttpMethod.GET, entity, UserResponseDTO.class);
    model.addAttribute("user", response.getBody());
    return BlogAppConstants.USER_ACCOUNT_PAGE;
  }

  @GetMapping("/logout")
  public String logout() {
    session.invalidate();
    return BlogAppConstants.REDIRECT_APP_ROOT;
  }

  private HttpHeaders getDefaultHttpHeader() {
    HttpHeaders headers = new HttpHeaders();
    String jwtToken = (String) session.getAttribute(BlogAppConstants.AUTH_TOKEN_JWT);
    headers.setBearerAuth(jwtToken); // attach JWT
    return headers;
  }
}
