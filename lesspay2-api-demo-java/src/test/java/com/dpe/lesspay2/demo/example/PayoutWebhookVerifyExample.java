package com.dpe.lesspay2.demo.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dpe.lesspay2.demo.util.SignUtil;
import okhttp3.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Payout Webhook Signature Verification Demo
 *
 * Demonstrates how to manually trigger merchant callback via triggerNotify API and verify platform signature
 *
 * Webhook Notification Flow:
 * 1. Merchant calls /api/global/payout/triggerNotify to manually trigger callback
 * 2. Platform returns callback content (PayoutQueryVO) and asynchronously sends notification to notifyUrl
 * 3. Async notification headers include: Content-Type: application/json, X-Auth-Timestamp, X-Auth-Signature
 * 4. Merchant can verify signature using APP_SECRET to confirm request authenticity
 * 5. Return "SUCCESS" or {"code": 0} after signature verification passes
 *
 * Usage:
 * 1. Modify APP_ID, APP_SECRET, BASE_URL below with real values
 * 2. Run testTriggerNotifyAndVerify() test method
 */
public class PayoutWebhookVerifyExample {

    // ======== Configuration - Please modify with real values ========
    private static final String APP_ID = "YOUR_APP_ID";
    private static final String APP_SECRET = "YOUR_APP_SECRET";
    private static final String BASE_URL = "https://lesspay2-pay-uat.doopayment.com";
    // Order ID (for triggering callback)
    private static final String PAY_ORDER_ID = "YOUR_PAY_ORDER_ID";  // or use request_id
    private static final String REQUEST_ID = "";  // Fill this if using request_id to query
    // ================================================================
    // Manual trigger callback
    private static final String TRIGGER_NOTIFY_API_PATH = "/api/global/payout/triggerNotify";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * Test complete flow: call triggerNotify -> get callback content -> verify signature
     */
    /**
     * Test complete flow: call triggerNotify -> get callback content -> verify signature
     */
    @Test
    public void testTriggerNotifyAndVerify() throws IOException {
        System.out.println("========== Payout Webhook Verification Demo (triggerNotify) ==========\n");

        if ("YOUR_APP_ID".equals(APP_ID) || "YOUR_APP_SECRET".equals(APP_SECRET)) {
            System.err.println("⚠️ Please configure APP_ID and APP_SECRET in the code first!");
            return;
        }

        // 1. Build triggerNotify request parameters
        JSONObject requestBody = new JSONObject();
        if (PAY_ORDER_ID != null && !PAY_ORDER_ID.isEmpty() && !"YOUR_PAY_ORDER_ID".equals(PAY_ORDER_ID)) {
            requestBody.put("pay_order_id", PAY_ORDER_ID);
        }
        if (REQUEST_ID != null && !REQUEST_ID.isEmpty()) {
            requestBody.put("request_id", REQUEST_ID);
        }

        String jsonBody = requestBody.toJSONString();
        System.out.println("=== Step 1: Build triggerNotify Request ===");
        System.out.println("Request Body: " + jsonBody);

        // 2. Generate request signature
        String requestSignature = SignUtil.createSign(requestBody, APP_SECRET);
        long timestamp = System.currentTimeMillis();
        System.out.println("Request Signature: " + requestSignature);
        System.out.println("Timestamp: " + timestamp);

        // 3. Send triggerNotify request
        String url = BASE_URL + TRIGGER_NOTIFY_API_PATH;
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(jsonBody, JSON_MEDIA_TYPE))
                .addHeader("Content-Type", "application/json")
                .addHeader("x-auth-appid", APP_ID)
                .addHeader("X-Auth-Timestamp", String.valueOf(timestamp))
                .addHeader("x-auth-signature", requestSignature)
                .build();

        System.out.println("\n=== Step 2: Call triggerNotify API ===");
        System.out.println("URL: " + url);

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            System.out.println("HTTP Status: " + response.code());
            System.out.println("Response Body: " + responseBody);

            if (!response.isSuccessful()) {
                System.out.println("\n✗ Request failed, please check configuration");
                return;
            }

            // 4. Parse response, get callback content
            JSONObject apiRes = JSON.parseObject(responseBody);
            if (apiRes.getIntValue("code") != 0) {
                System.out.println("\n✗ API returned error: " + apiRes.getString("msg"));
                return;
            }

            JSONObject notifyBody = apiRes.getJSONObject("data");
            System.out.println("\n=== Step 3: Get Callback Content (PayoutQueryVO) ===");
            System.out.println("Notify Body: " + JSON.toJSONString(notifyBody, true));

            // 5. Simulate merchant-side signature verification
            System.out.println("\n=== Step 4: Simulate Merchant Signature Verification ===");
            String calculatedSign = SignUtil.createSign(notifyBody, APP_SECRET);
            System.out.println("Merchant Calculated Signature: " + calculatedSign);
            System.out.println("(In real scenario, compare with X-Auth-Signature header)");

            // 6. Display callback content details (PayoutQueryVO fields)
            System.out.println("\n=== Step 5: PayoutQueryVO Callback Content Details ===");
            System.out.println("Platform Main Order ID (pay_order_id): " + notifyBody.getString("pay_order_id"));
            System.out.println("Merchant Request ID (request_id): " + notifyBody.getString("request_id"));
            System.out.println("Order Status (order_status): " + notifyBody.getString("order_status"));
            System.out.println("Currency (currency): " + notifyBody.getString("currency"));
            System.out.println("Total Amount (total_amount): " + notifyBody.getString("total_amount"));
            System.out.println("Decimal Places (decimal_places): " + notifyBody.getInteger("decimal_places"));
            System.out.println("Failure Reason (fail_reason): " + notifyBody.getString("fail_reason"));
            System.out.println("Creation Time (created_at): " + notifyBody.get("created_at"));

            // 7. Display detail list (PayoutDetailVO fields)
            JSONArray details = notifyBody.getJSONArray("details");
            if (details != null && !details.isEmpty()) {
                System.out.println("\n=== Step 6: PayoutDetailVO Detail List ===");
                for (int i = 0; i < details.size(); i++) {
                    JSONObject detail = details.getJSONObject(i);
                    System.out.println("\n--- Detail #" + (i + 1) + " ---");
                    System.out.println("  Payout Order Detail ID (payout_order_detail_id): " + detail.getString("payout_order_detail_id"));
                    System.out.println("  Merchant Order ID (mch_order_id): " + detail.getString("mch_order_id"));
                    System.out.println("  Merchant Request Amount (amount): " + detail.getString("amount"));
                    System.out.println("  Actual Paid Amount (actual_amount): " + detail.getString("actual_amount"));
                    System.out.println("  Detail Status (status): " + detail.getString("status"));
                    System.out.println("  Audit State (audit_state): " + detail.getString("audit_state"));
                    System.out.println("  Failure Reason (fail_reason): " + detail.getString("fail_reason"));
                    System.out.println("  Rejected Reason (rejected_reason): " + detail.getString("rejected_reason"));
                    System.out.println("  Channel Order No (channel_order_no): " + detail.getString("channel_order_no"));
                    System.out.println("  Beneficiary Account No (bank_account_no): " + detail.getString("bank_account_no"));
                    System.out.println("  Beneficiary Name (bank_account_name): " + detail.getString("bank_account_name"));
                    System.out.println("  Account Holder Type (bank_account_type): " + detail.getString("bank_account_type"));
                    System.out.println("  Bank Name (bank_name): " + detail.getString("bank_name"));
                    System.out.println("  Bank Country Code (bank_country_code): " + detail.getString("bank_country_code"));
                    System.out.println("  Bank SWIFT Code (bank_swift_code): " + detail.getString("bank_swift_code"));
                    System.out.println("  Success Time (success_time): " + detail.get("success_time"));
                }
            }
        }

        System.out.println("\n========== Demo End ==========");
    }

    /**
     * Test signature generation consistency
     * 
     * Sample data contains complete PayoutQueryVO field structure
     */
    @Test
    public void testSignatureConsistency() {
        System.out.println("========== Signature Consistency Test ==========\n");

        // Sample callback content (PayoutQueryVO format)
        // Note: Date fields (created_at, success_time) are serialized as ISO 8601 format by fastjson
        String sampleBody = """
                {
                    "pay_order_id": "P202501130001",
                    "request_id": "PO1736740800001",
                    "order_status": "SUCCEED",
                    "currency": "PHP",
                    "total_amount": "1000.00",
                    "decimal_places": 2,
                    "fail_reason": null,
                    "created_at": "2025-01-13T10:00:00.000+08:00",
                    "details": [
                        {
                            "payout_order_detail_id": "POD202501130001",
                            "mch_order_id": "MCH_001",
                            "amount": "500.00",
                            "actual_amount": "500.00",
                            "status": "Succeeded",
                            "audit_state": "APPROVED",
                            "fail_reason": null,
                            "rejected_reason": null,
                            "channel_order_no": "CH_001",
                            "bank_account_no": "1234****5678",
                            "bank_account_name": "J****N",
                            "bank_account_type": "business",
                            "bank_name": "BDO",
                            "bank_country_code": "PH",
                            "bank_swift_code": "BNORPHMM",
                            "success_time": "2025-01-13T10:01:40.000+08:00"
                        },
                        {
                            "payout_order_detail_id": "POD202501130002",
                            "mch_order_id": "MCH_002",
                            "amount": "500.00",
                            "actual_amount": "500.00",
                            "status": "Succeeded",
                            "audit_state": "APPROVED",
                            "fail_reason": null,
                            "rejected_reason": null,
                            "channel_order_no": "CH_002",
                            "bank_account_no": "8765****4321",
                            "bank_account_name": "M****Y",
                            "bank_account_type": "individual",
                            "bank_name": "BPI",
                            "bank_country_code": "PH",
                            "bank_swift_code": "BPIIPHMM",
                            "success_time": "2025-01-13T10:01:40.000+08:00"
                        }
                    ]
                }
                """;

        JSONObject params = JSON.parseObject(sampleBody);

        // Generate signature multiple times to verify consistency
        String sign1 = SignUtil.createSign(params, APP_SECRET);
        String sign2 = SignUtil.createSign(JSON.parseObject(sampleBody), APP_SECRET);

        System.out.println("Sign 1: " + sign1);
        System.out.println("Sign 2: " + sign2);
        System.out.println("Signatures Match: " + sign1.equals(sign2));
    }

    /**
     * Test signature verification failure scenario (tampered data)
     */
    @Test
    public void testVerifyWithTamperedData() {
        System.out.println("========== Signature Verification Failure Test ==========\n");

        String originalBody = """
                {
                    "pay_order_id": "P202501130001",
                    "request_id": "PO1736740800001",
                    "order_status": "SUCCEED",
                    "currency": "PHP",
                    "total_amount": "1000.00",
                    "decimal_places": 2,
                    "created_at": "2025-01-13T10:00:00.000+08:00",
                    "details": []
                }
                """;

        // Original signature
        JSONObject originalJson = JSON.parseObject(originalBody);
        String originalSign = SignUtil.createSign(originalJson, APP_SECRET);
        System.out.println("Original Signature: " + originalSign);

        // Tamper data (modify amount)
        JSONObject tamperedJson = originalJson.clone();
        tamperedJson.put("total_amount", "9999.00");
        String tamperedSign = SignUtil.createSign(tamperedJson, APP_SECRET);
        System.out.println("Tampered Signature: " + tamperedSign);

        // Verification result
        boolean verifyResult = originalSign.equals(tamperedSign);
        System.out.println("Verification Result: " + (verifyResult ? "✓ Passed (should not happen)" : "✗ Failed (expected, data was tampered)"));
    }

    /**
     * Merchant Webhook Receiver Pseudo Code Example
     *
     * PayoutQueryVO Field Description:
     * - pay_order_id: Platform Main Order ID
     * - request_id: Merchant Request ID
     * - order_status: Order Status (PENDING_CONFIRM, PENDING_PAY, SUCCEED, FAILED, CANCELED, REFUND, CLOSED, PARTIAL_SUCCESS)
     * - currency: Currency
     * - total_amount: Merchant Request Total Amount
     * - decimal_places: Currency Decimal Places (Monetary precision)
     * - fail_reason: Main Order Failure Reason (Only when failed or partial success)
     * - created_at: Creation Time
     * - details: Detail List (PayoutDetailVO[])
     *
     * PayoutDetailVO Field Description:
     * - payout_order_detail_id: Payout Order Detail No
     * - mch_order_id: Merchant Order ID, Detail Serial Number
     * - amount: Merchant Request Amount
     * - actual_amount: Actual Paid Amount
     * - status: Detail Status (CREATED, PROCESSING, SUCCEEDED, FAILED, CANCELED, REFUNDED, CLOSED)
     * - audit_state: Audit State (PENDING, APPROVED, REJECTED)
     * - fail_reason: Failure Reason (Only when failed)
     * - rejected_reason: Rejected Reason
     * - channel_order_no: Channel Order No
     * - bank_account_no: Beneficiary Account No (Masked: 1234****5678)
     * - bank_account_name: Beneficiary Name (Masked: G******H)
     * - bank_account_type: Account Holder Type (business/individual)
     * - bank_name: Bank Name
     * - bank_country_code: Bank Country Code (ISO 3166-1 alpha-2, e.g., PH, IN, ID)
     * - bank_swift_code: Bank SWIFT Network Code (8 or 11 characters)
     * - success_time: Payout Order Detail Success Time
     *
     * <pre>
     * {@code
     * @PostMapping("/payout-notify")
     * public String handlePayoutWebhook(
     *         @RequestHeader("X-Auth-Signature") String signature,
     *         @RequestHeader("X-Auth-Timestamp") String timestamp,
     *         @RequestBody String body) {
     *
     *     // 1. Verify signature (X-Auth-Signature generated by platform using merchant APP_SECRET)
     *     String calculatedSign = SignUtil.createSign(JSON.parseObject(body), APP_SECRET);
     *     if (!calculatedSign.equals(signature)) {
     *         log.error("Webhook signature verification failed");
     *         return "FAIL";
     *     }
     *
     *     // 2. Parse notification content (PayoutQueryVO)
     *     JSONObject notification = JSON.parseObject(body);
     *     String orderId = notification.getString("pay_order_id");
     *     String requestId = notification.getString("request_id");
     *     String status = notification.getString("order_status");
     *
     *     // 3. Business processing (update local order based on status)
     *     switch (status) {
     *         case "SUCCEED":
     *             // Handle success logic
     *             break;
     *         case "FAILED":
     *             // Handle failure logic
     *             String failReason = notification.getString("fail_reason");
     *             break;
     *         case "PARTIAL_SUCCESS":
     *             // Handle partial success logic, iterate details to check each detail status
     *             JSONArray details = notification.getJSONArray("details");
     *             for (int i = 0; i < details.size(); i++) {
     *                 JSONObject detail = details.getJSONObject(i);
     *                 String detailStatus = detail.getString("status");
     *                 // Process each detail...
     *             }
     *             break;
     *         default:
     *             log.warn("Unknown status: {}", status);
     *     }
     *
     *     // 4. Return success
     *     return "SUCCESS";
     * }
     * }
     * </pre>
     */
    @Test
    public void showWebhookControllerExample() {
        System.out.println("========== Webhook Controller Example Code ==========");
        System.out.println("Please see the Javadoc comments in showWebhookControllerExample() method");
        System.out.println("The comments contain complete Spring Boot Controller pseudo code for receiving Webhook");
        System.out.println("\nKey Points:");
        System.out.println("1. Get X-Auth-Signature from header (generated by platform using merchant APP_SECRET)");
        System.out.println("2. Merchant calculates signature using same APP_SECRET and compares");
        System.out.println("3. Return SUCCESS or {\"code\": 0} after verification passes");
        System.out.println("\nPayoutQueryVO Main Fields:");
        System.out.println("- pay_order_id: Platform Main Order ID");
        System.out.println("- request_id: Merchant Request ID");
        System.out.println("- order_status: Order Status");
        System.out.println("- currency: Currency");
        System.out.println("- total_amount: Merchant Request Total Amount");
        System.out.println("- decimal_places: Currency Decimal Places");
        System.out.println("- details: Detail List");
    }
}
