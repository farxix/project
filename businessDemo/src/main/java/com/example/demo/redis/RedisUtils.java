package com.example.demo.redis;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
	private Logger logger = LoggerFactory.getLogger(getClass());
	public static final int MaxSetGroupIndex = 999;

	@Autowired
	private RedisServiceImpl redisService;

	/*
	 * 将HashMap<String, ArrayList<Integer>>转为HashMap<String, String>存储redis
	 */
	public boolean setHash2JsonValue(String redisKey, HashMap<String, ArrayList<Integer>> map) {
		/*
		 * 删除redis中存在，而map中不存在的hash值
		 */
		Set<Object> cacheHashKeys = hashKeys(redisKey);
		if (cacheHashKeys != null && !cacheHashKeys.isEmpty()) {
			for (Object cacheHashKey : cacheHashKeys) {
				String cacheHashKey1 = (String) cacheHashKey;
				if (!map.keySet().contains(cacheHashKey1)) {
					delHashValue(redisKey, cacheHashKey1);
				}
			}
		}

		/*
		 * 添加/更新redisKey的值
		 */
		HashMap<String, String> jsonValueMap = new HashMap<String, String>();
		for (String hashKey : map.keySet()) {
			String jsonValue = JSONUtil.toJsonStr(map.get(hashKey));
			jsonValueMap.put(hashKey, jsonValue);
			logger.trace("hashKey:{} value:{}", hashKey, jsonValue);
		}
		return setHash(redisKey, jsonValueMap);
	}

	public HashMap<String, ArrayList<Integer>> getTotalHash(String redisKey) {
		HashMap<String, String> jsonValueMap = (HashMap<String, String>) this.getHash(redisKey);
		if (jsonValueMap != null && !jsonValueMap.isEmpty()) {
			HashMap<String, ArrayList<Integer>> resultMap = new HashMap<String, ArrayList<Integer>>();
			for (String hashKey : jsonValueMap.keySet()) {
				ArrayList<Integer> list = JSONUtil.toBean(JSONUtil.toJsonStr(jsonValueMap.get(hashKey)),ArrayList.class);
				resultMap.put(hashKey, list);
			}
			return resultMap;
		}
		return null;
	}

	/*
	 * 从redis获取指定的hash值，并将其转为ArrayList<Integer>
	 */
	public ArrayList<Integer> getHashValue4JsonValue2(String key, String hashKey) {
		String jsonValue = (String) getHashValue(key, hashKey);
		ArrayList<Integer> list = JSONUtil.toBean(jsonValue,ArrayList.class);
		return list;
	}

	/*
	 * 从redis中获取指定的hash值，包含hashKey值为##的
	 */
	public ArrayList<Integer> getHashValue4JsonValue(String key, String hashKey) {
		Set<Integer> result = new HashSet<Integer>();
		Collection<Object> hashKeys = new HashSet<Object>();
		hashKeys.add(hashKey);
		hashKeys.add("##");
		List<Object> redisResult = getHashValue(key, hashKeys);
		if (redisResult != null && !redisResult.isEmpty()) {
			for (Object jsonStr : redisResult) {
				ArrayList<Integer> list = JSONUtil.toBean(JSONUtil.toJsonStr(jsonStr),ArrayList.class);
				if (list != null && !redisResult.isEmpty()) {
					result.addAll(list);
				}
			}

			ArrayList<Integer> returnResult = new ArrayList<Integer>();
			returnResult.addAll(result);
			return returnResult;
		}
		return null;
	}

	/*
	 * 设置 HashMap<String, ArrayList<Integer>>
	 */
	public boolean setListMap(String key, HashMap<String, ArrayList<Integer>> map) {
		return setString(key, JSONUtil.toJsonStr(map));
	}

	public HashMap<String, ArrayList<Integer>> getListMap(String key) {
		String str = getString(key);
		if (str == null || str.isEmpty()) {
			return null;
		}
		HashMap<String, ArrayList<Integer>> map = JSONUtil.toBean(JSONUtil.toJsonStr(str),HashMap.class);

//		HashMap<String, ArrayList<Integer>> map = GsonUtil.fromJson(str,
//				GsonUtil.type(HashMap.class, String.class, GsonUtil.type(ArrayList.class, Integer.class)));
		return map;
	}

	/*
	 * 设置 ArrayList<Integer>
	 */
	public boolean setList(String key, ArrayList<Integer> list) {
		return setString(key, JSONUtil.toJsonStr(list));
	}

	public ArrayList<Integer> getList(String key) {
		String str = getString(key);
		if (str == null || str.isEmpty()) {
			return null;
		}
		ArrayList<Integer> list = JSONUtil.toBean(JSONUtil.toJsonStr(str),ArrayList.class);

		return list;
	}

	/*
	 * 直接将key和value均设置为字符串，不使用redisTemplate的序列化
	 */
	public Boolean setString(String key, String value) {
		return redisService.setString(key, value);
	}

	public String getString(String key) {
		return redisService.getString(key);
	}

	/*
	 * 使用redisTemplate的系列化来设置key和value值
	 */
	public boolean set(String key, Object value) {
		return redisService.set(key, value);
	}

	public boolean set(String key, Object value, long timeout, TimeUnit unit) {
		return redisService.set(key, value, timeout, unit);
	}

	public Object get(String key) {
		return redisService.get(key);
	}

	public Set<String> getAllKey() {
		return redisService.getAllKey();
	}

	public Set<String> getAllKey(String pattern) {
		return redisService.getAllKey(pattern);
	}

	public long incrby(final String key, long delta) {
		return redisService.incrby(key, delta);
	}

	public boolean isExist(String key) {
		return redisService.isExist(key);
	}

	public boolean delete(String key) {
		return redisService.delete(key);
	}

	public boolean delete(Set<String> keys) {
		return redisService.delete(keys);
	}

	public boolean setSet(String key, Object value) {
		return redisService.setSet(key, value);
	}

	public Set<Object> getSet(String key) {
		return redisService.members(key);
	}

	public boolean setIsMembers(String key, Object o) {
		return redisService.isMembers(key, o);
	}

	/******************************************************
	 * 操作list
	 ******************************************************/
	public Long listLeftPush(String key, String value) {
		return redisService.listLeftPush(key, value);
	}

	public Long listLeftPushAll(String key, String... values) {
		return redisService.listLeftPushAll(key, values);
	}

	public String listRightPop(String key, long timeout, TimeUnit unit) {
		return redisService.listRightPop(key, timeout, unit);
	}

	/*
	 * 操作set
	 */
	public Long setAdd(String key, Object... values) {
		return redisService.setAdd(key, values);
	}

	public Long setRemove(String key, Object... values) {
		return redisService.setRemove(key, values);
	}

	public Set<Object> setMembers(String key) {
		return redisService.setMembers(key);
	}

	/*
	 * 操作setGroup(每setGroup包含1000个set,从0到999)
	 */
	public Long setGroupAdd(String groupKeyPrefix, int hashCode, Object value) {
		String setKey = groupKeyPrefix + "(" + Math.abs(hashCode) % MaxSetGroupIndex + ")";
		logger.trace("setAdd key:{} value:{}", setKey, value);
		return redisService.setAdd(setKey, value);
	}

	public Long setGroupRemove(String groupKeyPrefix, int hashCode, Object value) {
		logger.debug("hashCode:{}", hashCode);
		String setKey = groupKeyPrefix + "(" + Math.abs(hashCode) % MaxSetGroupIndex + ")";
		logger.trace("setRemove key:{} value:{}", setKey, value);
		return redisService.setRemove(setKey, value);
	}

	public Set<Object> setGroupMembers(String groupKeyPrefix) {
		Set<Object> resultSet = new HashSet<Object>();
		for (int i = 0; i < MaxSetGroupIndex; ++i) {
			String setKey = groupKeyPrefix + "(" + i + ")";
			resultSet.addAll(redisService.setMembers(setKey));
		}
		return resultSet;
	}

	public Long setGroupAdd(String groupKeyPrefix, Object... values) {
		Long count = 0l;
		for (Object value : values) {
			count += this.setGroupAdd(groupKeyPrefix, value.hashCode(), value);
		}
		return count;
	}

	public Long setGroupRemove(String groupKeyPrefix, Object... values) {
		Long count = 0l;
		for (Object value : values) {
			count += this.setGroupRemove(groupKeyPrefix, value.hashCode(), value);
		}
		return count;
	}

	public Long setGroupRemove(String groupKeyPrefix, Object value) {
		return this.setGroupRemove(groupKeyPrefix, value.hashCode(), value);
	}

	/*
	 * hash
	 */
	public boolean setHash(String key, Map<String, ?> map) {
		return redisService.setHash(key, map);
	}

	public Map<?, ?> getHash(String key) {
		return redisService.getHash(key);
	}

	public boolean hashHasKey(String key, String hashKey) {
		return redisService.hashHasKey(key, hashKey);
	}

	public void setHashValue(String key, String hashKey, Object value) {
		redisService.setHashValue(key, hashKey, value);
	}

	public Object getHashValue(String key, String hashKey) {
		return redisService.getHashValue(key, hashKey);
	}

	public List<Object> getHashValue(String key, Collection<Object> hashKeys) {
		return redisService.getHashValue(key, hashKeys);
	}

	public Object delHashValue(String key, String hashKey) {
		return redisService.delHashValue(key, hashKey);
	}

	public Set<Object> hashKeys(String key) {
		return redisService.hashKeys(key);
	}

	/*
	 * pineline
	 */
	public List<?> executePinelined(RedisCallback redisCallback) {
		return redisService.executePinelined(redisCallback);
	}

	public List<Object> executePinelinedSet(RedisCallback redisCallback, RedisSerializer<?> redisSerializer) {
		return redisService.executePinelinedSet(redisCallback, redisSerializer);
	}

	public Set<String> keys(String keyPattern) {
		return redisService.keys(keyPattern);
	}

	public boolean setbit(String key, int offset, Boolean value) {
		return redisService.setbit(key, offset, value);
	}

	public BitSet getBitSet(String key) {
		return redisService.getBitSet(key);
	}

	/*
	 * 超时时间
	 */
	public boolean setExpireSec(String key, long sec) {
		return redisService.setExpireSec(key, sec);
	}

	/*
	 * scan操作
	 */
	public Set<String> scan(String match, long count) {
		return redisService.scan(match, count);
	}

	public void batchWritePerf(String resId, List<HashMap<byte[], Map<byte[], byte[]>>> perfList,
			Integer expireMinute) {
		redisService.batchWritePerf(resId, perfList, expireMinute);
	}

	public boolean setNX(final String key, final String value) {
		return redisService.setNX(key, value);
	}
}
