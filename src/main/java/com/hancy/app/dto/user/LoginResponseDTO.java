package com.hancy.app.dto.user;

public class LoginResponseDTO {

  private UserResponseDTO authUser;
  private String jwtToken;

  public UserResponseDTO getAuthUser() {
    return authUser;
  }

  public void setAuthUser(UserResponseDTO authUser) {
    this.authUser = authUser;
  }

  public String getJwtToken() {
    return jwtToken;
  }

  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }
}
