package com.main.glory.filters;

import com.main.glory.model.user.UserData;
import com.main.glory.model.user.UserPermission;
import com.main.glory.servicesImpl.UserServiceImpl;
import com.main.glory.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		System.out.println("-------------"+request.getRequestURI());

		final String authorizationHeader = request.getHeader("Authorization");

		String id = null;
		String jwt = null;
		try {
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				id = jwtUtil.extractUsername(jwt);
				Claims claims = jwtUtil.extractAllClaims(jwt);
				Object userPermissions = claims.get("permissions", Object.class);
				//All permissions
				System.out.println(userPermissions);

				//userPermissions.

			}

			//System.out.println(SecurityContextHolder.getContext().getAuthentication());
			if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserData userDetails = userService.getUserById(Long.parseLong(id));
				//System.out.println(userDetails);
				if (userDetails != null && jwtUtil.validateToken(jwt, userDetails, System.currentTimeMillis())) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, new ArrayList<>());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//					response.setHeader("accessToken", jwtUtil.generateToken(userDetails,otp));
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("Invalid token received");
		}
		chain.doFilter(request, response);
	}

}