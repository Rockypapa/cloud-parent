package me.rocky.gateway.security;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nimbusds.jose.JWSObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.PublicKey;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/3/19 15:40
 * @log
 */
@Component("authenticationManager")
public class AuthenticationManager implements ReactiveAuthenticationManager {

	@Autowired
	private PublicKey publicKey;

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		String token=authentication.getCredentials().toString();
		System.out.println("manager token: "+token);
		try {
			Claims claims = parseToken(token);
			//todo 此处应该列出token中携带的角色表。
			JWSObject jwsObject = null;
			try {
				jwsObject = JWSObject.parse(token);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String payload = jwsObject.getPayload().toString();
			JSONObject jsonObject = JSONUtil.parseObj(payload);

			List<String> roles = jsonObject.getJSONArray("authorities").toList(String.class);
			Authentication authentications=new UsernamePasswordAuthenticationToken(
					claims.getId(),
					null,
					roles.stream().map(role->new SimpleGrantedAuthority(role)).collect(Collectors.toList())
			);
			return Mono.just(authentications);
		} catch (Exception e) {
			throw  new BadCredentialsException(e.getMessage());
		}
	}

	private Claims parseToken(String token){
		Jwt<JwsHeader, Claims> parseClaimsJwt = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
		Claims claims = parseClaimsJwt.getBody();
		return claims;
	}
}
