package com.dpe.lesspay2.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * E-Wallet DTO
 *
 * @author Leo
 * @since 2026-02-11
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EWalletDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * E-wallet provider (e.g., GoPay, OVO, Dana)
     */
    @JSONField(name = "ewallet_provider")
    private String ewalletProvider;

    /**
     * Account number
     */
    @JSONField(name = "account_number")
    private String accountNumber;

    /**
     * Account type, primarily used as a basis for classification during account pix key verification
     * values: cpf:Individual CPF (tax ID), cnpj:Corporate CNPJ (tax ID), email:Email address, phone:Phone number, random:Random string
     */
    @JSONField(name = "account_type")
    private String accountType;

    /**
     * Account pix key to be verified
     */
    @JSONField(name = "deposit_key")
    private String depositKey;
}
