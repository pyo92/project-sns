package com.example.projectsns.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

@Configuration
public class ThymeleafConfig {

    /**
     * Thymeleaf configuration
     */
    @Bean
    public SpringResourceTemplateResolver thymeleafTemplateResolver (SpringResourceTemplateResolver defaultTemplateResolver) {
        defaultTemplateResolver.setUseDecoupledLogic(true); //Thymeleaf decoupled logic 활성화

        return defaultTemplateResolver;
    }
}
