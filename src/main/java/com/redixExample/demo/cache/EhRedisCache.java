package com.redixExample.demo.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import net.sf.ehcache.Element;

public class EhRedisCache implements Cache {
	private static final Logger LOG = LoggerFactory.getLogger(EhRedisCache.class);
	@Autowired
	private net.sf.ehcache.CacheManager cacheManager;

	private String name;
	@Autowired
	private net.sf.ehcache.Cache ehCache;

	private RedisTemplate<String, Object> redisTemplate;

	private long liveTime = 1 * 60 * 60; // 默認1小時=1*60*60
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Object getNativeCache() {
		return this;
	}

	@Override
	public ValueWrapper get(Object key) {
		Element value = ehCache.get(key);
		LOG.info("Cache L1 (ehcache) :{}={}", key, value);
		if (value != null) {
			return (value != null ? new SimpleValueWrapper(value.getObjectValue()) : null);
		}
		// TODO 訪問10次EhCache 強制訪問一次redis 使得數據不失效
		final String keyStr = key.toString();
		Object objectValue = redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] key = keyStr.getBytes();
				byte[] value = connection.get(key);
				if (value == null) {
					return null;
				}

				if (liveTime > 0) {
					connection.expire(key, liveTime);
				}
				return toObject(value);
			}
		}, true);
		// 取出來後緩存到本地
		ehCache.put(new Element(key, objectValue));
		LOG.info("Cache L2 (redis) :{}={}", key, objectValue);
		return (objectValue != null ? new SimpleValueWrapper(objectValue) : null);

	}

	@Override
	public void put(Object key, Object value) {
		ehCache.put(new Element(key, value));
		final String keyStr = key.toString();
		final Object valueStr = value;
		redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] keyb = keyStr.getBytes();
				byte[] valueb = toByteArray(valueStr);
				connection.set(keyb, valueb);
				if (liveTime > 0) {
					connection.expire(keyb, liveTime);
				}
				return 1L;
			}
		}, true);

	}

	@Override
	public void evict(Object key) {
		ehCache.remove(key);
		final String keyStr = key.toString();
		redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.del(keyStr.getBytes());
			}
		}, true);
	}

	@Override
	public void clear() {
		ehCache.removeAll();
		redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return "clear done.";
			}
		}, true);
	}

	public net.sf.ehcache.Cache getEhCache() {
		return ehCache;
	}

	public void setEhCache(net.sf.ehcache.Cache ehCache) {
		this.ehCache = ehCache;
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public long getLiveTime() {
		return liveTime;
	}

	public void setLiveTime(long liveTime) {
		this.liveTime = liveTime;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 描述 : Object轉byte[]. <br>
	 * 
	 * @param obj
	 * @return
	 */
	private byte[] toByteArray(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
			oos.close();
			bos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return bytes;
	}

	/**
	 * 描述 : byte[]轉Object . <br>
	 * 
	 * @param bytes
	 * @return
	 */
	private Object toObject(byte[] bytes) {
		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();
			ois.close();
			bis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return obj;
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}
}
