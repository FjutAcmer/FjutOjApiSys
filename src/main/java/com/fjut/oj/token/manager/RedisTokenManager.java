package com.fjut.oj.token.manager;

import com.fjut.oj.pojo.TokenModel;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @Author: wyx
 * @Despriction:
 * @Date:Created in 9:44 2019/7/5
 * @Modify By:
 */
@Component
public class RedisTokenManager implements TokenManager {

    @Resource
    private RedisTemplate redis;

    @Override
    public TokenModel createToken(String username) {
        String token = UUID.randomUUID().toString().replace("-", "");
        TokenModel model = new TokenModel(username, token);
        redis.boundValueOps(username).set(token);
        return model;
    }

    @Override
    public boolean checkToken(TokenModel model) {
        if (null == model) {
            return false;
        }
        String token = redis.boundValueOps(model.getUsername()).get().toString();
        if (null == token || !token.equals(model.getToken())) {
            return false;
        }
        // 如果鉴权成功，延长过期时间
        // redis.boundValueOps(model.getUsername()).expire();
        return true;
    }

    @Override
    public TokenModel getToken(String authentication) {
        if (authentication == null || authentication.length() == 0) {
            return null;
        }
        String[] param = authentication.split("_");
        if (2 != param.length) {
            return null;
        }
        //使用userId和源token简单拼接成的token，可以增加加密措施
        return new TokenModel(param[0], param[1]);
    }

    @Override
    public void deleteToken(String username) {
        redis.delete(username);
    }
}