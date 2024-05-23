package com.lucaschen.common.core.util.uuid;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 序列生成类
 */
public final class Seq {

    // 通用序列类型
    public static final String COMM_SEQ_TYPE = "COMMON";
    // 上传序列类型
    public static final String UPLOAD_SEQ_TYPE = "UPLOAD";
    // 通用接口序列数
    private static final AtomicInteger COMM_SEQ = new AtomicInteger(1);
    // 上传接口序列数
    private static final AtomicInteger UPLOAD_SEQ = new AtomicInteger(1);
    // 机器标识
    private static final String MACHINE_CODE = "A";

    private Seq() {
    }

    /**
     * 获取通用序列号
     *
     * @return 序列值
     */
    public static String getId() {
        return getId(COMM_SEQ_TYPE);
    }

    /**
     * 默认16位序列号 ISO_LOCAL_DATE_TIME + 一位机器标识 + 3长度循环递增字符串
     *
     * @return 序列值
     */
    public static String getId(final String type) {
        AtomicInteger atomicInt = UPLOAD_SEQ_TYPE.equals(type) ? UPLOAD_SEQ : COMM_SEQ;
        return getId(atomicInt, 3);
    }

    /**
     * 通用接口序列号 ISO_LOCAL_DATE_TIME + 一位机器标识 + length 长度循环递增字符串
     *
     * @param atomicInt 序列数
     * @param length    数值长度
     * @return 序列值
     */
    public static String getId(final AtomicInteger atomicInt, int length) {
        final String localDateTime =
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
        return localDateTime + MACHINE_CODE + getSeq(atomicInt, length);
    }

    /**
     * 序列循环递增字符串[1, 10 的 (length)幂次方), 用0左补齐length位数
     *
     * @return 序列值
     */
    private static synchronized String getSeq(final AtomicInteger atomicInt, int length) {
        // 先取值再 +1
        int value = atomicInt.getAndIncrement();

        // 如果更新后值 >= 10 的 (length) 幂次方则重置为 1
        int maxSeq = (int) Math.pow(10, length);
        if (atomicInt.get() >= maxSeq) {
            atomicInt.set(1);
        }
        // 转字符串，用 0 左补齐
        return StringUtils.leftPad(String.valueOf(value), length, '0');
    }
}