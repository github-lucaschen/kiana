package com.lucaschen.common.core.util.ip;

import com.lucaschen.common.core.util.ServletUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Objects;

/**
 * 获取 IP 方法
 */
public final class IpUtils {
    public static final String REGX_0_255 = "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
    // 匹配 ip
    public static final String REGX_IP = "((" + REGX_0_255 + "\\.){3}" + REGX_0_255 + ")";
    // 匹配网段
    public static final String REGX_IP_SEG = "(" + REGX_IP + "-" + REGX_IP + ")";
    public static final String REGX_IP_WILDCARD = "(((\\*\\.){3}\\*)|(" + REGX_0_255 +
            "(\\.\\*){3})|(" + REGX_0_255 +
            "\\." + REGX_0_255 + ")(\\.\\*){2}" +
            "|((" + REGX_0_255 + "\\.){3}\\*))";

    private IpUtils() {
    }

    /**
     * 获取客户端 IP
     *
     * @return IP 地址
     */
    public static String getIpAddr() {
        return getIpAddr(ServletUtils.getRequest());
    }

    /**
     * 获取客户端 IP
     *
     * @param request 请求对象
     * @return IP 地址
     */
    public static String getIpAddr(final HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : getMultistageReverseProxyIp(ip);
    }

    private static boolean isValidIp(final String ip) {
        return ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip);
    }

    /**
     * 检查是否为内部 IP 地址
     *
     * @param ip IP 地址
     * @return 结果
     */
    public static boolean internalIp(final String ip) {
        byte[] addr = textToNumericFormatV4(ip);
        return internalIp(addr) || "127.0.0.1".equals(ip);
    }

    /**
     * 检查是否为内部IP地址
     *
     * @param addr byte地址
     * @return 结果
     */
    private static boolean internalIp(byte[] addr) {
        if (Objects.isNull(addr) || addr.length < 2) {
            return true;
        }
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        // 10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        // 172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        // 192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (b0) {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    return true;
                }
                break;
            case SECTION_5:
                if (b1 == SECTION_6) {
                    return true;
                }
                break;
            default:
                return false;
        }
        return false;
    }

    /**
     * 将 IPv4 地址转换成字节
     *
     * @param text IPv4地址
     * @return byte 字节
     */
    public static byte[] textToNumericFormatV4(final String text) {
        if (text.isEmpty()) {
            return new byte[0];
        }
        byte[] bytes = new byte[4];
        String[] elements = text.split("\\.", -1);
        try {
            long l;
            int i;
            switch (elements.length) {
                case 1:
                    l = Long.parseLong(elements[0]);
                    if ((l < 0L) || (l > 4294967295L)) {
                        return new byte[0];
                    }
                    bytes[0] = (byte) (int) (l >> 24 & 0xFF);
                    bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 2:
                    l = Integer.parseInt(elements[0]);
                    if ((l < 0L) || (l > 255L)) {
                        return new byte[0];
                    }
                    bytes[0] = (byte) (int) (l & 0xFF);
                    l = Integer.parseInt(elements[1]);
                    if ((l < 0L) || (l > 16777215L)) {
                        return new byte[0];
                    }
                    bytes[1] = (byte) (int) (l >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 3:
                    for (i = 0; i < 2; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return new byte[0];
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    l = Integer.parseInt(elements[2]);
                    if ((l < 0L) || (l > 65535L)) {
                        return new byte[0];
                    }
                    bytes[2] = (byte) (int) (l >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 4:
                    for (i = 0; i < 4; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return new byte[0];
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    break;
                default:
                    return new byte[0];
            }
        } catch (NumberFormatException e) {
            return new byte[0];
        }
        return bytes;
    }

    /**
     * 获取 IP 地址
     *
     * @return 本地 IP 地址
     */
    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ignored) {
        }
        return "127.0.0.1";
    }

    /**
     * 获取主机名
     *
     * @return 本地主机名
     */
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignored) {
        }
        return "未知";
    }

    /**
     * 从多级反向代理中获得第一个非 unknown IP 地址
     *
     * @param ip 获得的 IP 地址
     * @return 第一个非unknown IP地址
     */
    public static String getMultistageReverseProxyIp(String ip) {
        // 多级反向代理检测
        if (ip != null && ip.contains(",")) {
            final String[] ips = ip.trim().split(",");
            for (String subIp : ips) {
                if (!isUnknown(subIp)) {
                    ip = subIp;
                    break;
                }
            }
        }
        return StringUtils.substring(ip, 0, 255);
    }

    /**
     * 检测给定字符串是否为未知，多用于检测HTTP请求相关
     *
     * @param checkString 被检测的字符串
     * @return 是否未知
     */
    public static boolean isUnknown(final String checkString) {

        return StringUtils.isBlank(checkString) || StringUtils.equalsIgnoreCase("unknown", checkString);
    }

    /**
     * 是否为 IP
     */
    public static boolean isIP(final String ip) {
        return StringUtils.isNotBlank(ip) && ip.matches(REGX_IP);
    }

    /**
     * 是否为 IP，或 * 为间隔的通配符地址
     */
    public static boolean isIpWildCard(final String ip) {
        return StringUtils.isNotBlank(ip) && ip.matches(REGX_IP_WILDCARD);
    }

    /**
     * 检测参数是否在 ip 通配符里
     */
    public static boolean ipIsInWildCardNoCheck(final String ipWildCard, final String ip) {
        final String[] s1 = ipWildCard.split("\\.");
        final String[] s2 = ip.split("\\.");
        boolean isMatchedSeg = true;
        for (int i = 0; i < s1.length && !s1[i].equals("*"); i++) {
            if (!s1[i].equals(s2[i])) {
                isMatchedSeg = false;
                break;
            }
        }
        return isMatchedSeg;
    }

    /**
     * 是否为特定格式如: “10.10.10.1-10.10.10.99” 的 ip 段字符串
     */
    public static boolean isIPSegment(final String ipSeg) {
        return StringUtils.isNotBlank(ipSeg) && ipSeg.matches(REGX_IP_SEG);
    }

    /**
     * 判断 ip 是否在指定网段中
     */
    public static boolean ipIsInNetNoCheck(final String ipArea, final String ip) {
        int idx = ipArea.indexOf('-');
        final String[] sips = ipArea.substring(0, idx).split("\\.");
        final String[] sipe = ipArea.substring(idx + 1).split("\\.");
        final String[] sipt = ip.split("\\.");
        long ips = 0L;
        long ipe = 0L;
        long ipt = 0L;
        for (int i = 0; i < 4; ++i) {
            ips = ips << 8 | Integer.parseInt(sips[i]);
            ipe = ipe << 8 | Integer.parseInt(sipe[i]);
            ipt = ipt << 8 | Integer.parseInt(sipt[i]);
        }
        if (ips > ipe) {
            long t = ips;
            ips = ipe;
            ipe = t;
        }
        return ips <= ipt && ipt <= ipe;
    }

    /**
     * 校验 ip 是否符合过滤串规则
     *
     * @param filter 过滤 IP 列表,支持后缀 '*' 通配,支持网段如: `10.10.10.1-10.10.10.99`
     * @param ip     校验 IP 地址
     * @return boolean 结果
     */
    public static boolean isMatchedIp(final String filter, final String ip) {
        if (StringUtils.isEmpty(filter) || StringUtils.isEmpty(ip)) {
            return false;
        }
        return Arrays.stream(filter.split(";"))
                .anyMatch(iStr -> isIP(iStr) && iStr.equals(ip) ||
                        isIpWildCard(iStr) && ipIsInWildCardNoCheck(iStr, ip) ||
                        isIPSegment(iStr) && ipIsInNetNoCheck(iStr, ip));
    }
}