package com.web;

import com.db.ReidsDb;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class Cookie {

	// 尝试获取并返回令牌对应的用户
	public void check_token(String token) {
		try (Jedis jedis = ReidsDb.DB().getJedis();) {
			jedis.hget("login:", token);
		}
	}

	public void update_token(String token, String user, String item) {
		long timestamp = System.currentTimeMillis();
		try (Jedis jedis = ReidsDb.DB().getJedis();) {
			jedis.hset("login:", token, user);// 登录用户与令牌之间的映射
			jedis.zadd("recent:", timestamp, token);// 记录令牌最后一次出现的时间
			if (StringUtils.isNotBlank(item)) {
				jedis.zadd("viewed:" + token, timestamp, item);// 记录用户浏览过的商品
				jedis.zremrangeByRank("viewed:" + token, 0, -26);// 移除旧的记录，只保留用户最近浏览过的25个商品
			}
		}
	}

	private boolean quit = false;
	private long limit = 10000000;

	// 每天500万用户访问
	// 一天有24*3600=86400秒
	// 网站平均每秒产生5000000/86400<58个新会话
	public void clean_session() {
		while (!quit) {
			try (Jedis jedis = ReidsDb.DB().getJedis();) {
				Long size = jedis.zcard("recent:");// 找出目前已有的令牌数量
				if (size <= limit) {// 令牌数量未超过限制，休眠并在之后重新检查
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}

				long end_index = Math.min(size - limit, 100L);// 获取需要移除的令牌ID
				Set<String> tokens = jedis.zrange("recent:", 0, end_index - 1);
				String[] array = tokens.stream().map(x -> "viewed:" + x).toArray(String[]::new);
				String[] keys = tokens.toArray(new String[0]);
				// 移除最旧的哪些令牌
				jedis.del(array);
				jedis.hdel("login:", keys);
				jedis.zrem("recent:", keys);
			}
		}
	}

	public void add_to_cart(String session, String item, int count) {
		try (Jedis jedis = ReidsDb.DB().getJedis();) {
			if (count <= 0) {
				jedis.hdel("cart:"+session, item);//从购物车里面移除指定的商品
			}else{
				jedis.hset("cart:"+session, item, String.valueOf(count));//将指定的商品添加到购物车
			}
		}
	}
	
	public void clean_full_session() {
		while (!quit) {
			try (Jedis jedis = ReidsDb.DB().getJedis();) {
				Long size = jedis.zcard("recent:");// 找出目前已有的令牌数量
				if (size <= limit) {// 令牌数量未超过限制，休眠并在之后重新检查
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}

				long end_index = Math.min(size - limit, 100L);// 获取需要移除的令牌ID
				Set<String> sessions = jedis.zrange("recent:", 0, end_index - 1);
				String [] elements = new String[]{"viewed:","cart:"};
				ArrayList<String> newArrayList = new ArrayList<String>(2);
				Collections.addAll(newArrayList, elements);
				String[] array = sessions.stream()
						.flatMap(x -> newArrayList
								.stream()
								.map(l->l+x))
						.toArray(String[]::new);
				String[] keys = sessions.toArray(new String[0]);
				// 移除最旧的哪些令牌
				jedis.del(array);
				jedis.hdel("login:", keys);
				jedis.zrem("recent:", keys);
			}
		}
	}
}