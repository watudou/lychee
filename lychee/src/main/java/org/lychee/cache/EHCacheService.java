package org.lychee.cache;

import org.lychee.framework.SpringContextHolder;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;

/** escache类 */
public class EHCacheService {

	private static EhCacheCacheManager cacheManager;

	/** 添加缓存 */
	public static void put(String cacheName, String key, Object value) {
		if (null == cacheManager) {
			cacheManager = SpringContextHolder.getBean(EhCacheCacheManager.class);
		}
		Cache cache = cacheManager.getCache(cacheName);
		cache.put(key, value);
	}

	/**
	 * 获取缓存
	 * 
	 * @return
	 */
	public static Object get(String cacheName, String key) {
		if (null == cacheManager) {
			cacheManager = SpringContextHolder.getBean(EhCacheCacheManager.class);
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (null == cache)
			return null;
		return cache.get(key);
	}

	/**
	 * 删除缓存
	 * 
	 * @return
	 */
	public static void evict(String cacheName, String key) {
		Cache cache = cacheManager.getCache(cacheName);
		cache.evict(key);
	}
}
