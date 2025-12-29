package com.dpe.lesspay2.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Payout Supported Banks Request DTO
 * 
 * Endpoint: POST /api/global/payout/bank
 */
@Data
public class PayoutBankDTO {

    /**
     * Bank Country Code (ISO 3166-1 alpha-2, e.g., PH, IN, ID)
     */
    @JSONField(name = "bank_country_code")
    private String bankCountryCode;

    /**
     * Target Currency, 3-letter currency code, e.g., PHP, INR, IDR
     */
    @JSONField(name = "currency")
    private String currency;

    /**
     * Payment Way Code: TAZAPAY_PAYOUT
     */
    @JSONField(name = "way_code")
    private String wayCode;
}
