package com.dpe.lesspay2.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Payout Create Order Request Parameters
 *
 * @author Leo
 * @since 2025-12-10
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PayoutCreateOrderDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Merchant Request ID, unique under app_id
     */
    @JSONField(name = "request_id")
    private String requestId;


    /**
     * Amount in the smallest currency unit (e.g., 100000 for 1000.00)
     */
    @JSONField(name = "amount")
    private String amount;

    /**
     * Beneficiary (单笔代付使用)
     */
    @JSONField(name = "beneficiary")
    private BeneficiaryDTO beneficiary;

    /**
     * Purpose of the payout (e.g., "PYR001")
     */
    @JSONField(name = "purpose")
    private String purpose;

    /**
     * Target Currency currency code, e.g., PHP, INR, IDR, USD
     */
    @JSONField(name = "currency")
    private String currency;

    /**
     * Product Name
     */
    @JSONField(name = "product_name")
    private String productName;

    /**
     * Product Description
     */
    @JSONField(name = "description")
    private String description;

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
     * Asynchronous Notification URL, supports http/https
     */
    @JSONField(name = "notify_url")
    private String notifyUrl;

}
