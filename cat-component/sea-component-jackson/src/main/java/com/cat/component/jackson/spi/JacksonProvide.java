package com.cat.component.jackson.spi;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * interface for Jackson module provide
 * implementer use @AutoService(JacksonProvide.class)
 */
public interface JacksonProvide {

    /**
     * provide objectMapper
     * @param objectMapper
     */
    void provide(ObjectMapper objectMapper);

}
