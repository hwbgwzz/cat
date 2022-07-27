package com.cat.component.jackson.provide;

import com.cat.common.toolkit.json.JSON;
import com.cat.component.jackson.spi.JacksonProvide;
import org.springframework.stereotype.Component;

import java.util.ServiceLoader;

/**
 * SPI服务管理
 * Jackson module provide
 */
@Component
public class ProvideManager {

    static {
        for (JacksonProvide provide : ServiceLoader.load(JacksonProvide.class)) {
            provide.provide(JSON.getInstance());
        }
    }

}
