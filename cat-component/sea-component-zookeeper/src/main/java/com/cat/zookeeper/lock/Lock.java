package com.cat.zookeeper.lock;

public interface Lock {
    /**
     * Lock method
     *
     * @return Whether the lock is successfully added
     */
    boolean lock() throws Exception;

    /**
     * Unlock method
     *
     * @return Whether the Unlock is successfully
     */
    boolean unlock();
}
