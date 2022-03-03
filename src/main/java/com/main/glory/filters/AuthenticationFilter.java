package com.main.glory.filters;

import com.main.glory.model.ConstantFile;
import com.main.glory.model.MappingPermission;
import com.main.glory.model.user.Permissions;
import com.main.glory.model.user.UserData;
import com.main.glory.model.user.UserPermission;
import com.main.glory.servicesImpl.UserServiceImpl;
import com.main.glory.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
			{

				//set the request timestamp in request
				request.setAttribute("requestTime",System.currentTimeMillis());

		String path = "";
		String method = "";

		try{

			// for swagger turn off the guards
//			if(true || !request.getRequestURI().startsWith("/swagger-ui.html")){
//				chain.doFilter(request, response);
//				return;
//			}

			path = request.getRequestURI().substring(5);
			System.out.println(path);
			/*StringBuffer jb = new StringBuffer();
			String line = null;
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);

			System.out.println(jb.toString());*/
			method = request.getMethod();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if( path.startsWith("login")  || path.startsWith("admin")  || request.getRequestURI().contains("machine") || request.getRequestURI().contains("db") || request.getRequestURI().contains("task") || request.getRequestURI().contains("testing") || request.getRequestURI().contains("document")){
			chain.doFilter(request, response);
			return;
		}

		final String authorizationHeader = request.getHeader("Authorization");

		/*if(authorizationHeader==null)
			chain.doFilter(request, response);*/

		String id = null;
		String jwt = null;
		try {

			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				id = jwtUtil.extractUsername(jwt);
				//validate user



				Claims claims = jwtUtil.extractAllClaims(jwt);
				Map userPermissions = (Map) claims.get("permissions", Object.class);

				System.out.println(path+"-"+method);
				System.out.println(userPermissions);

				System.out.println(path);

				Boolean pathFlag=false;
				//first find the request contain which page request
				MappingPermission mappingPermission=new MappingPermission();
				//Map<String,String> stringPath = mappingPermission.getMapping();
				for (Map.Entry<String,String> entry : mappingPermission.getMapping().entrySet()) {

					//getValue="moduleName" getKey="annottion"
					if(path.contains(entry.getValue()))
					{
						String[] pathArray = path.split("/");
						pathFlag=true;
						//if path contain then add check for that path
						Integer code = (Integer) userPermissions.get(entry.getKey());
						Permissions permissions = new Permissions(code);
						System.out.println(permissions);
						if(method.equals("GET")){
							if(Arrays.asList(pathArray).contains("all"))
							{
								/*if(path.contains("own"))
								{
									if(!permissions.getView())
										throw new Exception("Unauthorized user");
								}
								else if(path.contains("group"))
								{
									if(!permissions.getViewGroup())
										throw new Exception("Unauthorized user");
								}
								else if(path.contains("all"))
								{
									if(!permissions.getViewAll())
										throw new Exception("Unauthorized user");
								}*/
							}
							break;
						}
						else if(method.equals("POST")){
							if(!permissions.getAdd()){
								throw new Exception(ConstantFile.Unauthorized_User);
							}
						}
						else if(method.equals("PUT")){
							/*if(!permissions.getEdit()){
								throw new Exception("Unauthorized user");
							}
							else if(!permissions.getEditGroup()){
								throw new Exception("Unauthorized user");
							}
							else if(!permissions.getEditAll()){
								throw new Exception("Unauthorized user");
							}
							break;*/

						}
						else if(method.equals("DELETE")){
							if(!permissions.getDelete() || !permissions.getDeleteAll()){
								throw new Exception(ConstantFile.Unauthorized_User);
							}
						}
					}

				}
			}
			if(id == null){
				throw new Exception(ConstantFile.JWT_Not_Found);
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


			chain.doFilter(request, response);
		} catch(Exception e){
			e.printStackTrace();
			String errorMessage = e.getMessage();
			Integer statusCode = 402;//unauthotize access or no jwt
			if(errorMessage.contains("JWT expired")){
				errorMessage = "JWT expired";
				statusCode = 401;
			}
			else if(errorMessage.contains("No JWT found"))
			{
				errorMessage = "No JWT found";
				statusCode = 400;
			}

			System.out.println("------"+errorMessage);
			//response.get
			response.sendError(statusCode,errorMessage);
			//System.out.println(response.toString());
		}
	}

}
