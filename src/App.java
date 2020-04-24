public class App {
    //PRIVATES VARIABLES
    //------------------------------------------------------------------------------------------------------------------
    private final static int NUM_CONSUMERS = 10;
    private final static int NUM_PRODUCTORS = 10;

    //CONSTRUCTOR
    //------------------------------------------------------------------------------------------------------------------
    public App() {
        //create a buffer objects
        Buffer buffer = new Buffer();

        //create a consumers and productors objects
        Producer[] producers = new Producer[NUM_PRODUCTORS];
        Object keyP = new Object();
        for (int i = 0; i < NUM_PRODUCTORS; i++) {
            producers[i] = new Producer(buffer, keyP);
        }
        Consumer[] consumers = new Consumer[NUM_CONSUMERS];
        Object keyC = new Object();
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            consumers[i] = new Consumer(buffer, i, keyC);
        }

        //create a log objects
        Log log = new Log(consumers, buffer);

        //create a threads and assign its productors objects
        Thread threadsP[] = new Thread[NUM_PRODUCTORS];
        for (int i = 0; i < NUM_PRODUCTORS; i++) {
            threadsP[i] = new Thread(producers[i]);
        }

        //create a threads and assign its consumers objects
        Thread threadsC[] = new Thread[NUM_CONSUMERS];
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            threadsC[i] = new Thread(consumers[i]);
        }

        //create a threads and assign its buffer objects
        Thread threadL = new Thread(log);

        //check initial time
        long startTime = System.currentTimeMillis();

        //start all threads
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            threadsC[i].start();
        }
        for (int i = 0; i < NUM_PRODUCTORS; i++) {
            threadsP[i].start();
        }
        threadL.setPriority(Thread.MAX_PRIORITY);
        threadL.start();

        //check finish time
        try {
            threadL.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long finishTime = System.currentTimeMillis();

        //time execution report
        System.out.printf("\n");
        System.out.printf("------------------------\n");
        System.out.printf("EXECUTION TIME: " + (finishTime - startTime) + "\n");
        System.out.printf("------------------------\n");

    }

}
