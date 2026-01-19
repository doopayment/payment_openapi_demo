package com.dpe.lesspay2.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Create Payin Order Request DTO
 * 
 * Endpoint: POST /api/global/v1/pay/create-order
 */
@Data
public class CreatePayinOrderDTO {

    /**
     * Merchant Order ID, unique under AppId
     * Required, max 20 characters
     */
    @JSONField(name = "request_id")
    private String requestId;

    /**
     * Product Name
     * Required, max 30 characters
     */
    @JSONField(name = "product_name")
    private String productName;

    /**
     * Product Description
     * Required, max 50 characters
     */
    @JSONField(name = "description")
    private String description;

    /**
     * Order Amount, e.g. "100.00"
     * Required
     */
    @JSONField(name = "target_amount")
    private String targetAmount;

    /**
     * Target Currency, e.g. "USD", "CNY", "USDT"
     * Required
     */
    @JSONField(name = "target_currency")
    private String targetCurrency;

    /**
     * Transaction Type, fixed as "PAY_IN"
     * Required
     */
    @JSONField(name = "transaction_type")
    private String transactionType = "PAY_IN";

    /**
     * Payment Success Redirect URL
     * Required, max 255 characters
     */
    @JSONField(name = "success_url")
    private String successUrl;

    /**
     * Payment Failure Redirect URL
     * Required, max 255 characters
     */
    @JSONField(name = "fail_url")
    private String failUrl;

    /**
     * API Version
     * For merchant F11: use "V1", for all others: use "V2"
     * Required
     */
    @JSONField(name = "api_version")
    private String apiVersion = "V2";

    /**
     * Payment Access Type
     * 1 = cashier, 2 = openapi, defaults to cashier
     * Required
     */
    @JSONField(name = "pay_access_type")
    private Integer payAccessType = 1;

    /**
     * Async Callback Notification URL
     * Optional
     */
    @JSONField(name = "notify_url")
    private String notifyUrl;

    /**
     * Order Expiration Time, Unit: Seconds
     * Optional
     */
    @JSONField(name = "expired_time")
    private Integer expiredTime;

    /**
     * Payment Way Code
     * Optional
     */
    @JSONField(name = "way_code")
    private String wayCode;

    /**
     * Transaction Network, required for some payment methods
     * Optional
     */
    @JSONField(name = "transaction_network")
    private String transactionNetwork;

    /**
     * Payment Way Type: WECHAT, CRYPTO, BANK_TRANSFER, CARD_PAYMENT
     * Optional
     */
    @JSONField(name = "way_type")
    private String wayType;

    /**
     * Whether to use the merchant provided request_id as channel order ID
     * true = yes, false = no
     * Optional
     */
    @JSONField(name = "use_channel_request_id")
    private Boolean useChannelRequestId;

    /**
     * Channel extra parameters for payment channel
     * Optional (Required for Card Payment in OpenAPI mode)
     */
    @JSONField(name = "channel_extra")
    private ChannelExtraDTO channelExtra;
}
