package com.hancy.app.security;

import com.hancy.app.common.constants.BlogAppConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenUtil jwtTokenUtil;

  @Autowired
  public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil) {
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String header = request.getHeader("Authorization");

    if (header != null && header.startsWith("Bearer ")) {
      try {
        String token = header.substring(7);
        long userId = jwtTokenUtil.extractUserId(token);
        String username = jwtTokenUtil.extractUsername(token);

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                new User(username, "", Collections.emptyList()), null, Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Make userId accessible to controllers
        request.setAttribute(BlogAppConstants.AUTH_USER_ID, userId);
      } catch (Exception e) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
        return;
      }
    }
    filterChain.doFilter(request, response);
  }
}
