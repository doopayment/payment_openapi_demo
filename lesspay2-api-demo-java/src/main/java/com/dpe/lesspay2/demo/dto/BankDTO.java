package com.dpe.lesspay2.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Bank Details DTO
 *
 * @author Leo
 * @since 2026-02-11
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BankDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Account type: savings, checking, or payment
     */
    @JSONField(name = "account_type")
    private String accountType;

    /**
     * Bank country code (ISO 3166-1 alpha-2, e.g., US, PH, IN)
     */
    @JSONField(name = "country")
    private String country;

    /**
     * province of the receiving bank
     */
    @JSONField(name = "bank_province")
    private String bankProvince;

    /**
     * city of the receiving bank
     */
    @JSONField(name = "bank_city")
    private String bankCity;

    /**
     * Bank name
     */
    @JSONField(name = "bank_name")
    private String bankName;

    /**
     * Bank account number (PCI sensitive field)
     * Either account_number or iban is mandatory
     */
    @JSONField(name = "account_number")
    private String accountNumber;

    /**
     * IBAN (International Bank Account Number)
     * Either account_number or iban is mandatory
     */
    @JSONField(name = "iban")
    private String iban;

    /**
     * Bank codes (SWIFT, ABA, etc.)
     */
    @JSONField(name = "bank_codes")
    private BankCodesDTO bankCodes;

    /**
     * Purpose code (Optional)
     */
    @JSONField(name = "purpose_code")
    private String purposeCode;

    /**
     * Branch name (Optional)
     */
    @JSONField(name = "branch_name")
    private String branchName;
}
