package org.lychee.cache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

/** escache类 */
public class EHCacheService {

	private static Cache<String, Object> cache;
	CacheManager cacheManager;

	public EHCacheService() {
		cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
				.withCache("preConfigured",
						CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
								ResourcePoolsBuilder.heap(100))
								.build())
				.build(true);

		cache = cacheManager.createCache("captchaCache",
				CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Object.class,
						ResourcePoolsBuilder.heap(100)).build());

	}

	/** 添加缓存 */
	public void addCache(String key, Object value) {
		cache.put(key, value);
	}

	/**
	 * 获取缓存
	 * 
	 * @return
	 */
	public Object getCache(String key) {
		return cache.get(key);
	}

	/**
	 * 删除缓存
	 * 
	 * @return
	 */
	public void delCache(String key) {
		cache.remove(key);
	}
}
