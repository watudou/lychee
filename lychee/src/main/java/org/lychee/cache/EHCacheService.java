package org.lychee.cache;

import org.lychee.framework.SpringContextHolder;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;

/** escache类 */
public class EHCacheService {

	private EhCacheCacheManager cacheManager;

	public EHCacheService() {
		this.cacheManager = SpringContextHolder.getBean(EhCacheCacheManager.class);
	}

	/** 添加缓存 */
	public void put(String cacheName, String key, Object value) {
		Cache cache = cacheManager.getCache(cacheName);
		cache.put(key, value);
	}

	/**
	 * 获取缓存
	 * 
	 * @return
	 */
	public Object get(String cacheName, String key) {
		Cache cache = cacheManager.getCache(cacheName);
		return cache.get(key);
	}

	/**
	 * 删除缓存
	 * 
	 * @return
	 */
	public void evict(String cacheName, String key) {
		Cache cache = cacheManager.getCache(cacheName);
		cache.evict(key);
	}
}
