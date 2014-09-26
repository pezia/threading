package multithreading;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zsolt
 */
public class Multithreading {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        doDeadlock();
    }

    private static void doCallable() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newCachedThreadPool();
        List<SlowWorker> workerList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            workerList.add(new SlowWorker(null, "Async worker " + i, 0));
        }

        List<Future<String>> futures = executor.invokeAll(workerList);

        for (Future<String> future : futures) {
            System.out.println(future.get());
        }

        executor.shutdown();
    }

    private static void doExecutor() throws InterruptedException {
        List<String> stringList = Collections.synchronizedList(new ArrayList<>());
        ExecutorService executor = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 10; i++) {
            SlowWorker slowWorker = new SlowWorker(stringList, "Executor " + i, 10);
            executor.submit((Runnable) slowWorker);
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);

        printList(stringList);
    }

    private static void doThreads() {
        List<String> stringList = Collections.synchronizedList(new ArrayList<>());
        List<Thread> threadList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            SlowWorker slowWorker = new SlowWorker(stringList, "Worker " + i, 10);
            Thread thread = new Thread((Runnable) slowWorker);
            thread.start();
            threadList.add(thread);
        }

        threadList.stream().forEach((elem) -> {
            try {
                elem.join();
            } catch (Exception ex) {
            }
        });

        printList(stringList);
    }

    private static void printList(List<String> list) {
        list.stream().forEach((elem) -> System.out.println(elem));

        System.out.println(list.size());
    }

    private static void doDeadlock() throws InterruptedException {
        String hello = "Hello";
        String world = "World";

        Deadlocker deadlocker1 = new Deadlocker(hello, world);
        Deadlocker deadlocker2 = new Deadlocker(world, hello);

        Thread thread1 = new Thread(deadlocker1);
        Thread thread2 = new Thread(deadlocker2);

        thread1.start();
        thread2.start();

        Thread.sleep(1000);

        System.out.println("Waiting for the threads to finish (they will not, kill the application manually!)");

        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            System.out.print(".");
        }
    }

}
