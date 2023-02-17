package demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;


// 需对，底层有一个监控线程死循环运行，主线程等待10s后，终止监控线程
@Slf4j
public class Test01 {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                // 获取并清除打断标记，因为我已经处理了
                if (Thread.interrupted()) {
                    log.info("料理后事");
                    break;
                }
                try {
                    log.info("正在监控");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    // 设置打断标记，会进入if逻辑
                    Thread.currentThread().interrupt();
                }
            }
        }, "监控任务线程");

        thread.start();

        TimeUnit.SECONDS.sleep(10);
        log.info("打断监控线程");
        thread.interrupt();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(thread.isInterrupted());
    }
}
//
// class
//
// class TPTInterrupt {
//     private Thread thread;
//
//     public void start() {
//         thread = new Thread(() -> {
//             while (true) {
//                 Thread current = Thread.currentThread();
//                 if (current.isInterrupted()) {
//                     log.debug("料理后事");
//                     break;
//                 }
//                 try {
//                     Thread.sleep(1000);
//                     log.debug("将结果保存");
//                 } catch (InterruptedException e) {
//                     current.interrupt();
//                 }
//                 // 执行监控操作
//             }
//         }, "监控线程");
//         thread.start();
//     }
//
//     public void stop() {
//         thread.interrupt();
//     }
// }
//
// class TPTVolatile {
//     private Thread thread;
//     private volatile boolean stop = false;
//
//     public void start() {
//         thread = new Thread(() -> {
//             while (true) {
//                 Thread current = Thread.currentThread();
//                 if (stop) {
//                     log.debug("料理后事");
//                     break;
//                 }
//                 try {
//                     Thread.sleep(1000);
//                     log.debug("将结果保存");
//                 } catch (InterruptedException e) {
//                 }
//                 // 执行监控操作
//             }
//         }, "监控线程");
//         thread.start();
//     }
//
//     public void stop() {
//         stop = true;
//         thread.interrupt();
//     }
// }
