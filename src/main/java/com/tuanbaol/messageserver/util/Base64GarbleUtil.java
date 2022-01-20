package com.tuanbaol.messageserver.util;

import com.tuanbaol.messageserver.bean.Message;
import com.tuanbaol.messageserver.exception.ServiceException;

import java.util.HashMap;
import java.util.Map;

public class Base64GarbleUtil {
    public static String encode(String ori) {
        String encode = Base64Util.encode(ori);
        String swapped = StringUtil.swapFirstAndEnd(encode);
        return replaceChar(swapped, getReplacePair());
    }

    public static String decode(String encoded) {
        Map<String, String> pairMap = new HashMap<>();
        getReplacePair().forEach((ori, repl) -> pairMap.put(repl, ori));
        String reverseReplaced = replaceChar(encoded, pairMap);
        String reverseSwapped = StringUtil.swapFirstAndEnd(reverseReplaced);
        return Base64Util.decode(reverseSwapped);
    }

    public static String replaceChar(String src, Map<String, String> pairMap) {
        for (Map.Entry<String, String> entry : pairMap.entrySet()) {
            src = src.replace(entry.getKey(), entry.getValue());
        }
        return src;
    }

    public static Map<String, String> getReplacePair() {
        Map<String, String> pairMap = new HashMap<>();
        pairMap.put("0", "[0");
        pairMap.put("E", "%");
        return pairMap;
    }

    public static void main(String[] args) {
        String src = "{\"title\":\"title\",\"body\":\"这是一条消息这是一条消息这是一条消息\",\"srcPack\":\"cn.soulapp.android\",\"time\":\"2021-01-19 17:37:09\"}";
        System.out.println(encode(src));
    }
}
