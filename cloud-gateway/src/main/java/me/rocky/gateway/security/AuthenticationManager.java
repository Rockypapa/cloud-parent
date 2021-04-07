package me.rocky.gateway.security;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nimbusds.jose.JWSObject;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import me.rocky.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.security.PublicKey;
import java.text.ParseException;
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
@Primary
@Component("authenticationManager")
public class AuthenticationManager implements ReactiveAuthenticationManager {

	@Autowired
	private PublicKey publicKey;

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		String token=authentication.getCredentials().toString();
		try {
			Claims claims = parseToken(token);
			//todo 此处应该列出token中携带的角色表。
			JWSObject jwsObject = null;
			try {
				jwsObject = JWSObject.parse(token);
			} catch (ParseException e) {
				return Mono.error(new BusinessException("token异常"));
			}
			String payload = jwsObject.getPayload().toString();
			JSONObject jsonObject = JSONUtil.parseObj(payload);

			List<String> roles = jsonObject.getJSONArray("authorities").toList(String.class);
			System.out.println(roles.toString());
			Authentication authentications=new UsernamePasswordAuthenticationToken(
					claims.getId(),
					null,
					roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
			);
			return Mono.just(authentications);
		}catch (ExpiredJwtException e){
			return Mono.error(new BusinessException("token失效"));
		}catch(UnsupportedJwtException | MalformedJwtException e){
			return Mono.error(new BusinessException("token格式不正确"));
		}catch(SignatureException e){
			return Mono.error(new BusinessException("签名异常"));
		}catch (IllegalArgumentException e){
			return Mono.error(new BusinessException("token异常"));
		}
	}
	private Claims parseToken(String token){
		Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
		return claimsJws.getBody();
	}
}
