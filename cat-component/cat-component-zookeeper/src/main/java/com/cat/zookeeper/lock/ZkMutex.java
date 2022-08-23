package com.cat.zookeeper.lock;

import com.cat.common.toolkit.context.SpringContextUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 *use distributed lock of Curator client，such as InterProcessMutex
 */
public class ZkMutex implements Lock{
    //ZkLock的节点链接
    private static final String ZK_PATH = "/lock/";

    //Zk客户端
    private static CuratorFramework client = null;

    private String lock_prefix = null;
    private String locked_final_path = null;

    private InterProcessMutex zkMutex = null;

    static {
        client = SpringContextUtils.getBean(CuratorFramework.class);
    }

    public ZkMutex(String lockModuleName, String lockBusinessName) throws Exception {
        locked_final_path = ZK_PATH + lockModuleName+ "/" + lockBusinessName;
        lock_prefix = locked_final_path + "/";
        synchronized (ZkMutex.client) {
            if (zkMutex == null) {
                zkMutex = new InterProcessMutex(client, lock_prefix);
            }
        }
    }

    @Override
    public boolean lock() throws Exception {
        //获取互斥锁
        zkMutex.acquire();
        return true;
    }

    @Override
    public boolean unlock() {
        //获取互斥锁
        try {
            zkMutex.acquire();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
