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
 * AwePay Payout 调试示例（单笔）
 *
 * 对接接口：
 * 1) /api/global/payout/create-order
 * 2) /api/global/payout/bank
 * 3) /api/global/payout/balance
 * 适用场景：AwePay 印度尼西亚银行转账出金（IDR）
 */
public class PayoutAwePayExample {

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
     * AwePay 印度尼西亚银行转账出金请求调试（会真实发起 HTTP 请求）
     * 说明：使用银行账户方式进行印尼卢比出金
     */
    @Test
    public void testAwePayBankPayoutDemo() throws IOException {
        System.out.println("========== AwePay Bank Payout Demo Start ==========");

        PayoutCreateOrderDTO dto = buildAwePayBankPayoutRequest();
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

        System.out.println("\n========== AwePay Bank Payout Demo End ==========");
    }

    /**
     * 仅测试签名（不发请求）
     */
    @Test
    public void testAwePaySignatureOnly() {
        System.out.println("========== AwePay Signature Test ==========");

        PayoutCreateOrderDTO dto = buildAwePayBankPayoutRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);
    }

    /**
     * AwePay 查询支持银行列表（对应 PayoutController#getPayoutBank）
     */
    @Test
    public void testAwePayGetPayoutBankDemo() throws IOException {
        System.out.println("========== AwePay Get Payout Bank Demo Start ==========");

        PayoutBankDTO dto = buildAwePayPayoutBankRequest();
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

        System.out.println("\n========== AwePay Get Payout Bank Demo End ==========");
    }

    /**
     * AwePay 查询余额（对应 PayoutController#queryBalance）
     */
    @Test
    public void testAwePayQueryBalanceDemo() throws IOException {
        System.out.println("========== AwePay Query Balance Demo Start ==========");

        Map<String, Object> dto = buildAwePayBalanceRequest();
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

        System.out.println("\n========== AwePay Query Balance Demo End ==========");
    }

    /**
     * 构造 AwePay 印度尼西亚银行转账单笔出金参数
     */
    private PayoutCreateOrderDTO buildAwePayBankPayoutRequest() {
        PayoutCreateOrderDTO dto = new PayoutCreateOrderDTO();

        String requestId = "AWEPAY_PO_" + System.currentTimeMillis() + randomInt(1000, 9999);
        dto.setRequestId(requestId);
        dto.setAmount("100000");
        dto.setCurrency("IDR");
        dto.setProductName("Supplier Payment");
        dto.setDescription("AwePay Indonesia bank transfer payout demo");
        dto.setWayCode("AWEPAY_PAYOUT");
        dto.setWayType("BANK_TRANSFER");
        dto.setExpiredTime(86400);
        dto.setNotifyUrl("https://www.example.com/payout-notify");

        BeneficiaryDTO beneficiary = new BeneficiaryDTO();
        beneficiary.setBeneficiaryType("individual");

        BeneficiaryInfoDTO beneficiaryInfo = new BeneficiaryInfoDTO();
        beneficiaryInfo.setFirstName("Bill");
        beneficiaryInfo.setLastName("Bill");
        beneficiary.setBeneficiaryInfo(beneficiaryInfo);

        DestinationDTO destination = new DestinationDTO();
        destination.setType("bank");

        BankDTO bank = new BankDTO();
        bank.setAccountNumber("123412341234");
        bank.setBankProvince("Central Sumatra Province");
        bank.setBankCity("Jakarta");
        BankCodesDTO codes = new BankCodesDTO();
        codes.setBankCode("ALLVA.ID");
        bank.setBankCodes(codes);
        destination.setBank(bank);

        beneficiary.setDestination(destination);
        dto.setBeneficiary(beneficiary);

        return dto;
    }

    /**
     * 构造查询支持银行列表请求参数
     */
    private PayoutBankDTO buildAwePayPayoutBankRequest() {
        PayoutBankDTO dto = new PayoutBankDTO();
        dto.setBankCountryCode("ID");
        dto.setCurrency("IDR");
        dto.setWayCode("AWEPAY_PAYOUT");
        return dto;
    }

    /**
     * 构造查询余额请求参数
     */
    private Map<String, Object> buildAwePayBalanceRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("currency", "IDR");
        request.put("way_code", "AWEPAY_PAYOUT");
        return request;
    }

    /**
     * 生成指定范围随机整数
     */
    private int randomInt(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }
}
