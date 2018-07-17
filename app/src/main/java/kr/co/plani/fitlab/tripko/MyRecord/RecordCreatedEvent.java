package kr.co.plani.fitlab.tripko.MyRecord;

/**
 * Created by jihun on 2017-04-21.
 */

public class RecordCreatedEvent {

    private boolean isCreated;

    public RecordCreatedEvent(boolean isComplete) {
        this.isCreated = isComplete;
    }

    public boolean isCreated() {
        return isCreated;
    }
}
