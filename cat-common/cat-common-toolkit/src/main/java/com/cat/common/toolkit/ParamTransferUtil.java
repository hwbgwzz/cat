package com.cat.common.toolkit;

/**
 * 上下文参数传递
 */
public final class ParamTransferUtil {
    private static final ThreadLocal<Object> paramTrans = new ThreadLocal();

    public static void paramTransfer(Object param) {
        paramTrans.set(param);
    }

    public static <T> T paramAcquisition() {
        return (T)paramTrans.get();
    }

    public static void destroy() {
        paramTrans.remove();
    }
}
