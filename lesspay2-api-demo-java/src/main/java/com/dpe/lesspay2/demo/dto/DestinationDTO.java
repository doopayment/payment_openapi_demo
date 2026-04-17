package com.dpe.lesspay2.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Destination Details DTO
 *
 * @author Leo
 * @since 2026-02-11
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DestinationDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Destination type: "bank", "bank_account", "crypto_wallet" or "ewallet"
//     */
    @JSONField(name = "type")
    private String type;

    /**
     * Bank (required when type is "bank")
     */
    @JSONField(name = "bank")
    private BankDTO bank;


    /**
     * Crypto wallet details (required when type is "crypto_wallet")
     */
    @JSONField(name = "crypto_wallet")
    private CryptoWalletDTO cryptoWallet;

    /**
     * E-wallet details (required when type is "ewallet")
     */
    @JSONField(name = "e_wallet")
    private EWalletDTO eWallet;
}
