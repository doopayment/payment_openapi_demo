package com.dpe.lesspay2.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Payin Query Request DTO
 * 
 * Endpoint: POST /api/global/v1/pay/query-order
 */
@Data
public class PayinQueryDTO {

    /**
     * Merchant Request ID (optional, one of request_id or pay_order_id)
     */
    @JSONField(name = "request_id")
    private String requestId;

    /**
     * Platform Order ID (optional, one of request_id or pay_order_id)
     */
    @JSONField(name = "pay_order_id")
    private String payOrderId;

    /**
     * Query start time (milliseconds timestamp)
     * Required
     */
    @JSONField(name = "start_time")
    private Long startTime;

    /**
     * Query end time (milliseconds timestamp)
     * Max span: 7 days
     */
    @JSONField(name = "end_time")
    private Long endTime;

    /**
     * Page number (default: 1)
     */
    @JSONField(name = "page")
    private Integer page = 1;

    /**
     * Page size (default: 20, max: 1000)
     */
    @JSONField(name = "page_size")
    private Integer pageSize = 20;
}
