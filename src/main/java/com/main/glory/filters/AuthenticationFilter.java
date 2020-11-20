package com.main.glory.filters;

import com.main.glory.model.user.Permissions;
import com.main.glory.model.user.UserData;
import com.main.glory.model.user.UserPermission;
import com.main.glory.servicesImpl.UserServiceImpl;
import com.main.glory.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
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
import java.util.ArrayList;
import java.util.Map;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private JwtUtil jwtUtil;

	@SneakyThrows
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {


		String path = "";
		String method = "";

		try{
			path = request.getRequestURI().substring(5);
			method = request.getMethod();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(path.startsWith("user")){
			chain.doFilter(request, response);
			return;
		}

		final String authorizationHeader = request.getHeader("Authorization");

		String id = null;
		String jwt = null;
		try {
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				id = jwtUtil.extractUsername(jwt);
				Claims claims = jwtUtil.extractAllClaims(jwt);
				Map userPermissions = (Map) claims.get("permissions", Object.class);

				System.out.println(path+"-"+method);
				System.out.println(userPermissions);

				System.out.println(path);
				if(path.contains("party")){
					Integer code = (Integer) userPermissions.get("pa");
					Permissions permissions = new Permissions(code);
					System.out.println(permissions);
					if(method.equals("GET")){
						if(!permissions.getView() && !permissions.getViewAll()){
							throw new Exception("Unauthorized user");
						}
					}
					if(method.equals("POST")){
						if(!permissions.getAdd()){
							throw new Exception("Unauthorized user");
						}
					}
					if(method.equals("PUT")){
						if(!permissions.getEdit() && !permissions.getEditAll()){
							throw new Exception("Unauthorized user");
						}
					}
					if(method.equals("DELETE")){
						if(!permissions.getDelete() && !permissions.getDeleteAll()){
							throw new Exception("Unauthorized user");
						}
					}
				}

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

			if(id == null){
				throw new Exception("No JWT found");
			}

			chain.doFilter(request, response);
		} catch(Exception e){
			e.printStackTrace();
			response.sendError(403,e.getMessage());
		}
	}

}