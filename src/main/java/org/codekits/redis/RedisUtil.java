package org.codekits.redis;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

/**
 * redis 工具类
 * 
 * @version:
 * @Description:
 * @author: 14307
 * @date: 2019年6月11日 上午10:25:49
 */
public class RedisUtil {

	private RedisTemplate<String, Object> redisTemplate;

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
	public boolean expire(String key, long time) {
		try {
			if (time > 0) {
				this.redisTemplate.expire(key, time, TimeUnit.MICROSECONDS);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
	public long getExpire(String key) {
		return redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

	/**
	 * 判断key是否存在
	 * @param key 键
	 * @return true 存在 false不存在
	 */
	public boolean hasKey(String key) {
		try {
			return redisTemplate.hasKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
	public void del(String... key) {
		if(key != null && key.length > 0) {
			if(key.length == 1) {
				redisTemplate.delete(key[0]);
			}else {
				redisTemplate.delete(CollectionUtils.arrayToList(key));
			}
		}
	}
    
    /**
     * 获取缓存
     * @param key 
     * @return Object
     */
    public Object get(String key) {
    	return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 存入缓存
     * @param key 
     * @param value
     * @return Object
     */
	public boolean set(String key, Object value) {
		try {
			redisTemplate.opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
	public boolean set(String key, Object value, long time) {
		try {
			if(time > 0) {
				redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
			}else {
				set(key, value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
    /**
     * 递减
     * @param key 键
     * @param delta 要减少几(小于0)
     * @return
     */
	public long decr(String key, long delta) {
		if(delta < 0) {
			throw new RuntimeException("递减因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key, -delta);
	}
	
    /**
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
	public Object getHashValueByHashKey(String key, String item) {
		return redisTemplate.opsForHash().get(key, item);
	}
	
    /**
     * 获取hashKey对应的所有值
     * @param key 键
     * @return 对应的多个值
     */
	public Map<Object,Object> getHashValuesByKey(String key){
		return redisTemplate.opsForHash().entries(key);
	}
	
    /**
     * 存入HashMap
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
	public boolean setHashValue(String key, Map<String,Object> map) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
    /**
     * HashSet 并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
	public boolean setHashValueAndExpireTime(String key,Map<String,Object> map,long time) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
			if(time>0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true 成功 false失败
     */
	public boolean setHashValueIfNotExit(String key,String item,Object value) {
		try {
			redisTemplate.opsForHash().put(key,item, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
    /**
     * 删除hash表中的值
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     */
	public void delHashValue(String key,String item) {
		redisTemplate.opsForHash().delete(key, item);
	}
	
    /**
     * 判断hash表中是否有该项的值
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
	public boolean isExitHashValue(String key,String item) {
		return redisTemplate.opsForHash().hasKey(key, item);
	}
	
    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key 键
     * @param item 项
     * @param by 要增加几(大于0)
     * @return
     */
	public double getHashIncrementVal(String key,String item, double by) {
		return redisTemplate.opsForHash().increment(key, item, by);
	}
	
    /**
     * hash递减
     * @param key 键
     * @param item 项
     * @param by 要减少记(小于0)
     * @return
     */
	public double getHashDecrementVal(String key,String item, double by) {
		return redisTemplate.opsForHash().increment(key, item, -by);
	}
	
    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return
     */
	public Set<Object> getSetValues(String key){
		try {
			return redisTemplate.opsForSet().members(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
	public boolean isExitSetValue(String key, Object value) {
		try {
			return redisTemplate.opsForSet().isMember(key,value);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	 /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
	public long putSetValue(String key, Object...values) {
		try {
			return redisTemplate.opsForSet().add(key, values);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
    /**
     * 将set数据放入缓存,并设置时长
     * @param key 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
	public long putSetValueAndExpireTime(String key, long time, Object...values) {
		try {
			Long count = redisTemplate.opsForSet().add(key, values);
			if(time > 0) {
				expire(key, time);
			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
    /**
     * 获取set缓存的长度
     * @param key 键
     * @return
     */
	public long putSetSize(String key) {
		try {
			return redisTemplate.opsForSet().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
	public long delSetValue(String key,Object...values) {
		try {
			return redisTemplate.opsForSet().remove(key, values);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
    
	/**
     * 获取list缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 结束  0 到 -1代表所有值
     * @return
     */
	public List<Object> getListValues(String key,long start,long end){
		try {
			return redisTemplate.opsForList().range(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
    /**
     * 获取list缓存的长度
     * @param key 键
     * @return
     */
	public long getListSize(String key) {
		try {
			return redisTemplate.opsForList().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
	public Object getListValueByIndex(String key,long index) {
		try {
			return redisTemplate.opsForList().index(key, index);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return
     */
	public boolean putListValue(String key,Object value) {
		try {
			 redisTemplate.opsForList().rightPush(key, value);
			 return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
	public boolean putListValueSetExpireTime(String key,long time,Object value) {
		try {
			 redisTemplate.opsForList().rightPush(key, value);
			 if(time>0) expire(key, time);
			 return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return
     */
	public boolean putListValueAll(String key,List<Object> value) {
		try {
			 redisTemplate.opsForList().rightPushAll(key, value);
			 return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
    /**
     * 将list放入缓存,设置过期时间
     * @param key 键
     * @param time
     * @param value 值
     * @return
     */
	public boolean putListValueAll(String key,long time,List<Object> value) {
		try {
			 redisTemplate.opsForList().rightPushAll(key, value);
			 if(time>0)expire(key, time);
			 return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
    /**
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return
     */
	public boolean updateListValueByIndex(String key, long index, Object value) {
		try {
			 redisTemplate.opsForList().set(key,index, value);
			 return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
    /**
     * 用于移除键中指定的元素。接受3个参数，分别是缓存的键名，计数事件，要移除的值。
     * 计数事件可以传入的有三个值，分别是-1、0、1。
     	-1代表从存储容器的最右边开始，删除一个与要移除的值匹配的数据；
     	0  代表删除所有与传入值匹配的数据；
     	1  代表从存储容器的最左边开始，删除一个与要移除的值匹配的数据。
     * @param key 键
     * @param count
     * @param value 值
     * @return 移除的个数
     */
	public void delListItemBySize(String key, long count,Object value) {
		try {
			 redisTemplate.opsForList().remove(key,count,value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
