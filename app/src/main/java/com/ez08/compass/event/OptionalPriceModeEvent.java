package com.ez08.compass.event;

public class OptionalPriceModeEvent {

    boolean flag = false; // click price or percent block ,is price mode?

    public OptionalPriceModeEvent(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
