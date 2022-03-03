//package com.example.demo.redis;
//
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.MapPropertySource;
//import org.springframework.data.redis.connection.RedisClusterConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.stereotype.Component;
//import redis.clients.jedis.JedisPoolConfig;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@EnableAutoConfiguration
//@Component
//@ConfigurationProperties(prefix = "spring.redis.cluster")
//@Configuration
//public class RedisConfig {
//
//	private Logger logger = LoggerFactory.getLogger(getClass());
//
//	@Bean
//	@ConfigurationProperties(prefix = "spring.redis.pool")
//	public JedisPoolConfig getRedisConfig() {
//		JedisPoolConfig config = new JedisPoolConfig();
//		config.setBlockWhenExhausted(true);
//		config.setTestOnReturn(true);
//		config.setTestOnBorrow(true);
//		return config;
//	}
//
//	@Bean
//	@ConfigurationProperties(prefix = "spring.redis")
//	public JedisConnectionFactory getConnectionFactory() {
//		if (this.getNodes() == null) {
//			return getSingleFactory();
//		} else {
//			return getClusterFactory();
//		}
//
//	}
//
//	private JedisConnectionFactory getSingleFactory() {
//		JedisConnectionFactory factory = new JedisConnectionFactory();
//		JedisPoolConfig config = getRedisConfig();
//		logger.info("### redis connection config ###");
//		logger.info("config.getMaxTotal():{}", config.getMaxTotal());
//		logger.info("config.getMaxIdle():{}", config.getMaxIdle());
//		logger.info("config.getMinIdle():{}", config.getMinIdle());
//		logger.info("config.getMaxWaitMillis():{}", config.getMaxWaitMillis());
//
//		config.setTestOnReturn(true);
//		config.setTestOnBorrow(true);
//		factory.setPoolConfig(config);
//		logger.info("JedisConnectionFactory bean init success.");
//		return factory;
//	}
//
//	private JedisConnectionFactory getClusterFactory() {
//		Map<String, Object> source = new HashMap<String, Object>();
//		source.put("spring.redis.cluster.nodes", this.getNodes());
//		source.put("spring.redis.cluster.timeout", this.getTimeOut());
//		source.put("spring.redis.cluster.max-redirects", this.getMaxRedirects());
//		JedisPoolConfig config = getRedisConfig();
//		config.setTestOnReturn(true);
//		config.setTestOnBorrow(true);
//
//		logger.info("### redis connection config ###");
//		logger.info("config.getMaxTotal():{}", config.getMaxTotal());
//		logger.info("config.getMaxIdle():{}", config.getMaxIdle());
//		logger.info("config.getMinIdle():{}", config.getMinIdle());
//		logger.info("config.getMaxWaitMillis():{}", config.getMaxWaitMillis());
//
//		JedisConnectionFactory factory = new JedisConnectionFactory(
//				new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source)), config);
//		logger.info("cluster JedisConnectionFactory bean init success.");
//		return factory;
//	}
//
//	@Bean(name = "monitorRedisTemplate")
//	public RedisTemplate<?, ?> getRedisTemplate() {
//		RedisTemplate<?, ?> template = new RedisTemplate<>();
//		template.setConnectionFactory(getConnectionFactory());
////		template.setEnableTransactionSupport(true);
//
//		StringRedisSerializer StringRedisSerializer = new StringRedisSerializer();
//		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
//				Object.class);
//		ObjectMapper om = new ObjectMapper();
//		om.setVisibility(PropertyAccessor.ALL, com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY);
//		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//		jackson2JsonRedisSerializer.setObjectMapper(om);
//
//		template.setKeySerializer(StringRedisSerializer);
//		template.setValueSerializer(jackson2JsonRedisSerializer);
//		template.setHashKeySerializer(StringRedisSerializer);
//		template.setHashValueSerializer(jackson2JsonRedisSerializer);
//
////		template.afterPropertiesSet();
//		return template;
//	}
//
//	private String nodes;
//	private Integer maxRedirects;
//	private Integer timeOut;
//
//	@Value("${spring.redis.password}")
//	private String password;
//
//	public Integer getTimeOut() {
//		return timeOut;
//	}
//
//	public void setTimeOut(Integer timeOut) {
//		this.timeOut = timeOut;
//	}
//
//	public String getNodes() {
//		return nodes;
//	}
//
//	public void setNodes(String nodes) {
//		this.nodes = nodes;
//	}
//
//	public Integer getMaxRedirects() {
//		return maxRedirects;
//	}
//
//	public void setMaxRedirects(Integer maxRedirects) {
//		this.maxRedirects = maxRedirects;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//}