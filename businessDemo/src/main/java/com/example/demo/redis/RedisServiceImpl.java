package com.example.demo.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Primary
@Component
public class RedisServiceImpl {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisLettuceConfig redisConfig;

    @Resource
    private RedisTemplate<String, ?> redisTemplate;

    @SuppressWarnings("unchecked")
    public boolean setSet(String key, Object values) {
        SetOperations<String, Object> ops = (SetOperations<String, Object>) redisTemplate.opsForSet();
        return ops.add(key, values) > 0;
    }

    @SuppressWarnings("unchecked")
    public Set<Object> members(String key) {
        SetOperations<String, Object> ops = (SetOperations<String, Object>) redisTemplate.opsForSet();

        return ops.members(key);
    }

    @SuppressWarnings("unchecked")
    public boolean isMembers(String key, Object o) {
        SetOperations<String, Object> ops = (SetOperations<String, Object>) redisTemplate.opsForSet();
        return ops.isMember(key, o);
    }

    /******************************************************
     * 操作list
     ******************************************************/
    public Long listLeftPush(String key, String value) {
        ListOperations<String, String> ops = (ListOperations<String, String>) redisTemplate.opsForList();
        return ops.leftPush(key, value);
    }

    public Long listLeftPushAll(String key, String... values) {
        ListOperations<String, String> ops = (ListOperations<String, String>) redisTemplate.opsForList();
        return ops.leftPushAll(key, values);
    }

    public String listRightPop(String key, long timeout, TimeUnit unit) {
        ListOperations<String, String> ops = (ListOperations<String, String>) redisTemplate.opsForList();
        return ops.rightPop(key, timeout, unit);
    }

    /******************************************************
     * 操作set
     ******************************************************/
    @SuppressWarnings("unchecked")
    public Long setAdd(String key, Object... values) {
        SetOperations<String, Object> ops = (SetOperations<String, Object>) redisTemplate.opsForSet();
        return ops.add(key, values);
    }

    @SuppressWarnings("unchecked")
    public Long setRemove(String key, Object... values) {
        SetOperations<String, Object> ops = (SetOperations<String, Object>) redisTemplate.opsForSet();
        return ops.remove(key, values);
    }

    @SuppressWarnings("unchecked")
    public Set<Object> setMembers(String key) {
        SetOperations<String, Object> ops = (SetOperations<String, Object>) redisTemplate.opsForSet();
        return ops.members(key);
    }

    /******************************************************
     * 操作hash
     ******************************************************/
    public boolean setHash(String key, Map<String, ?> m) {
        try {
            redisTemplate.opsForHash().putAll(key, m);
        } catch (Exception e) {
            logger.error("set hash error,{}", e);
        }
        return false;
    }

    public Map<Object, Object> getHash(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public boolean hashHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    public void setHashValue(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public Object getHashValue(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public List<Object> getHashValue(String key, Collection<Object> hashKeys) {
        return redisTemplate.opsForHash().multiGet(key, hashKeys);
    }

    public Object delHashValue(String key, String hashKey) {
        return redisTemplate.opsForHash().delete(key, hashKey);
    }

    public Set<Object> hashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /******************************************************
     * 对象操作
     ******************************************************/
    @SuppressWarnings("unchecked")
    public boolean set(String key, Object value) {
        try {
            ValueOperations<String, Object> oper = (ValueOperations<String, Object>) redisTemplate.opsForValue();
            oper.set(key, value);
            // logger.debug("set redis [key:" + key + ",value:" + value + "]");
            return true;
        } catch (Exception e) {
            logger.error("set redis error,{}", e);
            return false;
        }
    }

    public boolean set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            ValueOperations<String, Object> oper = (ValueOperations<String, Object>) redisTemplate.opsForValue();
            oper.set(key, value, timeout, unit);
            // logger.debug("set redis [key:" + key + ",value:" + value + "]");
            return true;
        } catch (Exception e) {
            logger.error("set redis error,{}", e);
            return false;
        }
    }

    public Object get(final String key) {
        @SuppressWarnings("unchecked")
        ValueOperations<String, Object> oper = (ValueOperations<String, Object>) redisTemplate.opsForValue();
        return oper.get(key);
    }

    public Long incrby(final String key, long delta) {
        ValueOperations<String, Object> oper = (ValueOperations<String, Object>) redisTemplate.opsForValue();
        return oper.increment(key, delta);
    }

    public boolean isExist(String key) {
        return redisTemplate.hasKey(key);
    }

    public Set<String> getAllKey() {
        return redisTemplate.keys("*");
    }

    public boolean delete(String key) {
        try {
            logger.debug("begin del key:" + key);
            redisTemplate.delete(key);
            return true;
        } catch (Exception e) {
            logger.error("delete error,key:" + key + ",msg:" + e);
        }
        return false;
    }

    public Set<String> getAllKey(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public List<?> executePinelined(RedisCallback redisCallback) {
        return redisTemplate.executePipelined(redisCallback);
    }

    public List<Object> executePinelinedSet(RedisCallback redisCallback, RedisSerializer<?> redisSerializer) {
        return redisTemplate.executePipelined(redisCallback, redisSerializer);
    }

    public Set<String> keys(String keyPattern) {
        return redisTemplate.keys(keyPattern);
    }

    public boolean delete(Set<String> keys) {
        logger.trace("begin del keys:" + keys);
        try {
            redisTemplate.delete(keys);
            return true;
        } catch (Exception e) {
            logger.error("delete error,key:" + keys + ",msg:" + e);
        }
        return false;
    }

    /******************************************************
     * bit操作
     ******************************************************/
    public boolean setbit(String key, int offset, Boolean value) {
        logger.trace("setbit key:{}, offset:{}, value:{}", key, offset, value);
        try {
            redisTemplate.opsForValue().setBit(key, offset, value);
            return true;
        } catch (Exception e) {
            logger.error("setbit key:{} error, msg:{}", key, e);
        }
        return false;
    }

    public BitSet getBitSet(String key) {
        BitSet value = redisTemplate.execute(new RedisCallback<BitSet>() {
            public BitSet doInRedis(RedisConnection redis) throws DataAccessException {
                byte[] bytes = redis.get(key.getBytes());
                if (bytes == null || bytes.length <= 0) {
                    return null;
                }
                return fromByteArrayReverse(bytes);
            }
        });
        return value;
    }

    private BitSet fromByteArrayReverse(final byte[] bytes) {
        final BitSet bits = new BitSet();
        for (int i = 0; i < bytes.length * 8; ++i) {
            if ((bytes[i / 8] & (1 << (7 - (i % 8)))) != 0) {
                bits.set(i);
            }
        }
        return bits;
    }

    /*
     * String操作
     */
    public Boolean setString(String key, String value) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection redis) throws DataAccessException {
                redis.set(key.getBytes(), value.getBytes());
                return true;
            }
        });
        return result;
    }

    public String getString(String key) {
        String result = redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection redis) throws DataAccessException {
                byte[] values = redis.get(key.getBytes());
                if (null != values) {
                    return new String(values);
                }
                return null;
            }
        });
        return result;
    }

    /******************************************************
     * 超时时间
     ******************************************************/
    // 设置超时时间
    public boolean setExpireSec(String key, long sec) {
        return redisTemplate.expire(key, sec, TimeUnit.SECONDS);
    }

    /******************************************************
     * scan操作
     ******************************************************/
    public Set<String> scan(String match, long count) {
//		if (redisConfig.getNodes() == null || redisConfig.getNodes().isEmpty()) {
//			return scanSingleRedis(match, count);
//		} else {
//			return scanClusterRedis(match, count);
//		}
        return null;
    }

    // 仅可用于单个redis的scan操作
    public Set<String> scanSingleRedis(String match, long count) {
        Set<String> result = redisTemplate.execute(new RedisCallback<Set<String>>() {
            public Set<String> doInRedis(RedisConnection redis) throws DataAccessException {
                try {
                    Set<String> keys = new HashSet<String>();
                    ScanOptions options = ScanOptions.scanOptions().match(match).count(count).build();
                    Cursor<byte[]> cursor = redis.scan(options);
                    while (cursor.hasNext()) {
                        keys.add(new String(cursor.next()));
                    }
                    return keys;
                } catch (Exception e) {
                    logger.error("ERRR.", e);
                    return null;
                }
            }
        });
        return result;
    }

    // 仅可用于集群redis的scan操作
    public Set<String> scanClusterRedis(String match, long count) {
//		try {
//			Set<HostAndPort> nodeInfo = new HashSet<HostAndPort>();
//			String[] nodeArray = redisConfig.getNodes().split(",");
//			for (String nodeStr : nodeArray) {
//				String[] ip_port = nodeStr.split(":");
//				nodeInfo.add(new HostAndPort(ip_port[0], Integer.valueOf(ip_port[1])));
//			}
////			JedisCluster jedisCluster = new JedisCluster(nodeInfo);
//			JedisPoolConfig config = new JedisPoolConfig();
//			config.setTestOnReturn(true);
//			config.setTestOnBorrow(true);
//			JedisCluster jedisCluster = new JedisCluster(nodeInfo, 5000, redisConfig.getTimeOut(), 3,
//					redisConfig.getPassword(), config);
//
//			Set<String> result = new HashSet<String>();
//			Map<String, JedisPool> nodes = jedisCluster.getClusterNodes();
//			for (Entry<String, JedisPool> node : nodes.entrySet()) {
//				JedisPool pool = node.getValue();
//				Jedis connection = pool.getResource();
//				try {
//					String scanCursor = "0";
//					do {
//						ScanParams params = new ScanParams().match(match).count((int) count);
//						ScanResult<String> scanResult = connection.scan(scanCursor, params);
//						scanCursor = scanResult.getCursor();
//						result.addAll(scanResult.getResult());
//					} while (!scanCursor.equals("0"));
//				} catch (Exception e) {
//					logger.error("Error.", e);
//				} finally {
//					connection.close();
//				}
//			}
//			return result;
//		} catch (Exception e) {
//			logger.error("ERROR", e);
//			return null;
//		}
        return null;
    }

    public void batchWritePerf(String resId, List<HashMap<byte[], Map<byte[], byte[]>>> perfList,
                               Integer expireMinute) {
//		try {
//			RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
//			RedisConnection redisConnection = factory.getConnection();
//			JedisClusterConnection jedisClusterConnection = (JedisClusterConnection) redisConnection;
//			JedisCluster jedisCluster = jedisClusterConnection.getNativeConnection();
//			int slot = JedisClusterCRC16.getSlot(resId);
//			Field field = ReflectionUtils.findField(BinaryJedisCluster.class, null,
//					JedisClusterConnectionHandler.class);
//			field.setAccessible(true);
//			JedisSlotBasedConnectionHandler jedisClusterConnectionHandler = (JedisSlotBasedConnectionHandler) field
//					.get(jedisCluster);
//			Jedis jedis = jedisClusterConnectionHandler.getConnectionFromSlot(slot);
//
//			Pipeline pipeline = jedis.pipelined();
//			for (HashMap<byte[], Map<byte[], byte[]>> perfMap : perfList) {
//				Set<byte[]> keys = perfMap.keySet();
//				for (byte[] key : keys) {
//					pipeline.hmset(key, perfMap.get(key));
//					pipeline.expire(key, expireMinute * 60);
//				}
//			}
//			pipeline.syncAndReturnAll();
//
//			/*
//			 * jedis会自动将资源归还到连接池
//			 */
//			jedis.close();
//			RedisConnectionUtils.releaseConnection(redisConnection, factory);
//		} catch (Exception e) {
//			logger.error("ERROR.", e);
//		}
    }

    public boolean setNX(final String key, final String value) {
        Object obj = null;
        try {
            obj = redisTemplate.execute((RedisCallback<Object>) connection -> {
                StringRedisSerializer serializer = new StringRedisSerializer();
                Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
                        Object.class);
                Boolean success = connection.setNX(serializer.serialize(key),
                        jackson2JsonRedisSerializer.serialize(value));
                connection.close();
                return success;
            });
        } catch (Exception e) {
            logger.error("setNX redis error, key : {}", key);
        }
        return obj != null ? (Boolean) obj : false;
    }
}