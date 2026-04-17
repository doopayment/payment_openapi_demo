package com.dpe.lesspay2.demo.example.payout.waycode;

import com.alibaba.fastjson.JSON;
import com.dpe.lesspay2.demo.dto.AddressDTO;
import com.dpe.lesspay2.demo.dto.BankCodesDTO;
import com.dpe.lesspay2.demo.dto.BankDTO;
import com.dpe.lesspay2.demo.dto.BeneficiaryDTO;
import com.dpe.lesspay2.demo.dto.BeneficiaryInfoDTO;
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
 * PayRetailers Payout 调试示例（单笔）
 *
 * 对接接口：
 * 1) /api/global/payout/create-order
 * 2) /api/global/payout/bank
 * 3) /api/global/payout/balance
 * 适用场景：PayRetailers PIX 与 Bank Transfer（destination.type=ewallet/bank）
 */
public class PayoutPayRetailersExample {

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
     * PayRetailers PIX 出金请求调试（会真实发起 HTTP 请求）
     * 说明：使用 PIX 作为电子钱包方式进行出金
     */
    @Test
    public void testPayRetailersPixPayoutDemo() throws IOException {
        System.out.println("========== PayRetailers PIX Payout Demo Start ==========");

        PayoutCreateOrderDTO dto = buildPayRetailersPixPayoutRequest();
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

        System.out.println("\n========== PayRetailers PIX Payout Demo End ==========");
    }

    /**
     * PayRetailers 银行转账出金请求调试（会真实发起 HTTP 请求）
     * 说明：使用银行账户方式进行出金
     */
    @Test
    public void testPayRetailersBankPayoutDemo() throws IOException {
        System.out.println("========== PayRetailers Bank Transfer Payout Demo Start ==========");

        PayoutCreateOrderDTO dto = buildPayRetailersBankPayoutRequest();
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

        System.out.println("\n========== PayRetailers Bank Transfer Payout Demo End ==========");
    }

    /**
     * 仅测试签名（不发请求）- PIX
     */
    @Test
    public void testPayRetailersPixSignatureOnly() {
        System.out.println("========== PayRetailers PIX Signature Test ==========");

        PayoutCreateOrderDTO dto = buildPayRetailersPixPayoutRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);
    }

    /**
     * 仅测试签名（不发请求）- Bank Transfer
     */
    @Test
    public void testPayRetailersBankSignatureOnly() {
        System.out.println("========== PayRetailers Bank Transfer Signature Test ==========");

        PayoutCreateOrderDTO dto = buildPayRetailersBankPayoutRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);
    }

    /**
     * PayRetailers 查询支持银行列表（对应 PayoutController#getPayoutBank）
     */
    @Test
    public void testPayRetailersGetPayoutBankDemo() throws IOException {
        System.out.println("========== PayRetailers Get Payout Bank Demo Start ==========");

        PayoutBankDTO dto = buildPayRetailersPayoutBankRequest();
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

        System.out.println("\n========== PayRetailers Get Payout Bank Demo End ==========");
    }

    /**
     * PayRetailers 查询余额（对应 PayoutController#queryBalance）
     */
    @Test
    public void testPayRetailersQueryBalanceDemo() throws IOException {
        System.out.println("========== PayRetailers Query Balance Demo Start ==========");

        Map<String, Object> dto = buildPayRetailersBalanceRequest();
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

        System.out.println("\n========== PayRetailers Query Balance Demo End ==========");
    }

    /**
     * 构建 PayRetailers PIX 出金请求参数
     * Build PayRetailers PIX Payout request parameters
     */
    private PayoutCreateOrderDTO buildPayRetailersPixPayoutRequest() {
        PayoutCreateOrderDTO dto = new PayoutCreateOrderDTO();

        // Generate unique request_id using timestamp
        String requestId = "PAYOUT_PIX_" + System.currentTimeMillis();
        dto.setRequestId(requestId);

        // Amount and Currency (BRL - Brazilian Real)
        dto.setAmount("10.00");
        dto.setCurrency("BRL");

        // Product info
        dto.setProductName("payout product");
        dto.setDescription("test");

        // Payment method configuration
        dto.setWayCode("PAYRETAILERS_PAYOUT");

        // Async callback notification URL
        dto.setNotifyUrl("http://your.domain.com");

        // ========== Build Beneficiary ==========
        BeneficiaryDTO beneficiary = new BeneficiaryDTO();
        beneficiary.setBeneficiaryType("individual");

        // Build Beneficiary Info
        BeneficiaryInfoDTO beneficiaryInfo = new BeneficiaryInfoDTO();
        beneficiaryInfo.setName("test test");
        beneficiaryInfo.setFirstName("test");
        beneficiaryInfo.setLastName("test");
        beneficiaryInfo.setEmail("test@testmail.com");
        beneficiaryInfo.setTaxId("79052703515");  // documentNumber (CPF)

        // Build Address
        AddressDTO address = new AddressDTO();
        address.setCity("test");
        address.setCountry("BR");
        beneficiaryInfo.setAddress(address);

        beneficiary.setBeneficiaryInfo(beneficiaryInfo);

        // Build Destination (PIX as E-Wallet)
        DestinationDTO destination = new DestinationDTO();
        destination.setType("ewallet");

        // Build E-Wallet (PIX)
        EWalletDTO eWallet = new EWalletDTO();
        eWallet.setEwalletProvider("PIX");
        eWallet.setAccountType("email");
        eWallet.setAccountNumber("test@test.com");  // recipientPixKey

        destination.setEWallet(eWallet);
        beneficiary.setDestination(destination);

        dto.setBeneficiary(beneficiary);

        return dto;
    }

    /**
     * 构建 PayRetailers 银行转账出金请求参数
     * Build PayRetailers Bank Transfer Payout request parameters
     */
    private PayoutCreateOrderDTO buildPayRetailersBankPayoutRequest() {
        PayoutCreateOrderDTO dto = new PayoutCreateOrderDTO();

        // Generate unique request_id using timestamp
        String requestId = "PAYOUT_" + System.currentTimeMillis() + "_001";
        dto.setRequestId(requestId);

        // Amount and Currency (BRL - Brazilian Real)
        dto.setAmount("100");
        dto.setCurrency("BRL");

        // Product info
        dto.setProductName("Supplier Payment");
        dto.setDescription("Payment for services rendered");

        // Payment method configuration
        dto.setWayCode("PAYRETAILERS_PAYOUT");

        // Expired time (optional, 86400 seconds = 24 hours)
        dto.setExpiredTime(86400);

        // Async callback notification URL
        dto.setNotifyUrl("https://your-domain.com/api/payout/notify");

        // ========== Build Beneficiary ==========
        BeneficiaryDTO beneficiary = new BeneficiaryDTO();
        beneficiary.setBeneficiaryType("individual");

        // Build Beneficiary Info
        BeneficiaryInfoDTO beneficiaryInfo = new BeneficiaryInfoDTO();
        beneficiaryInfo.setName("John John");
        beneficiaryInfo.setFirstName("John");
        beneficiaryInfo.setLastName("John");
        beneficiaryInfo.setEmail("test@payretailers.com");
        beneficiaryInfo.setTaxId("07341712503");  // CPF

        // Build Address
        AddressDTO address = new AddressDTO();
        address.setCountry("BR");
        beneficiaryInfo.setAddress(address);

        beneficiary.setBeneficiaryInfo(beneficiaryInfo);

        // Build Destination (Bank Account)
        DestinationDTO destination = new DestinationDTO();
        destination.setType("bank");

        // Build Bank
        BankDTO bank = new BankDTO();
        bank.setAccountType("savings");  // savings or checking
        bank.setCountry("BR");
        bank.setBankName("Itaú");
        bank.setAccountNumber("1234567890123");

        // Build Bank Codes
        BankCodesDTO bankCodes = new BankCodesDTO();
        bankCodes.setBankCode("341");  // Itaú bank code
        bankCodes.setBranchCode("1234");  // Branch code
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
    private PayoutBankDTO buildPayRetailersPayoutBankRequest() {
        PayoutBankDTO dto = new PayoutBankDTO();
        dto.setBankCountryCode("BR");
        dto.setCurrency("BRL");
        dto.setWayCode("PAYRETAILERS_PAYOUT");
        return dto;
    }

    /**
     * 构造查询余额请求参数
     * Build request parameters for querying balance
     */
    private Map<String, Object> buildPayRetailersBalanceRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("currency", "BRL");
        request.put("way_code", "PAYRETAILERS_PAYOUT");
        return request;
    }
}
