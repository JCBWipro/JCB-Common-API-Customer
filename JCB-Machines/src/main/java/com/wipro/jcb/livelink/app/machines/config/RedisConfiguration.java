package com.wipro.jcb.livelink.app.machines.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@PropertySource("application.properties")
@EnableCaching
public class RedisConfiguration extends CachingConfigurerSupport {
	@Value("${redis.hostname}")
	private String redisHostName;
	@Value("${redis.port}")
	private int redisPort;
	@Value("${redis.expireInDays}")
	private String expireInDays;

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(300);
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(config);
		jedisConnectionFactory.setHostName(redisHostName);
		jedisConnectionFactory.setPort(redisPort);
		jedisConnectionFactory.setUsePool(true);
		return jedisConnectionFactory;
	}

	@Bean
	public RedisTemplate<Object, Object> redisTemplate() {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setExposeConnection(true);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		return redisTemplate;
	}

	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofHours(1)).disableCachingNullValues()
				.serializeKeysWith(
						RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair
						.fromSerializer(new GenericJackson2JsonRedisSerializer()))
				.prefixCacheNameWith("test");

		return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(redisCacheConfiguration)
				.transactionAware().build();
	}
}
