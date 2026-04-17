package com.dpe.lesspay2.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Beneficiary Details DTO
 * Restructured to separate beneficiary type and info
 *
 * @author Leo
 * @since 2026-02-11
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BeneficiaryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Beneficiary type: "individual" or "business"
     */
    @JSONField(name = "beneficiary_type")
    private String beneficiaryType;

    /**
     * Beneficiary information (personal or business details)
     */
    @JSONField(name = "beneficiary_info")
    private BeneficiaryInfoDTO beneficiaryInfo;

    /**
     * Destination (bank or wallet destination)
     */
    @JSONField(name = "destination")
    private DestinationDTO destination;
}
