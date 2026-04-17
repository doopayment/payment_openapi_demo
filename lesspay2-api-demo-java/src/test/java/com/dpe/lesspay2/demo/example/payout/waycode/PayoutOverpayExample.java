package com.dpe.lesspay2.demo.example.payout.waycode;

import com.alibaba.fastjson.JSON;
import com.dpe.lesspay2.demo.dto.BankDTO;
import com.dpe.lesspay2.demo.dto.BeneficiaryDTO;
import com.dpe.lesspay2.demo.dto.BeneficiaryInfoDTO;
import com.dpe.lesspay2.demo.dto.DestinationDTO;
import com.dpe.lesspay2.demo.dto.PayoutBankDTO;
import com.dpe.lesspay2.demo.dto.PayoutCreateOrderDTO;
import com.dpe.lesspay2.demo.util.SignUtil;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Overpay Payout 调试示例（单笔）
 *
 * 对接接口：
 * 1) /api/global/payout/create-order
 * 2) /api/global/payout/bank
 * 3) /api/global/payout/balance
 *
 * 说明：
 * - 当前 Overpay 仅支持 bank 出金。
 * - 平台内部会把 bank_name + account_number 映射到渠道字段 name。
 * - 与 Korapay 示例保持一致：提供 create-order、bank、balance 三个真实联调入口，以及一个签名示例。
 */
public class PayoutOverpayExample {

    // ======== 配置区：请替换为真实值 ========
    private static final String APP_ID = "YOUR_APP_ID";
    private static final String APP_SECRET = "YOUR_APP_SECRET";
    private static final String BASE_URL = "https://lesspay2-pay-uat.doopayment.com";
    // =======================================

    private static final String PAYOUT_API_PATH = "/api/global/payout/create-order";
    private static final String PAYOUT_BANK_API_PATH = "/api/global/payout/bank";
    private static final String PAYOUT_BALANCE_API_PATH = "/api/global/payout/balance";
    private static final String WAY_CODE = "OVERPAY_PAYOUT";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * Overpay 银行卡出金请求调试（会真实发起 HTTP 请求）
     */
    @Test
    public void testOverpayBankPayoutDemo() throws IOException {
        System.out.println("========== Overpay Bank Payout Demo Start ==========");

        PayoutCreateOrderDTO dto = buildOverpayBankPayoutRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);

        executeJsonPost(PAYOUT_API_PATH, jsonBody, signature);

        System.out.println("\n========== Overpay Bank Payout Demo End ==========");
    }

    /**
     * 仅测试签名（不发请求）
     */
    @Test
    public void testOverpaySignatureOnly() {
        System.out.println("========== Overpay Signature Test ==========");

        PayoutCreateOrderDTO dto = buildOverpayBankPayoutRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);
    }

    /**
     * 查询平台侧支持银行接口（会真实发起 HTTP 请求）
     */
    @Test
    public void testOverpayGetPayoutBankDemo() throws IOException {
        System.out.println("========== Overpay Get Payout Bank Demo Start ==========");

        PayoutBankDTO dto = buildOverpayPayoutBankRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);

        executeJsonPost(PAYOUT_BANK_API_PATH, jsonBody, signature);

        System.out.println("\n========== Overpay Get Payout Bank Demo End ==========");
    }

    /**
     * 查询平台侧余额接口（会真实发起 HTTP 请求）
     */
    @Test
    public void testOverpayQueryBalanceDemo() throws IOException {
        System.out.println("========== Overpay Query Balance Demo Start ==========");

        Map<String, Object> dto = buildOverpayBalanceRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);

        executeJsonPost(PAYOUT_BALANCE_API_PATH, jsonBody, signature);

        System.out.println("\n========== Overpay Query Balance Demo End ==========");
    }

    private void executeJsonPost(String apiPath, String jsonBody, String signature) throws IOException {
        String url = BASE_URL + apiPath;
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
     * 构造 Overpay 单笔银行卡出金参数。
     */
    private PayoutCreateOrderDTO buildOverpayBankPayoutRequest() {
        PayoutCreateOrderDTO dto = new PayoutCreateOrderDTO();

        String requestId = "OVERPAY_PO_" + System.currentTimeMillis() + randomInt(1000, 9999);
        dto.setRequestId(requestId);
        dto.setAmount("5231");
        dto.setCurrency("THB");
        dto.setWayCode(WAY_CODE);
        dto.setProductName("overpay payout product");
        dto.setDescription("Overpay bank payout demo");
        dto.setNotifyUrl("https://www.example.com/payout-notify");

        BeneficiaryDTO beneficiary = new BeneficiaryDTO();
        beneficiary.setBeneficiaryType("individual");

        BeneficiaryInfoDTO beneficiaryInfo = new BeneficiaryInfoDTO();
        beneficiaryInfo.setName("Test User");
        beneficiaryInfo.setEmail("test.user@example.com");
        beneficiary.setBeneficiaryInfo(beneficiaryInfo);

        DestinationDTO destination = new DestinationDTO();
        destination.setType("bank");

        // Overpay 当前最小必填是 bank_name + account_number。
        BankDTO bank = new BankDTO();
        bank.setCountry("TH");
//        bank.setBankName("Kasikorn Bank");
        bank.setBankName("บมจ.ธนาคารกรุงเทพ");
        bank.setAccountNumber("1234567890");
        destination.setBank(bank);

        beneficiary.setDestination(destination);
        dto.setBeneficiary(beneficiary);
        return dto;
    }

    private PayoutBankDTO buildOverpayPayoutBankRequest() {
        PayoutBankDTO dto = new PayoutBankDTO();
        dto.setBankCountryCode("TH");
        dto.setCurrency("THB");
        dto.setWayCode(WAY_CODE);
        return dto;
    }

    private Map<String, Object> buildOverpayBalanceRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("currency", "THB");
        request.put("way_code", WAY_CODE);
        return request;
    }

    private int randomInt(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }
}
