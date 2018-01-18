package com.zhiyang.media;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 线程处理类
 *
 * @author SHANHY(365384722@QQ.COM)
 * @date   2015年12月4日
 */
public class ExecutorProcessPool {

    private ExecutorService executor;
    private static ExecutorProcessPool pool = new ExecutorProcessPool();
    private final int threadMax = 500;

    private ExecutorProcessPool() {
        System.out.println("threadMax>>>>>>>" + threadMax);
        executor = ExecutorServiceFactory.getInstance().createFixedThreadPool(threadMax);
    }

    public static ExecutorProcessPool getInstance() {
        return pool;
    }

    
    public void shutdown(){
        executor.shutdown();
    }

    public Future<?> submit(Runnable task) {
        return executor.submit(task);
    }

    
    public void execute(Runnable task){
        executor.execute(task);
    }

}