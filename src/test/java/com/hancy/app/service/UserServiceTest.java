package com.hancy.app.service;

import com.hancy.app.dto.user.CreateUserDTO;
import com.hancy.app.dto.user.UpdateUserDTO;
import com.hancy.app.model.User;
import com.hancy.app.service.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

  @Autowired private UserService userService;

  @Test
  public void canCreateUser() {
    CreateUserDTO createUser = createUser();
    User user = userService.createUser(createUser);
    Assertions.assertEquals("hassank", user.getUsername());
  }

  @Test
  public void updateUser() {
    User user = userService.getUserByUsername("hassank");

    UpdateUserDTO updateUser = new UpdateUserDTO();
    updateUser.setEmail("hassan.kapadia@blog.com");

    User updatedUser = userService.updateUser(user.getId(), updateUser);
    Assertions.assertEquals("hassan.kapadia@blog.com", updatedUser.getEmail());
  }

  protected CreateUserDTO createUser() {
    CreateUserDTO createUser = new CreateUserDTO();
    createUser.setName("Hassan Kapadia");
    createUser.setUsername("hassank");
    createUser.setPassword("HassanK@7864");
    createUser.setEmail("hsn.kapadia@blog.com");
    return createUser;
  }
}
