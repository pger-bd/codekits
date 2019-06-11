package org.codekits.redis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis 配置类
* @ClassName: RedisConfig 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (作者)  
* @date 2019年6月11日 上午10:42:03 
* @version V1.0.0
 */
@Configuration
@EnableCaching  //开启缓存,还要继承于CachingConfigurerSupport，主要是为了注解@Cacheable、@CacheEvict、@CachePut等的使用
public class RedisConfig extends CachingConfigurerSupport{

	@Autowired
	RedisConnectionFactory redisConnectionFactory;
	
	@Bean
	public RedisTemplate<String, Object> functionDomainRedisTemplate(){
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		initDomainRedisTemplate(redisTemplate, redisConnectionFactory);
		return redisTemplate;
	}
	
	private void initDomainRedisTemplate(RedisTemplate<String, Object> redisTemplate, RedisConnectionFactory factory) {
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		//只有设置jdk序列化，才能对类对象比如User进行存储
		redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setConnectionFactory(factory);
	}
	
	@Bean
	public RedisUtil redisUtil(RedisTemplate<String, Object> redisTemplate) {
		RedisUtil redisUtil = new RedisUtil();
		redisUtil.setRedisTemplate(redisTemplate);
		return redisUtil;
	}
	
	
}
