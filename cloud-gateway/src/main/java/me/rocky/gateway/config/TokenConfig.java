package me.rocky.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;


/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/3/19 14:59
 * @log
 */
@Configuration
public class TokenConfig {

	@Bean
	public PublicKey publicKey(){
		KeyStoreKeyFactory factory = new KeyStoreKeyFactory(
				new ClassPathResource("youlai.jks"), "123456".toCharArray());
		KeyPair keyPair = factory.getKeyPair(
				"youlai", "123456".toCharArray());
		return (RSAPublicKey) keyPair.getPublic();
	}

}
