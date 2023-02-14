package com.banana.visual.shiro.cache;

import com.banana.visual.constants.ShiroConstant;
import com.banana.visual.handler.SpringContextHolder;
import com.banana.visual.utils.JwtUtil;
import com.banana.visual.utils.RedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collection;
import java.util.Set;

/**
 * 重写Shiro的Cache保存读取
 */
public class CustomCache<K, V> implements Cache<K, V> {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * RefreshToken过期时间
     */
    @Value("${shiro.refreshTokenExpireTime}")
    private long refreshTokenExpireTime;

    /**
     * 缓存的key名称获取为shiro:cache:account
     *
     * @param key
     * @return java.lang.String
     */
    private String getKey(Object key) {
        return ShiroConstant.PREFIX_SHIRO_CACHE + JwtUtil.getClaim(key.toString(), ShiroConstant.UID);
    }

    /**
     * 获取缓存
     */
    @Override
    public Object get(Object key) throws CacheException {
        if (redisUtil == null) {
            redisUtil = SpringContextHolder.getBean(RedisUtil.class);
        }
        if (!redisUtil.hasKey(this.getKey(key))) {
            return null;
        }
        return redisUtil.get(this.getKey(key));
    }

    /**
     * 保存缓存
     */
    @Override
    public Object put(Object key, Object value) throws CacheException {
        if (redisUtil == null) {
            redisUtil = SpringContextHolder.getBean(RedisUtil.class);
        }
        // 设置Redis的Shiro缓存
        redisUtil.set(this.getKey(key), value, refreshTokenExpireTime);
        return 0;
    }

    /**
     * 移除缓存
     */
    @Override
    public Object remove(Object key) throws CacheException {
        if (redisUtil == null) {
            redisUtil = SpringContextHolder.getBean(RedisUtil.class);
        }
        if (!redisUtil.hasKey(this.getKey(key))) {
            return null;
        }
        redisUtil.del(this.getKey(key));
        return null;
    }

    /**
     * 清空所有缓存
     */
    @Override
    public void clear() throws CacheException {
        // Objects.requireNonNull(JedisUtil.getJedis()).flushDB();
    }

    /**
     * 缓存的个数
     */
    @Override
    public int size() {
        // Long size = Objects.requireNonNull(JedisUtil.getJedis()).dbSize();
        //return size.intValue();
        return 0;
    }

    /**
     * 获取所有的key
     */
    @Override
    public Set keys() {
//        Set<byte[]> keys = Objects.requireNonNull(JedisUtil.getJedis()).keys("*".getBytes());
//        Set<Object> set = new HashSet<Object>();
//        for (byte[] bs : keys) {
//            set.add(SerializableUtil.unserializable(bs));
//        }
//        return set;
        return null;
    }

    /**
     * 获取所有的value
     */
    @Override
    public Collection values() {
//        Set keys = this.keys();
//        List<Object> values = new ArrayList<Object>();
//        for (Object key : keys) {
//            values.add(JedisUtil.getObject(this.getKey(key)));
//        }
//        return values;
        return null;
    }
}
