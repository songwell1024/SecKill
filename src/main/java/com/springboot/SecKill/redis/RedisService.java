package com.springboot.SecKill.redis;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;



/**
 * @author WilsonSong
 * @date 2018/8/1
 */
@Service
public class RedisService {
    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    JedisPool jedisPool;


    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz){
        Jedis jedis = null;
        try{
          jedis = jedisPool.getResource();
          //生成real  key
          String realKey = prefix.getPrefix() + key;
          String str = jedis.get(realKey);
          T t = String2Bean(str, clazz);
          return t;
        }catch (Exception e){
            logger.error("redis连接池异常get"+e.getMessage());
            return null;
        }finally {
            if (jedis != null){
               jedis.close();
            }
        }
    }

    public <T> boolean set(KeyPrefix prefix,String key, T value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String value_new = Bean2String(value);
            if (value_new == null || value_new.length() <0){
                return false;
            }

            //生成real  key
            String realKey = prefix.getPrefix() + key;
            //过期时间
            int seconds = prefix.expireSeconds();
            if (seconds <= 0){
                jedis.set(realKey, value_new);
            }else {
                jedis.setex(realKey,seconds,value_new);
            }

            return true;
        }catch (Exception e){
            logger.error("redis连接池异常set"+e.getMessage());
            return false;
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    public void setValue(String key, String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
                jedis.set(key, value);
        }catch (Exception e){
            logger.error("redis连接池异常set"+e.getMessage());
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    public String getValue(String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.get(key);
        }catch (Exception e){
            logger.error("redis连接池异常set"+e.getMessage());
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    //key 是否存在
    public <T> Boolean exists(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            //生成real  key
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);

        }catch (Exception e){
            logger.error("redis连接池异常"+e.getMessage());
            return null;
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    //增加key对应的值
    public <T> Long incr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            //生成real  key
            String realKey = prefix.getPrefix() + key;
            return jedis.incr(realKey);
        }catch (Exception e){
            logger.error("redis连接池异常"+e.getMessage());
            return null;
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    //减少key对应的对象的值
    public <T> Long decr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            //生成real  key
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        }catch (Exception e){
            logger.error("redis连接池异常"+e.getMessage());
            return null;
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    //删除缓存中的值
    public boolean delete(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            //生成real  key
            String realKey = prefix.getPrefix() + key;
            long ret = jedis.del(realKey);
            return ret >0;
        }catch (Exception e){
            logger.error("redis连接池异常"+e.getMessage());
            return false;
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    //bean对象准换为String
    public static  <T> String Bean2String(T value) {
        if (value == null){
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class){
            return  ""+value;
        }else if (clazz == String.class){
            return (String)value;
        }else if (clazz == long.class || clazz == Long.class){
            return  ""+value;
        }else {
            return JSON.toJSONString(value);
        }

    }

    //String转换为bean
    public static  <T> T String2Bean(String str, Class<T> clazz) {
        if (str == null || str.length() <0 || clazz == null){
            return null;
        }

        if (clazz == int.class || clazz == Integer.class){
            return  (T)Integer.valueOf(str);
        }else if (clazz == String.class){
            return (T)str;
        }else if (clazz == long.class || clazz == Long.class){
            return (T)Long.valueOf(str);
        }else {
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
        }
    }


}
