package com.dpe.lesspay2.demo.example.payout.waycode;

import com.alibaba.fastjson.JSON;
import com.dpe.lesspay2.demo.dto.BankCodesDTO;
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
 * OkaysPay Payout 调试示例（单笔）
 *
 * 对接接口：
 * 1) /api/global/payout/create-order
 * 2) /api/global/payout/bank
 * 3) /api/global/payout/balance
 * 适用场景：OkaysPay 越南银行转账出金（VND）
 */
public class PayoutOkaysExample {

    // ======== 配置区：请替换为真实值 ========
    private static final String APP_ID = "YOUR_APP_ID";
    private static final String APP_SECRET = "YOUR_APP_SECRET";
    private static final String BASE_URL = "https://lesspay2-pay-uat.doopayment.com";
    // =======================================

    private static final String PAYOUT_API_PATH = "/api/global/payout/create-order";
    private static final String PAYOUT_BANK_API_PATH = "/api/global/payout/bank";
    private static final String PAYOUT_BALANCE_API_PATH = "/api/global/payout/balance";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * OkaysPay 越南银行转账出金请求调试（会真实发起 HTTP 请求）
     * 说明：使用银行账户方式进行越南盾出金
     */
    @Test
    public void testOkaysPayBankPayoutDemo() throws IOException {
        System.out.println("========== OkaysPay Bank Transfer Payout Demo Start ==========");

        PayoutCreateOrderDTO dto = buildOkaysPayBankPayoutRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);

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

        System.out.println("\n========== OkaysPay Bank Transfer Payout Demo End ==========");
    }

    /**
     * 仅测试签名（不发请求）
     */
    @Test
    public void testOkaysPaySignatureOnly() {
        System.out.println("========== OkaysPay Signature Test ==========");

        PayoutCreateOrderDTO dto = buildOkaysPayBankPayoutRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);
    }

    /**
     * OkaysPay 查询支持银行列表（对应 PayoutController#getPayoutBank）
     */
    @Test
    public void testOkaysPayGetPayoutBankDemo() throws IOException {
        System.out.println("========== OkaysPay Get Payout Bank Demo Start ==========");

        PayoutBankDTO dto = buildOkaysPayPayoutBankRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);

        String url = BASE_URL + PAYOUT_BANK_API_PATH;
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

        System.out.println("\n========== OkaysPay Get Payout Bank Demo End ==========");
    }

    /**
     * OkaysPay 查询余额（对应 PayoutController#queryBalance）
     */
    @Test
    public void testOkaysPayQueryBalanceDemo() throws IOException {
        System.out.println("========== OkaysPay Query Balance Demo Start ==========");

        Map<String, Object> dto = buildOkaysPayBalanceRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);

        String url = BASE_URL + PAYOUT_BALANCE_API_PATH;
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

        System.out.println("\n========== OkaysPay Query Balance Demo End ==========");
    }

    /**
     * 构造 OkaysPay 越南银行转账单笔出金参数
     */
    private PayoutCreateOrderDTO buildOkaysPayBankPayoutRequest() {
        PayoutCreateOrderDTO dto = new PayoutCreateOrderDTO();

        dto.setRequestId("OKAYS_PO_" + System.currentTimeMillis() + randomInt(1000, 9999));
        dto.setAmount("300000");
        dto.setCurrency("VND");
        dto.setProductName("Supplier Payment");
        dto.setDescription("OkaysPay Vietnam bank transfer payout demo");
        dto.setWayCode("OKAYSPAY_PAYOUT");
        dto.setWayType("BANK_TRANSFER");
        dto.setExpiredTime(86400);
        dto.setNotifyUrl("https://www.example.com/payout-notify");

        BeneficiaryDTO beneficiary = new BeneficiaryDTO();
        beneficiary.setBeneficiaryType("individual");

        BeneficiaryInfoDTO beneficiaryInfo = new BeneficiaryInfoDTO();
        beneficiaryInfo.setName("Bill");
        beneficiary.setBeneficiaryInfo(beneficiaryInfo);

        DestinationDTO destination = new DestinationDTO();
        destination.setType("bank");

        BankDTO bank = new BankDTO();
        bank.setAccountNumber("123412341234");
        BankCodesDTO codes = new BankCodesDTO();
        codes.setBankCode("BFTV");
        bank.setBankCodes(codes);

        destination.setBank(bank);
        beneficiary.setDestination(destination);
        dto.setBeneficiary(beneficiary);

        return dto;
    }

    /**
     * 构造查询支持银行列表请求参数
     */
    private PayoutBankDTO buildOkaysPayPayoutBankRequest() {
        PayoutBankDTO dto = new PayoutBankDTO();
        dto.setBankCountryCode("VN");
        dto.setCurrency("VND");
        dto.setWayCode("OKAYSPAY_PAYOUT");
        return dto;
    }

    /**
     * 构造查询余额请求参数
     */
    private Map<String, Object> buildOkaysPayBalanceRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("currency", "VND");
        request.put("way_code", "OKAYSPAY_PAYOUT");
        return request;
    }

    /**
     * 生成指定范围随机整数
     */
    private int randomInt(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }
}
