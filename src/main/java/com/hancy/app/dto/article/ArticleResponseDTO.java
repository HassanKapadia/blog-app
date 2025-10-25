package com.hancy.app.dto.article;

import com.hancy.app.dto.comment.CommentResponseDTO;
import com.hancy.app.dto.user.UserResponseDTO;
import com.hancy.app.model.Article;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;

public class ArticleResponseDTO {

  private Long id;
  private String title;
  private String subTitle;
  private String content;
  private Date createdOn;
  private Date updatedOn;
  private UserResponseDTO author;
  private List<CommentResponseDTO> commentList = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public UserResponseDTO getAuthor() {
    return author;
  }

  public void setAuthor(UserResponseDTO author) {
    this.author = author;
  }

  public List<CommentResponseDTO> getCommentList() {
    return commentList;
  }

  public void setCommentList(List<CommentResponseDTO> commentList) {
    this.commentList = commentList;
  }

  public static ArticleResponseDTO createResponse(Article article) {
    ArticleResponseDTO articleResponseDTO = new ArticleResponseDTO();
    articleResponseDTO.setId(article.getId());
    articleResponseDTO.setTitle(article.getTitle());
    articleResponseDTO.setSubTitle(article.getSubTitle());
    articleResponseDTO.setContent(article.getContent());
    if (article.getAuthor() != null) {
      articleResponseDTO.setAuthor(UserResponseDTO.createResponse(article.getAuthor()));
    }
    articleResponseDTO.setCommentList(CommentResponseDTO.createResponse(article.getCommentList()));
    articleResponseDTO.setCreatedOn(article.getCreatedOn());
    articleResponseDTO.setUpdatedOn(article.getUpdatedOn());
    return articleResponseDTO;
  }

  public static List<ArticleResponseDTO> createResponse(List<Article> articleList) {
    List<ArticleResponseDTO> articleResponseList = new ArrayList<>();
    if (ObjectUtils.isNotEmpty(articleList)) {
      for (Article article : articleList) {
        articleResponseList.add(createResponse(article));
      }
    }
    return articleResponseList;
  }
}
