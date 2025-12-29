package com.dpe.lesspay2.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Lesspay2 API Configuration Class
 * 
 * Read configuration from application.yml:
 * - base-url: API gateway URL
 * - app-id: Merchant AppId
 * - app-secret: Merchant AppSecret (used for signature)
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "lesspay2.api")
public class ApiConfig {

    /**
     * API gateway URL
     * Example: https://sandbox-gateway.doopayment.com
     */
    private String baseUrl;

    /**
     * Merchant AppId
     */
    private String appId;

    /**
     * Merchant AppSecret (used for signature)
     */
    private String appSecret;
}
