import java.util.ArrayList;
import java.util.Random;

public class Consumer implements Runnable {
    //CLASS VARIABLES
    //------------------------------------------------------------------------------------------------------------------
    private static int dataConsumed = 0;
    private static final int FINAL_NUMBER = 1000;
    private static final int MAX_RANDOM_TIME = 1400;

    //PRIVATES VARIABLES
    //------------------------------------------------------------------------------------------------------------------
    private Buffer buffer;
    private String state;
    private int ID;
    private Object key;

    //CONSTRUCTOR
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @param buffer [Buffer]
     * @param ID     [int] consumer identifier
     * @apiNote Class constructor
     */
    public Consumer(Buffer buffer, int ID, Object key) {
        this.buffer = buffer;
        this.ID = ID;
        this.state = "free";
        this.key = key;
    }

    //PUBLIC METHODS
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @apiNote Run method
     */
    @Override
    public void run() {
        //System.out.printf("i am alive");
        while (!Consumer.finishWork()) {
            this.consume();
        }
    }

    /**
     * @return [String] report
     * @apiNote return a report a state of consumer
     */
    public String getState() {
        return this.state;
    }

    /**
     * @return [int] ID
     * @apiNote Return ID of this consumer
     */
    public int getID() {
        return this.ID;
    }

    //PRIVATE METHODS
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @apiNote Consume one item of its buffer in a random time if this is possible
     */
    private void consume() {
        Random randomData = new Random();
        boolean canTake = false;

        //synchronize because only une consumer can go to take a data in a same time
        synchronized (key) {
            if (!Consumer.finishWork()) {
                //take date
                canTake = this.buffer.give();

                if (canTake)
                    //increase consume counter
                    Consumer.dataConsumedIncreases();
            }
        }

        //only consumers who take a data will take time to consume
        if (canTake) {
            //change state
            this.state = "occupied";

            //time of consume
            long time = randomData.nextInt(MAX_RANDOM_TIME);
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //change state
            this.state = "free";
        }
    }

    //CLASS METHODS
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @return [boolean] true in case that we finish to consume, false in other case
     * @apiNote return status work
     */
    public static synchronized boolean finishWork() {
        return Consumer.dataConsumed == FINAL_NUMBER;
    }

    /**
     * @return [int]
     * @apiNote return a dataConsumed number
     */
    public static synchronized int getDataConsumed() {
        return Consumer.dataConsumed;
    }

    /**
     * @apiNote increases the counter by 1
     */
    private static synchronized void dataConsumedIncreases() {
        Consumer.dataConsumed++;
    }
}
