package com.dpe.lesspay2.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Crypto Wallet DTO
 *
 * @author Leo
 * @since 2026-02-11
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CryptoWalletDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Wallet address
     */
    @JSONField(name = "wallet_address")
    private String walletAddress;

    /**
     * Network type (e.g., TRC20, ERC20, BEP20)
     */
    @JSONField(name = "network")
    private String network;
}
