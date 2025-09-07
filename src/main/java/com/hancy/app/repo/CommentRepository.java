package com.hancy.app.repo;

import com.hancy.app.model.Article;
import com.hancy.app.model.Comment;
import com.hancy.app.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  public Optional<List<Comment>> findByArticle(Article article);

  public Optional<List<Comment>> findByCommentor(User commentor);
}
