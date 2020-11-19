package com.main.glory.utils;

import com.main.glory.model.user.UserData;
import com.main.glory.servicesImpl.UserServiceImpl;
import com.main.glory.servicesImpl.ciphers.CipherModule;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

	private String SECRET_KEY = "kYES5dasvh732gcysdhg76gsdsadjgfkjasdfvsdhvctagfvawevtywae74664gfehwjvcajgf4sadfg";

	@Autowired
	private CipherModule cipher;

	@Autowired
	private UserServiceImpl userService;

	public String extractUsername(String token) throws Exception{
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) throws Exception {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws Exception{
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	public Claims extractAllClaims(String token) throws Exception{
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) throws Exception {
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(UserData user, String tokenType) {
		Map<String, Object> claims = new HashMap<>();
		long timeStamp = System.currentTimeMillis();
		long exp = 0;
		if(tokenType.equals("accessToken")){
			exp = timeStamp + 1000 * 60 * 60;
			claims.put("permissions", user.getUserPermissionData());
			claims.put("designation", user.getDesignationId().getDesignation());
			claims.put("userHeadId", user.getUserHeadId());
		} else if(tokenType.equals("refreshToken")) {
			exp = timeStamp + 1000 * 60 * 60 * 60;
			claims.put("passcode", CipherModule.encrypt(user.getPassword()));
		}
		claims.put("tokenType", tokenType);
		claims.put("ts",timeStamp);
		claims.put("secret", CipherModule.encrypt(user.getId().toString()+ timeStamp));

		return createToken(claims, user.getId().toString(), timeStamp, exp);
	}

	private String createToken(Map<String, Object> claims, String subject, long timeStamp, long exp) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(timeStamp))
				.setExpiration(new Date(exp))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public Boolean validateToken(String token, UserData user, long timeStamp) {
		try{
			final String id = extractUsername(token);
			final Map<String, Object> claims = extractAllClaims(token);
			final long ts = ((Long) claims.get("ts"));
			if(!CipherModule.encrypt(id+ts).equals(claims.get("secret"))){
				throw new BadCredentialsException("Token is tampered");
			}

			UserData u = userService.getUserById(Long.parseLong(id));

			if(u == null) {
				throw new BadCredentialsException("Invalid Token No Such User");
			}
			return (!isTokenExpired(token));
		} catch(Exception e){
            e.printStackTrace(System.out);
			return false;
		}
	}

	public String generateRefreshToken(String id){
		final String data = id + "|-.-|" + (new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)).getTime();
		System.out.println("generated refresh token:"+data);
		return cipher.encrypt(data);
	}

	public String[] decryptRefreshToken(String refresh_token){
		final String rawString = cipher.decrypt(refresh_token);
		System.out.println(rawString);
		return rawString.split("\\|-.-\\|");
	}

}