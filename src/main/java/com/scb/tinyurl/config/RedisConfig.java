package com.scb.tinyurl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scb.tinyurl.models.Url;

@Configuration
public class RedisConfig {


  @Autowired
  ObjectMapper mapper;

  @Autowired
  RedisConnectionFactory connectionFactory;

  @Bean
  RedisTemplate<String, Url> redisTemplate() {
    final RedisTemplate<String, Url> redisTemplate = new RedisTemplate<>();
    Jackson2JsonRedisSerializer valueSerializer = new Jackson2JsonRedisSerializer(Url.class);
    valueSerializer.setObjectMapper(mapper);
    redisTemplate.setConnectionFactory(connectionFactory);
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(valueSerializer);
    return redisTemplate;
  }
}