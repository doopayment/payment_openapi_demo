package com.dpe.lesspay2.demo.example.payout;

import com.alibaba.fastjson.JSON;
import com.dpe.lesspay2.demo.dto.*;
import com.dpe.lesspay2.demo.util.SignUtil;
import okhttp3.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Payout API Call Example - Using wayType (not wayCode)
 * 
 * Demonstrates how to call the /api/global/payout/create-order endpoint
 * with three payment types:
 * 1. BANK_TRANSFER - Bank transfer payout
 * 2. E_WALLET - E-wallet payout
 * 3. CRYPTO - Cryptocurrency payout
 * 
 * Key Difference from waycode examples:
 * - waycode examples: specify specific channel codes (e.g., "EPAY_PAYOUT")
 * - wayType examples: specify payment types without specific channel
 * 
 * Usage:
 * 1. Modify the APP_ID, APP_SECRET, BASE_URL configuration below
 * 2. Run the test methods: testBankTransferPayout(), testEWalletPayout(), testCryptoPayout()
 */
public class PayoutExample {

    // ======== Configuration - Please modify to real values ========
    private static final String APP_ID = "YOUR_APP_ID";
    private static final String APP_SECRET = "YOUR_APP_SECRET";
    private static final String BASE_URL = "https://lesspay2-pay-uat.doopayment.com";
    // ===============================================================

    private static final String PAYOUT_API_PATH = "/api/global/payout/create-order";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * Test 1: Bank Transfer Payout Demo
     * Uses wayType = "BANK_TRANSFER" (no wayCode specified)
     */
    @Test
    public void testBankTransferPayout() throws IOException {
        System.out.println("========== Bank Transfer Payout Demo Start ==========");

        PayoutCreateOrderDTO dto = buildBankTransferRequest();
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

        System.out.println("\n========== Bank Transfer Payout Demo End ==========");
    }

    /**
     * Test 2: E-Wallet Payout Demo
     * Uses wayType = "E_WALLET" (no wayCode specified)
     */
    @Test
    public void testEWalletPayout() throws IOException {
        System.out.println("========== E-Wallet Payout Demo Start ==========");

        PayoutCreateOrderDTO dto = buildEWalletRequest();
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

        System.out.println("\n========== E-Wallet Payout Demo End ==========");
    }

    /**
     * Test 3: Cryptocurrency Payout Demo
     * Uses wayType = "CRYPTO" (no wayCode specified)
     */
    @Test
    public void testCryptoPayout() throws IOException {
        System.out.println("========== Cryptocurrency Payout Demo Start ==========");

        PayoutCreateOrderDTO dto = buildCryptoRequest();
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

        System.out.println("\n========== Cryptocurrency Payout Demo End ==========");
    }

    /**
     * Build Bank Transfer Payout Request
     * wayType = "BANK_TRANSFER", no wayCode specified
     */
    private PayoutCreateOrderDTO buildBankTransferRequest() {
        PayoutCreateOrderDTO dto = new PayoutCreateOrderDTO();

        // Basic order information
        dto.setRequestId("BANK_PO_" + System.currentTimeMillis() + randomInt(1000, 9999));
        dto.setAmount("100.50");
        dto.setCurrency("PHP");
        dto.setProductName("Bank Transfer Payout");
        dto.setDescription("Bank transfer payout using wayType");
        dto.setWayType("BANK_TRANSFER");  // Use wayType instead of wayCode
        dto.setExpiredTime(86400);
        dto.setNotifyUrl("https://www.example.com/payout-notify");

        // Beneficiary information
        BeneficiaryDTO beneficiary = new BeneficiaryDTO();
        beneficiary.setBeneficiaryType("individual");

        BeneficiaryInfoDTO info = new BeneficiaryInfoDTO();
        info.setFirstName("John");
        info.setLastName("Doe");
        info.setDateOfBirth("1990-01-15");
        info.setNationality("PH");

        PhoneDTO phone = new PhoneDTO();
        phone.setNumber("9171234567");
        info.setPhone(phone);

        AddressDTO address = new AddressDTO();
        address.setCountry("PH");
        address.setState("Metro Manila");
        address.setCity("Manila");
        address.setLine1("123 Main Street");
        info.setAddress(address);

        beneficiary.setBeneficiaryInfo(info);

        // Destination - Bank
        DestinationDTO destination = new DestinationDTO();
        destination.setType("bank");

        BankDTO bank = new BankDTO();
        bank.setAccountNumber("1234567890123");
        BankCodesDTO codes = new BankCodesDTO();
        codes.setBankCode("SMBP");
        bank.setBankCodes(codes);

        destination.setBank(bank);
        beneficiary.setDestination(destination);
        dto.setBeneficiary(beneficiary);

        return dto;
    }

    /**
     * Build E-Wallet Payout Request
     * wayType = "E_WALLET", no wayCode specified
     */
    private PayoutCreateOrderDTO buildEWalletRequest() {
        PayoutCreateOrderDTO dto = new PayoutCreateOrderDTO();

        // Basic order information
        dto.setRequestId("EWALLET_PO_" + System.currentTimeMillis() + randomInt(1000, 9999));
        dto.setAmount("50.75");
        dto.setCurrency("PHP");
        dto.setProductName("E-Wallet Payout");
        dto.setDescription("E-wallet payout using wayType");
        dto.setWayType("WALLET_TRANSFER");  // Use wayType instead of wayCode
        dto.setExpiredTime(86400);
        dto.setNotifyUrl("https://www.example.com/payout-notify");

        // Beneficiary information
        BeneficiaryDTO beneficiary = new BeneficiaryDTO();
        beneficiary.setBeneficiaryType("individual");

        BeneficiaryInfoDTO info = new BeneficiaryInfoDTO();
        info.setFirstName("Maria");
        info.setLastName("Santos");
        info.setDateOfBirth("1995-05-20");
        info.setNationality("PH");

        PhoneDTO phone = new PhoneDTO();
        phone.setNumber("9189876543");
        info.setPhone(phone);

        AddressDTO address = new AddressDTO();
        address.setCountry("PH");
        address.setState("Cebu");
        address.setCity("Cebu City");
        address.setLine1("456 Second Avenue");
        info.setAddress(address);

        beneficiary.setBeneficiaryInfo(info);

        // Destination - E-Wallet
        DestinationDTO destination = new DestinationDTO();
        destination.setType("ewallet");

        EWalletDTO eWallet = new EWalletDTO();
        eWallet.setEwalletProvider("GCash");  // Example: GCash provider
        eWallet.setAccountNumber("09189876543");
        eWallet.setAccountType("phone");

        destination.setEWallet(eWallet);
        beneficiary.setDestination(destination);
        dto.setBeneficiary(beneficiary);

        return dto;
    }

    /**
     * Build Cryptocurrency Payout Request
     * wayType = "CRYPTO", no wayCode specified
     */
    private PayoutCreateOrderDTO buildCryptoRequest() {
        PayoutCreateOrderDTO dto = new PayoutCreateOrderDTO();

        // Basic order information
        dto.setRequestId("CRYPTO_PO_" + System.currentTimeMillis() + randomInt(1000, 9999));
        dto.setAmount("100.00");
        dto.setCurrency("USDT");
        dto.setProductName("Crypto Payout");
        dto.setDescription("Cryptocurrency payout using wayType");
        dto.setWayType("CRYPTO");  // Use wayType instead of wayCode
        dto.setExpiredTime(86400);
        dto.setNotifyUrl("https://www.example.com/payout-notify");

        // Beneficiary information
        BeneficiaryDTO beneficiary = new BeneficiaryDTO();
        beneficiary.setBeneficiaryType("individual");

        BeneficiaryInfoDTO info = new BeneficiaryInfoDTO();
        info.setFirstName("Alex");
        info.setLastName("Chen");
        info.setDateOfBirth("1988-12-10");
        info.setNationality("US");

        PhoneDTO phone = new PhoneDTO();
        phone.setNumber("15551234567");
        info.setPhone(phone);

        AddressDTO address = new AddressDTO();
        address.setCountry("US");
        address.setState("California");
        address.setCity("San Francisco");
        address.setLine1("789 Crypto Street");
        info.setAddress(address);

        beneficiary.setBeneficiaryInfo(info);

        // Destination - Crypto Wallet
        DestinationDTO destination = new DestinationDTO();
        destination.setType("crypto_wallet");

        CryptoWalletDTO cryptoWallet = new CryptoWalletDTO();
        cryptoWallet.setWalletAddress("TXYZabcdefghijklmnopqrstuvwxyz123456");  // Example USDT TRC20 address
        cryptoWallet.setNetwork("TRC20");  // Example: TRC20 network

        destination.setCryptoWallet(cryptoWallet);
        beneficiary.setDestination(destination);
        dto.setBeneficiary(beneficiary);

        return dto;
    }

    /**
     * Generate random integer within specified range
     */
    private int randomInt(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }
}
