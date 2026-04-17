package com.dpe.lesspay2.demo.example.payout.waycode;

import com.alibaba.fastjson.JSON;
import com.dpe.lesspay2.demo.dto.BankCodesDTO;
import com.dpe.lesspay2.demo.dto.BankDTO;
import com.dpe.lesspay2.demo.dto.BeneficiaryDTO;
import com.dpe.lesspay2.demo.dto.BeneficiaryInfoDTO;
import com.dpe.lesspay2.demo.dto.DestinationDTO;
import com.dpe.lesspay2.demo.dto.PayoutBankDTO;
import com.dpe.lesspay2.demo.dto.PayoutCreateOrderDTO;
import java.util.HashMap;
import java.util.Map;
import com.dpe.lesspay2.demo.dto.PhoneDTO;
import com.dpe.lesspay2.demo.util.SignUtil;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 12Pay (12Group) Thailand Bank Transfer Payout 调试示例
 *
 * 适用场景：12Pay 泰国 THB 银行转账出金
 *
 * 注意：对接的是 Lesspay 平台接口，不是 12Pay 直接接口。
 */
public class PayoutTwelvepayExample {

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
     * 12Pay THB 出金请求调试（会真实发起 HTTP 请求）
     * 说明：使用泰国银行账户进行 THB 出金
     */
    @Test
    public void testTwelvepayThbPayoutDemo() throws IOException {
        System.out.println("========== 12Pay THB Payout Demo Start ==========");

        PayoutCreateOrderDTO dto = buildTwelvepayThbPayoutRequest();
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

        System.out.println("\n========== 12Pay THB Payout Demo End ==========");
    }

    /**
     * 仅测试签名构造（不发请求）— 适合 CI 验证
     */
    @Test
    public void testTwelvepaySignatureOnly() {
        System.out.println("========== 12Pay THB Signature Test ==========");

        PayoutCreateOrderDTO dto = buildTwelvepayThbPayoutRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);

        System.out.println("========== 12Pay THB Signature Test End ==========");
    }

    /**
     * 12Pay 查询支持银行列表（对应 PayoutController#getPayoutBank）
     * 返回 19 家泰国银行的 code 和 name
     */
    @Test
    public void testTwelvepayGetPayoutBankDemo() throws IOException {
        System.out.println("========== 12Pay Get Payout Bank Demo Start ==========");

        PayoutBankDTO dto = buildTwelvepayPayoutBankRequest();
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

        System.out.println("\n========== 12Pay Get Payout Bank Demo End ==========");
    }

    /**
     * 构建 12Pay THB 出金请求参数
     * Build 12Pay THB Payout request parameters
     *
     * 字段说明：
     * - wayCode: TWELVEPAY_PAYOUT
     * - currency: THB（泰铢）
     * - bankCode: 泰国银行 3 位编码，如 014=SCB、004=KBANK
     * - phone: 收款人泰国手机号（calling_code=66）
     */
    private PayoutCreateOrderDTO buildTwelvepayThbPayoutRequest() {
        PayoutCreateOrderDTO dto = new PayoutCreateOrderDTO();

        String requestId = "TWELVEPAY_THB_" + System.currentTimeMillis();
        dto.setRequestId(requestId);

        // 金额与币种（THB 不需要小数位）
        dto.setAmount("1000");
        dto.setCurrency("THB");

        dto.setProductName("Thailand Supplier Payment");
        dto.setDescription("Bank transfer payout via 12Pay");

        dto.setWayCode("TWELVEPAY_PAYOUT");

        dto.setNotifyUrl("https://your-domain.com/api/payout/notify");

        // ========== Build Beneficiary ==========
        BeneficiaryDTO beneficiary = new BeneficiaryDTO();
        beneficiary.setBeneficiaryType("individual");

        // Beneficiary Info（含手机号）
        BeneficiaryInfoDTO beneficiaryInfo = new BeneficiaryInfoDTO();
        beneficiaryInfo.setName("Apaka Kaka");

        PhoneDTO phone = new PhoneDTO();
        phone.setNumber("0123456790");
        beneficiaryInfo.setPhone(phone);

        beneficiary.setBeneficiaryInfo(beneficiaryInfo);

        // Destination（银行账户）
        DestinationDTO destination = new DestinationDTO();
        destination.setType("bank");

        BankDTO bank = new BankDTO();
        bank.setCountry("TH");
        bank.setBankName("BBL");
        bank.setAccountNumber("1234567890");

        // bankCode: 014 = SCB（Siam Commercial Bank）
        BankCodesDTO bankCodes = new BankCodesDTO();
        bankCodes.setBankCode("002");
        bank.setBankCodes(bankCodes);

        destination.setBank(bank);
        beneficiary.setDestination(destination);

        dto.setBeneficiary(beneficiary);

        return dto;
    }

    /**
     * 构造查询支持银行列表请求参数
     */
    private PayoutBankDTO buildTwelvepayPayoutBankRequest() {
        PayoutBankDTO dto = new PayoutBankDTO();
        dto.setBankCountryCode("TH");
        dto.setCurrency("THB");
        dto.setWayCode("TWELVEPAY_PAYOUT");
        return dto;
    }

    /**
     * 12Pay 查询余额（对应 PayoutController#queryBalance）
     *
     * 注意：12Pay 不提供余额查询接口，平台将返回"不支持"错误，此用例用于验证该预期行为。
     */
    @Test
    public void testTwelvepayQueryBalanceDemo() throws IOException {
        System.out.println("========== 12Pay Query Balance Demo Start ==========");
        System.out.println("Note: 12Pay does not support balance query; expected response is an unsupported error.");

        Map<String, Object> dto = buildTwelvepayBalanceRequest();
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

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            System.out.println("\n=== HTTP Response ===");
            System.out.println("Status: " + response.code());
            System.out.println("Body: " + responseBody);
        }

        System.out.println("\n========== 12Pay Query Balance Demo End ==========");
    }

    /**
     * 构造查询余额请求参数
     */
    private Map<String, Object> buildTwelvepayBalanceRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("currency", "THB");
        request.put("way_code", "TWELVEPAY_PAYOUT");
        return request;
    }

    // ==================== ONEPAY JPY Payout ====================

    /**
     * OnePay JPY 出金请求调试（会真实发起 HTTP 请求）
     * 说明：使用日本银行账户进行 JPY 出金
     *
     * 字段说明：
     * - wayCode: ONEPAY_PAYOUT
     * - currency: JPY（日元）
     * - bank_code: 日本金融机构代码（4 位），如 0006=Sumishin SBI Net Bank
     * - branch_code: 支店番号（3 位），如 106
     * - branch_name: 支店名，如 法人第一支店
     */
    @Test
    public void testOnepayJpyPayoutDemo() throws IOException {
        System.out.println("========== OnePay JPY Payout Demo Start ==========");

        PayoutCreateOrderDTO dto = buildOnepayJpyPayoutRequest();
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
     * 仅测试 OnePay JPY 签名构造（不发请求）
     */
    @Test
    public void testOnepayJpySignatureOnly() {
        System.out.println("========== OnePay JPY Signature Test ==========");

        PayoutCreateOrderDTO dto = buildOnepayJpyPayoutRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);

        System.out.println("========== OnePay JPY Signature Test End ==========");
    }

    /**
     * 构建 OnePay JPY 出金请求参数
     * Build OnePay JPY Payout request parameters
     */
    private PayoutCreateOrderDTO buildOnepayJpyPayoutRequest() {
        PayoutCreateOrderDTO dto = new PayoutCreateOrderDTO();

        String requestId = "ONEPAY_JPY_" + System.currentTimeMillis();
        dto.setRequestId(requestId);

        // 金额与币种（JPY 无小数位）
        dto.setAmount("2000");
        dto.setCurrency("JPY");

        dto.setProductName("Supplier Payment");
        dto.setDescription("Payment for services rendered");

        dto.setWayCode("ONEPAY_PAYOUT");

        dto.setNotifyUrl("https://your-domain.com/api/payout/notify");

        // ========== Build Beneficiary ==========
        BeneficiaryDTO beneficiary = new BeneficiaryDTO();
        beneficiary.setBeneficiaryType("individual");

        // Beneficiary Info（收款人姓名，日文片假名）
        BeneficiaryInfoDTO beneficiaryInfo = new BeneficiaryInfoDTO();
        beneficiaryInfo.setName("ヤマダ タロウ");
        beneficiary.setBeneficiaryInfo(beneficiaryInfo);

        // Destination（日本银行账户）
        DestinationDTO destination = new DestinationDTO();
        destination.setType("bank");

        BankDTO bank = new BankDTO();
        bank.setCountry("JP");
        bank.setBankName("Sumishin SBI Net Bank");
        bank.setAccountNumber("1234569880");
        bank.setBranchName("法人第一支店");

        // bank_code: 4 位金融机构代码；branch_code: 3 位支店番号
        BankCodesDTO bankCodes = new BankCodesDTO();
        bankCodes.setBankCode("0006");
        bankCodes.setBranchCode("106");
        bank.setBankCodes(bankCodes);

        destination.setBank(bank);
        beneficiary.setDestination(destination);

        dto.setBeneficiary(beneficiary);

        return dto;
    }
}
