package com.dpe.lesspay2.demo.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dpe.lesspay2.demo.config.ApiConfig;
import com.dpe.lesspay2.demo.util.SignUtil;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Lesspay2 API Client
 * 
 * Encapsulates HTTP requests and automatically handles:
 * - Signature calculation
 * - Header setting (x-auth-appid, X-Auth-Timestamp, x-auth-signature)
 * - Request/Response logging
 */
@Component
public class LesspayApiClient {

    private static final Logger logger = LoggerFactory.getLogger(LesspayApiClient.class);
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private final ApiConfig apiConfig;
    private final OkHttpClient httpClient;

    public LesspayApiClient(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Send POST request
     *
     * @param path API endpoint path (e.g. /api/global/v1/pay/create-order)
     * @param body Request body object
     * @return Response JSON string
     */
    public String post(String path, Object body) throws IOException {
        String url = apiConfig.getBaseUrl() + path;
        String jsonBody = JSON.toJSONString(body);
        long timestamp = System.currentTimeMillis();

        // Calculate signature
        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), apiConfig.getAppSecret());

        // Build request
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(jsonBody, JSON_MEDIA_TYPE))
                .addHeader("Content-Type", "application/json")
                .addHeader("x-auth-appid", apiConfig.getAppId())
                .addHeader("X-Auth-Timestamp", String.valueOf(timestamp))
                .addHeader("x-auth-signature", signature)
                .build();

        logger.info("=== HTTP Request ===");
        logger.info("URL: {}", url);
        logger.info("Headers: x-auth-appid={}, X-Auth-Timestamp={}", apiConfig.getAppId(), timestamp);
        logger.info("Signature: {}", signature);
        logger.info("Body: {}", jsonBody);

        // Send request
        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";

            logger.info("=== HTTP Response ===");
            logger.info("Status: {}", response.code());
            logger.info("Body: {}", responseBody);

            return responseBody;
        }
    }

    /**
     * Generate signature only (without sending request)
     * Used for verifying signature correctness
     *
     * @param body Request body object
     * @return Signature string
     */
    public String generateSignature(Object body) {
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(body));
        return SignUtil.createSign(jsonObject, apiConfig.getAppSecret());
    }
}
