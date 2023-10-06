package com.snow.common.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName CacheMap
 * @Description TODO
 * @Author Karl
 * @Date 2022/6/22 11:04
 * @Version 1.0
 */
public class CacheMap<K, V> extends ConcurrentHashMap<K, V> {

    private static final long serialVersionUID = 3666697890499256321L;

    /**
     * 保存key和对应的过期时间
     */
    private Map<K,Long> expireMap = new HashMap<K, Long>();

    /**
     * 默认过期时间
     */
    private long EXPIRE_TIME = 1000;

    public CacheMap(){
        super();
    }

    public CacheMap(long expire){
        this.EXPIRE_TIME = expire;
    }

    public CacheMap(TimeUnit unit, long duration){
        this.EXPIRE_TIME = unit.toMillis(duration);
    }

    @Override
    public V put(K key, V value) {
        if(key == null){
            return null;
        }
        expireMap.put(key, System.currentTimeMillis() + EXPIRE_TIME);
        return super.put(key, value);
    }

    public V put(K key, V value,long expireTime) {
        if(key == null){
            return null;
        }
        expireMap.put(key, System.currentTimeMillis() + expireTime);
        return super.put(key, value);
    }

    @Override
    public V get(Object key) {
        if(key == null){
            return null;
        }
        return super.get(key);
    }

    @Override
    public V remove(Object key) {
        if(key == null){
            return null;
        }
        expireMap.remove(key);
        return super.remove(key);
    }

    /**
     * 是否过期
     * @param key
     * @return
     */
    public boolean isExpired(K key){
        if(key == null || !containsKey(key)){
            return false;
        }else {
            long time1 = expireMap.get(key);
            long time2 = System.currentTimeMillis();
            return (time2 - time1) > 0;
        }
    }

    /**
     * 移除所有过期键值对
     * @return
     */
    public void removeALLExpired(){
        for(K key : this.keySet()){
            if(isExpired(key)){
                remove(key);
            }
        }
    }


}
