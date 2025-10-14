package com.hancy.app.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import org.springframework.data.annotation.CreatedDate;

@Entity
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq")
  @SequenceGenerator(name = "comment_seq", sequenceName = "comment_sequence", allocationSize = 1)
  private Long id;

  @NotNull private String comment;

  @CreatedDate private Date createdOn;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinColumn(name = "article")
  @NotNull
  private Article article;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinColumn(name = "commentor")
  @NotNull
  private User commentor;

  public Comment() {}

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

  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }

  public User getCommentor() {
    return commentor;
  }

  public void setCommentor(User commentor) {
    this.commentor = commentor;
  }
}
