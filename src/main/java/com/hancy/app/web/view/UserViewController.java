package com.hancy.app.web.view;

import com.hancy.app.common.constants.BlogAppConstants;
import com.hancy.app.dto.user.CreateUserDTO;
import com.hancy.app.dto.user.LoginUserDTO;
import com.hancy.app.dto.user.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
  private final String USER_BASE_URL = BlogAppConstants.BASE_URL + "/users";

  @Autowired
  public UserViewController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
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
    return "redirect:/blog-app";
  }

  @GetMapping("/blog-app")
  public String showLoginPage() {
    return BlogAppConstants.USER_LOGIN_PAGE;
  }

  @GetMapping("/signup")
  public String showSignupForm(Model model) {
    return "users/signup";
  }

  @PostMapping("/signup")
  public String handleSignup(
      @Valid @ModelAttribute("user") CreateUserDTO user, BindingResult result, Model model) {

    if (result.hasErrors()) {
      return BlogAppConstants.USER_SIGNUP_PAGE;
    }

    try {
      ResponseEntity<UserResponseDTO> response =
          restTemplate.postForEntity(USER_BASE_URL + "/signup", user, UserResponseDTO.class);

      model.addAttribute(
          "message", "User registered successfully: " + response.getBody().getUsername());
      return BlogAppConstants.USER_LOGIN_PAGE;

    } catch (Exception e) {
      model.addAttribute("error", "Signup failed: " + e.getMessage());
      return BlogAppConstants.USER_SIGNUP_PAGE;
    }
  }
}
