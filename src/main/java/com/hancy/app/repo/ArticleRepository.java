package com.hancy.app.repo;

import com.hancy.app.model.Article;
import com.hancy.app.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

  public Optional<Article> findBySlug(String slug);

  public Optional<List<Article>> findByAuthor(User author);
}
