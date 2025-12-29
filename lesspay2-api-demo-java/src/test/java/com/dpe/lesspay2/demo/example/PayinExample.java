package com.dpe.lesspay2.demo.example;

import com.alibaba.fastjson.JSON;
import com.dpe.lesspay2.demo.dto.CreatePayinOrderDTO;
import com.dpe.lesspay2.demo.util.SignUtil;
import okhttp3.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Payin API Call Example
 * 
 * Demonstrates how to call the /api/global/v1/pay/create-order endpoint
 * 
 * Usage:
 * 1. Modify the APP_ID, APP_SECRET, BASE_URL configuration below
 * 2. Run the testPayinDemo() test method
 */
public class PayinExample {

    // ======== Configuration - Please modify to real values ========
    private static final String APP_ID = "YOUR_APP_ID";
    private static final String APP_SECRET = "YOUR_APP_SECRET";
    private static final String BASE_URL = "https://lesspay2-pay-uat.doopayment.com";
    // ===============================================================

    private static final String PAYIN_API_PATH = "/api/global/v1/pay/create-order";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * Payin API call test
     */
    @Test
    public void testPayinDemo() throws IOException {
        System.out.println("========== Payin Demo Start ==========");

        // 1. Build request parameters
        CreatePayinOrderDTO dto = buildPayinRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Origin Param: " + jsonBody);

        // 2. Generate signature
        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Signature: " + signature);

        // 3. Send request
        String url = BASE_URL + PAYIN_API_PATH;
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

        System.out.println("\n========== Payin Demo End ==========");
    }

    /**
     * Test signature generation only (without sending request)
     */
    @Test
    public void testSignatureOnly() {
        System.out.println("========== Signature Test ==========");

        CreatePayinOrderDTO dto = buildPayinRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);
    }

    /**
     * Build Payin request parameters
     */
    private CreatePayinOrderDTO buildPayinRequest() {
        CreatePayinOrderDTO dto = new CreatePayinOrderDTO();

        // ========== Required Fields ==========

        // Merchant order ID (ensure uniqueness, max 20 chars)
        dto.setRequestId("MCH" + System.currentTimeMillis());

        // Product info
        dto.setProductName("Test Product");
        dto.setDescription("This is a test order for demo");

        // Amount and currency
        dto.setTargetAmount("100.00");
        dto.setTargetCurrency("HKD");

        // Transaction type (fixed as PAY_IN)
        dto.setTransactionType("PAY_IN");

        // Redirect URLs
        dto.setSuccessUrl("https://www.example.com/success");
        dto.setFailUrl("https://www.example.com/fail");

        // API version (F11 merchants use V1, others use V2)
        dto.setApiVersion("V2");

        // Payment access mode (1=cashier, 2=openapi)
        dto.setPayAccessType(1);

        // ========== Optional Fields ==========

        // Async callback notification URL
        dto.setNotifyUrl("https://www.example.com/notify");

        // Order expiration time (unit: seconds), e.g., 30 minutes
        dto.setExpiredTime(1800);

        // Payment method type: WECHAT, CRYPTO, BANK_TRANSFER, CARD_PAYMENT
        // dto.setWayType("CARD_PAYMENT");

        // Payment method code (fill according to actual payment method)
        // dto.setWayCode("WX_H5");

        // Transaction network (required for some payment methods)
        // dto.setTransactionNetwork("TRC20");

        // Whether to use request_id as channel order number
        // dto.setUseChannelRequestId(false);

        return dto;
    }
}
