package com.dpe.lesspay2.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Address Information DTO
 *
 * @author Leo
 * @since 2026-02-11
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddressDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Country code (ISO 3166-1 alpha-2, e.g., US, CN, PH)
     */
    @JSONField(name = "country")
    private String country;

    /**
     * State or province
     */
    @JSONField(name = "state")
    private String state;

    /**
     * City
     */
    @JSONField(name = "city")
    private String city;

    /**
     * Postal code
     */
    @JSONField(name = "postal_code")
    private String postalCode;

    /**
     * Address line 1
     */
    @JSONField(name = "line1")
    private String line1;

    /**
     * Address line 2 (Optional)
     */
    @JSONField(name = "line2")
    private String line2;
}
