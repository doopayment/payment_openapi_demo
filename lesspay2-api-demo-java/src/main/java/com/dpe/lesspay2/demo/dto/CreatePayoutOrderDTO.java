package com.dpe.lesspay2.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * Batch Payout Request DTO
 * 
 * Endpoint: POST /api/global/payout/batch/create-order
 */
@Data
public class CreatePayoutOrderDTO {

    /**
     * Merchant Request ID, unique under app_id
     */
    @JSONField(name = "request_id")
    private String requestId;

    /**
     * Product Name (Optional)
     */
    @JSONField(name = "product_name")
    private String productName;

    /**
     * Product Description (Optional)
     */
    @JSONField(name = "description")
    private String description;

    /**
     * Target Currency, 3-letter currency code, e.g., PHP, INR, IDR
     */
    @JSONField(name = "currency")
    private String currency;

    /**
     * Total Order Amount (Unit: yuan, e.g., "1500.00"), must equal the sum of
     * detail amounts
     */
    @JSONField(name = "total_amount")
    private String totalAmount;

    /**
     * Payment Way Code
     */
    @JSONField(name = "way_code")
    private String wayCode;

    /**
     * Payment Way Type: BANK_TRANSFER, E_WALLET, etc.
     */
    @JSONField(name = "way_type")
    private String wayType;

    /**
     * Order Expiration Time, Unit: Seconds
     */
    @JSONField(name = "expired_time")
    private Integer expiredTime;

    /**
     * Transaction Network (Optional), some payment ways require specifying the
     * network
     */
    @JSONField(name = "transaction_network")
    private String transactionNetwork;

    /**
     * Async Notification URL, supports http/https
     */
    @JSONField(name = "notify_url")
    private String notifyUrl;

    /**
     * Payout Detail List
     */
    @JSONField(name = "payout_order_details")
    private List<PayoutOrderDetailDTO> payoutOrderDetails;

    /**
     * Payout Detail DTO
     */
    @Data
    public static class PayoutOrderDetailDTO {

        /**
         * Merchant Order ID, Detail Serial Number, unique within a single request
         */
        @JSONField(name = "mch_order_id")
        private String mchOrderId;

        /**
         * Amount (e.g., "1000.00", string with decimal point)
         */
        @JSONField(name = "amount")
        private String amount;

        /**
         * Bank Account Number
         */
        @JSONField(name = "bank_account_no")
        private String bankAccountNo;

        /**
         * Account Holder Name
         */
        @JSONField(name = "bank_account_name")
        private String bankAccountName;

        /**
         * Account Holder Type: business or individual
         */
        @JSONField(name = "bank_account_type")
        private String bankAccountType;

        /**
         * Bank Name
         */
        @JSONField(name = "bank_name")
        private String bankName;

        /**
         * Bank Country Code (ISO 3166-1 alpha-2, e.g., PH, IN, ID)
         */
        @JSONField(name = "bank_country_code")
        private String bankCountryCode;

        /**
         * Bank SWIFT network code (8 or 11 characters)
         */
        @JSONField(name = "bank_swift_code")
        private String bankSwiftCode;
    }
}
