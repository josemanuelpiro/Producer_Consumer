import java.util.Random;

public class Producer implements Runnable {
    //CLASS VARIABLES
    //------------------------------------------------------------------------------------------------------------------
    private static int lostData = 0;
    private static int dataProduced = 0;
    private static int dataPlaced = 0;
    private static final int FINAL_NUMBER = 1000;
    private static final int MAX_RANDOM_TIME = 1400;
    private static final int MAX_RANDOM_VALUE = 100;


    //PRIVATES VARIABLES
    //------------------------------------------------------------------------------------------------------------------
    private Buffer buffer;
    private Object key;

    //CONSTRUCTOR
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @param buffer [Buffer] buffer of this producer
     * @apiNote Class constructor
     */
    public Producer(Buffer buffer, Object key) {
        this.buffer = buffer;
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
        while (!Producer.finishWork()) {
            this.produce();
        }
    }

    //PRIVATE METHODS
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @apiNote Produce a random data in a random time and put in its buffer
     */
    private void produce() {
        Random randomData = new Random();

        //time of production
        long time = randomData.nextInt(MAX_RANDOM_TIME);
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //create data
        int data = randomData.nextInt(MAX_RANDOM_VALUE);

        synchronized (key) {
            if (!Producer.finishWork()) {
                //increase data produced
                Producer.dataProducedIncreases();

                //put data
                if (!this.buffer.put(data)) {
                    Producer.dataTrashedIncreases();
                } else
                    Producer.dataPlacedIncreases();
            }
        }

    }

    //CLASS METHODS
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @return [int] dataProduced
     * @apiNote return a dataProduced number
     */
    public static synchronized int getDataProduced() {
        return Producer.dataProduced;
    }

    /**
     * @return [int] dataPlaced
     * @apiNote return a dataPlaced number
     */
    public static synchronized int getDataPlaced() {
        return Producer.dataPlaced;
    }

    /**
     * @return [int] lostData
     * @apiNote return a number of lost data
     */
    public static synchronized int getDataTrashed() {
        return Producer.lostData;
    }

    /**
     * @apiNote increases the counter by 1
     */
    private static synchronized void dataProducedIncreases() {
        Producer.dataProduced++;
    }

    /**
     * @apiNote increases the counter by 1
     */
    private static synchronized void dataPlacedIncreases() {
        Producer.dataPlaced++;
    }

    /**
     * @apiNote increases the counter by 1
     */
    private static synchronized void dataTrashedIncreases() {
        Producer.lostData++;
    }

    /**
     * @return [boolean] true in case that we finish to produce, false in other case
     * @apiNote return status work
     */
    public static  boolean finishWork() {
        return Producer.dataPlaced == FINAL_NUMBER;
    }

}