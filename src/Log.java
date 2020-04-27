import java.io.*;

public class Log implements Runnable {
    //PRIVATES VARIABLES
    //------------------------------------------------------------------------------------------------------------------
    private final long SAMPLE_TIME = 2000;
    private final String REPORT_FILE_NAME = "log.txt";

    private Consumer[] consumers;
    private Buffer buffer;
    private int reportNumber = 1;


    //CONSTRUCTOR
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @param consumers [array[Consumer]]
     * @param buffer    [Buffer]
     * @apiNote Class constructor
     */
    public Log(Consumer consumers[], Buffer buffer) {
        this.buffer = buffer;
        this.consumers = consumers;
    }

    //PUBLIC METHODS
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @apiNote Run method
     */
    @Override
    public void run() {
        //System.out.printf("i am alive");

        String tittle = "##########################################\n" +
                        "REPORT OF PROGRAM \n" +
                        "Five Threads\n" +
                        "##########################################";

        //write a tittle in a file
        this.writeFile(tittle);

        while (!Consumer.finishWork()) {
            //whit a 2 second
            try {
                Thread.sleep(SAMPLE_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //take a report sample and print
            String report = this.takeInformation();

            //write a sample in a file
            this.writeFile(report);

        }
    }

    //PRIVATE METHODS
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @return [String] report
     * @apiNote Print a information log
     */
    private String takeInformation() {
        String report = "";

        //save report
        report += ("----------------------------------\n");
        report += ("Report " + this.reportNumber +" |\n"+
                   "----------" +
                 "\nBuffer size:   " + this.buffer.getSize() +
                 "\nData produced: " + Producer.getDataProduced() +
                 "\nData placed:   " + Producer.getDataPlaced() +
                 "\nData consumed: " + Consumer.getDataConsumed() +
                 "\nData trashed:  " + Producer.getDataTrashed() +
                 "\n");
        report += ("----------------------------------\n");
        for (int i = 0; i < this.consumers.length; i++) {
            report += ("-----> CONSUMER N: " + this.consumers[i].getID() + "\n");
            report += ("            state: " + this.consumers[i].getState() + "\n");
        }
        report += ("----------------------------------\n");

        //increase a report number counter
        this.reportNumber++;

        //print report
        System.out.printf(report);

        return report;
    }

    /**
     * @param report [String]
     * @apiNote write report in a file
     */
    private void writeFile(String report) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(REPORT_FILE_NAME, true);
            pw = new PrintWriter(fichero);

            pw.println(report);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero)
                    fichero.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}

