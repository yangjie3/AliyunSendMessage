package com.zhiyang.media;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 线程池构造工厂
 *
 * @author SHANHY(365384722@QQ.COM)
 * @date   2015年12月4日
 */
public class ExecutorServiceFactory {
    private static ExecutorServiceFactory executorFactory = new ExecutorServiceFactory();
    /**
     * 定时任务线程池
     */
    private ExecutorService executors;

    private ExecutorServiceFactory() {
    }

    public static ExecutorServiceFactory getInstance() {
        return executorFactory;
    }

 
    public ExecutorService createFixedThreadPool(int count) {
        // 创建
        executors = Executors.newFixedThreadPool(count, getThreadFactory());
        return executors;
    }


    /**
     * 获取线程池工厂
     * 
     * @return
     */
    private ThreadFactory getThreadFactory() {
        return new ThreadFactory() {
            AtomicInteger sn = new AtomicInteger();
            public Thread newThread(Runnable r) {
                SecurityManager s = System.getSecurityManager();
                ThreadGroup group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
                Thread t = new Thread(group, r);
                t.setName("任务线程 - " + sn.incrementAndGet());
                return t;
            }
        };
    }
}