package com.dpe.lesspay2.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

/**
 * Channel extra parameters
 *
 * @author Leo
 * @create 2025-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ChannelExtraDTO implements Serializable {

    /**
     * extraType
     * payment session
     * card
     * token
     */
    @JSONField(name = "extra_type", alternateNames = {"extraType"})
    @JsonProperty("extra_type")
    private String extraType;

    /**
     * Card information
     */
    @JSONField(name = "card_data", alternateNames = {"cardData"})
    @JsonProperty("card_data")
    private CardData cardData;

    /**
     * Token information
     */
    @JSONField(name = "token_data", alternateNames = {"tokenData"})
    @JsonProperty("token_data")
    private TokenData tokenData;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static  class  TokenData {
        /**
         * Payment token
         */
        private String token;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static  class  CardData {
        /**
         * Card number (required when pay_type=1)
         * Format: 16-19 digits, no separator
         */
        @JSONField(name = "number")
        @JsonProperty("number")
        private String number;

        /**
         * Expiry month (required when pay_type=1)
         * Format: MM (01-12)
         */
        @JSONField(name = "expiry_month")
        @JsonProperty("expiry_month")
        private Integer expiryMonth;

        /**
         * Expiry year (required when pay_type=1)
         * Format: YYYY (4-digit year)
         */
        @JSONField(name = "expiry_year")
        @JsonProperty("expiry_year")
        private Integer expiryYear;

        /**
         * CVV (Card Verification Value)
         * When pay_type=1: Plain text CVV (3-4 digits)
         *
         */
        @JSONField(name = "cvv")
        @JsonProperty("cvv")
        private String cvv;

        /**
         * Whether to store card information for future use (optional, default: false)
         * - true: Store card information and return Token for future use
         * - false: Do not store, only for this payment
         */
        @JSONField(name = "store_for_future_use")
        @JsonProperty("store_for_future_use")
        private Boolean storeForFutureUse;
    }

}
