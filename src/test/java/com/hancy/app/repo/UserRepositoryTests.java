package com.hancy.app.repo;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.hancy.app.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTests {

  @Autowired private UserRepository userRepo;

  @Test
  public void canCreateUser() {
    User user = createUser();

    userRepo.save(user);
    userRepo.flush();
  }

  @Test
  public void checkNullableUser() {
    User user = createUser();
    user.setUsername(null);
    userRepo.save(user);
    assertThrows(Exception.class, () -> userRepo.flush());
  }

  @Test
  public void checkSearchUserByUsername() {
    User user = createUser();
    userRepo.save(user);
    userRepo.flush();

    User user1 = userRepo.findByUsername("hassank").get();
    Assertions.assertEquals("hassank", user1.getUsername());
  }

  @Test
  public void checkSearchUserByEmail() {
    User user = createUser();
    userRepo.save(user);
    userRepo.flush();

    User user1 = userRepo.findByEmail("hsn.kapadia@gmail.com").get();
    Assertions.assertEquals("hsn.kapadia@gmail.com", user1.getEmail());
  }

  protected User createUser() {
    User user = new User();
    user.setName("Hassan Kapadia");
    user.setUsername("hassank");
    user.setEmail("hsn.kapadia@gmail.com");
    user.setPassword("StrongPassword");
    return user;
  }
}
