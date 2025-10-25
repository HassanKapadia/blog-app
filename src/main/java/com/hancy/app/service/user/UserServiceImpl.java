package com.hancy.app.service.user;

import com.hancy.app.dto.user.CreateUserDTO;
import com.hancy.app.dto.user.LoginUserDTO;
import com.hancy.app.dto.user.UpdateUserDTO;
import com.hancy.app.model.User;
import com.hancy.app.repo.UserRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private UserRepository userRepo;

  @Autowired
  public UserServiceImpl(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public List<User> getAllUsers() {
    return userRepo.findAll();
  }

  @Override
  public User getUserByUsername(String username) {
    Optional<User> user = userRepo.findByUsername(username);
    if (!user.isPresent()) {
      throw new UserNotFoundException(username);
    }
    return user.get();
  }

  @Override
  public User getUserById(Long userId) {
    Optional<User> user = userRepo.findById(userId);
    if (!user.isPresent()) {
      throw new UserNotFoundException(userId);
    }
    return user.get();
  }

  @Override
  public User createUser(CreateUserDTO createUser) {
    User user = new User();
    user.setName(createUser.getName());
    user.setUsername(createUser.getUsername());
    user.setEmail(createUser.getEmail());
    user.setPassword(createUser.getPassword());
    user.setBio(createUser.getBio());
    user.setImage(createUser.getImage());
    user.setCreatedOn(new Date());
    return userRepo.save(user);
  }

  @Override
  public User updateUser(Long userId, UpdateUserDTO updateUser) {
    Optional<User> user = userRepo.findById(userId);
    if (!user.isPresent()) {
      throw new UserNotFoundException(userId);
    }
    User savedUser = user.get();
    if (updateUser.getName() != null) {
      savedUser.setName(updateUser.getName());
    }

    if (updateUser.getEmail() != null) {
      savedUser.setEmail(updateUser.getEmail());
    }

    if (updateUser.getBio() != null) {
      savedUser.setBio(updateUser.getBio());
    }

    if (updateUser.getImage() != null) {
      savedUser.setImage(updateUser.getImage());
    }

    savedUser.setUpdatedOn(new Date());
    return userRepo.save(savedUser);
  }

  @Override
  public User loginUser(LoginUserDTO loginUser) {
    Optional<User> user = userRepo.findByUsername(loginUser.getUsername());
    if (!user.isPresent()) {
      throw new UserNotFoundException(loginUser.getUsername());
    }
    User savedUser = user.get();
    if (!savedUser.getPassword().equals(loginUser.getPassword())) {
      throw new IllegalAccessError("Incorrect username or password!");
    }
    return savedUser;
  }

  @Override
  public void deleteUser(Long userId) {
    User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    userRepo.delete(user);
  }

  public static class UserNotFoundException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String username) {
      super("User with username: " + username + " does not exist");
    }

    public UserNotFoundException(Long userId) {
      super("User with id: " + userId + " does not exist");
    }
  }
}
