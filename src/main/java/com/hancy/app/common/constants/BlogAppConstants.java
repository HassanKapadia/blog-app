package com.hancy.app.common.constants;

public class BlogAppConstants {

  private BlogAppConstants() {}

  public static final String BASE_URL = "http://localhost:8080";

  // User API endpoints
  public static final String API_USER = "/api/users";
  public static final String USER_BASE_URL = BASE_URL + API_USER;
  public static final String API_USER_SIGNUP = USER_BASE_URL + "/signup";
  public static final String API_USER_LOGIN = USER_BASE_URL + "/login";

  // Article API endpoints
  public static final String API_ARTICLE = "/api/articles";
  public static final String ARTICLE_BASE_URL = BASE_URL + API_ARTICLE;
  public static final String API_ARTICLE_DETAIL = ARTICLE_BASE_URL + "/%s";

  // User thymeleaf pages
  public static final String USER_SIGNUP_PAGE = "users/signup";
  public static final String USER_LOGIN_PAGE = "users/login";

  // Article thymeleaf pages
  public static final String ARTICLE_LIST_PAGE = "articles/list";
  public static final String ARTICLE_DETAIL_PAGE = "articles/detail";
  public static final String ARTICLE_CREATE_PAGE = "articles/create";

  // Redirect thymeleaf pages
  public static final String REDIRECT_APP_ROOT = "redirect:/blog-app";
  public static final String REDIRECT_ARTICLES = "redirect:/articles";

  // Common key constants
  public static final String AUTH_USER = "authUser";
  public static final String AUTH_TOKEN_JWT = "jwtToken";
  public static final String AUTH_USER_ID = "userId";
}
