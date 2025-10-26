package com.hancy.app.service.user;

import com.hancy.app.dto.user.CreateUserDTO;
import com.hancy.app.dto.user.LoginUserDTO;
import com.hancy.app.dto.user.UpdateUserDTO;
import com.hancy.app.model.User;
import com.hancy.app.service.user.UserServiceImpl.InvalidCredentialsException;
import java.util.List;

public interface UserService {

  public List<User> getAllUsers();

  public User getUserByUsername(String username);

  public User getUserById(Long userId);

  public User createUser(CreateUserDTO createUser);

  public User updateUser(Long userId, UpdateUserDTO updateUser);

  public User loginUser(LoginUserDTO loginUser) throws InvalidCredentialsException;

  public void deleteUser(Long userId);
}
