package com.dpe.lesspay2.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class PayoutEWalletDTO {

    @JSONField(name = "account_name")
    private String accountName;

    /**
     * 账户类型: individual-个人, business-公司
     */
    @JSONField(name = "account_type")
    private String accountType;

    @JSONField(name = "account_number")
    private String accountNumber;

    @JSONField(name = "provider")
    private String provider;

    /**
     * 存款钥匙的类型 (e.g. random, pix_key, upi_handle)
     */
    @JSONField(name = "deposit_key_type")
    private String depositKeyType;

    /**
     * 存款密钥，对应本地支付网络类型（例如，PIX 的 PIX 密钥，UPI 的 UPI 手柄等）
     */
    @JSONField(name = "deposit_key")
    private String depositKey;
}