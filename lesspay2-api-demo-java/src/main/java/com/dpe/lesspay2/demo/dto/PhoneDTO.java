package com.dpe.lesspay2.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Phone Information DTO
 *
 * @author Leo
 * @since 2026-02-11
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PhoneDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * International calling code (e.g., "1" for US, "86" for China)
     */
    @JSONField(name = "calling_code")
    private String callingCode;

    /**
     * Phone number without country code
     */
    @JSONField(name = "number")
    private String number;
}
