package com.dpe.lesspay2.demo.example;

import com.alibaba.fastjson.JSON;
import com.dpe.lesspay2.demo.dto.PayoutQueryDTO;
import com.dpe.lesspay2.demo.util.SignUtil;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Fetch Payout API Call Example
 * 
 * Demonstrates how to call the /api/global/payout/query endpoint
 * 
 * Usage:
 * 1. Modify the APP_ID, APP_SECRET, BASE_URL configuration below
 * 2. Set the REQUEST_ID or PAY_ORDER_ID to query
 * 3. Run the testPayoutQueryDemo() test method
 */
public class PayoutQueryExample {

    // ======== Configuration - Please modify to real values ========
    private static final String APP_ID = "YOUR_APP_ID";
    private static final String APP_SECRET = "YOUR_APP_SECRET";
    private static final String BASE_URL = "https://lesspay2-pay-uat.doopayment.com";
    // ===============================================================

    // Query parameters - fill in the order ID to query
    private static final String REQUEST_ID = ""; // Merchant request ID
    private static final String PAY_ORDER_ID = "PO2005543497457831938"; // Or use platform order ID

    private static final String PAYOUT_QUERY_API_PATH = "/api/global/payout/query";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * Fetch Payout API call test - Query by pay_order_id
     */
    @Test
    public void testPayoutQueryByPayOrderId() throws IOException {
        System.out.println("========== Payout Query Demo (by pay_order_id) Start ==========");

        if (StringUtils.isBlank(PAY_ORDER_ID)) {
            System.out.println("PAY_ORDER_ID is empty. Please set PAY_ORDER_ID to query by pay_order_id.");
            return;
        }

        PayoutQueryDTO dto = new PayoutQueryDTO();
        dto.setPayOrderId(PAY_ORDER_ID);

        executeQuery(dto);

        System.out.println("\n========== Payout Query Demo End ==========");
    }

    /**
     * Execute query request
     */
    private void executeQuery(PayoutQueryDTO dto) throws IOException {
        // 1. Build request
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        // 2. Generate signature
        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);

        // 3. Send request
        String url = BASE_URL + PAYOUT_QUERY_API_PATH;
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
     * Fetch Payout API call test - Query by request_id
     */
    @Test
    public void testPayoutQueryByRequestId() throws IOException {
        System.out.println("========== Payout Query Demo (by request_id) Start ==========");

        if (StringUtils.isBlank(REQUEST_ID)) {
            System.out.println("REQUEST_ID is empty. Please set REQUEST_ID to query by request_id.");
            return;
        }

        PayoutQueryDTO dto = new PayoutQueryDTO();
        dto.setRequestId(REQUEST_ID);

        executeQuery(dto);

        System.out.println("\n========== Payout Query Demo End ==========");
    }

    /**
     * Test signature generation only (without sending request)
     */
    @Test
    public void testSignatureOnly() {
        System.out.println("========== Signature Test ==========");

        PayoutQueryDTO dto = new PayoutQueryDTO();
        dto.setRequestId(REQUEST_ID);

        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);
    }
}
