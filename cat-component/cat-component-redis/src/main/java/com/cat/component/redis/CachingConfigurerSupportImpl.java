package com.cat.component.redis;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * 默认通过接口委托
 */
@Component
@EnableCaching(proxyTargetClass = false, mode = AdviceMode.PROXY)
public class CachingConfigurerSupportImpl extends CachingConfigurerSupport {


    @Resource
    private DefaultRedisConfig defaultRedisConfig;


    /**
     * cacheConfigMap.put(CacheConstant.SYS_DICT_CACHE, config);
     * cacheConfigMap.put(CacheConstant.POLLUTION_DETECTION_STANDARDS, config);
     * cacheConfigMap.put(CacheConstant.DISASTER_TYPHOON_INFORMATION, config);
     *
     * @return
     */
    @Override
    public CacheManager cacheManager() {

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(defaultRedisConfig.genericSerializer()))
                .serializeKeysWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(StringRedisSerializer.UTF_8));

        return RedisCacheManager
                .builder(RedisCacheWriter.lockingRedisCacheWriter(defaultRedisConfig.connectionFactory()))
                .cacheDefaults(configuration)
                .transactionAware()
                .build();
    }


    @Override
    public CacheResolver cacheResolver() {
        return super.cacheResolver();
    }

    /**
     * @see {@link CacheEvict#key()}
     * 如果手动指定了不会执行此Key生成策略
     */
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> Joiner
                .on(":")
                .join(AopUtils.getTargetClass(target).getName(), method.getName(), params);
    }


    @Override
    public CacheErrorHandler errorHandler() {
        return new DefaultCacheErrorHandler();
    }


    @Slf4j
    private static class DefaultCacheErrorHandler extends SimpleCacheErrorHandler {
        @Override
        public void handleCacheGetError(RuntimeException e, Cache cache, Object o) {
            log.error(e.getMessage(), e);
        }

        @Override
        public void handleCachePutError(RuntimeException e, Cache cache, Object o, Object o1) {
            log.error(e.getMessage(), e);
            super.handleCachePutError(e, cache, o, o1);
        }

        @Override
        public void handleCacheEvictError(RuntimeException e, Cache cache, Object o) {
            log.error(e.getMessage(), e);
            super.handleCacheEvictError(e, cache, o);
        }

        @Override
        public void handleCacheClearError(RuntimeException e, Cache cache) {
            log.error(e.getMessage(), e);
            super.handleCacheClearError(e, cache);
        }
    }


}
