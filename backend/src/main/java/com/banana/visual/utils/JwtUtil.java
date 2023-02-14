package com.banana.visual.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.banana.visual.constants.ShiroConstant;
import com.banana.visual.enums.ResultEnum;
import com.banana.visual.exception.SelfDefineException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    /**
     * 过期时间改为从配置文件获取
     */
    private static String accessTokenExpireTime;

    /**
     * JWT认证加密私钥(Base64加密)
     */
    private static String encryptJWTKey;

    @Value("${shiro.accessTokenExpireTime}")
    public void setAccessTokenExpireTime(String accessTokenExpireTime) {
        JwtUtil.accessTokenExpireTime = accessTokenExpireTime;
    }

    @Value("${shiro.encryptJWTKey}")
    public void setEncryptJWTKey(String encryptJWTKey) {
        JwtUtil.encryptJWTKey = encryptJWTKey;
    }

    /**
     * 校验token是否正确
     *
     * @param token Token
     * @return
     */
    public static boolean verify(String token) {
        try {
            // 帐号加JWT私钥解密
            String secret = getClaim(token, ShiroConstant.UID) + Base64ConvertUtil.decode(encryptJWTKey);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(token);
        } catch (UnsupportedEncodingException e) {
            log.error("JWTToken认证解密UnsupportedEncodingException异常:" + e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("JWTToken认证解密异常:" + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 获得Token中的信息无需secret解密也能获得
     *
     * @param token
     * @param claim
     * @return java.lang.String
     */
    public static String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            log.error("解密Token中的公共信息出现JWTDecodeException异常:" + e.getMessage());
            throw new SelfDefineException(ResultEnum.JWTTOEN_JWTDECODE);
        }
    }

    /**
     * 生成签名
     *
     * @param uid               帐号
     * @param permission        权限描述
     * @param currentTimeMillis 获取当前最新时间戳
     * @param serviceType       服务渠道
     * @return 返回加密的Token
     */
    public static String sign(String uid, String permission, String currentTimeMillis, String serviceType) {
        try {
            // 帐号加JWT私钥加密
            String secret = uid + Base64ConvertUtil.decode(encryptJWTKey);
            // 此处过期时间是以毫秒为单位，所以乘以1000
            Date date = new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpireTime) * 1000);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带account帐号信息
            return JWT.create()
                    .withClaim(ShiroConstant.UID, uid)
                    .withClaim(ShiroConstant.PERM, permission)
                    .withClaim(ShiroConstant.CURRENT_TIME_MILLIS, currentTimeMillis)
                    .withClaim("serviceType", serviceType)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            log.error("JWTToken加密出现UnsupportedEncodingException异常:" + e.getMessage());
            throw new SelfDefineException(ResultEnum.JWTTOEN_UNSUPPORTEDENCODING);
        }
    }
}
