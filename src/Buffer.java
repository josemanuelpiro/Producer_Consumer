import java.util.ArrayList;

public class Buffer {
    //CLASS VARIABLES
    //------------------------------------------------------------------------------------------------------------------
    private static final int MAX_SIZE = 25;

    //PRIVATE VARIABLES
    //------------------------------------------------------------------------------------------------------------------
    private ArrayList<Integer> aData;

    //CONSTRUCTOR
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @apiNote Constructor of Class
     */
    public Buffer() {
        this.aData = new ArrayList<>();
    }

    //PUBLIC METHODS
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @param data [Integer] date to put
     * @return [boolean] true in case that data was stored, false in other case.
     * @apiNote Put some date in the storage
     */
    public synchronized boolean put(Integer data) {
        //ask again for the case in that Productor finish its work after that the thread take the micro
        if (!Producer.finishWork()) {
            if (aData.size() < MAX_SIZE) {
                aData.add(data);
                return true;
            } else
                return false;
        } else
            return false;
    }

    /**
     * @return [Integer] Remove a data of buffer
     * @apiNote Returns true in case that consumer take a data, false in other case
     */
    public synchronized boolean give() {
        if (!Consumer.finishWork()) {
            if (aData.size() > 0) {
                //consume data of buffer
                aData.remove(aData.size() - 1);
                return true;
            } else
                return false;
        } else
            return false;
    }

    /**
     * @return [String] report
     * @apiNote Returns a report of capacity
     */
    public synchronized String getState() {

        String report = "Occupied capacity: " + this.aData.size();
        return report;
    }

    /**
     * @return [int] size
     * @apiNote Return a size of this buffer
     */
    public synchronized int getSize() {
        return this.aData.size();
    }

}
