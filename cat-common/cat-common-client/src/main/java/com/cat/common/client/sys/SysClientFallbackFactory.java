package com.cat.common.client.sys;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SysClientFallbackFactory implements FallbackFactory<SysClient> {
    private final SysClientFallback sysClientFallback;

    @Override
    public SysClient create(Throwable cause) {
        log.error(cause.getMessage(), cause);
        return sysClientFallback;
    }

}
