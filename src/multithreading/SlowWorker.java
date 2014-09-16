package multithreading;

import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author zsolt
 */
public class SlowWorker implements Runnable, Callable<String> {

    private final List<String> stringList;
    private final String name;
    private final int cycleCount;

    public SlowWorker(List<String> stringList, String name, int cycleCount) {
        this.stringList = stringList;
        this.name = name;
        this.cycleCount = cycleCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < cycleCount; i++) {
            stringList.add(name + " " + i);
        }
    }

    @Override
    public String call() throws Exception {
        System.out.println("Before block");
        Thread.sleep(500);
        System.out.println("After block");
        return name;
    }

}
