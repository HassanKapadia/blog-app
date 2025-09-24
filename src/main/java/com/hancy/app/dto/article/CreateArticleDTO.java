package com.hancy.app.dto.article;

import jakarta.validation.constraints.NotNull;

public class CreateArticleDTO {

  @NotNull private String title;
  private String subTitle;
  @NotNull private String content;

  public CreateArticleDTO() {}

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubTitle() {
    return subTitle;
  }

  public void setSubTitle(String subTitle) {
    this.subTitle = subTitle;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
