package com.hancy.app.dto.comment;

import jakarta.validation.constraints.NotNull;

public class CommentDTO {

  @NotNull private String comment;

  public CommentDTO() {}

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
