package com.lucaschen.common.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService<T> {

    private final RedisTemplate<String, T> redisTemplate;

    /**
     * [Common] 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return Boolean
     */
    public Boolean expire(final String key, final long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * [Common] 指定缓存失效时间
     *
     * @param key      键
     * @param time     时间
     * @param timeUnit 时间单位
     * @return Boolean
     */
    public Boolean expire(final String key, final long time, final TimeUnit timeUnit) {
        return redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * [Common] 根据键获取过期时间
     *
     * @param key 键(不能为 null)
     * @return Long 时间(秒)，返回 0 则代表永久有效
     */
    public Long getExpire(final String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * [Common] 根据键获取过期时间
     *
     * @param key      key 键(不能为 null)
     * @param timeUnit 时间单位
     * @return Long 时间(秒)，返回 0 则代表永久有效
     */
    public Long getExpire(final String key, final TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * [Common] 判断键是否存在
     *
     * @param key 键
     * @return Boolean 是否存在
     */
    public Boolean hasKey(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * [Common] 删除
     *
     * @param key 键，可以是一个值，也可以是多个
     */
    public void delete(final String... key) {
        redisTemplate.delete(Arrays.asList(key));
    }

    /**
     * [Common] 删除
     *
     * @param keys 键列表
     */
    public void delete(final Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * [Common] 执行回调
     *
     * @param redisCallback 回调
     * @return T 返回值
     */
    public T execute(RedisCallback<T> redisCallback) {
        return redisTemplate.execute(redisCallback);
    }

    /**
     * [String] 获取
     *
     * @param key 键
     * @return T 值
     */
    public T stringGet(final String key) {
        return StringUtils.isBlank(key) ? null : redisTemplate.opsForValue().get(key);
    }


    /**
     * [String] 添加
     *
     * @param key   键
     * @param value 值
     * @return Boolean 是否成功
     */
    public Boolean stringSet(final String key, final T value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return Boolean.TRUE;
        } catch (Exception ex) {
            log.error("setString: {} {}", key, value, ex);
            return Boolean.FALSE;
        }
    }

    /**
     * [String] 添加
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)，time 要大于 0 如果 time 小于等于 0 则设置无限期
     * @return Boolean 是否成功
     */
    public Boolean stringSet(final String key, final T value, final long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time);
            } else {
                stringSet(key, value);
            }
            return Boolean.TRUE;
        } catch (Exception ex) {
            log.error("setString: {} {} {}", key, value, time, ex);
            return Boolean.FALSE;
        }
    }

    /**
     * [String] 添加
     *
     * @param key      键
     * @param value    值
     * @param time     时间，time 要大于 0 如果 time 小于等于 0 则设置无限期
     * @param timeUnit 时间单位
     * @return Boolean 是否成功
     */
    public Boolean stringSet(final String key,
                             final T value,
                             final long time,
                             final TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, timeUnit);
            } else {
                stringSet(key, value);
            }
            return Boolean.TRUE;
        } catch (Exception ex) {
            log.error("setString: {} {} {} {}", key, value, time, timeUnit, ex);
            return Boolean.FALSE;
        }
    }

    /**
     * [String] 递增
     *
     * @param key 键
     * @return Long 操作后的值
     */
    public Long increment(final String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * [String] 递增
     *
     * @param key   键
     * @param delta 变化值，大于 0
     * @return Long 操作后的值
     */
    public Long increment(final String key, final long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * [String] 递减
     *
     * @param key 键
     * @return Long 操作后的值
     */
    public Long decrement(final String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    /**
     * [String] 递减
     *
     * @param key   键
     * @param delta 变化值，大于 0
     * @return Long 操作后的值
     */
    public Long decrement(final String key, final long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * [Hash] 向 hash 表中放入数据，如果 key 不存在，则会自动创建
     *
     * @param key     键
     * @param hashKey hash 键
     * @param value   值
     * @return Boolean 是否成功
     */
    public Boolean hashPut(final String key, final String hashKey, final T value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            return Boolean.TRUE;
        } catch (Exception ex) {
            log.error("hashPut: {} {} {}", key, hashKey, value, ex);
            return Boolean.FALSE;
        }
    }

    /**
     * [Hash] 向 hash 表中放入数据，如果 key 不存在，则会自动创建
     *
     * @param key     键
     * @param hashKey hash 键
     * @param value   值
     * @param time    时间(秒)，如果已存在 hash 表有过期时间，这里会替换原有时间
     * @return Boolean 是否成功
     */
    public Boolean hashPut(final String key,
                           final String hashKey,
                           final T value,
                           final long time) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            if (time > 0) {
                expire(key, time);
            }
            return Boolean.TRUE;
        } catch (Exception ex) {
            log.error("hashPut: {} {} {} {}", key, hashKey, value, time, ex);
            return Boolean.FALSE;
        }
    }

    /**
     * [Hash] 添加
     *
     * @param key 键
     * @param map 多个键值
     * @return Boolean 是否成功
     */
    public Boolean hashPutAll(final String key, final Map<String, T> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return Boolean.TRUE;
        } catch (Exception ex) {
            log.error("hashPutAll: {} {}", key, map, ex);
            return Boolean.FALSE;
        }
    }

    /**
     * [Hash] 添加
     *
     * @param key  键
     * @param map  多个键值
     * @param time 时间(秒)，如果已存在 hash 表有过期时间，这里会替换原有时间
     * @return Boolean 是否成功
     */
    public Boolean hashPutAll(final String key, final Map<String, T> map, final long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return Boolean.TRUE;
        } catch (Exception ex) {
            log.error("hashPutAll: {} {}", key, map, ex);
            return Boolean.FALSE;
        }
    }

    /**
     * [Hash] 根据键获取 Map
     *
     * @param key 键
     * @return Map 多个键值对
     */
    public Map<Object, Object> hashEntries(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * [Hash] 根据键和 hash 键获取对应的值
     *
     * @param key     键
     * @param hashKey hash 键
     * @return Object 值
     */
    public Object hashGet(final String key, final String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * [Hash] 删除 hash 表中的值
     *
     * @param key     键
     * @param hashKey hash 键，可以为多个
     */
    public void hashDelete(final String key, final Object... hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    /**
     * [Hash] 判断 hash 表中是否存在该 hash 键
     *
     * @param key     键
     * @param hashKey hash 键
     * @return 是否存在
     */
    public Boolean hashHasKey(final String key, final String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * [Hash] 递增
     *
     * @param key     键
     * @param hashKey hash 键
     * @param delta   变化值，大于 0
     * @return Long 操作后的值
     */
    public Long hashIncrement(final String key, final String hashKey, final long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * [Hash] 递增
     *
     * @param key     键
     * @param hashKey hash 键
     * @param delta   变化值，大于 0
     * @return Double 操作后的值
     */
    public Double hashIncrement(final String key, final String hashKey, final double delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }


    /**
     * [Hash] 递减
     *
     * @param key     键
     * @param hashKey hash 键
     * @param delta   变化值，大于 0
     * @return Long 操作后的值
     */
    public Long hashDecrement(final String key, final String hashKey, final long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, -delta);
    }

    /**
     * [Hash] 递减
     *
     * @param key     键
     * @param hashKey hash 键
     * @param delta   变化值，大于 0
     * @return Double 操作后的值
     */
    public Double hashDecrement(final String key, final String hashKey, final double delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, -delta);
    }

    /**
     * [Set] 根据键获取 Set 中的所有值
     *
     * @param key 键
     * @return Set 值
     */
    public Set<T> setMembers(final String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception ex) {
            log.error("setMembers: {}", key, ex);
            return Collections.emptySet();
        }
    }

    /**
     * [Set] 根据 value 判断是否存在
     *
     * @param key   键
     * @param value 值
     * @return 是否存在
     */
    public Boolean setIsNumber(final String key, final T value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception ex) {
            log.error("setIsNumber: {} {}", key, value, ex);
            return Boolean.FALSE;
        }
    }

    /**
     * [Set] Set 中增加值
     *
     * @param key    键
     * @param values 值，可多个
     * @return Long 成功个数
     */
    public Long setAdd(final String key, final T... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception ex) {
            log.error("setAdd: {} {}", key, values, ex);
            return 0L;
        }
    }

    /**
     * [Set] 获取长度
     *
     * @param key 键
     * @return Long 长度
     */
    public Long setSize(final String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception ex) {
            log.error("setSize: {}", key, ex);
            return 0L;
        }
    }

    /**
     * [Set] 根据值移除
     *
     * @param key    键
     * @param values 值，可以为多个
     * @return Long 移除的个数
     */
    public Long setRemove(final String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception ex) {
            log.error("setRemove: {} {}", key, values, ex);
            return 0L;
        }
    }


    /**
     * [List] 将值放入 list 头
     *
     * @param key   键
     * @param value 值，可以为多个
     * @return Boolean 是否成功
     */
    public Boolean listLeftPush(final String key, final T... value) {
        try {
            redisTemplate.opsForList().leftPushAll(key, value);
            return Boolean.TRUE;
        } catch (Exception ex) {
            log.error("listLeftPush: {} {}", key, value, ex);
            return Boolean.FALSE;
        }
    }

    /**
     * [List] 将值放入 list 头
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return Boolean 是否成功
     */
    public Boolean listLeftPush(final String key, final T value, final long time) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return Boolean.TRUE;
        } catch (Exception ex) {
            log.error("listLeftPush: {} {} {}", key, value, time, ex);
            return Boolean.FALSE;
        }
    }

    /**
     * [List] 将值放入 list 尾
     *
     * @param key   键
     * @param value 值，可以为多个
     * @return Boolean 是否成功
     */
    public Boolean listRightPush(final String key, final T... value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return Boolean.TRUE;
        } catch (Exception ex) {
            log.error("listRightPush: {} {}", key, value, ex);
            return Boolean.FALSE;
        }
    }

    /**
     * [List] 将值放入 list 尾
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return Boolean 是否成功
     */
    public Boolean listRightPush(final String key, final T value, final long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return Boolean.TRUE;
        } catch (Exception ex) {
            log.error("listRightPush: {} {} {}", key, value, time, ex);
            return Boolean.FALSE;
        }
    }

    /**
     * [List] 取值
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 [0 ~ -1] 表示所有值
     * @return List 返回值列表
     */
    public List<T> listRange(final String key, final long start, final long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception ex) {
            log.error("listRange: {} {} {}", key, start, end, ex);
            return Collections.emptyList();
        }
    }

    /**
     * [List] 获取 list 长度
     *
     * @param key 键
     * @return Long 长度
     */
    public Long listSize(final String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception ex) {
            log.error("listSize: {}", key, ex);
            return 0L;
        }
    }

    /**
     * [List] 通过索引取值
     *
     * @param key   键
     * @param index 索引 正向 0-> 第一个，反向 -1-> 倒数第一个
     * @return T 值
     */
    public T listIndex(final String key, final long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception ex) {
            log.error("listIndex: {} {}", key, index, ex);
            return null;
        }
    }

    /**
     * [List] 根据索引修改值
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return Boolean 是否成功
     */
    public Boolean listSet(final String key, final long index, final T value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return Boolean.TRUE;
        } catch (Exception ex) {
            log.error("listSet: {} {} {}", key, index, value, ex);
            return Boolean.FALSE;
        }
    }

    /**
     * [List] 从 list 头取元素
     *
     * @param key 键
     * @return T 返回值
     */
    public T listLeftPop(final String key) {
        try {
            return redisTemplate.opsForList().leftPop(key);
        } catch (Exception ex) {
            log.error("listLeftPop: {}", key, ex);
            return null;
        }
    }

    /**
     * [List] 从 list 头取元素
     *
     * @param key   键
     * @param count 数量
     * @return List 返回值列表
     */
    public List<T> listLeftPop(final String key, final long count) {
        try {
            return redisTemplate.opsForList().leftPop(key, count);
        } catch (Exception ex) {
            log.error("listLeftPop: {} {}", key, count, ex);
            return Collections.emptyList();
        }
    }

    /**
     * [List] 从 list 头取元素
     *
     * @param key 键
     * @return T 返回值
     */
    public T listRightPop(final String key) {
        try {
            return redisTemplate.opsForList().rightPop(key);
        } catch (Exception ex) {
            log.error("listRightPop: {}", key, ex);
            return null;
        }
    }

    /**
     * [List] 从 list 头取元素
     *
     * @param key   键
     * @param count 数量
     * @return List 返回值列表
     */
    public List<T> listRightPop(final String key, final long count) {
        try {
            return redisTemplate.opsForList().rightPop(key, count);
        } catch (Exception ex) {
            log.error("listRightPop: {} {}", key, count, ex);
            return Collections.emptyList();
        }
    }
}
