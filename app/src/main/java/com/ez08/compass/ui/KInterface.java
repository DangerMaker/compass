package com.ez08.compass.ui;

import com.ez08.compass.entity.StockDetailEntity;

public interface KInterface {
    boolean getFocus();

    void setRefreshStock(StockDetailEntity entity);
}
