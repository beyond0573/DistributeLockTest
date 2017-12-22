package com.zlh.redistest;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class RedisLock {


    public Jedis jedis;

    private String lockKey;

    private String lockValue;

    private boolean locked = false;

    public RedisLock(String lockKey){
        this.jedis=new Jedis("redis://localhost:6379/9");
        this.lockKey=lockKey;
    }

    /**
     * 尝试获得锁，成功返回true，如果失败或异常立即返回false
     *
     * @param lockSeconds 加锁的时间(秒)，超过这个时间后锁会自动释放
     */
    public String tryLock(int lockSeconds) {

        long nowTime = System.currentTimeMillis();
        lockValue= UUID.randomUUID().toString();
        long end=nowTime+lockSeconds*1000;

        while(System.currentTimeMillis()<end) {
            if (jedis.setnx(lockKey, lockValue) > 0) {
                jedis.expire(lockKey, lockSeconds);
                return lockValue;
            }
            else if(jedis.ttl(lockKey) ==-1){
                jedis.expire(lockKey, lockSeconds);
            }
            long millis=10;
            try {

                /*
                延迟100 毫秒,  这里使用随机时间可能会好一点,可以防止饥饿进程的出现,即,当同时到达多个进程,
                只会有一个进程获得锁,其他的都用同样的频率进行尝试,后面有来了一些进行,也以同样的频率申请锁,这将可能导致前面来的锁得不到满足.
                使用随机的等待时间可以一定程度上保证公平性
             */
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return "false";
    }

//    private boolean tryLock(RedisConnection conn, int lockSeconds) throws Exception {
//        long nowTime = System.currentTimeMillis();
//        long expireTime = nowTime + lockSeconds * 1000 + 1000; // 容忍不同服务器时间有1秒内的误差
//        if (conn.setNX(lockKey, longToBytes(expireTime))) {
//            conn.expire(lockKey, lockSeconds);
//            return true;
//        } else {
//            byte[] oldValue = conn.get(lockKey);
//            if (oldValue != null && bytesToLong(oldValue) < nowTime) {
//                // 这个锁已经过期了，可以获得它
//                // PS: 如果setNX和expire之间客户端发生崩溃，可能会出现这样的情况
//                byte[] oldValue2 = conn.getSet(lockKey, longToBytes(expireTime));
//                if (Arrays.equals(oldValue, oldValue2)) {
//                    // 获得了锁
//                    conn.expire(lockKey, lockSeconds);
//                    return true;
//                } else {
//                    // 被别人抢占了锁(此时已经修改了lockKey中的值，不过误差很小可以忽略)
//                    return false;
//                }
//            }
//        }
//        return false;
//    }



//    /**
//     * 尝试获得锁，成功返回true，如果失败或异常立即返回false
//     *
//     * @param lockSeconds 加锁的时间(秒)，超过这个时间后锁会自动释放
//     */
    public String tryLock2(int lockSeconds) {
        // Jedis jedis = new Jedis("redis://localhost:6379/9");
        long nowTime = System.currentTimeMillis();
        lockValue= UUID.randomUUID().toString();

        long end=nowTime+lockSeconds*1000;
        while(System.currentTimeMillis()<end){

            if (jedis.set(lockKey,lockValue,"NX","EX",lockSeconds)!=null) {

                return lockValue;
            }
            long millis=1;
            try {

                /*
                延迟100 毫秒,  这里使用随机时间可能会好一点,可以防止饥饿进程的出现,即,当同时到达多个进程,
                只会有一个进程获得锁,其他的都用同样的频率进行尝试,后面有来了一些进行,也以同样的频率申请锁,这将可能导致前面来的锁得不到满足.
                使用随机的等待时间可以一定程度上保证公平性
             */
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "false";
    }

    /**
     * 如果加锁后的操作比较耗时，调用方其实可以在unlock前根据时间判断下锁是否已经过期
     * 如果已经过期可以不用调用，减少一次请求
     */
    public boolean unlock(String lockKey,String identifier) {
        //jedis.del(new String(lockKey));
        if(jedis.get(lockKey).equals(identifier)){
            Transaction tx=jedis.multi();
            tx.del(lockKey);
            tx.exec();
           // locked = false;
            return true;

        }
        return false;
    }


//    /**
//     * 轮询的方式去获得锁，成功返回true，超过轮询次数或异常返回false
//     *
//     * @param lockSeconds       加锁的时间(秒)，超过这个时间后锁会自动释放
//     * @param tryIntervalMillis 轮询的时间间隔(毫秒)
//     * @param maxTryCount       最大的轮询次数
//     */
//    public boolean tryLock(final int lockSeconds, final long tryIntervalMillis, final int maxTryCount) {
//        int tryCount = 0;
//        while (true) {
//            if (++tryCount >= maxTryCount) {
//                // 获取锁超时
//                return false;
//            }
//            try {
//                if (doTryLock(lockSeconds)) {
//                    return true;
//                }
//            } catch (Exception e) {
//                logger.error("tryLock Error", e);
//                return false;
//            }
//            try {
//                Thread.sleep(tryIntervalMillis);
//            } catch (InterruptedException e) {
//                logger.error("tryLock interrupted", e);
//                return false;
//            }
//        }
//    }


}
