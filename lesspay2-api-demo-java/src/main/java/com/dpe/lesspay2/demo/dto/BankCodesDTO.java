package com.dpe.lesspay2.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Bank Codes DTO - Contains various bank-specific codes
 *
 * @author Leo
 * @since 2026-02-11
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BankCodesDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * SWIFT code (8 or 11 characters)
     */
    @JSONField(name = "swift_code")
    private String swiftCode;

    /**
     * IFS Code (Indian Financial System Code)
     */
    @JSONField(name = "ifsc_code")
    private String ifscCode;

    /**
     * ABA / Routing Number (used for US banks)
     */
    @JSONField(name = "aba_code")
    private String abaCode;

    /**
     * SORT Code (used for UK banks)
     */
    @JSONField(name = "sort_code")
    private String sortCode;

    /**
     * Branch Code
     */
    @JSONField(name = "branch_code")
    private String branchCode;

    /**
     * BSB Code (Bank State Branch, used for Australian banks)
     */
    @JSONField(name = "bsb_code")
    private String bsbCode;

    /**
     * Bank Code
     */
    @JSONField(name = "bank_code")
    private String bankCode;

    /**
     * CNAPS (China National Advanced Payment System)
     */
    @JSONField(name = "cnaps")
    private String cnaps;
}
