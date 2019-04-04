package com.ez08.compass.tools;

import android.os.Handler;

import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.net.NetInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/20.
 */

public class SelfCodesManager {

    public static List<ItemStock> finalSelfCodesList = new ArrayList<>();

    public static void setData(String[] data) {
        if (data == null) {
            return;
        }

        finalSelfCodesList.clear();
        for (String s : data) {
            ItemStock sc = new ItemStock();
            sc.setExtra(s);
            finalSelfCodesList.add(sc);
        }
        sortByOrder();
    }

    private static void sortByOrder() {
        for (int i = 0; i < finalSelfCodesList.size() - 1; i++) {
            for (int j = i + 1; j < finalSelfCodesList.size(); j++) {
                if (finalSelfCodesList.get(i).order > finalSelfCodesList.get(j).order) {
                    ItemStock itemI = finalSelfCodesList.get(i);
                    ItemStock itemJ = finalSelfCodesList.get(j);
                    finalSelfCodesList.set(i, itemJ);
                    finalSelfCodesList.set(j, itemI);
                }
            }
        }

        List<ItemStock> temp0 = new ArrayList<>();
        List<ItemStock> temp1 = new ArrayList<>();
        for (int i = 0; i < finalSelfCodesList.size(); i++) {
            ItemStock stock = finalSelfCodesList.get(i);
            if (stock.category == 0) {
                temp0.add(stock);
            } else {
                temp1.add(stock);
            }
        }

        finalSelfCodesList.clear();
        // important - custom
        finalSelfCodesList.addAll(temp1);
        finalSelfCodesList.addAll(temp0);
    }

    public static List<ItemStock> getSelfCodes(int type) {
        List<ItemStock> list = new ArrayList<>();
        for (int i = 0; i < finalSelfCodesList.size(); i++) {
            ItemStock stock = finalSelfCodesList.get(i);
            if (stock.category == type) {
                list.add(stock);
            }
        }
        return list;
    }

    public static String getSelfOnlyCode(int type) {
        String path = "";
        List<ItemStock> requestCodeList = getSelfCodes(type);
        for (int i = 0; i < requestCodeList.size(); i++) {
            if (requestCodeList.get(i).getCode() != null) {
                path = path + requestCodeList.get(i).getCode().toUpperCase();

                if (i != requestCodeList.size() - 1) {
                    path = path + ",";
                }
            }
        }
        return path;
    }

    public static ArrayList<String> getSelfOnlyCodeList(int type) {
        ArrayList<String> list = new ArrayList<>();
        List<ItemStock> requestCodeList = getSelfCodes(type);
        for (int i = 0; i < requestCodeList.size(); i++) {
            if (requestCodeList.get(i).getCode() != null) {
                list.add(requestCodeList.get(i).getCode().toUpperCase());
            }
        }
        return list;
    }

    public static void deleteSelfCode(String code) {
        for (int i = 0; i < finalSelfCodesList.size(); i++) {
            ItemStock stock = finalSelfCodesList.get(i);
            if (stock.getCode().equals(code)) {
                finalSelfCodesList.remove(i);
                return;
            }
        }
    }

    public static void turnSelfCode(String code, int type) {
        int codePos = 0;
        for (int i = 0; i < finalSelfCodesList.size(); i++) {
            ItemStock stock = finalSelfCodesList.get(i);
            if (stock.getCode().equals(code)) {
                if (stock.category == 0) {
                    stock.category = 1;
                } else {
                    stock.category = 0;
                }
                stock.order = 0;
                codePos = i;
            }
        }

        if (type == 0) {
            ItemStock item = finalSelfCodesList.get(codePos);
            finalSelfCodesList.remove(codePos);
            finalSelfCodesList.add(0, item);
        } else {
            ItemStock item = finalSelfCodesList.get(codePos);
            finalSelfCodesList.remove(codePos);
            finalSelfCodesList.add(getSelfCodes(1).size(), item);
        }

        uploadInformation();

    }

    public static void sortSelfCode(int type, int position, String code) {
        int codePos = 0;
        for (int i = 0; i < finalSelfCodesList.size(); i++) {
            ItemStock stock = finalSelfCodesList.get(i);
            if (stock.getCode().equals(code)) {
                codePos = i;
                break;
            }
        }

        if (type == 0) {
            ItemStock item = finalSelfCodesList.get(codePos);
            finalSelfCodesList.remove(codePos);
            finalSelfCodesList.add(position + getSelfCodes(1).size(), item);
        } else {
            ItemStock item = finalSelfCodesList.get(codePos);
            finalSelfCodesList.remove(codePos);
            finalSelfCodesList.add(position, item);
        }

        uploadInformation();
    }

    public static void addSelfCode(String code) {
        code = code + ItemStock.splitChar + 0 + ItemStock.splitChar + 0 + ItemStock.splitChar + 0 + ItemStock.splitChar + 0;
        ItemStock itemStock = new ItemStock();
        itemStock.setExtra(code);
        finalSelfCodesList.add(getSelfCodes(1).size(), itemStock);

        uploadInformation();
    }

    public static void addImportantSelfCode(String code) {
        code = code + ItemStock.splitChar + 0 + ItemStock.splitChar + 0 + ItemStock.splitChar + 0 + ItemStock.splitChar + 0;
        ItemStock itemStock = new ItemStock();
        itemStock.setExtra(code);
        finalSelfCodesList.add(0, itemStock);

        uploadInformation();
    }

    public static boolean isExsit(String code) {
        for (int i = 0; i < finalSelfCodesList.size(); i++) {
            ItemStock stock = finalSelfCodesList.get(i);
            if (stock.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    static Handler handler = new Handler();

    public static void uploadInformation() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < finalSelfCodesList.size(); i++) {
            ItemStock stock = finalSelfCodesList.get(i);
            list.add(stock.getExtra(i));
        }

        String[] str = new String[list.size()];
        list.toArray(str);
        NetInterface.editMyStock(handler, 1000, str);
    }


}