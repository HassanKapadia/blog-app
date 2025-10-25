package com.hancy.app.dto.comment;

import com.hancy.app.dto.user.UserResponseDTO;
import com.hancy.app.model.Comment;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentResponseDTO {

  private Long id;
  private String comment;
  private Date createdOn;
  private Date updatedOn;
  private UserResponseDTO commentor;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public Date getUpdatedOn() {
    return updatedOn;
  }

  public void setUpdatedOn(Date updatedOn) {
    this.updatedOn = updatedOn;
  }

  public UserResponseDTO getCommentor() {
    return commentor;
  }

  public void setCommentor(UserResponseDTO commentor) {
    this.commentor = commentor;
  }

  public static CommentResponseDTO createResponse(Comment comment) {
    CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
    commentResponseDTO.setId(comment.getId());
    commentResponseDTO.setComment(comment.getComment());
    if (comment.getCommentor() != null) {
      commentResponseDTO.setCommentor(UserResponseDTO.createResponse(comment.getCommentor()));
    }
    commentResponseDTO.setCreatedOn(comment.getCreatedOn());
    commentResponseDTO.setUpdatedOn(comment.getUpdatedOn());
    return commentResponseDTO;
  }

  public static List<CommentResponseDTO> createResponse(List<Comment> commentList) {
    List<CommentResponseDTO> commentResponseDTOList = new ArrayList<>();
    for (Comment comment : commentList) {
      commentResponseDTOList.add(createResponse(comment));
    }
    return commentResponseDTOList;
  }
}
