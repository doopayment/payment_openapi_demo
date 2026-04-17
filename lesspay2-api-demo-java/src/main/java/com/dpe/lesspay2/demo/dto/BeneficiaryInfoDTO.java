package com.dpe.lesspay2.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Beneficiary Information DTO
 * Contains personal/business information of the beneficiary
 *
 * @author Leo
 * @since 2026-02-11
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BeneficiaryInfoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Beneficiary name
     */
    @JSONField(name = "name")
    private String name;


    /**
     * Beneficiary last name
     */
    @JSONField(name = "last_name")
    private String lastName;

    /**
     * Beneficiary first name
     */
    @JSONField(name = "first_name")
    private String firstName;


    /**
     * Beneficiary email
     */
    @JSONField(name = "email")
    private String email;

    /**
     * Phone information
     */
    @JSONField(name = "phone")
    private PhoneDTO phone;

    /**
     * Address information
     */
    @JSONField(name = "address")
    private AddressDTO address;

    /**
     * Tax ID (Optional)
     */
    @JSONField(name = "tax_id")
    private String taxId;

    /**
     * Date of birth of individual, Format DD-MM-YYYY (Mandatory if destination.type is a wallet and type is individual)
     */
    @JSONField(name = "date_of_birth")
    private String dateOfBirth;

    /**
     * ISO 3166-1 alpha-2 country code representing the beneficiary's nationality (e.g., US, GB, IN, FR).
     * Optional field used for enhanced compliance screening.
     */
    @JSONField(name = "nationality")
    private String nationality;

    /**
     * National ID of the individual (Optional)
     */
    @JSONField(name = "national_id")
    private String nationalId;
}
