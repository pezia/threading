package multithreading;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zsolt
 */
public class Deadlocker implements Runnable {

    private final String object1;
    private final String object2;

    public Deadlocker(String object1, String object2) {
        this.object1 = object1;
        this.object2 = object2;
    }

    @Override
    public void run() {
        synchronized (object1) {
            System.out.println("object1 aquired");
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Deadlocker.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Trying to lock object2");
            synchronized (object2) {
                System.out.println("object2 aquired");
            }
        }

        System.out.println("Work done, exiting.");
    }

}
