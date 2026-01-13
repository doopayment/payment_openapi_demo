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
            
            if (!response.isSuccessful()) {
                System.out.println("Request failed");
                return;
            }

            // 4. Parse response content
            com.alibaba.fastjson.JSONObject apiRes = JSON.parseObject(responseBody);
            if (apiRes.getIntValue("code") != 0) {
                System.out.println("API Error: " + apiRes.getString("msg"));
                return;
            }

            com.alibaba.fastjson.JSONObject data = apiRes.getJSONObject("data");
            System.out.println("\n=== Payout Query Result ===");
            
            // Display main order info
            System.out.println("Platform Main Order ID (pay_order_id): " + data.getString("pay_order_id"));
            System.out.println("Merchant Request ID (request_id): " + data.getString("request_id"));
            System.out.println("Order Status (order_status): " + data.getString("order_status"));
            System.out.println("Currency (currency): " + data.getString("currency"));
            System.out.println("Total Amount (total_amount): " + data.getString("total_amount"));
            System.out.println("Decimal Places (decimal_places): " + data.getString("decimal_places"));
            System.out.println("Failure Reason (fail_reason): " + data.getString("fail_reason"));
            System.out.println("Creation Time (created_at): " + data.getString("created_at"));

            // Display details
            com.alibaba.fastjson.JSONArray details = data.getJSONArray("details");
            if (details != null && !details.isEmpty()) {
                System.out.println("\n=== Detail List (" + details.size() + ") ===");
                for (int i = 0; i < details.size(); i++) {
                    com.alibaba.fastjson.JSONObject detail = details.getJSONObject(i);
                    System.out.println("\n--- Detail #" + (i + 1) + " ---");
                    System.out.println("  Detail ID (payout_order_detail_id): " + detail.getString("payout_order_detail_id"));
                    System.out.println("  Merchant Order ID (mch_order_id): " + detail.getString("mch_order_id"));
                    System.out.println("  Request Amount (amount): " + detail.getString("amount"));
                    System.out.println("  Actual Paid (actual_amount): " + detail.getString("actual_amount"));
                    System.out.println("  Status (status): " + detail.getString("status"));
                    System.out.println("  Audit State (audit_state): " + detail.getString("audit_state"));
                    System.out.println("  Fail Reason (fail_reason): " + detail.getString("fail_reason"));
                    System.out.println("  Reject Reason (rejected_reason): " + detail.getString("rejected_reason"));
                    System.out.println("  Channel Order No (channel_order_no): " + detail.getString("channel_order_no"));
                    System.out.println("  Bank Account No (bank_account_no): " + detail.getString("bank_account_no"));
                    System.out.println("  Account Name (bank_account_name): " + detail.getString("bank_account_name"));
                    System.out.println("  Account Type (bank_account_type): " + detail.getString("bank_account_type"));
                    System.out.println("  Bank Name (bank_name): " + detail.getString("bank_name"));
                    System.out.println("  Country Code (bank_country_code): " + detail.getString("bank_country_code"));
                    System.out.println("  SWIFT Code (bank_swift_code): " + detail.getString("bank_swift_code"));
                    System.out.println("  Success Time (success_time): " + detail.getString("success_time"));
                }
            }
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
