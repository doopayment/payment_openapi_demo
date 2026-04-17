package com.dpe.lesspay2.demo.example.payout.waycode;

import com.alibaba.fastjson.JSON;
import com.dpe.lesspay2.demo.dto.BeneficiaryDTO;
import com.dpe.lesspay2.demo.dto.BeneficiaryInfoDTO;
import com.dpe.lesspay2.demo.dto.BankCodesDTO;
import com.dpe.lesspay2.demo.dto.BankDTO;
import com.dpe.lesspay2.demo.dto.DestinationDTO;
import com.dpe.lesspay2.demo.dto.EWalletDTO;
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
 * Korapay Payout 调试示例（单笔）
 *
 * 对接接口：
 * 1) /api/global/payout/create-order
 * 2) /api/global/payout/bank
 * 3) /api/global/payout/balance
 * 适用场景：Korapay mobile money 与 bank（destination.type=ewallet/bank）
 */
public class PayoutKorapayExample {

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
     * Korapay 银行卡出金请求调试（会真实发起 HTTP 请求）
     * 说明：Korapay 文档已支持 bank_account，本示例用于联调 bank 入参。
     */
    @Test
    public void testKorapayBankPayoutDemo() throws IOException {
        System.out.println("========== Korapay Bank Payout Demo Start ==========");

        PayoutCreateOrderDTO dto = buildKorapayBankPayoutRequest();
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

        System.out.println("\n========== Korapay Bank Payout Demo End ==========");
    }


    /**
     * Korapay ewallet 出金请求调试（会真实发起 HTTP 请求）
     */
    @Test
    public void testKorapayEwalletPayoutDemo() throws IOException {
        System.out.println("========== Korapay Payout Demo Start ==========");

        PayoutCreateOrderDTO dto = buildKorapayPayoutRequest();
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

        System.out.println("\n========== Korapay Payout Demo End ==========");
    }



    /**
     * 仅测试签名（不发请求）
     */
    @Test
    public void testKorapaySignatureOnly() {
        System.out.println("========== Korapay Signature Test ==========");

        PayoutCreateOrderDTO dto = buildKorapayPayoutRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);
    }

    /**
     * Korapay 查询支持银行列表（对应 PayoutController#getPayoutBank）
     */
    @Test
    public void testKorapayGetPayoutBankDemo() throws IOException {
        System.out.println("========== Korapay Get Payout Bank Demo Start ==========");

        PayoutBankDTO dto = buildKorapayPayoutBankRequest();
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

        System.out.println("\n========== Korapay Get Payout Bank Demo End ==========");
    }

    /**
     * Korapay 查询余额（对应 PayoutController#queryBalance）
     */
    @Test
    public void testKorapayQueryBalanceDemo() throws IOException {
        System.out.println("========== Korapay Query Balance Demo Start ==========");

        Map<String, Object> dto = buildKorapayBalanceRequest();
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

        System.out.println("\n========== Korapay Query Balance Demo End ==========");
    }

    /**
     * 构造 Korapay（mobile money）单笔出金参数
     */
    private PayoutCreateOrderDTO buildKorapayPayoutRequest() {
        PayoutCreateOrderDTO dto = new PayoutCreateOrderDTO();

        String requestId = "KORA_PO_" + System.currentTimeMillis() + randomInt(1000, 9999);
        dto.setRequestId(requestId);
        dto.setAmount(String.valueOf(randomInt(110, 1200)));
        dto.setCurrency("KES");
        dto.setWayCode("KORAPAY_PAYOUT");
        dto.setProductName("korapay payout product");
        dto.setDescription("Korapay mobile money payout demo");
        dto.setNotifyUrl("https://www.example.com/payout-notify");

        BeneficiaryDTO beneficiary = new BeneficiaryDTO();
        beneficiary.setBeneficiaryType("individual");

        BeneficiaryInfoDTO beneficiaryInfo = new BeneficiaryInfoDTO();
        beneficiaryInfo.setName("John Doe");
        beneficiaryInfo.setEmail("johndoe@email.com");
        beneficiary.setBeneficiaryInfo(beneficiaryInfo);

        DestinationDTO destination = new DestinationDTO();
        destination.setType("ewallet");

        EWalletDTO eWallet = new EWalletDTO();
        eWallet.setEwalletProvider("safaricom-ke");
        eWallet.setAccountNumber("256700000000");
        destination.setEWallet(eWallet);

        beneficiary.setDestination(destination);
        dto.setBeneficiary(beneficiary);
        return dto;
    }

    /**
     * 构造 Korapay（bank_account）单笔出金参数
     */
    private PayoutCreateOrderDTO buildKorapayBankPayoutRequest() {
        PayoutCreateOrderDTO dto = new PayoutCreateOrderDTO();

        String requestId = "KORA_BANK_PO_" + System.currentTimeMillis() + randomInt(1000, 9999);
        dto.setRequestId(requestId);
        dto.setAmount(String.valueOf(randomInt(110, 1200)));
        dto.setCurrency("NGN");
        dto.setWayCode("KORAPAY_PAYOUT");
        dto.setProductName("korapay bank payout product");
        dto.setDescription("Korapay bank account payout demo");
        dto.setNotifyUrl("https://www.example.com/payout-notify");

        BeneficiaryDTO beneficiary = new BeneficiaryDTO();
        beneficiary.setBeneficiaryType("individual");

        BeneficiaryInfoDTO beneficiaryInfo = new BeneficiaryInfoDTO();
        beneficiaryInfo.setName("Jane Doe");
        beneficiaryInfo.setEmail("janedoe@email.com");
        beneficiary.setBeneficiaryInfo(beneficiaryInfo);

        DestinationDTO destination = new DestinationDTO();
        destination.setType("bank");

        BankDTO bank = new BankDTO();
        // Korapay NGN bank_account 最小必填：account_number + bank_codes.bank_code
        bank.setAccountNumber("0123456789");

        BankCodesDTO bankCodes = new BankCodesDTO();
        bankCodes.setBankCode("044");
        bank.setBankCodes(bankCodes);
        destination.setBank(bank);

        beneficiary.setDestination(destination);
        dto.setBeneficiary(beneficiary);
        return dto;
    }

    /**
     * 构造查询支持银行列表请求参数
     */
    private PayoutBankDTO buildKorapayPayoutBankRequest() {
        PayoutBankDTO dto = new PayoutBankDTO();
        dto.setBankCountryCode("NG");
        dto.setCurrency("NGN");
        dto.setWayCode("KORAPAY_PAYOUT");
        return dto;
    }

    /**
     * 构造查询余额请求参数
     */
    private Map<String, Object> buildKorapayBalanceRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("currency", "NGN");
        request.put("way_code", "KORAPAY_PAYOUT");
        return request;
    }

    /**
     * 生成指定范围随机整数
     */
    private int randomInt(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }
}
