package me.rocky.gateway.config;


//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
///**
// * @author Rocky
// * @version 1.0
// * @description
// * @email inaho00@foxmail.com
// * @createDate 2021/3/16 10:29
// * @log
// */
//@Configuration
//public class RedisConfig {
//	@Autowired
//	private RedisConnectionFactory connectionFactory;
//
//	@Bean
//	public RedisTemplate<String,Object> redisTemplate(){
//		RedisTemplate<String,Object> template = new RedisTemplate<>();
//		template.setConnectionFactory(connectionFactory);
//		//GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
//		// 序列化后会产生java类型说明，如果不需要用“Jackson2JsonRedisSerializer”和“ObjectMapper ”配合效果更好
//		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
//		ObjectMapper om = new ObjectMapper();
//		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//		jackson2JsonRedisSerializer.setObjectMapper(om);
//
//		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//		template.setKeySerializer(stringRedisSerializer);
//		template.setValueSerializer(jackson2JsonRedisSerializer);
//
//		template.setHashKeySerializer(jackson2JsonRedisSerializer);
//		template.setHashValueSerializer(jackson2JsonRedisSerializer);
//
//		template.afterPropertiesSet();
//		return template;
//	}
//
//}
