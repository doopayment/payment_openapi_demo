package com.dpe.lesspay2.demo.example;

import com.alibaba.fastjson.JSON;
import com.dpe.lesspay2.demo.dto.PayinQueryDTO;
import com.dpe.lesspay2.demo.util.SignUtil;
import okhttp3.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Fetch Payin API Call Example
 * 
 * Demonstrates how to call the /api/global/v1/pay/query-order endpoint
 * 
 * Usage:
 * 1. Modify the APP_ID, APP_SECRET, BASE_URL configuration below
 * 2. Set the REQUEST_ID or PAY_ORDER_ID to query
 * 3. Run the test methods
 */
public class PayinQueryExample {

    // ======== Configuration - Please modify to real values ========
    private static final String APP_ID = "YOUR_APP_ID";
    private static final String APP_SECRET = "YOUR_APP_SECRET";
    private static final String BASE_URL = "https://lesspay2-pay-uat.doopayment.com";
    // ===============================================================

    // Query parameters - fill in the order ID to query
    private static final String REQUEST_ID = "MCH1766992517141"; // Merchant request ID
    private static final String PAY_ORDER_ID = ""; // Or use platform order ID

    private static final String PAYIN_QUERY_API_PATH = "/api/global/v1/pay/query-order";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * Fetch Payin API call test - Query by request_id
     */
    @Test
    public void testPayinQueryByRequestId() throws IOException {
        System.out.println("========== Payin Query Demo (by request_id) Start ==========");

        PayinQueryDTO dto = buildQueryRequest();
        dto.setRequestId(REQUEST_ID);

        executeQuery(dto);

        System.out.println("\n========== Payin Query Demo End ==========");
    }

    /**
     * Fetch Payin API call test - Query by pay_order_id
     */
    @Test
    public void testPayinQueryByPayOrderId() throws IOException {
        System.out.println("========== Payin Query Demo (by pay_order_id) Start ==========");

        PayinQueryDTO dto = buildQueryRequest();
        dto.setPayOrderId(PAY_ORDER_ID);

        executeQuery(dto);

        System.out.println("\n========== Payin Query Demo End ==========");
    }

    /**
     * Fetch Payin API call test - Query by time range
     */
    @Test
    public void testPayinQueryByTimeRange() throws IOException {
        System.out.println("========== Payin Query Demo (by time range) Start ==========");

        PayinQueryDTO dto = buildQueryRequest();
        // Query without specific order ID, just time range

        executeQuery(dto);

        System.out.println("\n========== Payin Query Demo End ==========");
    }

    /**
     * Build base query request with time range
     */
    private PayinQueryDTO buildQueryRequest() {
        PayinQueryDTO dto = new PayinQueryDTO();

        // Time range (last 24 hours)
        long now = System.currentTimeMillis();
        dto.setStartTime(now - 24 * 60 * 60 * 1000L); // 24 hours ago
        dto.setEndTime(now);

        // Pagination
        dto.setPage(1);
        dto.setPageSize(20);

        return dto;
    }

    /**
     * Execute query request
     */
    private void executeQuery(PayinQueryDTO dto) throws IOException {
        // 1. Build request
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        // 2. Generate signature
        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);

        // 3. Send request
        String url = BASE_URL + PAYIN_QUERY_API_PATH;
        long timestamp = System.currentTimeMillis();

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(jsonBody, JSON_MEDIA_TYPE))
                .addHeader("Content-Type", "application/json")
                .addHeader("x-auth-appid", APP_ID)
                .addHeader("X-Auth-Timestamp", String.valueOf(timestamp))
                .addHeader("x-auth-signature", signature)
                .build();

        System.out.println("\n=== HTTP Request ===");
        System.out.println("URL: " + url);
        System.out.println("Headers: x-auth-appid=" + APP_ID + ", X-Auth-Timestamp=" + timestamp);
        System.out.println("Signature: " + signature);

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            System.out.println("\n=== HTTP Response ===");
            System.out.println("Status: " + response.code());
            System.out.println("Body: " + responseBody);
        }
    }

    /**
     * Test signature generation only (without sending request)
     */
    @Test
    public void testSignatureOnly() {
        System.out.println("========== Signature Test ==========");

        PayinQueryDTO dto = buildQueryRequest();
        dto.setRequestId(REQUEST_ID);

        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);
    }
}
