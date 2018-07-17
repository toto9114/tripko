package kr.co.plani.fitlab.tripko.Tutorial;

/**
 * Created by jihun on 2017-05-19.
 */

public class TutorialEvent {
    public static final int TYPE_MY_LIST_GUIDE_FINISH = 0;
    public static final int TYPE_ADD_MY_LIST_GUIDE_FINISH = 1;
    public static final int TYPE_ADD_PLAN_GUIDE_FINISH = 2;

    private boolean isFinish;
    private int type;

    public TutorialEvent(int type, boolean isFinish) {
        this.type = type;
        this.isFinish = isFinish;
    }

    public boolean isCreated() {
        return isFinish;
    }

    public int getType() {
        return type;
    }
}
