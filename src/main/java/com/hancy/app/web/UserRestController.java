package com.hancy.app.web;

import com.hancy.app.common.constants.BlogAppConstants;
import com.hancy.app.common.dto.ErrorResponseDTO;
import com.hancy.app.dto.user.CreateUserDTO;
import com.hancy.app.dto.user.LoginUserDTO;
import com.hancy.app.dto.user.UserResponseDTO;
import com.hancy.app.model.User;
import com.hancy.app.security.JwtTokenUtil;
import com.hancy.app.service.user.UserService;
import com.hancy.app.service.user.UserServiceImpl.UserNotFoundException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
  public ResponseEntity<Map<String, Object>> loginUser(@RequestBody LoginUserDTO loginUser) {
    User savedUser = userService.loginUser(loginUser);
    UserResponseDTO userResponse = UserResponseDTO.createResponse(savedUser);
    String token = jwtTokenUtil.generateToken(userResponse.getId(), userResponse.getUsername());

    Map<String, Object> response = new HashMap<String, Object>();
    response.put(BlogAppConstants.AUTH_USER, userResponse);
    response.put(BlogAppConstants.AUTH_TOKEN_JWT, token);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/")
  public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
    List<User> userList = userService.getAllUsers();
    return ResponseEntity.ok().body(UserResponseDTO.createResponse(userList));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponseDTO> manageErrorResponse(Exception ex) {
    HttpStatus status;
    String message;

    if (ex instanceof UserNotFoundException) {
      status = HttpStatus.NOT_FOUND;
    } else {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    message = ex.getLocalizedMessage();
    ErrorResponseDTO errorResponse = new ErrorResponseDTO(message);
    return ResponseEntity.status(status).body(errorResponse);
  }
}
