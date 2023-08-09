package com.videoSite.config;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Spring Security RememberMe 的 Token 存储在 Redis 中的实现
 */
@Slf4j
@Configuration
@Api(tags = "Spring Security RememberMe Token 存储", description = "Spring Security RememberMe 的 Token 存储在 Redis 中的实现")
public class RedisTokenRepository implements PersistentTokenRepository {

    private final static String REMEMBER_ME_KEY = "spring:security:rememberMe:";

    @Resource
    RedisTemplate<Object, Object> tokenTemplate;

    @PostConstruct
    private void init() {
        log.info("存储 Spring-RememberMe 缓存的 Redis 配置加载");
    }

    @ApiOperation("获取 Token")
    private PersistentRememberMeToken getToken(String series) {
        Map<Object, Object> map = tokenTemplate.opsForHash().entries(series);
        if (map.isEmpty()) return null;
        Object date = map.get("date");
        if (date == null) return null;
        return new PersistentRememberMeToken(
                (String) map.get("username"),
                (String) map.get("series"),
                (String) map.get("tokenValue"),
                new Date(Long.parseLong((String) map.get("date")))
        );
    }

    @ApiOperation("设置 Token")
    private void setTokenTemplate(PersistentRememberMeToken token) {
        Map<Object, Object> map = new HashMap<>();
        map.put("username", token.getUsername());
        map.put("series", token.getSeries());
        map.put("tokenValue", token.getTokenValue());
        map.put("date", token.getDate().getTime());
        tokenTemplate.opsForHash().putAll(REMEMBER_ME_KEY + map.get("series"), map);
        tokenTemplate.expire(REMEMBER_ME_KEY + map.get("series"), 1, TimeUnit.DAYS);
    }

    @ApiOperation("创建新 Token")
    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        tokenTemplate.opsForValue().set(REMEMBER_ME_KEY + "name:" + token.getUsername(), token.getSeries());
        tokenTemplate.expire(REMEMBER_ME_KEY + "name:" + token.getUsername(), 1, TimeUnit.DAYS);
    }

    @ApiOperation("更新 Token")
    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        PersistentRememberMeToken token = getToken(series);
        if (token != null)
            this.setTokenTemplate(new PersistentRememberMeToken(token.getUsername(), series, tokenValue, lastUsed));
    }

    @ApiOperation("根据 Series 获取 Token")
    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        return getToken(seriesId);
    }

    @ApiOperation("移除用户 Token")
    @Override
    public void removeUserTokens(String username) {
        String series = (String) tokenTemplate.opsForValue().get(REMEMBER_ME_KEY + "name:" + username);
        tokenTemplate.delete(REMEMBER_ME_KEY + "name:" + username);
        tokenTemplate.delete(REMEMBER_ME_KEY + series);
    }
}
