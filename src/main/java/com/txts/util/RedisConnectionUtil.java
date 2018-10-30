package com.txts.util;

import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RedisConnectionUtil {
	
	@Autowired
	private JedisPool jedisPool;//连接池
	/**
	 * 获取连接
	 * @time 2018年8月3日-下午2:02:11
	 * @author lfy
	 */
	public Jedis getResource() {
		return jedisPool.getResource();
	}
	
	/**
	 * 归还连接
	 * @time 2018年8月3日-下午2:02:21
	 * @author lfy
	 */
	public void returnResource(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}
	
	/**
	 * 无返的执行 自动获取redis连接与归还
	 * @time 2018年8月3日-下午2:02:29
	 * @author lfy
	 */
	public void execute(Consumer<Jedis> fun) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			fun.accept(jedis);
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * 有返的执行 自动获取redis连接与归还
	 * @time 2018年8月3日-下午2:02:51
	 * @author lfy
	 */
	public Object execute(Function<Jedis, Object> fun) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return fun.apply(jedis);
		} finally {
			returnResource(jedis);
		}
	}
	
}