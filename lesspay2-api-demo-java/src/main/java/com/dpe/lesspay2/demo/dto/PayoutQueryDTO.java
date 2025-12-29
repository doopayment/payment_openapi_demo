package com.dpe.lesspay2.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Payout Query Request DTO
 * 
 * Endpoint: POST /api/global/payout/query
 */
@Data
public class PayoutQueryDTO {

    /**
     * Merchant Request ID (One of request_id or pay_order_id is required)
     */
    @JSONField(name = "request_id")
    private String requestId;

    /**
     * Unique payout order ID (One of request_id or pay_order_id is required)
     */
    @JSONField(name = "pay_order_id")
    private String payOrderId;
}
