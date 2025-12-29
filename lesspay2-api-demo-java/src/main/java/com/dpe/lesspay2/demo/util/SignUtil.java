package com.dpe.lesspay2.demo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Lesspay2 Signature Utility Class
 * 
 * Signature Algorithm (SHA256):
 * 1. Collect all non-empty parameters
 * 2. Sort by ASCII dictionary order
 * 3. Concatenate as key1=value1&key2=value2 format
 * 4. Append &key=YOUR_SECRET at the end
 * 5. Calculate SHA256 hash and convert to uppercase
 */
public final class SignUtil {

    private static final Logger logger = LoggerFactory.getLogger(SignUtil.class);

    /**
     * Private constructor to prevent instantiation
     */
    private SignUtil() {
        // Utility class, prevent instantiation
    }

    /**
     * Create signature for request parameters
     *
     * @param parameters Request parameters as JSONObject
     * @param key        Merchant AppSecret
     * @return Signature string (uppercase hexadecimal)
     */
    public static String createSign(JSONObject parameters, String key) {
        if (parameters == null || parameters.isEmpty()) {
            logger.info("createSign: parameters isEmpty");
            String sbkey = "key=" + key;
            String sign = sha256(sbkey).toUpperCase();
            logger.info("End of encryption (sha256)sign: {}", sign);
            return sign;
        }
        replaceEmptyWithNull(parameters);
        SortedMap<String, Object> sortedMap = JSON.parseObject(
                JSON.toJSONString(parameters),
                new TypeReference<TreeMap<String, Object>>() {
                });
        String qString = queryString(sortedMap);
        logger.info("createSign: {}", qString);
        String sbkey = qString + "&key=" + key;
        String sign = sha256(sbkey).toUpperCase();
        logger.info("End of encryption (sha256)sign: {}", sign);
        return sign;
    }

    private static void replaceEmptyWithNull(JSONObject parameters) {
        parameters.replaceAll((k, v) -> {
            if ("".equals(v)) {
                return null;
            }
            if (v instanceof JSONObject jsonObj) {
                replaceEmptyWithNull(jsonObj);
            }
            return v;
        });
    }

    private static String sha256(String source) {
        byte[] bt = source.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bt);
            return bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            logger.error("SHA256 encryption exception", e);
            throw new IllegalStateException("SHA256 algorithm not available", e);
        }
    }

    private static String bytes2Hex(byte[] bts) {
        StringBuilder target = new StringBuilder();
        for (byte bt : bts) {
            String tmp = Integer.toHexString(bt & 0xFF);
            if (tmp.length() == 1) {
                target.append('0');
            }
            target.append(tmp);
        }
        return target.toString();
    }

    private static String queryString(SortedMap<String, Object> jObj) {
        StringBuilder qString = new StringBuilder();
        if (jObj != null) {
            for (String key : jObj.keySet()) {
                Object value = jObj.get(key);
                if (value instanceof JSONObject jsonObj) {
                    String jsonStr = queryString(JSON.parseObject(
                            JSON.toJSONString(jsonObj),
                            new TypeReference<TreeMap<String, Object>>() {
                            }));
                    qString.append(key).append("=").append("{").append(jsonStr).append("}").append("&");
                } else {
                    qString.append(key).append("=").append(value).append("&");
                }
            }
            if (!qString.isEmpty()) {
                qString.setLength(qString.length() - 1);
            }
        }
        return qString.toString();
    }
}
