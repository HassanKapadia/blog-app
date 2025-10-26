package com.hancy.app.web;

import com.hancy.app.common.constants.BlogAppConstants;
import com.hancy.app.common.dto.ErrorResponseDTO;
import com.hancy.app.dto.user.CreateUserDTO;
import com.hancy.app.dto.user.LoginResponseDTO;
import com.hancy.app.dto.user.LoginUserDTO;
import com.hancy.app.dto.user.UpdateUserDTO;
import com.hancy.app.dto.user.UserResponseDTO;
import com.hancy.app.model.User;
import com.hancy.app.security.JwtTokenUtil;
import com.hancy.app.service.user.UserService;
import com.hancy.app.service.user.UserServiceImpl.InvalidCredentialsException;
import com.hancy.app.service.user.UserServiceImpl.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

  private final UserService userService;
  private final JwtTokenUtil jwtTokenUtil;

  @Autowired
  public UserRestController(UserService userService, JwtTokenUtil jwtTokenUtil) {
    this.userService = userService;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @PostMapping("/signup")
  public ResponseEntity<UserResponseDTO> signUpUser(@RequestBody CreateUserDTO createUser) {
    User createdUser = userService.createUser(createUser);
    URI createdUserURI = URI.create(BlogAppConstants.API_USER + "/" + createdUser.getId());
    return ResponseEntity.created(createdUserURI).body(UserResponseDTO.createResponse(createdUser));
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginUserDTO loginUser)
      throws InvalidCredentialsException {
    User savedUser = userService.loginUser(loginUser);
    UserResponseDTO userResponse = UserResponseDTO.createResponse(savedUser);
    String token = jwtTokenUtil.generateToken(userResponse.getId(), userResponse.getUsername());

    Map<String, Object> response = new HashMap<String, Object>();
    response.put(BlogAppConstants.AUTH_USER, userResponse);
    response.put(BlogAppConstants.AUTH_TOKEN_JWT, token);

    LoginResponseDTO loginResponse = new LoginResponseDTO();
    loginResponse.setAuthUser(userResponse);
    loginResponse.setJwtToken(token);

    return ResponseEntity.ok().body(loginResponse);
  }

  @GetMapping("/")
  public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
    List<User> userList = userService.getAllUsers();
    return ResponseEntity.ok().body(UserResponseDTO.createResponse(userList));
  }

  @GetMapping("/account")
  public ResponseEntity<UserResponseDTO> getUserAccountDetails(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute(BlogAppConstants.AUTH_USER_ID);
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }
    User user = userService.getUserById(userId);
    return ResponseEntity.ok().body(UserResponseDTO.createResponse(user));
  }

  @PutMapping("/account")
  public ResponseEntity<UserResponseDTO> updateUserAccount(
      @RequestBody UpdateUserDTO updateUserDTO, HttpServletRequest request) {
    Long userId = (Long) request.getAttribute(BlogAppConstants.AUTH_USER_ID);
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }
    User updatedUser = userService.updateUser(userId, updateUserDTO);
    return ResponseEntity.ok().body(UserResponseDTO.createResponse(updatedUser));
  }

  @DeleteMapping("/account")
  public ResponseEntity<UserResponseDTO> deleteUserAccount(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute(BlogAppConstants.AUTH_USER_ID);
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }
    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler({
    UserNotFoundException.class,
    InvalidCredentialsException.class,
    Exception.class
  })
  public ResponseEntity<ErrorResponseDTO> manageErrorResponse(Exception ex) {
    HttpStatus status;
    String message;

    if (ex instanceof UserNotFoundException) {
      status = HttpStatus.NOT_FOUND;
    } else if (ex instanceof InvalidCredentialsException) {
      status = HttpStatus.UNAUTHORIZED;
    } else {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    message =
        ex.getLocalizedMessage() != null
            ? ex.getLocalizedMessage()
            : "An unexpected error occurred";
    ErrorResponseDTO errorResponse = new ErrorResponseDTO(message);
    return ResponseEntity.status(status).body(errorResponse);
  }
}
