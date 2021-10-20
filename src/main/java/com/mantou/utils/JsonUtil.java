package com.mantou.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mantou
 * @date 2021/10/20 13:51
 * @desc
 */
@UtilityClass
@Slf4j
public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T parse(String jsonStr, TypeReference<T> ref) {
        if (null == jsonStr) {
            return null;
        }
        try {
            return mapper.readValue(jsonStr, ref);
        } catch (Exception e) {
            log.error("读取json异常", e);
        }
        return null;
    }

    public static JsonNode read(String jsonStr) {
        if (null == jsonStr) {
            return null;
        }
        if (!isJson(jsonStr)) {
            return new TextNode(jsonStr);
        }
        try {
            return mapper.readTree(jsonStr);
        } catch (JsonProcessingException e) {
            log.error("读取json异常2", e);
        }
        return null;
    }

    private static boolean isJson(CharSequence str) {
        if (null == str) {
            return false;
        }
        return (str.charAt(0) == '{' && str.charAt(str.length() - 1) == '}')
                || (str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']');
    }

    public static String toStr(Object obj) {
        if (null == obj) {
            return null;
        }
        if (obj instanceof JsonNode) {
            JsonNode node = (JsonNode) obj;
            if (!node.isObject() && !node.isArray()) {
                return node.asText();
            }
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("json转换异常", e);
        }
        return null;
    }

    public static JsonNode beanToNode(Object obj) {
        if (null == obj) {
            return null;
        }
        return read(toStr(obj));
    }

    public static void main(String[] args) {
        JsonNode x = new TextNode(String.valueOf("fwfwefewf"));
        System.out.println(x.asText());
    }
}
