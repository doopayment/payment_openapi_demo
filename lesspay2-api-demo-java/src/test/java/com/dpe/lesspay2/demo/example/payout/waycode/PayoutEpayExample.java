package com.dpe.lesspay2.demo.example.payout.waycode;

import com.alibaba.fastjson.JSON;
import com.dpe.lesspay2.demo.dto.AddressDTO;
import com.dpe.lesspay2.demo.dto.BankCodesDTO;
import com.dpe.lesspay2.demo.dto.BankDTO;
import com.dpe.lesspay2.demo.dto.BeneficiaryDTO;
import com.dpe.lesspay2.demo.dto.BeneficiaryInfoDTO;
import com.dpe.lesspay2.demo.dto.DestinationDTO;
import com.dpe.lesspay2.demo.dto.PayoutBankDTO;
import com.dpe.lesspay2.demo.dto.PayoutCreateOrderDTO;
import com.dpe.lesspay2.demo.dto.PhoneDTO;
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
 * EPay Payout 调试示例（单笔）
 *
 * 对接接口：
 * 1) /api/global/payout/create-order
 * 2) /api/global/payout/bank
 * 3) /api/global/payout/balance
 * 适用场景：EPay 菲律宾银行转账出金（PHP）
 *
 * <p>渠道说明：
 * <ul>
 *   <li>ifCode: epay</li>
 *   <li>wayCode: EPAY_PAYOUT</li>
 *   <li>支持币种: PHP</li>
 *   <li>出金类型: 银行转账 (BANK)</li>
 *   <li>下单流程: createTransaction → confirmTransaction（平台内部串行，调用方无感知）</li>
 *   <li>bankCode: 固定传 SMBP（渠道暂未校验银行编码）</li>
 * </ul>
 */
public class PayoutEpayExample {

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
     * EPay 菲律宾银行转账出金请求调试（会真实发起 HTTP 请求）
     * 说明：使用银行账户方式进行菲律宾比索出金
     */
    @Test
    public void testEpayBankPayoutDemo() throws IOException {
        System.out.println("========== EPay PHP Bank Transfer Payout Demo Start ==========");

        PayoutCreateOrderDTO dto = buildEpayBankPayoutRequest();
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

        System.out.println("\n========== EPay PHP Bank Transfer Payout Demo End ==========");
    }

    /**
     * 仅测试签名（不发请求）
     */
    @Test
    public void testEpaySignatureOnly() {
        System.out.println("========== EPay Signature Test ==========");

        PayoutCreateOrderDTO dto = buildEpayBankPayoutRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);
    }

    /**
     * EPay 查询支持银行列表（对应 PayoutController#getPayoutBank）
     */
    @Test
    public void testEpayGetPayoutBankDemo() throws IOException {
        System.out.println("========== EPay Get Payout Bank Demo Start ==========");

        PayoutBankDTO dto = buildEpayPayoutBankRequest();
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

        System.out.println("\n========== EPay Get Payout Bank Demo End ==========");
    }

    /**
     * EPay 查询余额（对应 PayoutController#queryBalance）
     */
    @Test
    public void testEpayQueryBalanceDemo() throws IOException {
        System.out.println("========== EPay Query Balance Demo Start ==========");

        Map<String, Object> dto = buildEpayBalanceRequest();
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

        System.out.println("\n========== EPay Query Balance Demo End ==========");
    }

    /**
     * 构造 EPay 菲律宾银行转账单笔出金参数
     * <p>
     * EPay receiverInfo 字段对应关系：
     * <ul>
     *   <li>givName  → beneficiaryInfo.firstName</li>
     *   <li>surName  → beneficiaryInfo.lastName</li>
     *   <li>birthday → beneficiaryInfo.dateOfBirth</li>
     *   <li>country  → address.country</li>
     *   <li>states   → address.state</li>
     *   <li>city     → address.city</li>
     *   <li>address  → address.line1</li>
     *   <li>phone    → phone.number</li>
     *   <li>accountNo → bank.accountNumber</li>
     *   <li>bankCode → bank.bankCodes.bankCode（固定传 "epaytest"，渠道暂未校验）</li>
     * </ul>
     */
    private PayoutCreateOrderDTO buildEpayBankPayoutRequest() {
        PayoutCreateOrderDTO dto = new PayoutCreateOrderDTO();

        dto.setRequestId("EPAY_PO_" + System.currentTimeMillis() + randomInt(1000, 9999));
        dto.setAmount("100.70");
        dto.setCurrency("PHP");
        dto.setProductName("Bank Payout");
        dto.setDescription("EPay PHP bank transfer payout");
        dto.setWayCode("EPAY_PAYOUT");
        dto.setNotifyUrl("https://www.example.com/payout-notify");

        BeneficiaryDTO beneficiary = new BeneficiaryDTO();
        beneficiary.setBeneficiaryType("individual");

        BeneficiaryInfoDTO info = new BeneficiaryInfoDTO();
        info.setFirstName("DA");
        info.setLastName("DAFSA");
        info.setDateOfBirth("1999-01-02");
        info.setNationality("PH");

        PhoneDTO phone = new PhoneDTO();
        phone.setNumber("181546464");
        info.setPhone(phone);

        AddressDTO address = new AddressDTO();
        address.setCountry("PH");
        address.setState("zhkhjou");
        address.setCity("shengfsshi");
        address.setLine1("GFEWGEW202");
        info.setAddress(address);

        beneficiary.setBeneficiaryInfo(info);

        DestinationDTO destination = new DestinationDTO();
        destination.setType("bank");

        BankDTO bank = new BankDTO();
        bank.setAccountNumber("478456143514");
        BankCodesDTO codes = new BankCodesDTO();
        codes.setBankCode("SMBP");   // 固定值，渠道暂未校验银行编码
        bank.setBankCodes(codes);

        destination.setBank(bank);
        beneficiary.setDestination(destination);
        dto.setBeneficiary(beneficiary);

        return dto;
    }

    /**
     * 构造查询支持银行列表请求参数
     */
    private PayoutBankDTO buildEpayPayoutBankRequest() {
        PayoutBankDTO dto = new PayoutBankDTO();
        dto.setBankCountryCode("PH");
        dto.setCurrency("PHP");
        dto.setWayCode("EPAY_PAYOUT");
        return dto;
    }

    /**
     * 构造查询余额请求参数
     */
    private Map<String, Object> buildEpayBalanceRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("currency", "PHP");
        request.put("way_code", "EPAY_PAYOUT");
        return request;
    }

    /**
     * 生成指定范围随机整数
     */
    private int randomInt(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }
}
