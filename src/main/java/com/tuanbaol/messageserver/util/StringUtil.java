package com.tuanbaol.messageserver.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @Description 字符串操作工具类
 * @ClassName StringUtil
 */
public class StringUtil {
    private final static String DEFAULT_DELIMITER = "\\{\\}";
    private final static Pattern PATTERN = Pattern.compile(DEFAULT_DELIMITER);

    /**
     * 判断多个字符串是否全部为blank
     *
     * @param params
     * @return
     */
    public static Boolean isBlankBoth(String... params) {
        if ((null == params) || (params.length == 0)) {
            return true;
        }
        for (String item : params) {
            if (!StringUtils.isBlank(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断多个字符串是否至少一个为blank
     *
     * @param params
     * @return
     */
    public static Boolean isBlankLeastone(String... params) {
        if ((null == params) || (params.length == 0)) {
            return true;
        }
        for (String item : params) {
            if (StringUtils.isBlank(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断多个字符串是否全部为empty
     *
     * @param params
     * @return
     */
    public static Boolean isEmptyBoth(String... params) {
        if ((null == params) || (params.length == 0)) {
            return true;
        }
        for (String item : params) {
            if (!StringUtils.isEmpty(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断多个字符串是否至少一个为empty
     *
     * @param params
     * @return
     */
    public static Boolean isEmptyLeastone(String... params) {
        if ((null == params) || (params.length == 0)) {
            return true;
        }
        for (String item : params) {
            if (StringUtils.isEmpty(item)) {
                return true;
            }
        }
        return false;
    }


    public static String format(String pattern, Object... params) {
        if (StringUtils.isBlank(pattern) || null == params || params.length <= 0) {
            return pattern;
        }
        StringBuilder sb = new StringBuilder();
        int j = 0;
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == '{') {
                if (i < pattern.length() - 1 && pattern.charAt(i + 1) == '}') {
                    sb.append(params[j++]);
                    i++;
                    continue;
                }
            }
            sb.append(pattern.charAt(i));
        }
        return sb.toString();
    }

    public static String formatByRegex(String pattern, Object... params) {
        int len;
        if (StringUtils.isBlank(pattern) || null == params || (len = params.length) <= 0) {
            return pattern;
        }
        Matcher matcher = PATTERN.matcher(pattern);
        for (int i = 0; matcher.find(); i++) {
            if (i > len - 1) {
                return pattern;
            }
            String param = params[i] == null ? "null" : params[i].toString();
            param = "【" + param + "】";
            pattern = PATTERN.matcher(pattern).replaceFirst(param);
        }
        return pattern;
    }

    public static String swapFirstAndEnd(String ori) {
        if (StringUtils.isBlank(ori) || ori.length() == 1) {
            return ori;
        }
        int length = ori.length();
        if (length == 2) {
            return ori.substring(1) + ori.substring(0, 1);
        }
        return ori.substring(length - 1) + ori.substring(1, length - 1) + ori.substring(0, 1);
    }

}
