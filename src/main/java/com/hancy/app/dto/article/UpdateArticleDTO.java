package com.hancy.app.dto.article;

public class UpdateArticleDTO {

  private String title;
  private String subTitle;
  private String content;

  public UpdateArticleDTO() {}

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
