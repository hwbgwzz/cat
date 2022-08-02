package com.cat.component.redis;


import com.cat.common.toolkit.json.JSON;
import com.cat.component.redis.util.RedisTemplates;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

@Configuration
@ComponentScan("com.cat.component.redis")
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class DefaultRedisConfig {

    @Resource
    private RedisProperties properties;


    /**
     * RedisTemplateUtil With Construct
     * {@link  RedisTemplate}
     */
    @Bean
    public RedisTemplates redisUtils() {
        return new RedisTemplates(redisTemplate());
    }


    /**
     * @see JedisConnectionFactory#setPoolConfig(JedisPoolConfig)
     * @see org.springframework.data.redis.connection.RedisStandaloneConfiguration
     * @see org.springframework.data.redis.connection.RedisSentinelConfiguration
     * @see org.springframework.data.redis.connection.RedisClusterConfiguration
     */
    @Bean
    public JedisConnectionFactory connectionFactory() {

        RedisClusterConfiguration configuration = new RedisClusterConfiguration(properties.getCluster().getNodes());
        configuration.setPassword(properties.getPassword());

        return new JedisConnectionFactory(configuration,
                JedisClientConfiguration.builder()
                        .connectTimeout(properties.getTimeout())
                        .usePooling()
                        .poolConfig(poolConfiguration())
                        .build());
    }


    @Bean
    public JedisPoolConfig poolConfiguration() {
        JedisPoolConfig config = new JedisPoolConfig();
        RedisProperties.Pool pool = properties.getJedis().getPool();
        config.setMaxIdle(pool.getMaxIdle());
        config.setMinIdle(pool.getMinIdle());
        config.setMaxWaitMillis(pool.getMaxWait().toMillis());
        config.setMaxTotal(pool.getMaxActive());
        config.setTestOnCreate(false);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        config.setTestWhileIdle(false);
        return config;
    }


    /**
     * @see Jackson2JsonRedisSerializer
     * @see JSON#getInstance()
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory());
        redisTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
        redisTemplate.setValueSerializer(genericSerializer());
        redisTemplate.setHashKeySerializer(StringRedisSerializer.UTF_8);
        redisTemplate.setHashValueSerializer(genericSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    /**
     * @return 带有类型的redis序列化
     * @see {@link  JsonTypeInfo.As}
     * <p>
     * @see {@link ObjectMapper#enableDefaultTyping()}  }
     * @see {@link ObjectMapper#activateDefaultTyping(PolymorphicTypeValidator, ObjectMapper.DefaultTyping, JsonTypeInfo.As)}
     */
    @Bean
    public GenericJackson2JsonRedisSerializer genericSerializer() {
        return new GenericJackson2JsonRedisSerializer(JSON.copy()
                .activateDefaultTyping(LaissezFaireSubTypeValidator.instance
                        , ObjectMapper.DefaultTyping.NON_FINAL
                        , JsonTypeInfo.As.PROPERTY));
    }

}
