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
 * OnePay Payout 调试示例（单笔）
 *
 * 对接接口：
 * 1) /api/global/payout/create-order
 * 2) /api/global/payout/bank
 * 3) /api/global/payout/balance
 * 适用场景：OnePay 银行转账出金（日本 JPY）
 */
public class PayoutOnePayExample {

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
     * OnePay JPY 出金请求调试（会真实发起 HTTP 请求）
     * 说明：使用银行账户方式进行日元出金
     */
    @Test
    public void testOnePayJpyPayoutDemo() throws IOException {
        System.out.println("========== OnePay JPY Payout Demo Start ==========");

        PayoutCreateOrderDTO dto = buildOnePayJpyPayoutRequest();
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

        System.out.println("\n========== OnePay JPY Payout Demo End ==========");
    }

    /**
     * 仅测试签名（不发请求）- JPY
     */
    @Test
    public void testOnePayJpySignatureOnly() {
        System.out.println("========== OnePay JPY Signature Test ==========");

        PayoutCreateOrderDTO dto = buildOnePayJpyPayoutRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);
    }

    /**
     * OnePay 查询支持银行列表（对应 PayoutController#getPayoutBank）
     */
    @Test
    public void testOnePayGetPayoutBankDemo() throws IOException {
        System.out.println("========== OnePay Get Payout Bank Demo Start ==========");

        PayoutBankDTO dto = buildOnePayPayoutBankRequest();
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

        System.out.println("\n========== OnePay Get Payout Bank Demo End ==========");
    }

    /**
     * OnePay 查询余额（对应 PayoutController#queryBalance）
     */
    @Test
    public void testOnePayQueryBalanceDemo() throws IOException {
        System.out.println("========== OnePay Query Balance Demo Start ==========");

        Map<String, Object> dto = buildOnePayBalanceRequest();
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

        System.out.println("\n========== OnePay Query Balance Demo End ==========");
    }

    /**
     * 构建 OnePay JPY 出金请求参数
     * Build OnePay JPY Payout request parameters
     */
    private PayoutCreateOrderDTO buildOnePayJpyPayoutRequest() {
        PayoutCreateOrderDTO dto = new PayoutCreateOrderDTO();

        // Generate unique request_id using timestamp
        String requestId = "ONEPAY_JPY_" + System.currentTimeMillis();
        dto.setRequestId(requestId);

        // Amount and Currency (JPY - Japanese Yen, no decimal places)
        dto.setAmount("2000");
        dto.setCurrency("JPY");

        // Product info
        dto.setProductName("Supplier Payment");
        dto.setDescription("Payment for services rendered");

        // Payment method configuration
        dto.setWayCode("ONEPAY_PAYOUT");

        // Async callback notification URL
        dto.setNotifyUrl("https://your-domain.com/api/payout/notify");

        // ========== Build Beneficiary ==========
        BeneficiaryDTO beneficiary = new BeneficiaryDTO();
        beneficiary.setBeneficiaryType("individual");

        // Build Beneficiary Info
        BeneficiaryInfoDTO beneficiaryInfo = new BeneficiaryInfoDTO();
        beneficiaryInfo.setName("ヤマダ タロウ");

        beneficiary.setBeneficiaryInfo(beneficiaryInfo);

        // Build Destination (Bank Account)
        DestinationDTO destination = new DestinationDTO();
        destination.setType("bank");

        // Build Bank
        BankDTO bank = new BankDTO();
        bank.setCountry("JP");
        bank.setBankName("Sumishin SBI Net Bank");
        bank.setAccountNumber("1234569880");
        bank.setBranchName("法人第一支店");

        // Build Bank Codes (optional)
        BankCodesDTO bankCodes = new BankCodesDTO();
        bankCodes.setBankCode("0010");  // Sumishin SBI Net Bank code
        bankCodes.setBranchCode("106");  // Branch code
        bank.setBankCodes(bankCodes);

        destination.setBank(bank);
        beneficiary.setDestination(destination);

        dto.setBeneficiary(beneficiary);

        return dto;
    }

    /**
     * 构造查询支持银行列表请求参数
     * Build request parameters for querying supported bank list
     */
    private PayoutBankDTO buildOnePayPayoutBankRequest() {
        PayoutBankDTO dto = new PayoutBankDTO();
        dto.setBankCountryCode("JP");
        dto.setCurrency("JPY");
        dto.setWayCode("ONEPAY_PAYOUT");
        return dto;
    }

    /**
     * 构造查询余额请求参数
     * Build request parameters for querying balance
     */
    private Map<String, Object> buildOnePayBalanceRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("currency", "JPY");
        request.put("way_code", "ONEPAY_PAYOUT");
        return request;
    }
}
