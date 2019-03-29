package com.ez08.compass.ui.stocks;

import com.ez08.support.net.EzMessage;

public interface EzParser<T> {
    T invoke(EzMessage msg);
}
