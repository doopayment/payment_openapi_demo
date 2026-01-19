package com.dpe.lesspay2.demo.example;

import com.alibaba.fastjson.JSON;
import com.dpe.lesspay2.demo.dto.ChannelExtraDTO;
import com.dpe.lesspay2.demo.dto.CreatePayinOrderDTO;
import com.dpe.lesspay2.demo.util.SignUtil;
import okhttp3.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Card Payment Example
 * <p>
 * Demonstrates 3 integration modes for Card Payment:
 * 1. Cashier Mode (Hosted Payment Page)
 * 2. OpenAPI Mode - Direct Card Payment (Server-to-Server)
 * 3. OpenAPI Mode - Token Payment (Server-to-Server)
 */
public class CardPaymentExample {

    // ======== Configuration - Please modify to real values ========
    private static final String APP_ID = "YOUR_APP_ID";
    private static final String APP_SECRET = "YOUR_APP_SECRET";
    private static final String BASE_URL = "https://lesspay2-pay-uat.doopayment.com";
    // ===============================================================

    private static final String CREATE_ORDER_API_PATH = "/api/global/v1/pay/create-order";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * Mode 1: Cashier Mode
     * The simplest integration. Redirects user to hosted payment page.
     */
    @Test
    public void testCashierMode() throws IOException {
        System.out.println("========== Card Payment - Cashier Mode Demo Start ==========");

        CreatePayinOrderDTO dto = buildBaseOrderRequest();
        
        // Specific parameters for Cashier Mode
        dto.setPayAccessType(1); // 1 = Cashier Mode
        dto.setWayType("CARD_PAYMENT");

        executeCreateOrder(dto);

        System.out.println("\n========== Card Payment - Cashier Mode Demo End ==========");
    }

    /**
     * Mode 2: OpenAPI Mode - Direct Card Payment
     * Merchant collects card info and sends it via API.
     */
    @Test
    public void testOpenApiDirectCardPayment() throws IOException {
        System.out.println("========== Card Payment - OpenAPI Direct Card Demo Start ==========");

        CreatePayinOrderDTO dto = buildBaseOrderRequest();

        // Specific parameters for OpenAPI Mode
        dto.setPayAccessType(2); // 2 = OpenAPI Mode
        dto.setWayType("CARD_PAYMENT");
        
        // Construct Channel Extra Data for Direct Card Payment
        ChannelExtraDTO extra = new ChannelExtraDTO();
        extra.setExtraType("card"); // Must be "card" for direct card payment
        
        ChannelExtraDTO.CardData cardData = ChannelExtraDTO.CardData.builder()
                .number("4242424242424242")
                .expiryMonth(12)
                .expiryYear(2027)
                .cvv("123")
                .storeForFutureUse(true) // Optional: Store card for future token payments
                .build();
        extra.setCardData(cardData);

        dto.setChannelExtra(extra);

        executeCreateOrder(dto);

        System.out.println("\n========== Card Payment - OpenAPI Direct Card Demo End ==========");
    }

    /**
     * Mode 3: OpenAPI Mode - Token Payment
     * Use a saved card token for payment.
     */
    @Test
    public void testOpenApiTokenPayment() throws IOException {
        System.out.println("========== Card Payment - OpenAPI Token Payment Demo Start ==========");

        CreatePayinOrderDTO dto = buildBaseOrderRequest();

        // Specific parameters for OpenAPI Mode
        dto.setPayAccessType(2); // 2 = OpenAPI Mode
        dto.setWayType("CARD_PAYMENT");

        // Construct Channel Extra Data for Token Payment
        ChannelExtraDTO extra = new ChannelExtraDTO();
        extra.setExtraType("token"); // Must be "token" for token payment
        
        ChannelExtraDTO.TokenData tokenData = ChannelExtraDTO.TokenData.builder()
                .token("src_ushjzwmcr3pezjrcmmlcufhl6m") // Replace with actual token from previous payment callback
                .build();
        extra.setTokenData(tokenData);

        dto.setChannelExtra(extra);

        executeCreateOrder(dto);

        System.out.println("\n========== Card Payment - OpenAPI Token Payment Demo End ==========");
    }

    /**
     * Execute Create Request
     */
    private void executeCreateOrder(CreatePayinOrderDTO dto) throws IOException {
        // 1. Serialize Request Body
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        // 2. Generate Signature
        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);

        // 3. Send Request
        String url = BASE_URL + CREATE_ORDER_API_PATH;
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
     * Build Base Order Request
     */
    private CreatePayinOrderDTO buildBaseOrderRequest() {
        CreatePayinOrderDTO dto = new CreatePayinOrderDTO();
        String requestId = "M" + System.currentTimeMillis();
        
        dto.setRequestId(requestId);
        dto.setProductName("Lesspay Order");
        dto.setDescription("Lesspay Order Description");
        dto.setTargetAmount("100"); // Note: String type
        dto.setTargetCurrency("CNY");
        dto.setTransactionType("PAY_IN");
        dto.setSuccessUrl("https://www.doopayment.com");
        dto.setFailUrl("https://www.doopayment.com");
        dto.setNotifyUrl("https://merchant.domain.com/api/callbackUrl");
        dto.setApiVersion("V2"); // Use V2
        dto.setExpiredTime(1800); // Note: Integer type, in seconds
        
        return dto;
    }
}
