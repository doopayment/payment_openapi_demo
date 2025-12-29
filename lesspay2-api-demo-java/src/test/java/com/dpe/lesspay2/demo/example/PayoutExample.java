package com.dpe.lesspay2.demo.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dpe.lesspay2.demo.dto.CreatePayoutOrderDTO;
import com.dpe.lesspay2.demo.dto.CreatePayoutOrderDTO.PayoutOrderDetailDTO;
import com.dpe.lesspay2.demo.util.SignUtil;
import okhttp3.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Payout API Call Example
 * 
 * Demonstrates how to call the /api/global/payout/batch/create-order endpoint
 * 
 * Usage:
 * 1. Modify the APP_ID, APP_SECRET, BASE_URL configuration below
 * 2. Run the testPayoutDemo() test method
 */
public class PayoutExample {

    // ======== Configuration - Please modify to real values ========
    private static final String APP_ID = "YOUR_APP_ID";
    private static final String APP_SECRET = "YOUR_APP_SECRET";
    private static final String BASE_URL = "https://lesspay2-pay-uat.doopayment.com";
    // ===============================================================

    private static final String PAYOUT_API_PATH = "/api/global/payout/batch/create-order";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * Payout API call test
     */
    @Test
    public void testPayoutDemo() throws IOException {
        System.out.println("========== Payout Demo Start ==========");

        // 1. Build request parameters
        CreatePayoutOrderDTO dto = buildPayoutRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        // 2. Generate signature
        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);

        // 3. Send request
        String url = BASE_URL + PAYOUT_API_PATH;
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

        System.out.println("\n========== Payout Demo End ==========");
    }

    /**
     * Test signature generation only (without sending request)
     */
    @Test
    public void testSignatureOnly() {
        System.out.println("========== Signature Test ==========");

        CreatePayoutOrderDTO dto = buildPayoutRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);
    }

    /**
     * Build Payout request parameters
     * Reference: Create_test_data.py
     */
    private CreatePayoutOrderDTO buildPayoutRequest() {
        CreatePayoutOrderDTO dto = new CreatePayoutOrderDTO();

        // Generate unique request_id using timestamp + random number
        String requestId = "PO" + System.currentTimeMillis() + randomInt(1000, 9999);
        dto.setRequestId(requestId);

        // Currency - PHP for Philippines
        dto.setCurrency("PHP");

        // Product info
        dto.setProductName("payout product");
        dto.setDescription("payout product");

        // Async callback notification URL
        dto.setNotifyUrl("https://www.example.com/payout-notify");

        dto.setWayCode("TAZAPAY_PAYOUT");
        // Payment Way (optional)
        // dto.setWayType("");

        // ========== Build Payout Detail List ==========
        List<PayoutOrderDetailDTO> details = new ArrayList<>();

        // Random amounts between 100 and 500
        int amount1 = randomInt(100, 500);
        int amount2 = randomInt(100, 500);

        // Detail 1
        PayoutOrderDetailDTO detail1 = new PayoutOrderDetailDTO();
        detail1.setMchOrderId("POD" + System.currentTimeMillis() + randomInt(100000, 999999));
        detail1.setAmount(String.valueOf(amount1));
        detail1.setBankAccountName("account_name_" + randomInt(1000, 9999));
        detail1.setBankAccountNo(randomBankAccountNo());
        detail1.setBankAccountType("business");
        detail1.setBankCountryCode("PH");
        detail1.setBankName("Hong Kong and Shanghai Banking Corp.");
        detail1.setBankSwiftCode("HSBCPH22");
        details.add(detail1);

        // Detail 2
        PayoutOrderDetailDTO detail2 = new PayoutOrderDetailDTO();
        detail2.setMchOrderId("POD" + System.currentTimeMillis() + randomInt(100000, 999999));
        detail2.setAmount(String.valueOf(amount2));
        detail2.setBankAccountName("account_name_" + randomInt(1000, 9999));
        detail2.setBankAccountNo(randomBankAccountNo());
        detail2.setBankAccountType("business");
        detail2.setBankCountryCode("PH");
        detail2.setBankName("Hong Kong and Shanghai Banking Corp.");
        detail2.setBankSwiftCode("HSBCPH22");
        details.add(detail2);

        // Total amount = sum of detail amounts
        dto.setTotalAmount(String.valueOf(amount1 + amount2));
        dto.setPayoutOrderDetails(details);

        return dto;
    }

    /**
     * Generate random integer between min and max (inclusive)
     */
    private int randomInt(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }

    /**
     * Generate random 12-digit bank account number with prefix 138
     */
    private String randomBankAccountNo() {
        StringBuilder sb = new StringBuilder("138");
        for (int i = 0; i < 9; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }
}
