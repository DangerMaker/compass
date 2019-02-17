package com.ez08.compass.tools;

import android.text.TextUtils;

import com.ez08.compass.entity.ItemStock;

import java.util.ArrayList;
import java.util.List;

public class StockSortManager {

    public static List<ItemStock> sortPriceAsc(List<ItemStock> stockList) {
        List<ItemStock> sortList = new ArrayList<>(stockList);
        for (int i = 0; i < sortList.size() - 1; i++) {
            for (int j = i + 1; j < sortList.size(); j++) {
                if (!TextUtils.isEmpty(sortList.get(i).getValue()) && !TextUtils.isEmpty(sortList.get(j).getValue()) && Float.parseFloat(sortList.get(i).getValue()) < Float.parseFloat(sortList.get(j).getValue())) {
                    ItemStock itemI = sortList.get(i);
                    ItemStock itemJ = sortList.get(j);
                    sortList.set(i, itemJ);
                    sortList.set(j, itemI);
                }
            }
        }
        filterStock(sortList);
        return sortList;
    }

    public static List<ItemStock> sortPriceDesc(List<ItemStock> stockList) {
        List<ItemStock> sortList = new ArrayList<>(stockList);
        for (int i = 0; i < sortList.size() - 1; i++) {
            for (int j = i + 1; j < sortList.size(); j++) {
                if (!TextUtils.isEmpty(sortList.get(i).getValue()) && !TextUtils.isEmpty(sortList.get(j).getValue()) && Float.parseFloat(sortList.get(i).getValue()) > Float.parseFloat(sortList.get(j).getValue())) {
                    ItemStock itemI = sortList.get(i);
                    ItemStock itemJ = sortList.get(j);
                    sortList.set(i, itemJ);
                    sortList.set(j, itemI);
                }
            }
        }
        filterStock(sortList);
        return sortList;
    }

    public static List<ItemStock> sortIncreaseAsc(List<ItemStock> stockList, boolean mSetPriceValue) {
        List<ItemStock> sortList = new ArrayList<>(stockList);
        for (int i = 0; i < sortList.size() - 1; i++) {
            for (int j = i + 1; j < sortList.size(); j++) {
                if (mSetPriceValue) {
                    if (Float.parseFloat(sortList.get(i).getIncresePrice()) < Float.parseFloat(sortList.get(j).getIncresePrice())) {
                        ItemStock itemI = sortList.get(i);
                        ItemStock itemJ = sortList.get(j);
                        sortList.set(i, itemJ);
                        sortList.set(j, itemI);
                    }
                } else {
                    if (Float.parseFloat(sortList.get(i).getIncrease()) < Float.parseFloat(sortList.get(j).getIncrease())) {
                        ItemStock itemI = sortList.get(i);
                        ItemStock itemJ = sortList.get(j);
                        sortList.set(i, itemJ);
                        sortList.set(j, itemI);
                    }
                }
            }
        }
        filterStock(sortList);
        return sortList;
    }

    public static List<ItemStock> sortIncreaseDesc(List<ItemStock> stockList, boolean mSetPriceValue) {
        List<ItemStock> sortList = new ArrayList<>(stockList);
        for (int i = 0; i < sortList.size() - 1; i++) {
            for (int j = i + 1; j < sortList.size(); j++) {
                if (mSetPriceValue) {
                    if (Float.parseFloat(sortList.get(i).getIncresePrice()) > Float.parseFloat(sortList.get(j).getIncresePrice())) {
                        ItemStock itemI = sortList.get(i);
                        ItemStock itemJ = sortList.get(j);
                        sortList.set(i, itemJ);
                        sortList.set(j, itemI);
                    }

                } else {
                    if (Float.parseFloat(sortList.get(i).getIncrease()) > Float.parseFloat(sortList.get(j).getIncrease())) {
                        ItemStock itemI = sortList.get(i);
                        ItemStock itemJ = sortList.get(j);
                        sortList.set(i, itemJ);
                        sortList.set(j, itemI);
                    }
                }

            }
        }
        filterStock(sortList);
        return sortList;
    }

    /**
     * 将显示为“－ －”的排在最尾端
     *
     * @param list
     */
    private static void filterStock(List<ItemStock> list) {
/*        if (TimeTool.isBeforeTotalTrade()) {//8点到集合竞价阶段
            return;
        } else*/
        if (TimeTool.isInTotalTrade()) { //集合竞价阶段
            int num = 0;
            for (int i = 0; i < list.size(); i++) {
                if (TextUtils.isEmpty(list.get(i).getValue())
                        || "0".equalsIgnoreCase(list.get(i).getValue())
                        || "0.0".equalsIgnoreCase(list.get(i).getValue())
                        || "0.00".equalsIgnoreCase(list.get(i).getValue()) || (list.get(i).getState() == 1 || list.get(i).getState() == 3)) {
                    num++;
                }
            }
            for (int i = 0; i < num; i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (TextUtils.isEmpty(list.get(j).getValue())
                            || "0".equalsIgnoreCase(list.get(j).getValue())
                            || "0.0".equalsIgnoreCase(list.get(j).getValue())
                            || "0.00".equalsIgnoreCase(list.get(j).getValue()) || (list.get(j).getState() == 1 || list.get(j).getState() == 3)) {
                        ItemStock item = list.get(j);
                        list.remove(j);
                        list.add(item);
                    }
                }
            }
        } else {//其他时刻
            int num = 0;
            for (int i = 0; i < list.size(); i++) {
                if (TextUtils.isEmpty(list.get(i).getAmount())
                        || "0".equalsIgnoreCase(list.get(i).getAmount())
                        || "0.0".equalsIgnoreCase(list.get(i).getAmount())
                        || "0.00".equalsIgnoreCase(list.get(i).getAmount())
                        || TextUtils.isEmpty(list.get(i).getValue())
                        || "0".equalsIgnoreCase(list.get(i).getValue())
                        || "0.0".equalsIgnoreCase(list.get(i).getValue())
                        || "0.00".equalsIgnoreCase(list.get(i).getValue()) || (list.get(i).getState() == 1 || list.get(i).getState() == 3)) {
                    num++;
                }
            }
            for (int i = 0; i < num; i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (TextUtils.isEmpty(list.get(j).getAmount())
                            || "0".equalsIgnoreCase(list.get(j).getAmount())
                            || "0.0".equalsIgnoreCase(list.get(j).getAmount())
                            || "0.00".equalsIgnoreCase(list.get(j).getAmount())
                            || TextUtils.isEmpty(list.get(j).getValue())
                            || "0".equalsIgnoreCase(list.get(j).getValue())
                            || "0.0".equalsIgnoreCase(list.get(j).getValue())
                            || "0.00".equalsIgnoreCase(list.get(j).getValue()) || (list.get(j).getState() == 1 || list.get(j).getState() == 3)) {
                        ItemStock item = list.get(j);
                        list.remove(j);
                        list.add(item);
                    }
                }
            }
        }

    }
}
