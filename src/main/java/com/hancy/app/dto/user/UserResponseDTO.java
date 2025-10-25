package com.hancy.app.dto.user;

import com.hancy.app.model.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;

public class UserResponseDTO {

  private Long id;
  private String name;
  private String username;
  private String email;
  private String bio;
  private String image;
  private Date createdOn;
  private Date updatedOn;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
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

  public static UserResponseDTO createResponse(User user) {
    UserResponseDTO userResponseDTO = new UserResponseDTO();
    userResponseDTO.setId(user.getId());
    userResponseDTO.setName(user.getName());
    userResponseDTO.setUsername(user.getUsername());
    userResponseDTO.setEmail(user.getEmail());
    userResponseDTO.setBio(user.getBio());
    userResponseDTO.setCreatedOn(user.getCreatedOn());
    userResponseDTO.setImage(user.getImage());
    userResponseDTO.setUpdatedOn(user.getUpdatedOn());
    return userResponseDTO;
  }

  public static List<UserResponseDTO> createResponse(List<User> userList) {
    List<UserResponseDTO> userResponseList = new ArrayList<>();
    if (ObjectUtils.isNotEmpty(userList)) {
      for (User user : userList) {
        userResponseList.add(createResponse(user));
      }
    }
    return userResponseList;
  }
}
