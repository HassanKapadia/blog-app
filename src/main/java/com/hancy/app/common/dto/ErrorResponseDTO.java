package com.hancy.app.common.dto;

public class ErrorResponseDTO {

  public String message;

  public ErrorResponseDTO(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
