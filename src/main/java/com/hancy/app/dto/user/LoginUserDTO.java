package com.hancy.app.dto.user;

import jakarta.validation.constraints.NotNull;

public class LoginUserDTO {

  @NotNull private String username;

  @NotNull private String password;

  public LoginUserDTO() {}

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
