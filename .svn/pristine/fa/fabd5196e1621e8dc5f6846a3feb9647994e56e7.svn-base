package com.txts.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Pipeline;

@Service
public class RedisUtil {

	@Autowired
	private RedisConnectionUtil connUtil;
	/**
	 * 存
	 * @time 2018年8月3日-下午4:04:07
	 * @author lfy
	 */
	public void set(String key, String value) {
		connUtil.execute(jedis -> {
			jedis.set(key, value);
		});
	}
	/**
	 * 取
	 * @time 2018年8月3日-下午4:04:18
	 * @author lfy
	 */
	public Object get(String key) {
		return connUtil.execute(jedis -> {
			//取
			Object o = jedis.get(key);
			return o;
		});
	}
	/**
	 * 集合（list）的存
	 * @time 2018年8月3日-下午4:04:24
	 * @author lfy
	 */
	public void lpush(String key, String value) {
		connUtil.execute(jedis -> {
			//集合新增
			jedis.lpush(key, value);
		});
	}
	/**
	 * 集合（list）的存
	 * @time 2018年8月3日-下午4:04:39
	 * @author lfy
	 */
	public void lpush(String key, byte[] value) {
		connUtil.execute(jedis -> {
			//集合新增
			jedis.lpush(key.getBytes(), value);
		});
	}
	/**
	 * 获取全部集合数据（list类型）并清空
	 * @time 2018年8月3日-下午4:04:47
	 * @author lfy
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<String> getListDataAndClear(String key) {
		return (List) connUtil.execute(jedis -> {
			Pipeline pl = null;
			List<Object> list = null;
			try {
				// 获得通道
				pl = jedis.pipelined();
				// 先取全部
				pl.lrange(key, 0, -1);
				// 后删全部
				pl.ltrim(key, 1, 0);
				// 返回值集合的第一个对象为全部数据 直接将该数据返回
				list = (List) pl.syncAndReturnAll().get(0);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					// 关闭通道
					if (pl != null)
						pl.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			return list;

		});
	}
}
