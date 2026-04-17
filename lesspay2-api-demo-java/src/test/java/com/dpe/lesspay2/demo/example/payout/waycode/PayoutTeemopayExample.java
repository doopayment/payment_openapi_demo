package com.dpe.lesspay2.demo.example.payout.waycode;

import com.alibaba.fastjson.JSON;
import com.dpe.lesspay2.demo.dto.BankCodesDTO;
import com.dpe.lesspay2.demo.dto.BankDTO;
import com.dpe.lesspay2.demo.dto.BeneficiaryDTO;
import com.dpe.lesspay2.demo.dto.BeneficiaryInfoDTO;
import com.dpe.lesspay2.demo.dto.DestinationDTO;
import com.dpe.lesspay2.demo.dto.PhoneDTO;
import com.dpe.lesspay2.demo.dto.PayoutBankDTO;
import com.dpe.lesspay2.demo.dto.PayoutCreateOrderDTO;
import com.dpe.lesspay2.demo.util.SignUtil;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Teemopay Payout 调试示例（巴基斯坦代付）
 *
 * 对接接口：
 * 1) /api/global/payout/create-order
 * 2) /api/global/payout/bank
 * 3) /api/global/payout/balance
 *
 * 说明：
 * - 当前 Teemopay 支持巴基斯坦 BANK + E-Wallet 出金。
 * - 银行列表由平台静态返回，bank_code 与 bank_name 需要传相同编码字符串。
 * - 电子钱包能力由渠道支持列表返回，目前主要为 EASYPAISA / JAZZCASH。
 */
public class PayoutTeemopayExample {

    private static final String APP_ID = "YOUR_APP_ID";
    private static final String APP_SECRET = "YOUR_APP_SECRET";
    private static final String BASE_URL = "https://lesspay2-pay-uat.doopayment.com";

    private static final String PAYOUT_API_PATH = "/api/global/payout/create-order";
    private static final String PAYOUT_BANK_API_PATH = "/api/global/payout/bank";
    private static final String PAYOUT_BALANCE_API_PATH = "/api/global/payout/balance";
    private static final String WAY_CODE = "TEEMOPAY_PAYOUT";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * Teemopay 巴基斯坦银行卡出金请求调试（会真实发起 HTTP 请求）
     */
    @Test
    public void testTeemopayBankPayoutDemo() throws IOException {
        System.out.println("========== Teemopay Bank Payout Demo Start ==========");

        PayoutCreateOrderDTO dto = buildTeemopayBankPayoutRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);

        executeJsonPost(PAYOUT_API_PATH, jsonBody, signature);

        System.out.println("\n========== Teemopay Bank Payout Demo End ==========");
    }

    /**
     * 仅测试签名（不发请求）
     */
    @Test
    public void testTeemopaySignatureOnly() {
        System.out.println("========== Teemopay Signature Test ==========");

        PayoutCreateOrderDTO dto = buildTeemopayBankPayoutRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);
    }

    /**
     * 查询平台侧 Teemopay 支持的银行列表（会真实发起 HTTP 请求）
     */
    @Test
    public void testTeemopayGetPayoutBankDemo() throws IOException {
        System.out.println("========== Teemopay Get Payout Bank Demo Start ==========");

        PayoutBankDTO dto = buildTeemopayPayoutBankRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);

        executeJsonPost(PAYOUT_BANK_API_PATH, jsonBody, signature);

        System.out.println("\n========== Teemopay Get Payout Bank Demo End ==========");
    }

    /**
     * 查询平台侧 Teemopay 余额（会真实发起 HTTP 请求）
     */
    @Test
    public void testTeemopayQueryBalanceDemo() throws IOException {
        System.out.println("========== Teemopay Query Balance Demo Start ==========");

        Map<String, Object> dto = buildTeemopayBalanceRequest();
        String jsonBody = JSON.toJSONString(dto);
        System.out.println("Request Params: " + jsonBody);

        String signature = SignUtil.createSign(JSON.parseObject(jsonBody), APP_SECRET);
        System.out.println("Generated Signature: " + signature);

        executeJsonPost(PAYOUT_BALANCE_API_PATH, jsonBody, signature);

        System.out.println("\n========== Teemopay Query Balance Demo End ==========");
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
        } catch (SocketTimeoutException | ConnectException e) {
            System.out.println("\n=== HTTP Response Skipped ===");
            System.out.println("Reason: 本地联调服务未就绪，跳过真实 HTTP 调试");
            System.out.println("URL: " + url);
            System.out.println("Detail: " + e.getMessage());
        }
    }

    private PayoutCreateOrderDTO buildTeemopayBankPayoutRequest() {
        PayoutCreateOrderDTO dto = new PayoutCreateOrderDTO();
        dto.setRequestId("TEEMOPAY_PO_" + System.currentTimeMillis());
        dto.setAmount("1000");
        dto.setCurrency("PKR");
        dto.setWayCode(WAY_CODE);
        dto.setProductName("teemopay payout product");
        dto.setDescription("Teemopay Pakistan bank payout demo");
        dto.setNotifyUrl("https://www.example.com/payout-notify");

        BeneficiaryDTO beneficiary = new BeneficiaryDTO();
        beneficiary.setBeneficiaryType("individual");

        BeneficiaryInfoDTO beneficiaryInfo = new BeneficiaryInfoDTO();
        beneficiaryInfo.setName("AHMED TEST");
        beneficiaryInfo.setNationalId("3520212345671");
        beneficiaryInfo.setEmail("ahmed@example.com");
        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setNumber("3000000000");
        beneficiaryInfo.setPhone(phoneDTO);
        beneficiary.setBeneficiaryInfo(beneficiaryInfo);

        DestinationDTO destination = new DestinationDTO();
        destination.setType("bank");

        BankCodesDTO bankCodes = new BankCodesDTO();
        bankCodes.setBankCode("MEEZAN_BANK");

        BankDTO bank = new BankDTO();
        bank.setCountry("PK");
        bank.setBankName("MEEZAN_BANK");
        bank.setAccountNumber("00112233445566");
        bank.setIban("PK36SCBL0000001123456702");
        bank.setBankCodes(bankCodes);

        destination.setBank(bank);
        beneficiary.setDestination(destination);
        dto.setBeneficiary(beneficiary);
        return dto;
    }

    private PayoutBankDTO buildTeemopayPayoutBankRequest() {
        PayoutBankDTO dto = new PayoutBankDTO();
        dto.setBankCountryCode("PK");
        dto.setCurrency("PKR");
        dto.setWayCode(WAY_CODE);
        return dto;
    }

    private Map<String, Object> buildTeemopayBalanceRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("currency", "PKR");
        request.put("way_code", WAY_CODE);
        return request;
    }
}
