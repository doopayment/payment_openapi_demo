package com.dpe.lesspay2.demo.example.payin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dpe.lesspay2.demo.util.SignUtil;
import org.junit.jupiter.api.Test;

/**
 * Payin webhook signature verification example.
 *
 * Notes:
 * 1. This example does not place an order. It focuses on how a merchant verifies
 *    and parses a payin webhook after receiving it.
 * 2. The sample payload uses the current real payin webhook structure directly,
 *    so the query-order response is not confused with the webhook body.
 * 3. According to PayOrderNotifyServiceImpl#doPost, the webhook body is serialized
 *    from OrderCallbackRequest for both versions, but the signature payload is
 *    version-sensitive: v1 signs the OrderCallbackV1SignRequest field set, while
 *    v2 signs the full OrderCallbackRequest field set.
 * 4. To connect this example to a real merchant environment, replace APP_SECRET
 *    with the real value and adapt the controller pseudo code to your callback endpoint.
 */
public class PayinWebhookVerifyExample {

    // ======== Configuration: replace with the real merchant APP_SECRET ========
    private static final String APP_SECRET = "YOUR_APP_SECRET";
    // ====================================================
    private static final String API_VERSION_V1 = "v1";
    private static final String API_VERSION_V2 = "v2";

    /**
     * Current real payin webhook sample payload.
     *
     * It stays aligned with the live callback fields, including:
     * - Standard payment result fields
     * - channel_biz_data extended channel fields
     * - Reserved event / event_data fields (null for standard payment callbacks)
     */
    private static final String ACTUAL_WEBHOOK_SAMPLE = """
            {
              "pay_order_id": "P2045118164054515713",
              "request_id": "1585581674",
              "order_status": "SUCCEED",
              "target_currency": "USD",
              "target_amount": "100.00",
              "success_url": "https://helios.cashier-dev.com/api/apm/response/LessPay/b3IxQzFjckd4eXp4RU84eVFHbHJxUT09",
              "fail_url": "https://helios.cashier-dev.com/api/apm/response/LessPay/b3IxQzFjckd4eXp4RU84eVFHbHJxUT09",
              "product_name": "deposit",
              "description": "Deposit #1585581674",
              "complete_time": null,
              "error_code": null,
              "error_msg": null,
              "notify_url": null,
              "merchant_reserved_field": null,
              "api_version": "v1",
              "order_status_int": 2,
              "channel_biz_data": {
                "issuer_country": "PL",
                "3ds": {
                  "downgraded": false,
                  "enrolled": "Y"
                },
                "issuer": "CREDIT AGRICOLE BANK POLSKA S.A."
              },
              "event": null,
              "event_data": null
            }
            """;

    /**
     * Real webhook sample reused by the example methods.
     */
    static JSONObject buildActualWebhookSample() {
        return JSON.parseObject(ACTUAL_WEBHOOK_SAMPLE);
    }

    /**
     * Sample webhook body when api_version is v1.
     * The body still keeps the full callback fields, matching OrderCallbackRequest.
     */
    static JSONObject buildV1WebhookSample() {
        JSONObject webhook = buildActualWebhookSample();
        webhook.put("api_version", API_VERSION_V1);
        return webhook;
    }

    /**
     * Sample webhook body when api_version is v2.
     * The body shape is still the same callback object, but the signature uses the full body.
     */
    static JSONObject buildV2WebhookSample() {
        JSONObject webhook = buildActualWebhookSample();
        webhook.put("api_version", API_VERSION_V2);
        return webhook;
    }

    /**
     * Build the payload used for signature verification based on api_version.
     *
     * Source of truth:
     * com.dpe.lesspay2.thirdparty.service.notify.impl.PayOrderNotifyServiceImpl#getSign
     */
    static JSONObject buildSignPayloadByApiVersion(JSONObject webhook) {
        JSONObject signPayload = JSON.parseObject(JSON.toJSONString(webhook));
        String apiVersion = signPayload.getString("api_version");
        if (apiVersion == null || apiVersion.isBlank() || API_VERSION_V1.equalsIgnoreCase(apiVersion)) {
            signPayload.remove("api_version");
            signPayload.remove("order_status_int");
            signPayload.remove("channel_biz_data");
            signPayload.remove("event");
            signPayload.remove("event_data");
        }
        return signPayload;
    }

    @Test
    public void demoV1WebhookVerification() {
        System.out.println("========== Payin Webhook V1 Verification Example ==========\n");

        JSONObject webhook = buildV1WebhookSample();
        JSONObject signPayload = buildSignPayloadByApiVersion(webhook);
        String calculatedSign = SignUtil.createSign(signPayload, APP_SECRET);

        System.out.println("Webhook Body: " + JSON.toJSONString(webhook, true));
        System.out.println("Sign Payload (v1): " + JSON.toJSONString(signPayload, true));
        System.out.println("Calculated Signature: " + calculatedSign);
        System.out.println("Use this signature value to compare with the X-Auth-Signature request header.");
    }

    @Test
    public void demoV2WebhookVerification() {
        System.out.println("========== Payin Webhook V2 Verification Example ==========\n");

        JSONObject webhook = buildV2WebhookSample();
        JSONObject signPayload = buildSignPayloadByApiVersion(webhook);
        String calculatedSign = SignUtil.createSign(signPayload, APP_SECRET);

        System.out.println("Webhook Body: " + JSON.toJSONString(webhook, true));
        System.out.println("Sign Payload (v2): " + JSON.toJSONString(signPayload, true));
        System.out.println("Calculated Signature: " + calculatedSign);
        System.out.println("Use this signature value to compare with the X-Auth-Signature request header.");
    }

    @Test
    public void showActualWebhookFields() {
        System.out.println("========== Payin Actual Webhook Fields ==========\n");

        JSONObject webhook = buildActualWebhookSample();
        JSONObject channelBizData = webhook.getJSONObject("channel_biz_data");
        JSONObject threeDs = channelBizData.getJSONObject("3ds");

        System.out.println("Platform Order ID (pay_order_id): " + webhook.getString("pay_order_id"));
        System.out.println("Merchant Request ID (request_id): " + webhook.getString("request_id"));
        System.out.println("Order Status (order_status): " + webhook.getString("order_status"));
        System.out.println("Order Status Int (order_status_int): " + webhook.getIntValue("order_status_int"));
        System.out.println("Target Currency (target_currency): " + webhook.getString("target_currency"));
        System.out.println("Target Amount (target_amount): " + webhook.getString("target_amount"));
        System.out.println("Product Name (product_name): " + webhook.getString("product_name"));
        System.out.println("Description (description): " + webhook.getString("description"));
        System.out.println("Complete Time (complete_time): " + webhook.get("complete_time"));
        System.out.println("Error Code (error_code): " + webhook.get("error_code"));
        System.out.println("Error Message (error_msg): " + webhook.get("error_msg"));
        System.out.println("API Version (api_version): " + webhook.getString("api_version"));
        System.out.println("Event (event): " + webhook.get("event"));
        System.out.println("Event Data (event_data): " + webhook.get("event_data"));

        System.out.println("\nChannel Biz Data:");
        System.out.println("Issuer Country: " + channelBizData.getString("issuer_country"));
        System.out.println("Issuer: " + channelBizData.getString("issuer"));
        System.out.println("3DS Enrolled: " + threeDs.getString("enrolled"));
        System.out.println("3DS Downgraded: " + threeDs.getBooleanValue("downgraded"));
    }

    /**
     * Controller pseudo code for receiving a payin webhook on the merchant side.
     *
     * <pre>
     * {@code
     * @PostMapping("/payin-notify")
     * public String handlePayinWebhook(
     *         @RequestHeader("X-Auth-Signature") String signature,
     *         @RequestHeader(value = "X-Auth-Timestamp", required = false) String timestamp,
     *         @RequestBody String body) {
     *
     *     // 1. Parse the body and verify the signature
     *     JSONObject webhook = JSON.parseObject(body);
     *     JSONObject signPayload = buildSignPayloadByApiVersion(webhook);
     *     String calculatedSign = SignUtil.createSign(signPayload, APP_SECRET);
     *     if (!calculatedSign.equals(signature)) {
     *         log.error("Payin webhook signature verification failed, timestamp={}", timestamp);
     *         return "FAIL";
     *     }
     *
     *     // 2. Read the core fields
     *     String payOrderId = webhook.getString("pay_order_id");
     *     String requestId = webhook.getString("request_id");
     *     String orderStatus = webhook.getString("order_status");
     *     Integer orderStatusInt = webhook.getInteger("order_status_int");
     *
     *     // 3. If event is not null, it is an event notification.
     *     //    For standard payment callbacks, it is usually null.
     *     String event = webhook.getString("event");
     *     if (event != null) {
     *         // Handle as an event notification
     *         return "SUCCESS";
     *     }
     *
     *     // 4. Update the local order based on the payment result
     *     switch (orderStatus) {
     *         case "SUCCEED":
     *             // Handle payment success
     *             break;
     *         case "FAILED":
     *             String errorCode = webhook.getString("error_code");
     *             String errorMsg = webhook.getString("error_msg");
     *             // Handle payment failure
     *             break;
     *         default:
     *             // Handle other statuses according to your business flow
     *             break;
     *     }
     *
     *     // 5. Return success to avoid further platform retries
     *     return "SUCCESS";
     * }
     * }
     * </pre>
     */
    @Test
    public void showWebhookControllerExample() {
        System.out.println("========== Payin Webhook Controller Example ==========");
        System.out.println("See the Javadoc on showWebhookControllerExample() for the full controller pseudo code.");
        System.out.println("Key points:");
        System.out.println("1. Build the signature payload by api_version, then sign it with APP_SECRET and compare it with X-Auth-Signature.");
        System.out.println("2. For standard payment callbacks, event and event_data are usually null.");
        System.out.println("3. Return SUCCESS or your agreed 2xx response after verification and processing.");
    }
}
